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

package de.sogomn.rat.util;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import de.sogomn.engine.util.ImageUtils;

public final class FrameEncoder {
	
	private static final int SKIP = 6;
	private static final int TOLERANCE = 50;
	
	private static final int CELLS_WIDE = 6;
	private static final int CELLS_HIGH = 6;
	private static final Frame[] EMPTY_ARRAY = new Frame[0];
	private static final int CURSOR_SIZE = 6;
	private static final Stroke CURSOR_STROKE = new BasicStroke(2);
	
	private FrameEncoder() {
		//...
	}
	
	private static boolean isEqual(final int rgb1, final int rgb2, final int tolerance) {
		final int red1 = (rgb1 >> 16) & 0xff;
		final int green1 = (rgb1 >> 8) & 0xff;
		final int blue1 = rgb1 & 0xff;
		final int red2 = (rgb1 >> 16) & 0xff;
		final int green2 = (rgb2 >> 8) & 0xff;
		final int blue2 = rgb2 & 0xff;
		final int red = Math.abs(red1 - red2);
		final int green = Math.abs(green1 - green2);
		final int blue = Math.abs(blue1 - blue2);
		
		if (red <= tolerance && green <= tolerance && blue <= tolerance) {
			return true;
		}
		
		return false;
	}
	
	public static BufferedImage takeScreenshot() {
		final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		final Rectangle screenRect = new Rectangle(screen);
		
		try {
			final Robot robot = new Robot();
			final BufferedImage image = robot.createScreenCapture(screenRect);
			
			return image;
		} catch (final AWTException ex) {
			ex.printStackTrace();
			
			return null;
		}
	}
	
	public static BufferedImage captureScreen() {
		final BufferedImage image = takeScreenshot();
		
		if (image == null) {
			return null;
		}
		
		final Point mousePoint = MouseInfo.getPointerInfo().getLocation();
		final int mouseX = (int)(mousePoint.x - CURSOR_SIZE / 2);
		final int mouseY = (int)(mousePoint.y - CURSOR_SIZE / 2);
		final Graphics2D g = image.createGraphics();
		
		ImageUtils.applyHighGraphics(g);
		
		g.setStroke(CURSOR_STROKE);
		g.setColor(Color.RED);
		g.drawOval(mouseX, mouseY, CURSOR_SIZE, CURSOR_SIZE);
		g.dispose();
		
		return image;
	}
	
	public static Frame[] getIFrames(final BufferedImage previous, final BufferedImage next) {
		final int width = previous.getWidth();
		final int height = previous.getHeight();
		
		if (next.getWidth() != width || next.getHeight() != height) {
			return EMPTY_ARRAY;
		}
		
		final int cellWidth = width / CELLS_WIDE;
		final int cellHeight = height / CELLS_HIGH;
		final ArrayList<Frame> frames = new ArrayList<Frame>();
		
		
		for (int x = 0; x < CELLS_WIDE; x++) {
			for (int y = 0; y < CELLS_HIGH; y++) {
				final int cellX = x * cellWidth;
				final int cellY = y * cellHeight;
				final int cellEndX = cellX + cellWidth;
				final int cellEndY = cellY + cellHeight;
				
				outer:
				for (int xx = cellX; xx < cellEndX && xx < width; xx += SKIP) {
					for (int yy = cellY; yy < cellEndY && yy < height; yy += SKIP) {
						final int previousRgb = previous.getRGB(xx, yy);
						final int nextRgb = next.getRGB(xx, yy);
						final boolean equal = isEqual(previousRgb, nextRgb, TOLERANCE);
						
						if (equal) {
							continue;
						}
						
						final BufferedImage image = next.getSubimage(cellX, cellY, cellWidth, cellHeight);
						final Frame frame = new Frame(cellX, cellY, image);
						
						frames.add(frame);
						
						break outer;
					}
				}
				
			}
		}
		
		final Frame[] framesArray = frames.stream().toArray(Frame[]::new);
		
		return framesArray;
	}
	
	public static final class Frame {
		
		public final int x, y;
		public final BufferedImage image;
		
		public static final Frame EMPTY = new Frame(0, 0, ImageUtils.EMPTY_IMAGE);
		
		public Frame(final int x, final int y, final BufferedImage image) {
			this.x = x;
			this.y = y;
			this.image = image;
		}
		
	}
	
}
