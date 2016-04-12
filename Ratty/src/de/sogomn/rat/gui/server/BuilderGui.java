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
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import de.sogomn.engine.util.AbstractListenerContainer;
import de.sogomn.rat.gui.IGuiController;

final class BuilderGui extends AbstractListenerContainer<IGuiController> {
	
	private JFrame frame;
	private JTextField address, fileName;
	private JFormattedTextField port;
	private JButton add, remove, choose, build;
	private JList<String> list;
	private JPanel leftPanel, rightPanel;
	private JSplitPane splitPane;
	
	private DefaultListModel<String> listModel;
	
	private static final Dimension SIZE = new Dimension(775, 350);
	private static final GridBagLayout LAYOUT_LEFT = new GridBagLayout();
	private static final BorderLayout LAYOUT_RIGHT = new BorderLayout();
	private static final GridLayout BOTTOM_PANEL_LAYOUT = new GridLayout(1, 2, 5, 0);
	private static final EmptyBorder PADDING = new EmptyBorder(5, 5, 5, 5);
	private static final NumberFormat PORT_NUMBER_FORMAT = NumberFormat.getInstance();
	private static final int DIVIDER_LOCATION = 500;
	private static final double SPLIT_PANE_RESIZE_WEIGHT = 0.75;
	
	private static final String ADDRESS = LANGUAGE.getString("builder.address");
	private static final String PORT = LANGUAGE.getString("builder.port");
	
	private static final TitledBorder ADDRESS_BORDER = new TitledBorder(ADDRESS);
	private static final TitledBorder PORT_BORDER = new TitledBorder(PORT);
	private static final CompoundBorder FILE_NAME_BORDER = new CompoundBorder(new LineBorder(Color.GRAY, 1, true), new EmptyBorder(5, 5, 5, 5));
	
	public static final String ADD = LANGUAGE.getString("builder.add");
	public static final String REMOVE = LANGUAGE.getString("builder.remove");
	public static final String CHOOSE = LANGUAGE.getString("builder.choose");
	public static final String BUILD = LANGUAGE.getString("builder.build");
	public static final String NO_FILE = LANGUAGE.getString("builder.no_file");
	
	static {
		PORT_NUMBER_FORMAT.setGroupingUsed(false);
	}
	
	public BuilderGui() {
		frame = new JFrame();
		address = new JTextField();
		port = new JFormattedTextField(PORT_NUMBER_FORMAT);
		fileName = new JTextField(NO_FILE);
		add = new JButton(ADD);
		remove = new JButton(REMOVE);
		choose = new JButton(CHOOSE);
		build = new JButton(BUILD);
		list = new JList<String>();
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
		
		splitPane.setDividerLocation(DIVIDER_LOCATION);
		splitPane.setResizeWeight(SPLIT_PANE_RESIZE_WEIGHT);
		
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setContentPane(splitPane);
		frame.setPreferredSize(SIZE);
		frame.pack();
		frame.setLocationRelativeTo(null);
	}
	
	private JPanel createLeftPanel() {
		final JPanel panel = new JPanel();
		final GridBagConstraints c = new GridBagConstraints();
		final JSeparator separator = new JSeparator();
		
		panel.setLayout(LAYOUT_LEFT);
		
		c.gridwidth = 2;
		c.weightx = c.weighty = 1;
		c.anchor = GridBagConstraints.NORTH;
		c.insets = new Insets(5, 5, 5, 5);
		c.fill = GridBagConstraints.HORIZONTAL;
		panel.add(address, c);
		
		c.gridy = 1;
		panel.add(port, c);
		
		c.gridy = 2;
		c.anchor = GridBagConstraints.CENTER;
		panel.add(separator, c);
		
		c.gridy = 3;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.SOUTH;
		panel.add(choose, c);
		
		c.gridx = 1;
		panel.add(fileName, c);
		
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		panel.add(build, c);
		
		return panel;
	}
	
	private JPanel createRightPanel() {
		final JPanel panel = new JPanel();
		final JPanel bottomPanel = new JPanel();
		
		bottomPanel.setLayout(BOTTOM_PANEL_LAYOUT);
		bottomPanel.add(add, BorderLayout.SOUTH);
		bottomPanel.add(remove, BorderLayout.SOUTH);
		
		panel.setBorder(PADDING);
		panel.setLayout(LAYOUT_RIGHT);
		panel.add(list, BorderLayout.CENTER);
		panel.add(bottomPanel, BorderLayout.SOUTH);
		
		return panel;
	}
	
	private void buttonClicked(final ActionEvent a) {
		final String command = a.getActionCommand();
		
		notifyListeners(controller -> controller.userInput(command, this));
	}
	
	public void close() {
		SwingUtilities.invokeLater(() -> {
			frame.setVisible(false);
			frame.dispose();
		});
	}
	
	public void addListEntry(final String entry) {
		listModel.addElement(entry);
	}
	
	public void removeListEntry(final String entry) {
		listModel.removeElement(entry);
	}
	
	public void setVisible(final boolean visible) {
		SwingUtilities.invokeLater(() -> {
			frame.setVisible(visible);
		});
	}
	
	public void setAddressInput(final String input) {
		address.setText(input);
	}
	
	public void setPortInput(final String input) {
		port.setText(input);
	}
	
	public void setFileName(final String name) {
		fileName.setText(name);
	}
	
	public String getAddressInput() {
		return address.getText();
	}
	
	public String getPortInput() {
		return port.getText();
	}
	
	public String getSelectedListEntry() {
		return list.getSelectedValue();
	}
	
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
