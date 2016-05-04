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

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.MouseEvent;

import de.sogomn.rat.ActiveConnection;

public final class MouseEventPacket implements IPacket {
	
	private int x, y;
	private int button;
	private byte eventType;
	
	public static final byte PRESS = 0;
	public static final byte RELEASE = 1;
	public static final byte CLICK = 2;
	public static final byte MOVE = 3;
	
	public MouseEventPacket(final int x, final int y, final int button, final byte strokeType) {
		this.x = x;
		this.y = y;
		this.button = button;
		this.eventType = strokeType;
	}
	
	public MouseEventPacket() {
		this(0, 0, MouseEvent.NOBUTTON, CLICK);
	}
	
	@Override
	public void send(final ActiveConnection connection) {
		connection.writeInt(x);
		connection.writeInt(y);
		connection.writeInt(button);
		connection.writeByte(eventType);
	}
	
	@Override
	public void receive(final ActiveConnection connection) {
		x = connection.readInt();
		y = connection.readInt();
		button = connection.readInt();
		eventType = connection.readByte();
	}
	
	@Override
	public void execute(final ActiveConnection connection) {
		try {
			final Robot rob = new Robot();
			
			if (eventType == PRESS) {
				rob.mouseMove(x, y);
				rob.mousePress(button);
			} else if (eventType == RELEASE) {
				rob.mouseMove(x, y);
				rob.mouseRelease(button);
			} else if (eventType == CLICK) {
				rob.mouseMove(x, y);
				rob.mousePress(button);
				rob.mousePress(button);
			} else if (eventType == MOVE) {
				rob.mouseMove(x, y);
			}
		} catch (final IllegalArgumentException ex) {
			System.err.println("No valid mouse button");
		} catch (final AWTException ex) {
			ex.printStackTrace();
		}
	}
	
}
