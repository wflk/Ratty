/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat.gui.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionListener;

import de.sogomn.rat.gui.IFileBrowserGui;
import de.sogomn.rat.util.Resources;

public final class FileBrowserSwingGui extends AbstractSwingGui implements IFileBrowserGui {
	
	private JButton up;
	private JMenuBar menuBar;
	private JList<String> rootList, fileList;
	private FileCellRenderer fileListRenderer;
	private JScrollPane scrollPane;
	private JSplitPane splitPane;
	
	private DefaultListModel<String> rootListModel, fileListModel;
	private Path directory;
	
	private static final Dimension SIZE = new Dimension(750, 550);
	private static final int DIVIDER_SIZE = 5;
	private static final double SPLIT_PANE_RESIZE_WEIGHT = 0.25;
	
	public FileBrowserSwingGui() {
		up = new JButton(Resources.UP_ICON);
		menuBar = new JMenuBar();
		rootList = new JList<String>();
		fileList = new JList<String>();
		fileListRenderer = new FileCellRenderer(Resources.FILE_ICON, Resources.DIRECTORY_ICON);
		scrollPane = new JScrollPane(fileList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, rootList, scrollPane);
		rootListModel = new DefaultListModel<String>();
		fileListModel = new DefaultListModel<String>();
		directory = Paths.get("");
		
		final ListSelectionListener rootListener = l -> {
			notifyListeners(controller -> controller.userInput(REQUEST_ROOT, this));
		};
		
		splitPane.setDividerSize(DIVIDER_SIZE);
		splitPane.setResizeWeight(SPLIT_PANE_RESIZE_WEIGHT);
		fileList.setModel(fileListModel);
		fileList.setCellRenderer(fileListRenderer);
		rootList.addListSelectionListener(rootListener);
		rootList.setModel(rootListModel);
		menuBar.add(up);
		
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setContentPane(splitPane);
		frame.setJMenuBar(menuBar);
		frame.setPreferredSize(SIZE);
		frame.pack();
		frame.setLocationRelativeTo(null);
	}

	@Override
	public void addFile(final String path) {
		final Path file = Paths.get(path);
		final Path relativePath = directory.relativize(file);
		final int level = relativePath.getNameCount();
		
		if (level == 1) {
			final String name = relativePath.getFileName().toString();
			
			fileListModel.addElement(name);
		}
	}
	
	@Override
	public void addRoot(final String root) {
		rootListModel.addElement(root);
	}

	@Override
	public void removeFile(final String path) {
		final Path relativePath = Paths.get(path).relativize(directory);
		final int level = relativePath.getNameCount();
		
		if (level == 1) {
			final String name = relativePath.getFileName().toString();
			
			fileListModel.removeElement(name);
		}
	}
	
	@Override
	public void removeRoot(final String root) {
		rootListModel.removeElement(root);
	}

	@Override
	public void clearFiles() {
		fileListModel.removeAllElements();
	}
	
	@Override
	public void clearRoots() {
		rootListModel.removeAllElements();
	}
	
	@Override
	public void setDirectory(final String path) {
		directory = Paths.get(path);
	}
	
	@Override
	public boolean isDirectory(final String name) {
		/*TODO*/
		return false;
	}

	@Override
	public String getCurrentDirectory() {
		return directory.toString();
	}

	@Override
	public String getSelectedFile() {
		return fileList.getSelectedValue();
	}
	
	@Override
	public String getSelectedRoot() {
		return rootList.getSelectedValue();
	}
	
	public static final class FileCellRenderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = 7899145670559346634L;
		
		@SuppressWarnings("unused")
		private Icon fileIcon, directoryIcon;
		
		public FileCellRenderer(final Icon fileIcon, final Icon directoryIcon) {
			this.fileIcon = fileIcon;
			this.directoryIcon = directoryIcon;
		}
		
		@Override
		public Component getListCellRendererComponent(final JList<?> list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
			final Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			final JLabel label = (JLabel)component;
			
			label.setIcon(fileIcon);
			
			return label;
		}
		
	}
	
}
