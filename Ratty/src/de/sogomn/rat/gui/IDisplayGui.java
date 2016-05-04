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

package de.sogomn.rat.gui;

import java.awt.image.BufferedImage;

import de.sogomn.rat.util.FrameEncoder.Frame;

public interface IDisplayGui extends IGui {
	
	String MOUSE_PRESSED = "Mouse pressed";
	String MOUSE_RELEASED = "Mouse released";
	String KEY_PRESSED = "Key pressed";
	String KEY_RELEASED = "Key released";
	String CLOSE = "Closed";
	String MOUSE_MOVED = "Mouse moved";
	
	void showImage(final BufferedImage image);
	
	void showFrames(final int screenWidth, final int screenHeight, final Frame... frames);
	
	int getMouseButtonInput();
	
	int getMouseX();
	
	int getMouseY();
	
	int getKeyboardKeyInput();
	
}
