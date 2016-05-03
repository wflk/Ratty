/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat.gui;

import static de.sogomn.rat.util.Constants.LANGUAGE;

public interface IBuilderGui extends IGui {
	
	String ADDRESS = LANGUAGE.getString("server.address");
	String PORT = LANGUAGE.getString("server.port");
	String ADD = LANGUAGE.getString("server.add");
	String REMOVE = LANGUAGE.getString("server.remove");
	String CHOOSE = LANGUAGE.getString("server.choose");
	String BUILD = LANGUAGE.getString("server.build");
	String NO_FILE = LANGUAGE.getString("server.no_file");
	
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
