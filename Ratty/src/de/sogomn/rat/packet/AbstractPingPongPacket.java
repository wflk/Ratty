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

import de.sogomn.rat.ActiveConnection;

public abstract class AbstractPingPongPacket implements IPacket {
	
	protected byte type;
	
	public static final byte REQUEST = 0;
	public static final byte DATA = 1;
	
	public AbstractPingPongPacket(final byte type) {
		this.type = type;
	}
	
	public AbstractPingPongPacket() {
		this(REQUEST);
	}
	
	protected abstract void sendRequest(final ActiveConnection connection);
	
	protected abstract void sendData(final ActiveConnection connection);
	
	protected abstract void receiveRequest(final ActiveConnection connection);
	
	protected abstract void receiveData(final ActiveConnection connection);
	
	protected abstract void executeRequest(final ActiveConnection connection);
	
	protected abstract void executeData(final ActiveConnection connection);
	
	@Override
	public final void send(final ActiveConnection connection) {
		connection.writeByte(type);
		
		if (type == REQUEST) {
			sendRequest(connection);
		} else if (type == DATA) {
			sendData(connection);
		}
	}
	
	@Override
	public final void receive(final ActiveConnection connection) {
		type = connection.readByte();
		
		if (type == REQUEST) {
			receiveRequest(connection);
		} else if (type == DATA) {
			receiveData(connection);
		}
	}
	
	@Override
	public final void execute(final ActiveConnection connection) {
		if (type == REQUEST) {
			executeRequest(connection);
		} else if (type == DATA) {
			executeData(connection);
		}
	}
	
	public final byte getType() {
		return type;
	}
	
}
