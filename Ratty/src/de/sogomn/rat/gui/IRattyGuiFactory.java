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

import javax.swing.Icon;

public interface IRattyGuiFactory {
	
	IRattyGui createRattyGui();
	
	IBuilderGui createBuilderGui();
	
	IServerListGui createServerListGui();
	
	IFileBrowserGui createFileBrowserGui();
	
	IDisplayGui createDisplayGui();
	
	IChatGui createChatGui();
	
	ILoggingGui createLoggingGui();
	
	INotificationGui createNotificationGui(final String text, final Icon icon);
	
	default INotificationGui createNotificationGui(final String text) {
		return createNotificationGui(text, null);
	}
	
}
