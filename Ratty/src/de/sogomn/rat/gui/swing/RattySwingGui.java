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

package de.sogomn.rat.gui.swing;

import static de.sogomn.rat.util.Resources.ICONS_FILE_MANAGEMENT;
import static de.sogomn.rat.util.Resources.ICONS_OTHER;
import static de.sogomn.rat.util.Resources.ICONS_SURVEILLANCE;
import static de.sogomn.rat.util.Resources.ICONS_UTILITY;
import static de.sogomn.rat.util.Resources.ICON_CROSSHAIR;
import static de.sogomn.rat.util.Resources.ICON_DOTS;
import static de.sogomn.rat.util.Resources.ICON_EYE;
import static de.sogomn.rat.util.Resources.ICON_FILE;
import static de.sogomn.rat.util.Resources.ICON_GEAR;
import static de.sogomn.rat.util.Resources.ICON_SERVER;
import static de.sogomn.rat.util.Resources.ICON_WRENCH;
import static de.sogomn.rat.util.Resources.WINDOW_ICON_LIST;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import de.sogomn.rat.gui.IRattyGui;
import de.sogomn.rat.gui.ServerClient;
import de.sogomn.rat.util.Constants;

/*
 * CONSTANT OVERLOAD!!! WHEEE!
 */
public final class RattySwingGui extends AbstractSwingGui implements IRattyGui {
	
	private JTable table;
	private ServerClientTableModel tableModel;
	private JScrollPane scrollPane;
	private JPopupMenu menu;
	private JMenuBar menuBar;
	private JButton manageServers, build, attack;
	private ServerClient selectedClient;
	
	private static final String TITLE = "Ratty " + Constants.VERSION;
	private static final Dimension SIZE = new Dimension(1200, 600);
	private static final FlowLayout MENU_BAR_LAYOUT = new FlowLayout(FlowLayout.LEFT, 6, 0);
	private static final Insets MENU_BAR_MARGIN = new Insets(3, 0, 3, 0);
	private static final int ROW_HEIGHT = 25;
	private static final int TABLE_HEADER_HEIGHT = 30;
	
	private static final String[] SURVEILLANCE_COMMANDS = {
		SCREENSHOT,
		DESKTOP,
		VOICE,
		CLIPBOARD,
		KEYLOG
	};
	
	private static final String[] FILE_MANAGEMENT_COMMANDS = {
		BROWSE_FILES,
		UPLOAD_EXECUTE,
		DROP_EXECUTE
	};
	
	private static final String[] UTILITY_COMMANDS = {
		POPUP,
		COMMAND,
		WEBSITE,
		AUDIO,
		CHAT
	};
	
	private static final String[] OTHER_COMMANDS = {
		INFORMATION,
		RESTART,
		SHUT_DOWN,
		FREE,
		UNINSTALL
	};
	
	public RattySwingGui() {
		table = new JTable();
		tableModel = new ServerClientTableModel();
		scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		menu = new JPopupMenu();
		menuBar = new JMenuBar();
		manageServers = createButton(MANAGE_SERVERS, ICON_SERVER);
		build = createButton(BUILD, ICON_GEAR);
		attack = createButton(ATTACK, ICON_CROSSHAIR);
		
		final Container contentPane = frame.getContentPane();
		final JMenu surveillance = createMenu(SURVEILLANCE, ICON_EYE, SURVEILLANCE_COMMANDS, ICONS_SURVEILLANCE);
		final JMenu fileManagement = createMenu(FILE_MANAGEMENT, ICON_FILE, FILE_MANAGEMENT_COMMANDS, ICONS_FILE_MANAGEMENT);
		final JMenu utility = createMenu(UTILITY, ICON_WRENCH, UTILITY_COMMANDS, ICONS_UTILITY);
		final JMenu other = createMenu(OTHER, ICON_DOTS, OTHER_COMMANDS, ICONS_OTHER);
		final JTableHeader tableHeader = table.getTableHeader();
		final WindowAdapter closingAdapter = new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent w) {
				notifyListeners(controller -> controller.userInput(CLOSE, this));
			}
		};
		final MouseAdapter rightClickAdapter = new MouseAdapter() {
			@Override
			public void mousePressed(final MouseEvent m) {
				final Point point = m.getPoint();
				final int row = table.rowAtPoint(point);
				
				selectedClient = tableModel.getServerClient(row);
				table.setRowSelectionInterval(row, row);
			}
		};
		final DefaultTableCellRenderer cellRenderer = (DefaultTableCellRenderer)tableHeader.getDefaultRenderer();
		final int tableHeaderWidth = tableHeader.getWidth();
		final Dimension tableHeaderSize = new Dimension(tableHeaderWidth, TABLE_HEADER_HEIGHT);
		
		tableHeader.setReorderingAllowed(false);
		tableHeader.setPreferredSize(tableHeaderSize);
		cellRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		
		menuBar.setLayout(MENU_BAR_LAYOUT);
		menuBar.setMargin(MENU_BAR_MARGIN);
		menuBar.add(manageServers);
		menuBar.add(build);
		menuBar.add(attack);
		menu.add(surveillance);
		menu.add(fileManagement);
		menu.add(utility);
		menu.addSeparator();
		menu.add(other);
		scrollPane.setBorder(null);
		table.setComponentPopupMenu(menu);
		table.setModel(tableModel);
		table.setRowHeight(ROW_HEIGHT);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setShowGrid(true);
		table.addMouseListener(rightClickAdapter);
		
		contentPane.add(scrollPane, BorderLayout.CENTER);
		contentPane.add(menuBar, BorderLayout.SOUTH);
		
		frame.setTitle(TITLE);
		frame.setIconImages(WINDOW_ICON_LIST);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(closingAdapter);
		frame.setPreferredSize(SIZE);
		frame.pack();
		frame.setLocationRelativeTo(null);
	}
	
	private JButton createButton(final String name, final BufferedImage icon) {
		final ImageIcon imageIcon = new ImageIcon(icon);
		final JButton button = new JButton(name, imageIcon);
		
		button.setActionCommand(name);
		button.addActionListener(this::actionPerformed);
		
		return button;
	}
	
	private JMenu createMenu(final String name, final BufferedImage image, final String[] commands, final BufferedImage[] icons) {
		final JMenu menu = new JMenu(name);
		final ImageIcon icon = new ImageIcon(image);
		
		menu.setIcon(icon);
		
		for (int i = 0; i < commands.length && i < icons.length; i++) {
			final String command = commands[i];
			final BufferedImage itemIcon = icons[i];
			final JMenuItem item = createMenuItem(command, itemIcon);
			
			menu.add(item);
		}
		
		return menu;
	}
	
	private JMenuItem createMenuItem(final String name, final BufferedImage image) {
		final JMenuItem item = new JMenuItem(name);
		final ImageIcon icon = new ImageIcon(image);
		
		item.setActionCommand(name);
		item.addActionListener(this::actionPerformed);
		item.setIcon(icon);
		
		return item;
	}
	
	private void actionPerformed(final ActionEvent a) {
		final String command = a.getActionCommand();
		
		notifyListeners(controller -> controller.userInput(command, this));
	}
	
	@Override
	public void update() {
		final int selectedRow = table.getSelectedRow();
		
		tableModel.fireTableDataChanged();
		
		if (selectedRow != -1) {
			table.setRowSelectionInterval(selectedRow, selectedRow);
		}
	}
	
	@Override
	public void addClient(final ServerClient client) {
		tableModel.addServerClient(client);
	}
	
	@Override
	public void removeClient(final ServerClient client) {
		tableModel.removeServerClient(client);
	}
	
	@Override
	public ServerClient getSelectedClient() {
		return selectedClient;
	}
	
}
