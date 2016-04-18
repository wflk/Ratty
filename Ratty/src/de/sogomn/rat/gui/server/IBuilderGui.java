/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat.gui.server;

import de.sogomn.rat.gui.IGui;

public interface IBuilderGui extends IGui {
	
	void addListEntry(final String entry);
	
	void removeListEntry(final String entry);
	
	void clearListEntries();
	
	boolean containsListEntry(final String entry);
	
	void setAddressInput(final String input);
	
	void setPortInput(final String input);
	
	void setFileName(final String name);
	
	String getAddressInput();
	
	String getPortInput();
	
	String getFileName();
	
	String getSelectedListEntry();
	
	String[] getListEntries();
	
}
