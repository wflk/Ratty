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

import static de.sogomn.rat.util.Resources.ICON_ERROR;
import static de.sogomn.rat.util.Resources.ICON_FILE;
import static de.sogomn.rat.util.Resources.ICON_FLOPPY_DISK;
import static de.sogomn.rat.util.Resources.ICON_FOLDER;
import static de.sogomn.rat.util.Resources.ICON_FOLDER_PLUS;
import static de.sogomn.rat.util.Resources.ICON_FOLDER_UP;
import static de.sogomn.rat.util.Resources.ICON_HARD_DRIVE;
import static de.sogomn.rat.util.Resources.ICON_HOUSE;
import static de.sogomn.rat.util.Resources.ICON_INFORMATION;
import static de.sogomn.rat.util.Resources.ICON_LIST;
import static de.sogomn.rat.util.Resources.ICON_QUESTION;
import static de.sogomn.rat.util.Resources.ICON_SEARCH;
import static de.sogomn.rat.util.Resources.ICON_WARNING;

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

import de.sogomn.rat.RattyServer;

/*
 * WHEEE! Hardcoding!
 */
final class NimbusGuiSettings {
	
	private static final Color BACKGROUND = new Color(250, 250, 255);
	private static final Color DARKER = new Color(200, 200, 200);
	private static final Color ALTERNATIVE = new Color(235, 235, 235);
	private static final Color SELECTION = new Color(185, 185, 185);
	private static final Color SELECTION_BORDER = new Color(225, 225, 225);
	
	private static final EmptyBorder TABLE_CELL_BORDER = new EmptyBorder(2, 5, 2, 5);
	
	private static final Font FONT;
	
	private static final Painter<?> SELECTION_PAINTER = (g, object, width, height) -> {
		g.setColor(SELECTION);
		g.fillRect(0, 0, width, height);
	};
	
	private static final Painter<?> SEPARATOR_PAINTER = (g, object, width, height) -> {
		g.setColor(new Color(190, 190, 190));
		g.fillRect(width / 10, height / 2, width - (width / 10) * 2, height / 4);
	};
	
	private static final Painter<?> BACKGROUND_PAINTER = (g, object, width, height) -> {
		g.setColor(BACKGROUND);
		g.fillRect(0, 0, width, height);
	};
	
	private static final Painter<?> BUTTON_PAINTER = (g, object, width, height) -> {
		final GradientPaint gradient = new GradientPaint(0, 0, new Color(250, 250, 250), 0, height, new Color(190, 190, 190));
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setPaint(gradient);
		g.fillRoundRect(0, 0, width, height, 5, 5);
		g.setPaint(new Color(170, 170, 170));
		g.drawRoundRect(0, 0, width - 1, height - 1, 5, 5);
	};
	
	private static final Painter<?> BUTTON_HOVERED_PAINTER = (g, object, width, height) -> {
		final GradientPaint gradient = new GradientPaint(0, 0, new Color(255, 255, 255), 0, height, new Color(200, 200, 200));
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setPaint(gradient);
		g.fillRoundRect(0, 0, width, height, 5, 5);
		g.setPaint(new Color(160, 160, 160));
		g.drawRoundRect(0, 0, width - 1, height - 1, 5, 5);
	};
	
	private static final Painter<?> BUTTON_PRESSED_PAINTER = (g, object, width, height) -> {
		final GradientPaint gradient = new GradientPaint(0, 0, new Color(200, 200, 200), 0, height, new Color(230, 230, 230));
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setPaint(gradient);
		g.fillRoundRect(0, 0, width, height, 5, 5);
		g.setPaint(new Color(180, 180, 180));
		g.drawRoundRect(0, 0, width - 1, height - 1, 5, 5);
	};
	
	private static final Painter<?> MENU_BAR_PAINTER = (g, object, width, height) -> {
		final GradientPaint gradient = new GradientPaint(0, 0, new Color(190, 190, 190), 0, height / 2, new Color(225, 225, 225));
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setPaint(gradient);
		g.fillRect(0, 0, width, height);
		g.setPaint(new Color(150, 150, 150));
		g.drawRect(0, 0, width - 1, height - 1);
	};
	
	private static final Painter<?> TABLE_HEADER_PAINTER = (g, object, width, height) -> {
		final GradientPaint gradient = new GradientPaint(0, height / 2, new Color(225, 225, 225), 0, height, new Color(190, 190, 190));
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setPaint(gradient);
		g.fillRect(0, 0, width, height);
		g.setPaint(new Color(130, 130, 130));
		g.drawLine(width - 1, height / 6, width - 1, height - height / 6);
		g.setPaint(new Color(150, 150, 150));
		g.drawLine(0, 0, width - 1, 0);
		g.drawLine(0, height - 1, width - 1, height - 1);
	};
	
	private static final Painter<?> TABLE_HEADER_HOVERED_PAINTER = (g, object, width, height) -> {
		final GradientPaint gradient = new GradientPaint(0, height / 2, new Color(230, 230, 230), 0, height, new Color(200, 200, 200));
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setPaint(gradient);
		g.fillRect(0, 0, width, height);
		g.setPaint(new Color(130, 130, 130));
		g.drawLine(width - 1, height / 6, width - 1, height - height / 6);
		g.setPaint(new Color(150, 150, 150));
		g.drawLine(0, 0, width - 1, 0);
		g.drawLine(0, height - 1, width - 1, height - 1);
	};
	
	private static final Painter<?> POPUP_MENU_PAINTER = (g, object, width, height) -> {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setPaint(new Color(240, 240, 240));
		g.fillRect(0, 0, width, height);
		g.setPaint(new Color(150, 150, 150));
		g.drawRect(0, 0, width - 1, height - 1);
	};
	
	static {
		Font newFont;
		
		try {
			newFont = Font.createFont(Font.TRUETYPE_FONT, RattyServer.class.getResourceAsStream("/lato.ttf")).deriveFont(14f);
		} catch (final IOException | FontFormatException ex) {
			ex.printStackTrace();
			
			newFont = new Font("Trebuchet MS", Font.PLAIN, 14);
		}
		
		FONT = newFont;
	}
	
	private NimbusGuiSettings() {
		//...
	}
	
	public static void setDefaults(final UIDefaults defaults) {
		defaults.put("nimbusBase", Color.GRAY);
		defaults.put("control", BACKGROUND);
		defaults.put("textHighlight", SELECTION);
		defaults.put("nimbusFocus", SELECTION_BORDER);
		
		defaults.put("TextArea[Enabled].backgroundPainter", BACKGROUND_PAINTER);
		defaults.put("TextField[Enabled].backgroundPainter", BACKGROUND_PAINTER);
		defaults.put("FormattedTextField[Enabled].backgroundPainter", BACKGROUND_PAINTER);
		
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
		
		defaults.put("TableHeader:\"TableHeader.renderer\"[Enabled+Focused].backgroundPainter", TABLE_HEADER_PAINTER);
		defaults.put("TableHeader:\"TableHeader.renderer\"[Enabled].backgroundPainter", TABLE_HEADER_PAINTER);
		defaults.put("TableHeader:\"TableHeader.renderer\"[MouseOver].backgroundPainter", TABLE_HEADER_HOVERED_PAINTER);
		defaults.put("TableHeader:\"TableHeader.renderer\"[Pressed].backgroundPainter", TABLE_HEADER_PAINTER);
		
		defaults.put("PopupMenu[Enabled].backgroundPainter", POPUP_MENU_PAINTER);
		defaults.put("Menu[Enabled+Selected].backgroundPainter", SELECTION_PAINTER);
		defaults.put("MenuItem[MouseOver].backgroundPainter", SELECTION_PAINTER);
		defaults.put("PopupMenuSeparator[Enabled].backgroundPainter", SEPARATOR_PAINTER);
		
		defaults.put("MenuBar[Enabled].backgroundPainter", MENU_BAR_PAINTER);
		
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
		defaults.put("TitledBorder.font", FONT);
		
		defaults.put("OptionPane.errorIcon", new ImageIcon(ICON_ERROR));
		defaults.put("OptionPane.informationIcon", new ImageIcon(ICON_INFORMATION));
		defaults.put("OptionPane.questionIcon", new ImageIcon(ICON_QUESTION));
		defaults.put("OptionPane.warningIcon", new ImageIcon(ICON_WARNING));
		defaults.put("FileChooser.directoryIcon", new ImageIcon(ICON_FOLDER));
		defaults.put("FileChooser.floppyDriveIcon", new ImageIcon(ICON_FLOPPY_DISK));
		defaults.put("FileChooser.homeFolderIcon", new ImageIcon(ICON_HOUSE));
		defaults.put("FileChooser.listViewIcon", new ImageIcon(ICON_LIST));
		defaults.put("FileChooser.newFolderIcon", new ImageIcon(ICON_FOLDER_PLUS));
		defaults.put("FileChooser.upFolderIcon", new ImageIcon(ICON_FOLDER_UP));
		defaults.put("FileChooser.hardDriveIcon", new ImageIcon(ICON_HARD_DRIVE));
		defaults.put("FileChooser.detailsViewIcon", new ImageIcon(ICON_SEARCH));
		defaults.put("FileChooser.fileIcon", new ImageIcon(ICON_FILE));
	}
	
}
