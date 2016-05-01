/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat.packet;

import java.io.File;
import java.util.ArrayList;
import java.util.stream.Stream;

import de.sogomn.rat.ActiveConnection;

public class RootRequestPacket extends AbstractPingPongPacket {
	
	private String[] roots;
	
	private static final byte INCOMING = 1;
	private static final byte END = 0;
	
	public RootRequestPacket() {
		type = REQUEST;
		roots = new String[0];
	}
	
	@Override
	protected void sendRequest(final ActiveConnection connection) {
		//...
	}
	
	@Override
	protected void sendData(final ActiveConnection connection) {
		for (final String root : roots) {
			connection.writeByte(INCOMING);
			connection.writeUtf(root);
		}
		
		connection.writeByte(END);
	}
	
	@Override
	protected void receiveRequest(final ActiveConnection connection) {
		//...
	}
	
	@Override
	protected void receiveData(final ActiveConnection connection) {
		final ArrayList<String> rootList = new ArrayList<String>();
		
		while (connection.readByte() == INCOMING) {
			final String root = connection.readUtf();
			
			rootList.add(root);
		}
		
		final int length = rootList.size();
		
		roots = new String[length];
		roots = rootList.toArray(roots);
	}
	
	@Override
	protected void executeRequest(final ActiveConnection connection) {
		final File[] rootFiles = File.listRoots();
		
		type = DATA;
		roots = Stream.of(rootFiles)
				.map(File::getAbsolutePath)
				.toArray(String[]::new);
		
		connection.addPacket(this);
	}
	
	@Override
	protected void executeData(final ActiveConnection connection) {
		//...
	}
	
	public String[] getRoots() {
		return roots;
	}
	
}
