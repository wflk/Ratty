/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat.gui;


public interface IRattyGuiFactory {
	
	IRattyGui createRattyGui();
	
	IBuilderGui createBuilderGui();
	
	IServerListGui createServerListGui();
	
	IFileBrowserGui createFileBrowserGui();
	
	IDisplayGui createDisplayGui();
	
	IChatGui createChatGui();
	
	ILoggingGui createLoggingGui();
	
}
