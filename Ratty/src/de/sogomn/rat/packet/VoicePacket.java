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
import de.sogomn.rat.ActiveConnection;

public final class VoicePacket extends AbstractPingPongPacket {
	
	private byte[] data;
	
	public VoicePacket(final byte[] data) {
		this.data = data;
		
		type = DATA;
	}
	
	public VoicePacket() {
		this(new byte[0]);
		
		type = REQUEST;
	}
	
	@Override
	protected void sendRequest(final ActiveConnection connection) {
		//...
	}
	
	@Override
	protected void sendData(final ActiveConnection connection) {
		connection.writeInt(data.length);
		connection.write(data);
	}
	
	@Override
	protected void receiveRequest(final ActiveConnection connection) {
		//...
	}
	
	@Override
	protected void receiveData(final ActiveConnection connection) {
		final int length = connection.readInt();
		
		data = new byte[length];
		connection.read(data);
	}
	
	@Override
	protected void executeRequest(final ActiveConnection connection) {
		type = DATA;
		
		connection.addPacket(this);
	}
	
	@Override
	protected void executeData(final ActiveConnection connection) {
		final Sound sound = Sound.loadSound(data);
		
		sound.play();
	}
	
	public void setData(final byte[] data) {
		this.data = data;
	}
	
	public byte[] getData() {
		return data;
	}
	
	public Sound getSound() {
		final Sound sound = Sound.loadSound(data);
		
		return sound;
	}
	
}
