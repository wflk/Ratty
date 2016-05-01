/*
 * Copyright 2016 Johannes Boczek
 */

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
	String REQUEST_ROOT = "ROOTS";
	
	void setDirectory(final String path);
	
	void addFile(final String path);
	
	void addRoot(final String root);
	
	void removeFile(final String path);
	
	void removeRoot(final String root);
	
	void clearFiles();
	
	void clearRoots();
	
	boolean isDirectory(final String path);
	
	String getCurrentDirectory();
	
	String getSelectedFile();
	
	String getSelectedRoot();
	
}
