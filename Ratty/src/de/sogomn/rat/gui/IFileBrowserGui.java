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

package de.sogomn.rat.gui;

import static de.sogomn.rat.util.Constants.LANGUAGE;

public interface IFileBrowserGui extends IGui {
	
	String REQUEST = LANGUAGE.getString("action.request_files");
	String DOWNLOAD = LANGUAGE.getString("action.download");
	String UPLOAD = LANGUAGE.getString("action.upload");
	String EXECUTE = LANGUAGE.getString("action.execute");
	String DELETE = LANGUAGE.getString("action.delete");
	String NEW_DIRECTORY = LANGUAGE.getString("action.new_directory");
	String DROP_FILE = LANGUAGE.getString("action.drop_file");
	String INFORMATION = LANGUAGE.getString("action.file_information");
	String DIRECTORY_UP = LANGUAGE.getString("action.directory_up");
	String REQUEST_ROOT = "ROOTS";
	
	void addFile(final String name);
	
	void addFilePath(final String path);
	
	void addDirectory(final String name);
	
	void addDirectoryPath(final String path);
	
	void addRoot(final String name);
	
	void removeEntry(final String name);
	
	void removeEntryPath(final String path);
	
	void removeRoot(final String name);
	
	void clearFiles();
	
	void clearRoots();
	
	void setDirectoryPath(final String path);
	
	boolean isDirectory(final String name);
	
	boolean isDirectoryPath(final String path);
	
	String getCurrentDirectoryPath();
	
	String getParentDirectory();
	
	String getSelectedFile();
	
	String getSelectedFilePath();
	
	String getSelectedRoot();
	
}
