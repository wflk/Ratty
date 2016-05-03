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

import java.awt.Image;
import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.sogomn.engine.util.AbstractListenerContainer;
import de.sogomn.rat.gui.IGui;
import de.sogomn.rat.gui.IGuiController;

public abstract class AbstractSwingGui extends AbstractListenerContainer<IGuiController> implements IGui {
	
	protected JFrame frame;
	protected JFileChooser fileChooser;
	
	public AbstractSwingGui() {
		frame = new JFrame();
		fileChooser = new JFileChooser(".");
	}
	
	private int showOptionDialog(final int messageType, final String message, final int optionType, final String... options) {
		final int input = JOptionPane.showOptionDialog(frame, message, null, optionType, messageType, null, options, null);
		
		return input;
	}
	
	private void showMessageDialog(final int messageType, final String message) {
		JOptionPane.showMessageDialog(frame, message, null, messageType, null);
	}
	
	@Override
	public void update() {
		//...
	}
	
	@Override
	public void close() {
		SwingUtilities.invokeLater(() -> {
			frame.setVisible(false);
			frame.dispose();
		});
	}
	
	@Override
	public void setVisible(final boolean visible) {
		SwingUtilities.invokeLater(() -> {
			frame.setVisible(visible);
		});
	}
	
	@Override
	public void setTitle(final String title) {
		frame.setTitle(title);
	}
	
	@Override
	public void setIcons(final List<? extends Image> icons) {
		frame.setIconImages(icons);
	}
	
	@Override
	public void showWarning(final String message) {
		if (!isVisible()) {
			return;
		}
		
		showMessageDialog(JOptionPane.WARNING_MESSAGE, message);
	}
	
	@Override
	public int showWarning(final String message, final String yes, final String no) {
		if (!isVisible()) {
			return JOptionPane.CLOSED_OPTION;
		}
		
		return showOptionDialog(JOptionPane.WARNING_MESSAGE, message, JOptionPane.YES_NO_OPTION, yes, no);
	}
	
	@Override
	public void showError(final String message) {
		if (!isVisible()) {
			return;
		}
		
		showMessageDialog(JOptionPane.ERROR_MESSAGE, message);
	}
	
	@Override
	public void showMessage(final String message) {
		if (!isVisible()) {
			return;
		}
		
		showMessageDialog(JOptionPane.INFORMATION_MESSAGE, message);
	}
	
	@Override
	public int showOptions(final String message, final String yes, final String no, final String cancel) {
		if (!isVisible()) {
			return JOptionPane.CLOSED_OPTION;
		}
		
		return showOptionDialog(JOptionPane.QUESTION_MESSAGE, message, JOptionPane.YES_NO_CANCEL_OPTION, yes, no, cancel);
	}
	
	@Override
	public String getInput(final String message) {
		if (!isVisible()) {
			return null;
		}
		
		final String input = JOptionPane.showInputDialog(frame, message);
		
		return input;
	}
	
	@Override
	public boolean isVisible() {
		return frame.isVisible();
	}
	
	@Override
	public File getOpenFile(final String type) {
		final FileFilter filter;
		
		if (type != null) {
			filter = new FileNameExtensionFilter("*." + type, type);
		} else {
			filter = null;
		}
		
		fileChooser.setFileFilter(filter);
		
		final int input = fileChooser.showOpenDialog(frame);
		
		if (input == JFileChooser.APPROVE_OPTION) {
			final File file = fileChooser.getSelectedFile();
			
			return file;
		}
		
		return null;
	}
	
	@Override
	public File getSaveFile() {
		final int input = fileChooser.showSaveDialog(frame);
		
		if (input == JFileChooser.APPROVE_OPTION) {
			final File file = fileChooser.getSelectedFile();
			
			return file;
		}
		
		return null;
	}
	
	@Override
	public File getSaveFile(final String type) {
		File file = getSaveFile();
		
		if (file == null) {
			return null;
		}
		
		final String path = file.toString().toLowerCase();
		final String suffix = "." + type.toLowerCase();
		
		if (!path.endsWith(suffix)) {
			file = new File(file + suffix);
		}
		
		return file;
	}
	
}
