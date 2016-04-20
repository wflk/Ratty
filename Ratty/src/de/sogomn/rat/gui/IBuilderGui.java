/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat.gui;

import static de.sogomn.rat.util.Constants.LANGUAGE;

public interface IBuilderGui extends IGui {
	
	String ADDRESS = LANGUAGE.getString("builder.address");
	String PORT = LANGUAGE.getString("builder.port");
	String ADD = LANGUAGE.getString("builder.add");
	String REMOVE = LANGUAGE.getString("builder.remove");
	String CHOOSE = LANGUAGE.getString("builder.choose");
	String BUILD = LANGUAGE.getString("builder.build");
	String NO_FILE = LANGUAGE.getString("builder.no_file");
	
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
