/*******************************************************************************
 * Copyright 2016 Johannes Boczek
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package de.sogomn.rat.gui.swing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import de.sogomn.engine.IKeyboardListener;
import de.sogomn.engine.IMouseListener;
import de.sogomn.engine.Screen;
import de.sogomn.engine.Screen.ResizeBehavior;
import de.sogomn.engine.util.AbstractListenerContainer;
import de.sogomn.engine.util.ImageUtils;
import de.sogomn.rat.gui.IDisplayGui;
import de.sogomn.rat.gui.IGuiController;
import de.sogomn.rat.util.FrameEncoder.Frame;

public final class DisplaySwingGui extends AbstractListenerContainer<IGuiController> implements IDisplayGui, IMouseListener, IKeyboardListener {
	
	private Screen screen;
	private BufferedImage image;
	
	private String title;
	private BufferedImage[] icons;
	
	private int mouseButtonInput;
	private int mouseX, mouseY;
	private int keyboardKeyInput;
	
	private static final int SCREEN_WIDTH = 1920 / 2;
	private static final int SCREEN_HEIGHT = 1080 / 2;
	
	public DisplaySwingGui() {
		mouseButtonInput = MouseEvent.NOBUTTON;
		keyboardKeyInput = KeyEvent.VK_UNDEFINED;
	}
	
	private Screen createScreen(final int screenWidth, final int screenHeight) {
		final Screen screen = new Screen(screenWidth, screenHeight);
		final WindowAdapter windowAdapter = new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent w) {
				notifyListeners(controller -> controller.userInput(CLOSE, this));
			}
		};
		
		if (icons != null) {
			screen.setIcons(icons);
		}
		
		screen.setTitle(title);
		screen.setResizeBehavior(ResizeBehavior.KEEP_ASPECT_RATIO);
		screen.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		screen.setBackgroundColor(Color.BLACK);
		screen.addMouseListener(this);
		screen.addKeyboardListener(this);
		screen.addWindowListener(windowAdapter);
		screen.addListener(g -> {
			ImageUtils.applyLowGraphics(g);
			g.drawImage(image, 0, 0, null);
		});
		
		return screen;
	}
	
	private void drawToScreenImage(final BufferedImage imagePart, final int x, final int y) {
		final Graphics2D g = image.createGraphics();
		
		ImageUtils.applyLowGraphics(g);
		
		g.drawImage(imagePart, x, y, null);
		g.dispose();
	}
	
	private void updateImage(final int screenWidth, final int screenHeight) {
		if (image == null || image.getWidth() != screenWidth || image.getHeight() != screenHeight) {
			image = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
		}
	}
	
	private void updateScreen(final int screenWidth, final int screenHeight) {
		if (screen == null || screen.getInitialWidth() != screenWidth || screen.getInitialHeight() != screenHeight || !screen.isOpen()) {
			if (screen != null) {
				screen.close();
			}
			
			screen = createScreen(screenWidth, screenHeight);
		}
		
		screen.show();
		screen.redraw();
	}
	
	@Override
	public void mouseEvent(final int x, final int y, final int button, final boolean flag) {
		final String event = flag ? MOUSE_PRESSED : MOUSE_RELEASED;
		
		if (button == MouseEvent.BUTTON1) {
			mouseButtonInput = MouseEvent.BUTTON1_DOWN_MASK;
		} else if (button == MouseEvent.BUTTON2) {
			mouseButtonInput = MouseEvent.BUTTON2_DOWN_MASK;
		} else if (button == MouseEvent.BUTTON3) {
			mouseButtonInput = MouseEvent.BUTTON3_DOWN_MASK;
		} else {
			mouseButtonInput = MouseEvent.NOBUTTON;
		}
		
		notifyListeners(controller -> controller.userInput(event, this));
	}
	
	@Override
	public void mouseMotionEvent(final int x, final int y, final int modifiers) {
		mouseX = x;
		mouseY = y;
		
		//notifyListeners(controller -> controller.userInput(MOUSE_MOVED, this));//TODO
	}
	
	@Override
	public void mouseWheelEvent(final int x, final int y, final int rotation) {
		//...
	}
	
	@Override
	public void keyboardEvent(final int key, final boolean flag) {
		final String event = flag ? KEY_PRESSED : KEY_RELEASED;
		
		keyboardKeyInput = key;
		
		notifyListeners(controller -> controller.userInput(event, this));
	}
	
	@Override
	public void setTitle(final String title) {
		this.title = title;
	}
	
	@Override
	public void setIcons(final List<? extends Image> icons) {
		this.icons = icons.stream()
				.filter(icon -> icon instanceof BufferedImage)
				.map(icon -> (BufferedImage)icon)
				.toArray(BufferedImage[]::new);
	}
	
	@Override
	public void update() {
		screen.redraw();
	}

	@Override
	public void setVisible(final boolean visible) {
		SwingUtilities.invokeLater(() -> {
			if (visible) {
				screen.show();
			} else {
				screen.hide();
			}
		});
	}
	
	@Override
	public void close() {
		if (screen != null) {
			SwingUtilities.invokeLater(() -> {
				screen.close();
			});
		}
	}
	
	@Override
	public void showWarning(final String message) {
		//...
	}
	
	@Override
	public int showWarning(final String message, final String yes, final String no) {
		return JOptionPane.CLOSED_OPTION;
	}
	
	@Override
	public void showError(final String message) {
		//...
	}
	
	@Override
	public void showMessage(final String message) {
		//...
	}
	
	@Override
	public int showOptions(final String message, final String yes, final String no, final String cancel) {
		return JOptionPane.CLOSED_OPTION;
	}
	
	@Override
	public String getInput(final String message) {
		return null;
	}
	
	@Override
	public boolean isVisible() {
		return screen.isVisible();
	}
	
	@Override
	public File getOpenFile(final String type) {
		return null;
	}
	
	@Override
	public File getSaveFile(final String type) {
		return null;
	}
	
	@Override
	public void showImage(final BufferedImage image) {
		this.image = image;
		
		final int width = image.getWidth();
		final int height = image.getHeight();
		
		updateScreen(width, height);
	}
	
	@Override
	public void showFrames(final int screenWidth, final int screenHeight, final Frame... frames) {
		updateImage(screenWidth, screenHeight);
		
		for (final Frame frame : frames) {
			drawToScreenImage(frame.image, frame.x, frame.y);
		}
		
		updateScreen(screenWidth, screenHeight);
	}
	
	@Override
	public int getMouseButtonInput() {
		return mouseButtonInput;
	}
	
	@Override
	public int getMouseX() {
		return mouseX;
	}
	
	@Override
	public int getMouseY() {
		return mouseY;
	}
	
	@Override
	public int getKeyboardKeyInput() {
		return keyboardKeyInput;
	}
	
}
