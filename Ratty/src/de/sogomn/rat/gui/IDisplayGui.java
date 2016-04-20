/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat.gui;

import java.awt.image.BufferedImage;

import de.sogomn.rat.util.FrameEncoder.Frame;

public interface IDisplayGui extends IGui {
	
	String MOUSE_PRESSED = "Mouse pressed";
	String MOUSE_RELEASED = "Mouse released";
	String KEY_PRESSED = "Key pressed";
	String KEY_RELEASED = "Key released";
	String CLOSE = "Closed";
	
	void showImage(final BufferedImage image);
	
	void showFrames(final int screenWidth, final int screenHeight, final Frame... frames);
	
	int getMouseButtonInput();
	
	int getMouseX();
	
	int getMouseY();
	
	int getKeyboardKeyInput();
	
}
