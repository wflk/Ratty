/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat.gui;

import static de.sogomn.rat.util.Constants.LANGUAGE;

public interface ILoggingGui extends IGui {
	
	String CLEAR = LANGUAGE.getString("logger.clear");
	
	void log(final String message);
	
	void clear();
	
}
