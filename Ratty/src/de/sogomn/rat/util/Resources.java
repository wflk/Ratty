package de.sogomn.rat.util;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import de.sogomn.engine.fx.SpriteSheet;
import de.sogomn.engine.util.ImageUtils;

public final class Resources {
	
	private static final BufferedImage GUI_ICON_SMALL = ImageUtils.loadImage("/gui_icon.png");
	private static final BufferedImage GUI_ICON_MEDIUM = ImageUtils.scaleImage(GUI_ICON_SMALL, 64, 64);
	private static final BufferedImage GUI_ICON_LARGE = ImageUtils.scaleImage(GUI_ICON_SMALL, 128, 128);
	
	public static final List<BufferedImage> GUI_ICONS = Arrays.asList(GUI_ICON_SMALL, GUI_ICON_MEDIUM, GUI_ICON_LARGE);
	public static final BufferedImage[] MENU_ICONS = new SpriteSheet("/gui_menu_icons.png", 16, 16).getSprites();
	public static final BufferedImage[] CATEGORY_ICONS = new SpriteSheet("/gui_category_icons.png", 16, 16).getSprites();
	public static final BufferedImage[] NOTIFICATION_ICONS = new SpriteSheet("/gui_notification_icons.png", 16, 16).getSprites();
	public static final BufferedImage[] FILE_ICONS = new SpriteSheet("/gui_file_icons.png", 16, 16).getSprites();
	public static final BufferedImage[] TREE_ICONS = new SpriteSheet("/gui_tree_icons.png", 16, 16).getSprites();
	
	private Resources() {
		//...
	}
	
}
