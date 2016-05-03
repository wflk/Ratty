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

import de.sogomn.engine.fx.Sound;
import de.sogomn.engine.util.FileUtils;
import de.sogomn.rat.ActiveConnection;

public final class AudioPacket implements IPacket {
	
	private byte[] data;
	
	public AudioPacket(final byte[] data) {
		this.data = data;
	}
	
	public AudioPacket(final String path) {
		data = FileUtils.readExternalData(path);
	}
	
	public AudioPacket() {
		data = new byte[0];
	}
	
	@Override
	public void send(final ActiveConnection connection) {
		connection.writeInt(data.length);
		connection.write(data);
	}
	
	@Override
	public void receive(final ActiveConnection connection) {
		final int length = connection.readInt();
		
		data = new byte[length];
		
		connection.read(data);
	}
	
	@Override
	public void execute(final ActiveConnection connection) {
		final Sound sound = Sound.loadSound(data);
		
		sound.play();
	}
	
	public byte[] getData() {
		return data;
	}
	
}
