/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat.gui;

import java.awt.Image;
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
	
}
