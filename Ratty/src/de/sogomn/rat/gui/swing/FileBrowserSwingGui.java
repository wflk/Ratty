/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat.gui.swing;

import java.awt.Component;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import de.sogomn.rat.gui.IFileBrowserGui;
import de.sogomn.rat.util.Resources;

public final class FileBrowserSwingGui extends AbstractSwingGui implements IFileBrowserGui {
	
	private JList<Path> list;
	private FileCellRenderer cellRenderer;
	
	private DefaultListModel<Path> listModel;
	private Path directory;
	
	public FileBrowserSwingGui() {
		list = new JList<Path>();
		cellRenderer = new FileCellRenderer(Resources.FILE_ICON, Resources.DIRECTORY_ICON);
		listModel = new DefaultListModel<Path>();
		directory = Paths.get("");
		
		list.setModel(listModel);
		list.setCellRenderer(cellRenderer);
		
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setContentPane(list);
		frame.pack();
		frame.setLocationRelativeTo(null);
	}

	@Override
	public void setDirectory(final Path directory) {
		this.directory = directory;
	}

	@Override
	public void addFile(final Path file) {
		listModel.addElement(file);
	}

	@Override
	public void removeFile(final Path file) {
		listModel.removeElement(file);
	}

	@Override
	public void clearFiles() {
		listModel.removeAllElements();
	}

	@Override
	public Path getDirectory() {
		return directory;
	}

	@Override
	public Path getSelectedFile() {
		return list.getSelectedValue();
	}
	
	public static final class FileCellRenderer implements ListCellRenderer<Path> {
		
		private Icon fileIcon, directoryIcon;
		
		public FileCellRenderer(final Icon fileIcon, final Icon directoryIcon) {
			this.fileIcon = fileIcon;
			this.directoryIcon = directoryIcon;
		}
		
		@Override
		public Component getListCellRendererComponent(final JList<? extends Path> list, final Path value, final int index, final boolean isSelected, final boolean cellHasFocus) {
			final String name = value.getFileName().toString();
			final Icon icon = Files.isRegularFile(value) ? fileIcon : directoryIcon;
			final JLabel label = new JLabel(name, icon, JLabel.LEFT);
			
			return label;
		}
		
	}
	
}
