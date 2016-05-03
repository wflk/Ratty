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

package de.sogomn.rat;

import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import de.sogomn.engine.net.TCPConnection;
import de.sogomn.rat.packet.IPacket;
import de.sogomn.rat.packet.PacketType;


public final class ActiveConnection extends TCPConnection {
	
	private LinkedBlockingQueue<IPacket> packetQueue;
	private Thread sender, reader;
	
	private IConnectionObserver observer;
	
	public ActiveConnection(final String address, final int port) {
		super(address, port);
		
		packetQueue = new LinkedBlockingQueue<IPacket>();
	}
	
	public ActiveConnection(final Socket socket) {
		super(socket);
		
		packetQueue = new LinkedBlockingQueue<IPacket>();
	}
	
	private IPacket nextPacket() {
		try {
			final IPacket packet = packetQueue.take();
			
			return packet;
		} catch (final Exception ex) {
			return null;
		}
	}
	
	private void sendPacket(final IPacket packet) {
		final byte id = PacketType.getId(packet);
		
		if (id != 0) {
			writeByte(id);
			packet.send(this);
		}
	}
	
	private IPacket readPacket() {
		final byte id = readByte();
		final Class<? extends IPacket> packetClass = PacketType.getClass(id);
		
		if (packetClass == null) {
			return null;
		}
		
		try {
			final IPacket packet = packetClass.newInstance();
			
			packet.receive(this);
			
			return packet;
		} catch (final Exception ex) {
			ex.printStackTrace();
			
			return null;
		}
	}
	
	@Override
	public void close() {
		super.close();
		
		if (sender != null) {
			sender.interrupt();
			sender = null;
		}
		
		if (reader != null) {
			reader.interrupt();
			reader = null;
		}
		
		if (packetQueue != null) {
			packetQueue.clear();
		}
		
		if (observer != null) {
			observer.disconnected(this);
		}
	}
	
	public void start() {
		final Runnable sendingRunnable = () -> {
			while (isOpen()) {
				final IPacket packet = nextPacket();
				
				if (packet == null) {
					break;
				}
				
				if (packet != null) {
					sendPacket(packet);
				}
			}
		};
		
		final Runnable readingRunnable = () -> {
			while (isOpen()) {
				final IPacket packet = readPacket();
				
				if (packet == null) {
					break;
				}
				
				if (observer != null) {
					observer.packetReceived(this, packet);
				}
			}
		};
		
		sender = new Thread(sendingRunnable, "Sender");
		reader = new Thread(readingRunnable, "Reader");
		
		sender.start();
		reader.start();
	}
	
	public void clearPackets() {
		packetQueue.clear();
	}
	
	public void addPacket(final IPacket packet) {
		packetQueue.add(packet);
	}
	
	public void removePacket(final IPacket packet) {
		packetQueue.remove(packet);
	}
	
	public void setObserver(final IConnectionObserver observer) {
		this.observer = observer;
	}
	
	public boolean isIdling() {
		return packetQueue.isEmpty();
	}
	
}
