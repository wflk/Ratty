/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat.gui.swing;

import de.sogomn.rat.gui.IBuilderGui;
import de.sogomn.rat.gui.IChatGui;
import de.sogomn.rat.gui.IDisplayGui;
import de.sogomn.rat.gui.IFileBrowserGui;
import de.sogomn.rat.gui.ILoggingGui;
import de.sogomn.rat.gui.IRattyGui;
import de.sogomn.rat.gui.IRattyGuiFactory;
import de.sogomn.rat.gui.IServerListGui;


public final class RattySwingGuiFactory implements IRattyGuiFactory {
	
	public RattySwingGuiFactory() {
		//...
	}
	
	@Override
	public IRattyGui createRattyGui() {
		return new RattySwingGui();
	}
	
	@Override
	public IBuilderGui createBuilderGui() {
		return new BuilderSwingGui();
	}
	
	@Override
	public IServerListGui createServerListGui() {
		return new ServerListSwingGui();
	}
	
	@Override
	public IFileBrowserGui createFileBrowserGui() {
		return new FileBrowserSwingGui();
	}
	
	@Override
	public IDisplayGui createDisplayGui() {
		return new DisplaySwingGui();
	}
	
	@Override
	public IChatGui createChatGui() {
		return new ChatSwingGui();
	}
	
	@Override
	public ILoggingGui createLoggingGui() {
		return new LoggingSwingGui();
	}
	
}
