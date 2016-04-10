package de.sogomn.rat.util;

import static de.sogomn.rat.util.Resources.FILE_ICONS;
import static de.sogomn.rat.util.Resources.NOTIFICATION_ICONS;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.Painter;
import javax.swing.UIDefaults;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;

import de.sogomn.engine.util.ImageUtils;
import de.sogomn.rat.Server;

/*
 * WHEEE! Hardcoding!
 */
final class NimbusGuiSettings {
	
	private static final Color BACKGROUND = new Color(250, 250, 255);
	private static final Color BASE = new Color(220, 220, 220);
	private static final Color DARKER = new Color(200, 200, 200);
	private static final Color ALTERNATIVE = new Color(235, 235, 235);
	private static final Color SELECTION = new Color(165, 165, 165);
	
	private static final EmptyBorder TABLE_CELL_BORDER = new EmptyBorder(2, 5, 2, 5);
	
	private static final Font FONT;
	
	private static final ImageIcon ERROR_ICON = new ImageIcon(ImageUtils.scaleImage(NOTIFICATION_ICONS[0], 2));
	private static final ImageIcon INFORMATION_ICON = new ImageIcon(ImageUtils.scaleImage(NOTIFICATION_ICONS[1], 2));
	private static final ImageIcon QUESTION_ICON = new ImageIcon(ImageUtils.scaleImage(NOTIFICATION_ICONS[2], 2));
	private static final ImageIcon WARNING_ICON = new ImageIcon(ImageUtils.scaleImage(NOTIFICATION_ICONS[3], 2));
	private static final ImageIcon SAVE_ICON = new ImageIcon(FILE_ICONS[0]);
	private static final ImageIcon FOLDER_ICON = new ImageIcon(FILE_ICONS[1]);
	private static final ImageIcon FOLDER_OPEN_ICON = new ImageIcon(FILE_ICONS[2]);
	private static final ImageIcon FILE_ICON = new ImageIcon(FILE_ICONS[3]);
	private static final ImageIcon LIST_ICON = new ImageIcon(FILE_ICONS[4]);
	private static final ImageIcon DISC_ICON = new ImageIcon(FILE_ICONS[5]);
	private static final ImageIcon HOME_ICON = new ImageIcon(FILE_ICONS[6]);
	private static final ImageIcon NEW_FOLDER_ICON = new ImageIcon(FILE_ICONS[7]);
	private static final ImageIcon UP_FOLDER_ICON = new ImageIcon(FILE_ICONS[8]);
	private static final ImageIcon DETAILS_ICON = new ImageIcon(FILE_ICONS[9]);
	
	private static final Painter<?> BASE_PAINTER = (g, object, width, height) -> {
		g.setColor(BASE);
		g.fillRect(0, 0, width, height);
	};
	
	private static final Painter<?> SELECTION_PAINTER = (g, object, width, height) -> {
		g.setColor(SELECTION);
		g.fillRect(0, 0, width, height);
	};
	
	private static final Painter<?> SEPARATOR_PAINTER = (g, object, width, height) -> {
		g.setColor(DARKER);
		g.fillRect(0, height / 2, width, height / 4);
	};
	
	private static final Painter<?> BACKGROUND_PAINTER = (g, object, width, height) -> {
		g.setColor(BACKGROUND);
		g.fillRect(0, 0, width, height);
	};
	
	private static final Painter<?> BUTTON_PAINTER = (g, object, width, height) -> {
		final GradientPaint gradient = new GradientPaint(0, 0, Color.WHITE, 0, height, new Color(160, 160, 160));
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setPaint(gradient);
		g.fillRoundRect(0, 0, width, height, 25, 50);
		g.setPaint(new Color(190, 190, 190));
		g.drawRoundRect(0, 0, width - 1, height - 1, 25, 50);
	};
	
	private static final Painter<?> BUTTON_HOVERED_PAINTER = (g, object, width, height) -> {
		final GradientPaint gradient = new GradientPaint(0, 0, Color.WHITE, 0, height, new Color(180, 180, 180));
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setPaint(gradient);
		g.fillRoundRect(0, 0, width, height, 25, 50);
		g.setPaint(new Color(180, 180, 180));
		g.drawRoundRect(0, 0, width - 1, height - 1, 25, 50);
	};
	
	private static final Painter<?> BUTTON_PRESSED_PAINTER = (g, object, width, height) -> {
		final GradientPaint gradient = new GradientPaint(0, 0, new Color(180, 180, 180), 0, height, new Color(230, 230, 230));
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setPaint(gradient);
		g.fillRoundRect(0, 0, width, height, 25, 50);
		g.setPaint(new Color(210, 210, 210));
		g.drawRoundRect(0, 0, width - 1, height - 1, 25, 50);
	};
	
	static {
		Font newFont;
		
		try {
			newFont = Font.createFont(Font.TRUETYPE_FONT, Server.class.getResourceAsStream("/lato.ttf")).deriveFont(13f);
		} catch (final IOException | FontFormatException ex) {
			ex.printStackTrace();
			
			newFont = new Font("Trebuchet MS", Font.PLAIN, 13);
		}
		
		FONT = newFont;
	}
	
	private NimbusGuiSettings() {
		//...
	}
	
	public static void setDefaults(final UIDefaults defaults) {
		defaults.put("nimbusBase", Color.GRAY);
		defaults.put("control", BACKGROUND);
		defaults.put("nimbusSelection", Color.WHITE);
		defaults.put("nimbusFocus", Color.WHITE);
		defaults.put("textHighlight", SELECTION);
		
		defaults.put("TextArea[Enabled].backgroundPainter", BACKGROUND_PAINTER);
		defaults.put("TextField[Enabled].backgroundPainter", BACKGROUND_PAINTER);
		
		defaults.put("Button[Enabled].backgroundPainter", BUTTON_PAINTER);
		defaults.put("Button[Default].backgroundPainter", BUTTON_PAINTER);
		defaults.put("Button[Focused].backgroundPainter", BUTTON_PAINTER);
		defaults.put("Button[Default+Focused].backgroundPainter", BUTTON_PAINTER);
		defaults.put("Button[MouseOver].backgroundPainter", BUTTON_HOVERED_PAINTER);
		defaults.put("Button[Default+MouseOver].backgroundPainter", BUTTON_HOVERED_PAINTER);
		defaults.put("Button[Focused+MouseOver].backgroundPainter", BUTTON_HOVERED_PAINTER);
		defaults.put("Button[Default+Focused+MouseOver].backgroundPainter", BUTTON_HOVERED_PAINTER);
		defaults.put("Button[Pressed].backgroundPainter", BUTTON_PRESSED_PAINTER);
		defaults.put("Button[Default+Pressed].backgroundPainter", BUTTON_PRESSED_PAINTER);
		defaults.put("Button[Focused+Pressed].backgroundPainter", BUTTON_PRESSED_PAINTER);
		defaults.put("Button[Default+Focused+Pressed].backgroundPainter", BUTTON_PRESSED_PAINTER);
		
		defaults.put("ToggleButton[Enabled].backgroundPainter", BUTTON_PAINTER);
		defaults.put("ToggleButton[Focused].backgroundPainter", BUTTON_PAINTER);
		defaults.put("ToggleButton[MouseOver].backgroundPainter", BUTTON_HOVERED_PAINTER);
		defaults.put("ToggleButton[Focused+MouseOver].backgroundPainter", BUTTON_HOVERED_PAINTER);
		defaults.put("ToggleButton[Pressed].backgroundPainter", BUTTON_HOVERED_PAINTER);
		defaults.put("ToggleButton[Focused+Pressed].backgroundPainter", BUTTON_HOVERED_PAINTER);
		defaults.put("ToggleButton[Selected].backgroundPainter", BUTTON_PRESSED_PAINTER);
		defaults.put("ToggleButton[Focused+Selected].backgroundPainter", BUTTON_PRESSED_PAINTER);
		defaults.put("ToggleButton[MouseOver+Selected].backgroundPainter", BUTTON_PRESSED_PAINTER);
		defaults.put("ToggleButton[Focused+MouseOver+Selected].backgroundPainter", BUTTON_PRESSED_PAINTER);
		defaults.put("ToggleButton[Pressed+Selected].backgroundPainter", BUTTON_PRESSED_PAINTER);
		defaults.put("ToggleButton[Focused+Pressed+Selected].backgroundPainter", BUTTON_PRESSED_PAINTER);
		
		defaults.put("Table.background", new ColorUIResource(ALTERNATIVE));
		defaults.put("Table.gridColor", DARKER);
		defaults.put("Table:\"Table.cellRenderer\".background", ALTERNATIVE);
		defaults.put("Table.alternateRowColor", ALTERNATIVE);
		defaults.put("Table[Enabled+Selected].textBackground", SELECTION);
		defaults.put("Table.focusCellHighlightBorder", TABLE_CELL_BORDER);
		
		defaults.put("Menu.background", BASE);
		defaults.put("Menu[Enabled+Selected].backgroundPainter", SELECTION_PAINTER);
		defaults.put("PopupMenu[Enabled].backgroundPainter", BASE_PAINTER);
		defaults.put("MenuItem[MouseOver].backgroundPainter", SELECTION_PAINTER);
		defaults.put("MenuBar[Enabled].backgroundPainter", BASE_PAINTER);
		defaults.put("PopupMenuSeparator[Enabled].backgroundPainter", SEPARATOR_PAINTER);
		
		defaults.put("Tree:TreeCell[Enabled+Selected].backgroundPainter", SELECTION_PAINTER);
		defaults.put("Tree:TreeCell[Focused+Selected].backgroundPainter", SELECTION_PAINTER);
		
		defaults.put("List[Selected].textBackground", SELECTION);
		defaults.put("List[Selected].textForeground", Color.WHITE);
		
		defaults.put("Button.font", FONT);
		defaults.put("Table.font", FONT);
		defaults.put("Label.font", FONT);
		defaults.put("TableHeader.font", FONT);
		defaults.put("FileChooser.font", FONT);
		defaults.put("TextField.font", FONT);
		defaults.put("TextArea.font", FONT);
		defaults.put("FormattedTextField.font", FONT);
		defaults.put("PopupMenu.font", FONT);
		defaults.put("Menu.font", FONT);
		defaults.put("MenuItem.font", FONT);
		defaults.put("Panel.font", FONT);
		defaults.put("Tree.font", FONT);
		defaults.put("ToggleButton.font", FONT);
		defaults.put("List.font", FONT);
		defaults.put("OptionPane.font", FONT);
		defaults.put("ComboBox.font", FONT);
		
		defaults.put("OptionPane.errorIcon", ERROR_ICON);
		defaults.put("OptionPane.informationIcon", INFORMATION_ICON);
		defaults.put("OptionPane.questionIcon", QUESTION_ICON);
		defaults.put("OptionPane.warningIcon", WARNING_ICON);
		defaults.put("FileChooser.directoryIcon", FOLDER_ICON);
		defaults.put("FileChooser.floppyDriveIcon", SAVE_ICON);
		defaults.put("FileChooser.homeFolderIcon", HOME_ICON);
		defaults.put("FileChooser.listViewIcon", LIST_ICON);
		defaults.put("FileChooser.newFolderIcon", NEW_FOLDER_ICON);
		defaults.put("FileChooser.upFolderIcon", UP_FOLDER_ICON);
		defaults.put("FileChooser.hardDriveIcon", DISC_ICON);
		defaults.put("FileChooser.detailsViewIcon", DETAILS_ICON);
		defaults.put("Tree.closedIcon", FOLDER_ICON);
		defaults.put("Tree.openIcon", FOLDER_OPEN_ICON);
		defaults.put("Tree.leafIcon", FILE_ICON);
	}
	
}
