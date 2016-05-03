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

package de.sogomn.rat.gui.swing;

import javax.swing.Icon;

import de.sogomn.rat.gui.IBuilderGui;
import de.sogomn.rat.gui.IChatGui;
import de.sogomn.rat.gui.IDisplayGui;
import de.sogomn.rat.gui.IFileBrowserGui;
import de.sogomn.rat.gui.ILoggingGui;
import de.sogomn.rat.gui.INotificationGui;
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
	
	@Override
	public INotificationGui createNotificationGui(final String text, final Icon icon) {
		return new NotificationSwingGui(text, icon);
	}
	
}
