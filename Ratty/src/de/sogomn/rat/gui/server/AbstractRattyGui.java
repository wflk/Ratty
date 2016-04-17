package de.sogomn.rat.gui.server;

import de.sogomn.rat.gui.AbstractGui;


public abstract class AbstractRattyGui extends AbstractGui {
	
	public AbstractRattyGui() {
		//...
	}
	
	public abstract void addClient(final ServerClient client);
	
	public abstract void removeClient(final ServerClient client);
	
	public abstract ServerClient getSelectedClient();
	
}
