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

package de.sogomn.rat.packet;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import de.sogomn.engine.util.ImageUtils;
import de.sogomn.rat.ActiveConnection;



public final class PopupPacket implements IPacket {
	
	private String message;
	
	public PopupPacket(final String message) {
		this.message = message;
	}
	
	public PopupPacket() {
		this("");
	}
	
	@Override
	public void send(final ActiveConnection connection) {
		connection.writeUtf(message);
	}
	
	@Override
	public void receive(final ActiveConnection connection) {
		message = connection.readUtf();
	}
	
	@Override
	public void execute(final ActiveConnection connection) {
		final JOptionPane optionPane = new JOptionPane(message);
		final JDialog dialog = optionPane.createDialog(null);
		
		dialog.setAlwaysOnTop(true);
		dialog.setIconImage(ImageUtils.EMPTY_IMAGE);
		dialog.setModal(false);
		dialog.setVisible(true);
	}
	
	public String getMessage() {
		return message;
	}
	
}
