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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import de.sogomn.engine.debug.Hardcoded;
import de.sogomn.rat.gui.IBuilderGui;

public final class BuilderSwingGui extends AbstractSwingGui implements IBuilderGui {
	
	private JTextField address, fileName;
	private JFormattedTextField port;
	private JButton add, remove, choose, build;
	private JList<String> list;
	private JScrollPane scrollPane;
	
	private DefaultListModel<String> listModel;
	
	private static final Dimension SIZE = new Dimension(750, 450);
	private static final NumberFormat PORT_NUMBER_FORMAT = NumberFormat.getInstance();
	private static final TitledBorder ADDRESS_BORDER = new TitledBorder(ADDRESS);
	private static final TitledBorder PORT_BORDER = new TitledBorder(PORT);
	private static final CompoundBorder FILE_NAME_BORDER = new CompoundBorder(new LineBorder(Color.GRAY), new EmptyBorder(5, 5, 5, 5));
	
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
		
		final JPanel contentPane = createRightPanel();
		
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setContentPane(contentPane);
		frame.setPreferredSize(SIZE);
		frame.setMinimumSize(SIZE);
		frame.pack();
		frame.setLocationRelativeTo(null);
	}
	
	@Hardcoded("Well... GUI.")
	private JPanel createRightPanel() {
		final JPanel panel = new JPanel();
		final GridBagLayout layout = new GridBagLayout();
		final GridBagConstraints c = new GridBagConstraints();
		final JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
		
		panel.setLayout(layout);
		
		c.gridwidth = 2;
		c.weightx = 1;
		c.weighty = 3;
		c.insets = new Insets(5, 5, 5, 5);
		c.fill = GridBagConstraints.HORIZONTAL;
		panel.add(scrollPane, c);
		
		c.gridy = 1;
		c.weighty = 1;
		c.gridwidth = 1;
		panel.add(address, c);
		
		c.gridx = 1;
		panel.add(port, c);
		
		c.gridx = 0;
		c.gridy = 2;
		panel.add(add, c);
		
		c.gridx = 1;
		panel.add(remove, c);
		
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		panel.add(separator, c);
		
		c.gridy = 4;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.SOUTH;
		panel.add(choose, c);
		
		c.gridx = 1;
		panel.add(fileName, c);
		
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 2;
		panel.add(build, c);
		
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
