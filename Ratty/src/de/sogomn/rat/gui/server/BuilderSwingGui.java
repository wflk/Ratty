/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat.gui.server;

import static de.sogomn.rat.util.Constants.LANGUAGE;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import de.sogomn.rat.gui.AbstractSwingGui;

public final class BuilderSwingGui extends AbstractSwingGui implements IBuilderGui {
	
	private JTextField address, fileName;
	private JFormattedTextField port;
	private JButton add, remove, choose, build;
	private JList<String> list;
	private JScrollPane scrollPane;
	private JPanel leftPanel, rightPanel;
	private JSplitPane splitPane;
	
	private DefaultListModel<String> listModel;
	
	private static final Dimension SIZE = new Dimension(750, 450);
	private static final NumberFormat PORT_NUMBER_FORMAT = NumberFormat.getInstance();
	private static final double SPLIT_PANE_RESIZE_WEIGHT = 0.3;
	
	private static final String ADDRESS = LANGUAGE.getString("builder.address");
	private static final String PORT = LANGUAGE.getString("builder.port");
	
	private static final TitledBorder ADDRESS_BORDER = new TitledBorder(ADDRESS);
	private static final TitledBorder PORT_BORDER = new TitledBorder(PORT);
	private static final CompoundBorder FILE_NAME_BORDER = new CompoundBorder(new LineBorder(Color.GRAY), new EmptyBorder(5, 5, 5, 5));
	
	public static final String ADD = LANGUAGE.getString("builder.add");
	public static final String REMOVE = LANGUAGE.getString("builder.remove");
	public static final String CHOOSE = LANGUAGE.getString("builder.choose");
	public static final String BUILD = LANGUAGE.getString("builder.build");
	public static final String NO_FILE = LANGUAGE.getString("builder.no_file");
	
	static {
		PORT_NUMBER_FORMAT.setGroupingUsed(false);
	}
	
	public BuilderSwingGui() {
		address = new JTextField();
		port = new JFormattedTextField(PORT_NUMBER_FORMAT);
		fileName = new JTextField(NO_FILE);
		add = new JButton(ADD);
		remove = new JButton(REMOVE);
		choose = new JButton(CHOOSE);
		build = new JButton(BUILD);
		list = new JList<String>();
		scrollPane = new JScrollPane(list);
		listModel = new DefaultListModel<String>();
		
		list.setModel(listModel);
		add.setActionCommand(ADD);
		add.addActionListener(this::buttonClicked);
		remove.setActionCommand(REMOVE);
		remove.addActionListener(this::buttonClicked);
		choose.setActionCommand(CHOOSE);
		choose.addActionListener(this::buttonClicked);
		build.setActionCommand(BUILD);
		build.addActionListener(this::buttonClicked);
		fileName.setEditable(false);
		fileName.setBorder(FILE_NAME_BORDER);
		fileName.setHorizontalAlignment(JLabel.CENTER);
		address.setBorder(ADDRESS_BORDER);
		port.setBorder(PORT_BORDER);
		
		leftPanel = createLeftPanel();
		rightPanel = createRightPanel();
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
		
		splitPane.setResizeWeight(SPLIT_PANE_RESIZE_WEIGHT);
		
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setContentPane(splitPane);
		frame.setPreferredSize(SIZE);
		frame.setMinimumSize(SIZE);
		frame.pack();
		frame.setLocationRelativeTo(null);
	}
	
	private JPanel createLeftPanel() {
		final JPanel panel = new JPanel();
		final GridBagLayout layout = new GridBagLayout();
		final GridBagConstraints c = new GridBagConstraints();
		final JSeparator separator = new JSeparator();
		
		panel.setLayout(layout);
		
		c.weightx = c.weighty = 1;
		c.insets = new Insets(5, 5, 5, 5);
		c.fill = GridBagConstraints.HORIZONTAL;
		panel.add(choose, c);
		
		c.gridx = 1;
		panel.add(fileName, c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		panel.add(separator, c);
		
		c.gridy = 2;
		panel.add(build, c);
		
		return panel;
	}
	
	private JPanel createRightPanel() {
		final JPanel panel = new JPanel();
		final BorderLayout layout = new BorderLayout();
		final JPanel centerPanel = new JPanel();
		final GridBagLayout centerLayout = new GridBagLayout();
		final GridBagConstraints c = new GridBagConstraints();
		final JPanel bottomPanel = new JPanel();
		final GridLayout bottomLayout = new GridLayout(1, 2, 10, 0);
		final EmptyBorder bottomBorder = new EmptyBorder(5, 5, 5, 5);
		
		bottomPanel.setBorder(bottomBorder);
		bottomPanel.setLayout(bottomLayout);
		bottomPanel.add(add);
		bottomPanel.add(remove);
		
		centerPanel.setLayout(centerLayout);
		
		c.weightx = 1;
		c.weighty = 3;
		c.insets = new Insets(5, 5, 5, 5);
		c.fill = GridBagConstraints.BOTH;
		centerPanel.add(scrollPane, c);
		
		c.gridy = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		centerPanel.add(address, c);
		
		c.gridy = 2;
		centerPanel.add(port, c);
		
		panel.setLayout(layout);
		panel.add(centerPanel, BorderLayout.CENTER);
		panel.add(bottomPanel, BorderLayout.SOUTH);
		
		return panel;
	}
	
	private void buttonClicked(final ActionEvent a) {
		final String command = a.getActionCommand();
		
		notifyListeners(controller -> controller.userInput(command, this));
	}
	
	@Override
	public void addListEntry(final String entry) {
		listModel.addElement(entry);
	}
	
	@Override
	public void removeListEntry(final String entry) {
		listModel.removeElement(entry);
	}
	
	@Override
	public void clearListEntries() {
		listModel.clear();
	}
	
	@Override
	public boolean containsListEntry(final String entry) {
		return listModel.contains(entry);
	}
	
	@Override
	public void setAddressInput(final String input) {
		address.setText(input);
	}
	
	@Override
	public void setPortInput(final String input) {
		port.setText(input);
	}
	
	@Override
	public void setFileName(final String name) {
		fileName.setText(name);
	}
	
	@Override
	public String getAddressInput() {
		return address.getText();
	}
	
	@Override
	public String getPortInput() {
		return port.getText();
	}
	
	@Override
	public String getFileName() {
		return fileName.getText();
	}
	
	@Override
	public String getSelectedListEntry() {
		return list.getSelectedValue();
	}
	
	@Override
	public String[] getListEntries() {
		final int size = listModel.size();
		final String[] entries = new String[size];
		
		for (int i = 0; i < size; i++) {
			final String entry = listModel.getElementAt(i);
			
			entries[i] = entry;
		}
		
		return entries;
	}
	
}
