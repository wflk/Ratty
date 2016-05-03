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
import java.util.ArrayList;
import java.util.stream.Stream;

import de.sogomn.rat.ActiveConnection;

public class FileRequestPacket extends AbstractPingPongPacket {
	
	private String rootFile;
	private String[] filePaths;
	private String[] directoryPaths;
	
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
		for (final String file : filePaths) {
			connection.writeByte(FILE);
			connection.writeUtf(file);
		}
		
		for (final String directory : directoryPaths) {
			connection.writeByte(DIRECTORY);
			connection.writeUtf(directory);
		}
		
		connection.writeByte(END);
	}
	
	@Override
	protected void receiveRequest(final ActiveConnection connection) {
		rootFile = connection.readUtf();
	}
	
	@Override
	protected void receiveData(final ActiveConnection connection) {
		final ArrayList<String> filePathList = new ArrayList<String>();
		final ArrayList<String> directoryPathList = new ArrayList<String>();
		
		byte type = END;
		
		while ((type = connection.readByte()) != END) {
			final String path = connection.readUtf();
			
			if (type == FILE) {
				filePathList.add(path);
			} else if (type == DIRECTORY) {
				directoryPathList.add(path);
			}
		}
		
		final int fileCount = filePathList.size();
		final int directoryCount = directoryPathList.size();
		
		filePaths = new String[fileCount];
		filePaths = filePathList.toArray(filePaths);
		directoryPaths = new String[directoryCount];
		directoryPaths = directoryPathList.toArray(directoryPaths);
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
				.filter(File::isFile)
				.map(File::getAbsolutePath)
				.toArray(String[]::new);
		directoryPaths = Stream.of(children)
				.filter(File::isDirectory)
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
