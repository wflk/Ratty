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

import static de.sogomn.rat.util.Resources.ICON_ARROW_DOWN;
import static de.sogomn.rat.util.Resources.ICON_ARROW_UP;
import static de.sogomn.rat.util.Resources.ICON_EXCLAMATION_MARK;
import static de.sogomn.rat.util.Resources.ICON_FILE;
import static de.sogomn.rat.util.Resources.ICON_FILE_DELETE;
import static de.sogomn.rat.util.Resources.ICON_FOLDER;
import static de.sogomn.rat.util.Resources.ICON_FOLDER_PLUS;
import static de.sogomn.rat.util.Resources.ICON_FOLDER_UP;
import static de.sogomn.rat.util.Resources.ICON_HARD_DRIVE;
import static de.sogomn.rat.util.Resources.ICON_POINTER;
import static de.sogomn.rat.util.Resources.ICON_QUESTION_MARK;
import static de.sogomn.rat.util.Resources.ICON_WORLD_DOWN;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionListener;

import de.sogomn.rat.gui.IFileBrowserGui;

public final class FileBrowserSwingGui extends AbstractSwingGui implements IFileBrowserGui {
	
	private JLabel pathLabel;
	private JList<String> rootList;
	private RootCellRenderer rootListRenderer;
	private JList<FileEntry> fileList;
	private FileCellRenderer fileListRenderer;
	private JScrollPane scrollPane;
	private JSplitPane splitPane;
	private JMenuBar menuBar;
	private JButton directoryUp, upload, newDirectory, dropFile;
	private JPopupMenu menu;
	private JMenuItem request, download, execute, delete, information;
	
	private DefaultListModel<String> rootListModel;
	private DefaultListModel<FileEntry> fileListModel;
	private Path directory;
	
	private static final int HEIGHT = 600;
	private static final int DIVIDER_SIZE = 5;
	private static final double SPLIT_PANE_RESIZE_WEIGHT = 0.15;
	private static final MatteBorder SPLIT_PANE_BORDER = new MatteBorder(0, 1, 0, 0, Color.BLACK);
	private static final FlowLayout MENU_BAR_LAYOUT = new FlowLayout(FlowLayout.LEFT, 6, 0);
	private static final Insets MENU_BAR_MARGIN = new Insets(3, 0, 3, 0);
	private static final CompoundBorder PATH_LABEL_BORDER = new CompoundBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK), new EmptyBorder(5, 5, 5, 5));
	private static final CompoundBorder ROOT_LIST_BORDER = new CompoundBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK), new EmptyBorder(5, 5, 5, 5));
	private static final CompoundBorder FILE_LIST_BORDER = new CompoundBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK), new EmptyBorder(5, 5, 5, 5));
	
	public FileBrowserSwingGui() {
		pathLabel = new JLabel("...");
		rootList = new JList<String>();
		rootListRenderer = new RootCellRenderer(ICON_HARD_DRIVE);
		fileList = new JList<FileEntry>();
		fileListRenderer = new FileCellRenderer(ICON_FILE, ICON_FOLDER);
		scrollPane = new JScrollPane(fileList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, rootList, scrollPane);
		menuBar = new JMenuBar();
		directoryUp = createButton(DIRECTORY_UP, ICON_FOLDER_UP);
		upload = createButton(UPLOAD, ICON_ARROW_UP);
		newDirectory = createButton(NEW_DIRECTORY, ICON_FOLDER_PLUS);
		dropFile = createButton(DROP_FILE, ICON_WORLD_DOWN);
		menu = new JPopupMenu();
		request = createMenuItem(REQUEST, ICON_QUESTION_MARK);
		download = createMenuItem(DOWNLOAD, ICON_ARROW_DOWN);
		execute = createMenuItem(EXECUTE, ICON_POINTER);
		delete = createMenuItem(DELETE, ICON_FILE_DELETE);
		information = createMenuItem(INFORMATION, ICON_EXCLAMATION_MARK);
		rootListModel = new DefaultListModel<String>();
		fileListModel = new DefaultListModel<FileEntry>();
		directory = Paths.get("");
		
		final Container contentPane = frame.getContentPane();
		final ListSelectionListener rootListener = l -> {
			final boolean adjusting = l.getValueIsAdjusting();
			
			if (adjusting) {
				notifyListeners(controller -> controller.userInput(REQUEST_ROOT, this));
			}
		};
		final ListSelectionListener fileListener = l -> {
			rootList.clearSelection();
		};
		final MouseAdapter doubleClickAdapter = new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent m) {
				final Point mousePoint = m.getPoint();
				final boolean doubleClick = m.getClickCount() == 2;
				final int index = fileList.locationToIndex(mousePoint);
				
				if (doubleClick && index != -1) {
					final Rectangle selectionRectangle = fileList.getCellBounds(index, index);
					final boolean contains = selectionRectangle.contains(mousePoint);
					
					if (contains) {
						notifyListeners(controller -> controller.userInput(REQUEST, this));
					}
				}
			}
		};
		
		menu.add(request);
		menu.add(download);
		menu.add(upload);
		menu.add(execute);
		menu.add(delete);
		menu.add(newDirectory);
		menu.add(information);
		menuBar.setLayout(MENU_BAR_LAYOUT);
		menuBar.setMargin(MENU_BAR_MARGIN);
		menuBar.add(directoryUp);
		menuBar.add(upload);
		menuBar.add(newDirectory);
		menuBar.add(dropFile);
		splitPane.setDividerSize(DIVIDER_SIZE);
		splitPane.setResizeWeight(SPLIT_PANE_RESIZE_WEIGHT);
		splitPane.setEnabled(false);
		scrollPane.setBorder(SPLIT_PANE_BORDER);
		fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fileList.setModel(fileListModel);
		fileList.setCellRenderer(fileListRenderer);
		fileList.setComponentPopupMenu(menu);
		fileList.addListSelectionListener(fileListener);
		fileList.addMouseListener(doubleClickAdapter);
		fileList.setBorder(FILE_LIST_BORDER);
		rootList.setCellRenderer(rootListRenderer);
		rootList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		rootList.addListSelectionListener(rootListener);
		rootList.setBorder(ROOT_LIST_BORDER);
		rootList.setModel(rootListModel);
		pathLabel.setBorder(PATH_LABEL_BORDER);
		
		contentPane.add(pathLabel, BorderLayout.NORTH);
		contentPane.add(splitPane, BorderLayout.CENTER);
		contentPane.add(menuBar, BorderLayout.SOUTH);
		
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.pack();
		
		final Dimension size = frame.getSize();
		final Dimension newSize = new Dimension(size.width, HEIGHT);
		
		frame.setPreferredSize(newSize);
		frame.pack();
		frame.setLocationRelativeTo(null);
	}
	
	private JMenuItem createMenuItem(final String name, final BufferedImage image) {
		final JMenuItem item = new JMenuItem(name);
		final ImageIcon icon = new ImageIcon(image);
		
		item.setActionCommand(name);
		item.addActionListener(this::actionPerformed);
		item.setIcon(icon);
		
		return item;
	}
	
	private JButton createButton(final String name, final BufferedImage image) {
		final ImageIcon icon = new ImageIcon(image);
		final JButton button = new JButton(name, icon);
		
		button.setActionCommand(name);
		button.addActionListener(this::actionPerformed);
		
		return button;
	}
	
	private void actionPerformed(final ActionEvent a) {
		if (!fileList.isSelectionEmpty()){
			final String actionCommand = a.getActionCommand();
		
			notifyListeners(controller -> controller.userInput(actionCommand, this));
		}
	}
	
	private void addFileEntry(final String name, final boolean directory) {
		final FileEntry entry = new FileEntry(name, directory);
		
		SwingUtilities.invokeLater(() -> {
			fileListModel.addElement(entry);
		});
	}
	
	private void removeFileEntry(final String name) {
		final FileEntry entry = getEntryByName(name);
		
		SwingUtilities.invokeLater(() -> {
			fileListModel.removeElement(entry);
		});
	}
	
	private FileEntry getEntryByName(final String name) {
		final Enumeration<FileEntry> entries = fileListModel.elements();
		
		while (entries.hasMoreElements()) {
			final FileEntry entry = entries.nextElement();
			
			if (entry.name.equals(name)) {
				return entry;
			}
		}
		
		return null;
	}
	
	private String getNameByPath(final String path) {
		final Path file = Paths.get(path);
		
		if (!file.getRoot().equals(directory.getRoot())) {
			return null;
		}
		
		final Path relativePath = directory.relativize(file);
		final int level = relativePath.getNameCount();
		
		if (level == 1) {
			final String name = relativePath.getFileName().toString();
			
			return name;
		}
		
		return null;
	}
	
	@Override
	public void close() {
		clearFiles();
		clearRoots();
		
		super.close();
	}
	
	@Override
	public void addFile(final String name) {
		addFileEntry(name, false);
	}

	@Override
	public void addFilePath(final String path) {
		final String name = getNameByPath(path);
		
		if (name != null) {
			addFileEntry(name, false);
		}
	}
	
	@Override
	public void addDirectory(final String name) {
		addFileEntry(name, true);
	}
	
	@Override
	public void addDirectoryPath(final String path) {
		final String name = getNameByPath(path);
		
		if (name != null) {
			addFileEntry(name, true);
		}
	}
	
	@Override
	public void addRoot(final String name) {
		SwingUtilities.invokeLater(() -> {
			rootListModel.addElement(name);
		});
	}
	
	@Override
	public void removeEntry(final String name) {
		removeFileEntry(name);
	}

	@Override
	public void removeEntryPath(final String path) {
		final String name = getNameByPath(path);
		
		if (name != null) {
			removeFileEntry(name);
		}
	}
	
	@Override
	public void removeRoot(final String name) {
		SwingUtilities.invokeLater(() -> {
			rootListModel.removeElement(name);
		});
	}

	@Override
	public void clearFiles() {
		SwingUtilities.invokeLater(() -> {
			fileListModel.clear();
		});
	}
	
	@Override
	public void clearRoots() {
		SwingUtilities.invokeLater(() -> {
			rootListModel.clear();
		});
	}
	
	@Override
	public void setDirectoryPath(final String path) {
		directory = Paths.get(path);
		pathLabel.setText(path);
	}
	
	@Override
	public boolean isDirectory(final String name) {
		final FileEntry entry = getEntryByName(name);
		
		return entry != null && entry.directory;
	}
	
	@Override
	public boolean isDirectoryPath(final String path) {
		final String name = getNameByPath(path);
		final FileEntry entry = getEntryByName(name);
		
		return entry != null && entry.directory;
	}

	@Override
	public String getCurrentDirectoryPath() {
		String path = directory.toString().replace("\\", "/");
		
		if (path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}
		
		return path;
	}
	
	@Override
	public String getParentDirectory() {
		final Path parent = directory.getParent();
		
		if (parent != null) {
			return parent.toString();
		}
		
		return null;
	}

	@Override
	public String getSelectedFile() {
		return fileList.getSelectedValue().name.replace("\\", "/");
	}
	
	@Override
	public String getSelectedFilePath() {
		final FileEntry selectedFile = fileList.getSelectedValue();
		
		if (selectedFile == null) {
			return null;
		}
		
		final String file = fileList.getSelectedValue().name.replace("\\", "/");
		
		String path = getCurrentDirectoryPath();
		
		if (file.endsWith("/")) {
			path += file;
		} else {
			path += "/" + file;
		}
		
		return path;
	}
	
	@Override
	public String getSelectedRoot() {
		final String selectedRoot = rootList.getSelectedValue();
		
		if (selectedRoot != null) {
			return selectedRoot.replace("\\", "/");
		}
		
		return null;
	}
	
	private class FileEntry {
		
		final String name;
		final boolean directory;
		
		public FileEntry(final String name, final boolean directory) {
			this.name = name;
			this.directory = directory;
		}
		
	}
	
	private static final class FileCellRenderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = 7899145670559346634L;
		
		private Icon fileIcon, directoryIcon;
		
		public FileCellRenderer(final Icon fileIcon, final Icon directoryIcon) {
			this.fileIcon = fileIcon;
			this.directoryIcon = directoryIcon;
		}
		
		public FileCellRenderer(final BufferedImage fileIcon, final BufferedImage directoryIcon) {
			this(new ImageIcon(fileIcon), new ImageIcon(directoryIcon));
		}
		
		@Override
		public Component getListCellRendererComponent(final JList<?> list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
			if (!(value instanceof FileEntry)) {
				return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			}
			
			final FileEntry entry = (FileEntry)value;
			final Component component = super.getListCellRendererComponent(list, entry.name, index, isSelected, cellHasFocus);
			final JLabel label = (JLabel)component;
			
			if (entry.directory) {
				label.setIcon(directoryIcon);
			} else {
				label.setIcon(fileIcon);
			}
			
			return label;
		}
		
	}
	
	private static final class RootCellRenderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = 7899145670559346634L;
		
		private Icon icon;
		
		public RootCellRenderer(final Icon icon) {
			this.icon = icon;
		}
		
		public RootCellRenderer(final BufferedImage icon) {
			this(new ImageIcon(icon));
		}
		
		@Override
		public Component getListCellRendererComponent(final JList<?> list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
			final JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			
			label.setIcon(icon);
			
			return label;
		}
		
	}
	
}
