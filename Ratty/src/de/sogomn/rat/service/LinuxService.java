/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat.service;

import java.io.File;

public final class LinuxService implements IOperatingSystemService {
	
	LinuxService() {
		//...
	}
	
	@Override
	public void shutDown() {
		//...
	}
	
	@Override
	public void restart() {
		//...
	}
	
	@Override
	public void addToStartup(final File file) {
		//...
	}
	
	@Override
	public void removeFromStartup(final String name) {
		//...
	}
	
	public boolean isVm() {
		return false;
	}
	
}
