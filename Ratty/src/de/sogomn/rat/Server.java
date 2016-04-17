package de.sogomn.rat;

import static de.sogomn.rat.util.Constants.LANGUAGE;

import javax.swing.JOptionPane;

import de.sogomn.rat.gui.server.RattyGui;
import de.sogomn.rat.gui.server.RattyGuiController;
import de.sogomn.rat.util.Constants;

public final class Server {
	
	private static final boolean DEBUG = true;
	private static final int DEBUG_PORT = 23456;
	
	private static final String DEBUG_MESSAGE = LANGUAGE.getString("debug.question");
	private static final String DEBUG_SERVER = LANGUAGE.getString("debug.server");
	private static final String DEBUG_CLIENT = LANGUAGE.getString("debug.client");
	
	private Server() {
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
			
			Client.startClient("localhost", DEBUG_PORT);
		}
	}
	
	public static void startServer() {
		Constants.setNimbusLookAndFeel();
		
		final RattyGui gui = new RattyGui();
		final RattyGuiController controller = new RattyGuiController(gui);
		
		gui.addListener(controller);
		gui.setVisible(true);
	}
	
	public static void startServer(final int port) {
		Constants.setNimbusLookAndFeel();
		
		final RattyGui gui = new RattyGui();
		final RattyGuiController controller = new RattyGuiController(gui);
		
		controller.startServer(port);
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
