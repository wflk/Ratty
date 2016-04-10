package de.sogomn.rat.util;

import java.awt.image.BufferedImage;

import de.sogomn.engine.fx.SpriteSheet;
import de.sogomn.engine.util.ImageUtils;

public final class Resources {
	
	public static final BufferedImage[] MENU_ICONS = new SpriteSheet(ImageUtils.scaleImage(ImageUtils.loadImage("/gui_menu_icons.png"), 2), 16 * 2, 16 * 2).getSprites();
	public static final BufferedImage[] CATEGORY_ICONS = new SpriteSheet(ImageUtils.scaleImage(ImageUtils.loadImage("/gui_category_icons.png"), 2), 16 * 2, 16 * 2).getSprites();
	public static final BufferedImage[] NOTIFICATION_ICONS = new SpriteSheet("/gui_notification_icons.png", 16, 16).getSprites();
	public static final BufferedImage[] FILE_ICONS = new SpriteSheet("/gui_file_icons.png", 16, 16).getSprites();
	
	private Resources() {
		//...
	}
	
}
