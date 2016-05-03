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
