/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat;

import static de.sogomn.rat.util.Constants.LANGUAGE;

import javax.swing.JOptionPane;

import de.sogomn.rat.gui.server.BuilderSwingGui;
import de.sogomn.rat.gui.server.IBuilderGui;
import de.sogomn.rat.gui.server.IRattyGui;
import de.sogomn.rat.gui.server.IServerListGui;
import de.sogomn.rat.gui.server.RattyGuiController;
import de.sogomn.rat.gui.server.RattySwingGui;
import de.sogomn.rat.gui.server.ServerListSwingGui;
import de.sogomn.rat.util.Constants;

public final class RattyServer {
	
	private static final boolean DEBUG = true;
	private static final int DEBUG_PORT = 23456;
	
	private static final String DEBUG_MESSAGE = LANGUAGE.getString("debug.question");
	private static final String DEBUG_SERVER = LANGUAGE.getString("debug.server");
	private static final String DEBUG_CLIENT = LANGUAGE.getString("debug.client");
	
	private RattyServer() {
		//...
	}
	
	private static void startDebug() {
		Constants.setSystemLookAndFeel();
		
		final String[] options = {DEBUG_SERVER, DEBUG_CLIENT};
		final int input = JOptionPane.showOptionDialog(null, DEBUG_MESSAGE, null, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
		
		if (input == JOptionPane.YES_OPTION) {
			System.out.println(DEBUG_SERVER);
			
			startServer(DEBUG_PORT);
		} else if (input == JOptionPane.NO_OPTION) {
			System.out.println(DEBUG_CLIENT);
			
			RattyClient.startClient("localhost", DEBUG_PORT);
		}
	}
	
	public static void startServer() {
		Constants.setNimbusLookAndFeel();
		
		final IRattyGui gui = new RattySwingGui();
		final IBuilderGui builder = new BuilderSwingGui();
		final IServerListGui serverList = new ServerListSwingGui();
		final RattyGuiController controller = new RattyGuiController(gui, builder, serverList);
		
		serverList.addListener(controller);
		builder.addListener(controller);
		gui.addListener(controller);
		gui.setVisible(true);
	}
	
	public static void startServer(final int port) {
		Constants.setNimbusLookAndFeel();
		
		final IRattyGui gui = new RattySwingGui();
		final IBuilderGui builder = new BuilderSwingGui();
		final IServerListGui serverList = new ServerListSwingGui();
		final RattyGuiController controller = new RattyGuiController(gui, builder, serverList);
		
		controller.startServer(port);
		builder.addListener(controller);
		gui.addListener(controller);
		gui.setVisible(true);
	}
	
	public static void main(final String[] args) {
		if (DEBUG) {
			startDebug();
		} else {
			startServer();
		}
	}
	
}
