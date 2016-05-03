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

import java.awt.Image;
import java.io.File;
import java.util.List;

public interface IGui {
	
	void addListener(final IGuiController controller);
	
	void removeListener(final IGuiController controller);
	
	void removeAllListeners();
	
	void update();
	
	void close();
	
	void setVisible(final boolean visible);
	
	void setTitle(final String title);
	
	void setIcons(final List<? extends Image> icons);
	
	void showWarning(final String message);
	
	int showWarning(final String message, final String yes, final String no);
	
	void showError(final String message);
	
	void showMessage(final String message);
	
	int showOptions(final String message, final String yes, final String no, final String cancel);
	
	String getInput(final String message);
	
	default String getInput() {
		return getInput(null);
	}
	
	boolean isVisible();
	
	File getOpenFile(final String type);
	
	default File getOpenFile() {
		return getOpenFile(null);
	}
	
	File getSaveFile(final String type);
	
	default File getSaveFile() {
		return getSaveFile(null);
	}
	
}
