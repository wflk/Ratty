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

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import de.sogomn.engine.fx.SpriteSheet;
import de.sogomn.engine.util.ImageUtils;

public final class Resources {
	
	public static final SpriteSheet ICON_SPRITE_SHEET = new SpriteSheet(ImageUtils.scaleImage(ImageUtils.loadImage("/icons.png"), 2), 32, 32);
	public static final BufferedImage[] ICONS = ICON_SPRITE_SHEET.getSprites();
	
	public static final BufferedImage WINDOW_ICON_SMALL = ICONS[26];
	public static final BufferedImage WINDOW_ICON_NORMAL = ImageUtils.scaleImage(WINDOW_ICON_SMALL, 32, 32);
	public static final BufferedImage WINDOW_ICON_MEDIUM = ImageUtils.scaleImage(WINDOW_ICON_SMALL, 64, 64);
	public static final BufferedImage WINDOW_ICON_LARGE = ImageUtils.scaleImage(WINDOW_ICON_SMALL, 128, 128);
	public static final BufferedImage[] WINDOW_ICONS = {WINDOW_ICON_SMALL, WINDOW_ICON_NORMAL, WINDOW_ICON_MEDIUM, WINDOW_ICON_LARGE};
	public static final List<BufferedImage> WINDOW_ICON_LIST = Arrays.asList(WINDOW_ICONS);
	
	public static final BufferedImage ICON_SPEECH_BUBBLE = ICONS[0];
	public static final BufferedImage ICON_FRAME = ICONS[1];
	public static final BufferedImage ICON_MONITOR = ICONS[2];
	public static final BufferedImage ICON_MICROPHONE = ICONS[3];
	public static final BufferedImage ICON_FLOPPY_DISK = ICONS[4];
	public static final BufferedImage ICON_FOLDER = ICONS[5];
	public static final BufferedImage ICON_FILE = ICONS[6];
	public static final BufferedImage ICON_TERMINAL = ICONS[7];
	public static final BufferedImage ICON_CLIPBOARD = ICONS[8];
	public static final BufferedImage ICON_MUSIC = ICONS[9];
	public static final BufferedImage ICON_LIST = ICONS[10];
	public static final BufferedImage ICON_HARD_DRIVE = ICONS[11];
	public static final BufferedImage ICON_WORLD = ICONS[12];
	public static final BufferedImage ICON_TARGET_MARKER = ICONS[13];
	public static final BufferedImage ICON_CROSS = ICONS[14];
	public static final BufferedImage ICON_WORLD_TARGET = ICONS[15];
	public static final BufferedImage ICON_FOLDER_UP = ICONS[16];
	public static final BufferedImage ICON_SEARCH = ICONS[17];
	public static final BufferedImage ICON_SPEECH_BUBBLES = ICONS[18];
	public static final BufferedImage ICON_EXCLAMATION_MARK = ICONS[19];
	public static final BufferedImage ICON_GEAR = ICONS[20];
	public static final BufferedImage ICON_KEYBOARD = ICONS[21];
	public static final BufferedImage ICON_FOLDER_OPEN = ICONS[22];
	public static final BufferedImage ICON_POINTER = ICONS[23];
	public static final BufferedImage ICON_EXCLAMATION_CIRCLE_RED = ICONS[24];
	public static final BufferedImage ICON_EXCLAMATION_CIRCLE_YELLOW = ICONS[25];
	public static final BufferedImage ICON_RAT = ICONS[26];
	public static final BufferedImage ICON_DOTS = ICONS[27];
	public static final BufferedImage ICON_HOUSE = ICONS[28];
	public static final BufferedImage ICON_FOLDER_PLUS = ICONS[29];
	public static final BufferedImage ICON_EYE = ICONS[30];
	public static final BufferedImage ICON_WORLD_DOWN = ICONS[31];
	public static final BufferedImage ICON_WRENCH = ICONS[32];
	public static final BufferedImage ICON_ERROR = ICONS[33];
	public static final BufferedImage ICON_INFORMATION = ICONS[34];
	public static final BufferedImage ICON_FILE_DELETE = ICONS[35];
	public static final BufferedImage ICON_QUESTION_MARK = ICONS[36];
	public static final BufferedImage ICON_ARROW_DOWN = ICONS[37];
	public static final BufferedImage ICON_ARROW_UP = ICONS[38];
	public static final BufferedImage ICON_QUESTION = ICONS[39];
	public static final BufferedImage ICON_WARNING = ICONS[40];
	public static final BufferedImage ICON_SERVER = ICONS[41];
	public static final BufferedImage ICON_CROSSHAIR = ICONS[42];
	
	public static final BufferedImage[] ICONS_SURVEILLANCE = {
		ICON_FRAME,
		ICON_MONITOR,
		ICON_MICROPHONE,
		ICON_CLIPBOARD,
		ICON_KEYBOARD
	};
	
	public static final BufferedImage[] ICONS_FILE_MANAGEMENT = {
		ICON_FILE,
		ICON_TARGET_MARKER,
		ICON_WORLD_TARGET
	};
	
	public static final BufferedImage[] ICONS_UTILITY = {
		ICON_SPEECH_BUBBLE,
		ICON_TERMINAL,
		ICON_WORLD,
		ICON_MUSIC,
		ICON_SPEECH_BUBBLES
	};
	
	public static final BufferedImage[] ICONS_OTHER = {
		ICON_EXCLAMATION_MARK,
		ICON_EXCLAMATION_CIRCLE_YELLOW,
		ICON_EXCLAMATION_CIRCLE_RED,
		ICON_CROSS,
		ICON_GEAR
	};
	
	private Resources() {
		//...
	}
	
}
