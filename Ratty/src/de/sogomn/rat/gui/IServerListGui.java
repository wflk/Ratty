/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat.gui;

import static de.sogomn.rat.util.Constants.LANGUAGE;

public interface IServerListGui extends IGui {
	
	String START = LANGUAGE.getString("server.start");
	String STOP = LANGUAGE.getString("server.stop");
	
	void addListEntry(final String entry);
	
	void removeListEntry(final String entry);
	
	void clearListEntries();
	
	boolean containsListEntry(final String entry);
	
	void setPortInput(final String input);
	
	String getPortInput();
	
	String getSelectedItem();
	
}
