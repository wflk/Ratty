package de.sogomn.rat.gui.server;


public interface IRattyGui {
	
	String getInput(final String message);
	
	default String getInput() {
		return getInput(null);
	}
	
	void showMessage(final String message);
	
	void showError(final String message);
	
	boolean showWarning(final String message, final String yes, final String no);
	
	int showOptions(final String message, final String yes, final String no, final String cancel);
	
	void addClient(final ServerClient client);
	
	void removeClient(final ServerClient client);
	
	ServerClient getSelectedClient();
	
	void update();
	
}
