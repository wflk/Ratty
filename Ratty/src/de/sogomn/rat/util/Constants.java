/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat.util;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import de.sogomn.engine.util.FileUtils;
import de.sogomn.rat.service.IOperatingSystemService;

/*
 * More hardcoding!
 */
public final class Constants {
	
	private static final String DATA_PATH = "/data";
	
	public static final String VERSION = "1.29.0";
	public static final ResourceBundle LANGUAGE = ResourceBundle.getBundle("language.lang");
	public static final IOperatingSystemService OS_SERVICE = IOperatingSystemService.getInstance();
	public static final String[] ADDRESSES;
	public static final int[] PORTS;
	public static final Path JAR_FILE;
	
	static {
		final byte[] data = FileUtils.readInternalData(DATA_PATH);
		
		XorCipher.crypt(data);
		
		final String text = new String(data);
		final String[] lines = text.split("\r\n");
		
		ADDRESSES = new String[lines.length];
		PORTS = new int[lines.length];
		
		for (int i = 0; i < lines.length; i++) {
			try {
				final String line = lines[i];
				final String[] connectionData = line.split(":");
				final String address = connectionData[0];
				final int port = Integer.parseInt(connectionData[1]);
				
				ADDRESSES[i] = address;
				PORTS[i] = port;
			} catch (final Exception ex) {
				ADDRESSES[i] = "";
				PORTS[i] = 0;
			}
		}
	}
	
	static {
		Path jarFile = null;
		
		try {
			jarFile = Paths.get(Constants.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		} catch (final URISyntaxException ex) {
			ex.printStackTrace();
		}
		
		JAR_FILE = jarFile;
	}
	
	private Constants() {
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
		NimbusGuiSettings.setDefaults(defaults);
		
		try {
			UIManager.setLookAndFeel(nimbus);
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
