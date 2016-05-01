/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat.packet;

import java.io.File;
import java.util.ArrayList;
import java.util.stream.Stream;

import de.sogomn.rat.ActiveConnection;

public class FileRequestPacket extends AbstractPingPongPacket {
	
	private String rootFile;
	private String[] filePaths;
	private String[] directoryPaths;
	
	@SuppressWarnings("unused")
	private static final byte DIRECTORY = 2;
	private static final byte FILE = 1;
	private static final byte END = 0;
	
	public FileRequestPacket(final String rootFile) {
		this.rootFile = rootFile;
		
		type = REQUEST;
		filePaths = directoryPaths = new String[0];
	}
	
	public FileRequestPacket() {
		this("");
		
		type = DATA;
	}
	
	@Override
	protected void sendRequest(final ActiveConnection connection) {
		connection.writeUtf(rootFile);
	}
	
	@Override
	protected void sendData(final ActiveConnection connection) {
		for (final String path : filePaths) {
			connection.writeByte(FILE);
			connection.writeUtf(path);
		}
		
		connection.writeByte(END);
	}
	
	@Override
	protected void receiveRequest(final ActiveConnection connection) {
		rootFile = connection.readUtf();
	}
	
	@Override
	protected void receiveData(final ActiveConnection connection) {
		final ArrayList<String> pathList = new ArrayList<String>();
		
		while (connection.readByte() == FILE) {
			final String path = connection.readUtf();
			
			pathList.add(path);
		}
		
		final int length = pathList.size();
		
		filePaths = new String[length];
		filePaths = pathList.toArray(filePaths);
	}
	
	@Override
	protected void executeRequest(final ActiveConnection connection) {
		final File file = new File(rootFile);
		final File[] children = file.listFiles();
		
		if (children == null) {
			return;
		}
		
		type = DATA;
		filePaths = Stream.of(children)
				.map(File::getAbsolutePath)
				.toArray(String[]::new);
		
		connection.addPacket(this);
	}
	
	@Override
	protected void executeData(final ActiveConnection connection) {
		//...
	}
	
	public String[] getFilePaths() {
		return filePaths;
	}
	
	public String[] getDirectoryPaths() {
		return directoryPaths;
	}
	
}
