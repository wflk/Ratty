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

public final class ChatPacket implements IPacket {
	
	private String message;
	
	public ChatPacket(final String message) {
		this.message = message;
	}
	
	public ChatPacket() {
		this("");
	}
	
	@Override
	public void send(final ActiveConnection connection) {
		connection.writeUtf(message);
	}
	
	@Override
	public void receive(final ActiveConnection connection) {
		message = connection.readUtf();
	}
	
	@Override
	public void execute(final ActiveConnection connection) {
		//...
	}
	
	public String getMessage() {
		return message;
	}
	
}
