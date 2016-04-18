/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat.packet;

import de.sogomn.rat.ActiveConnection;
import de.sogomn.rat.util.Constants;

public final class ShutdownPacket implements IPacket {
	
	public ShutdownPacket() {
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
		Constants.OS_SERVICE.shutDown();
	}
	
}
