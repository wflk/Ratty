package de.sogomn.rat.gui.server;

import de.sogomn.rat.gui.IGui;

public interface IRattyGui extends IGui {
	
	void addClient(final ServerClient client);
	
	void removeClient(final ServerClient client);
	
	ServerClient getSelectedClient();
	
}
