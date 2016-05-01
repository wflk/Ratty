/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat.util;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;

import de.sogomn.engine.fx.SpriteSheet;
import de.sogomn.engine.util.ImageUtils;

public final class Resources {
	
	private static final BufferedImage GUI_ICON_SMALL = ImageUtils.loadImage("/gui_icon.png");
	private static final BufferedImage GUI_ICON_NORMAL = ImageUtils.scaleImage(GUI_ICON_SMALL, 32, 32);
	private static final BufferedImage GUI_ICON_MEDIUM = ImageUtils.scaleImage(GUI_ICON_SMALL, 64, 64);
	private static final BufferedImage GUI_ICON_LARGE = ImageUtils.scaleImage(GUI_ICON_SMALL, 128, 128);
	
	public static final List<BufferedImage> GUI_ICONS = Arrays.asList(GUI_ICON_SMALL, GUI_ICON_NORMAL, GUI_ICON_MEDIUM, GUI_ICON_LARGE);
	
	public static final BufferedImage[] MENU_ICONS = new SpriteSheet(ImageUtils.scaleImage(ImageUtils.loadImage("/gui_menu_icons.png"), 2), 16 * 2, 16 * 2).getSprites();
	public static final BufferedImage[] CATEGORY_ICONS = new SpriteSheet(ImageUtils.scaleImage(ImageUtils.loadImage("/gui_category_icons.png"), 2), 16 * 2, 16 * 2).getSprites();
	public static final BufferedImage[] NOTIFICATION_ICONS = new SpriteSheet("/gui_notification_icons.png", 16, 16).getSprites();
	public static final BufferedImage[] FILE_ICONS = new SpriteSheet("/gui_file_icons.png", 16, 16).getSprites();
	public static final BufferedImage[] TREE_ICONS = new SpriteSheet(ImageUtils.scaleImage(ImageUtils.loadImage("/gui_tree_icons.png"), 2), 16 * 2, 16 * 2).getSprites();
	
	public static final ImageIcon FILE_ICON = new ImageIcon(FILE_ICONS[3]);
	public static final ImageIcon DIRECTORY_ICON = new ImageIcon(FILE_ICONS[1]);
	public static final ImageIcon UP_ICON = new ImageIcon(FILE_ICONS[8]);
	
	private Resources() {
		//...
	}
	
}
