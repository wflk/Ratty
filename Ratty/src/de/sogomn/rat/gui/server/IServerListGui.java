/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat.gui.server;

import de.sogomn.rat.gui.IGui;

public interface IServerListGui extends IGui {
	
	void addListEntry(final String entry);
	
	void removeListEntry(final String entry);
	
	void clearListEntries();
	
	boolean containsListEntry(final String entry);
	
	void setPortInput(final String input);
	
	String getPortInput();
	
	String getSelectedItem();
	
}
