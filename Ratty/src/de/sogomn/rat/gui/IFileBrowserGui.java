/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat.gui;

import static de.sogomn.rat.util.Constants.LANGUAGE;

import java.nio.file.Path;

public interface IFileBrowserGui extends IGui {
	
	String REQUEST = LANGUAGE.getString("action.request_files");
	String DOWNLOAD = LANGUAGE.getString("action.download");
	String UPLOAD = LANGUAGE.getString("action.upload");
	String EXECUTE = LANGUAGE.getString("action.execute");
	String DELETE = LANGUAGE.getString("action.delete");
	String NEW_DIRECTORY = LANGUAGE.getString("action.new_directory");
	String DROP_FILE = LANGUAGE.getString("action.drop_file");
	String INFORMATION = LANGUAGE.getString("action.file_information");
	
	void setDirectory(final Path directory);
	
	void addFile(final Path file);
	
	void removeFile(final Path file);
	
	void clearFiles();
	
	Path getDirectory();
	
	Path getSelectedFile();
	
}
