package de.sogomn.rat.gui.server;

import static de.sogomn.rat.util.Constants.LANGUAGE;

import java.awt.BorderLayout;
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
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import de.sogomn.engine.util.AbstractListenerContainer;
import de.sogomn.rat.gui.IGuiController;

final class BuilderGui extends AbstractListenerContainer<IGuiController> {
	
	private JFrame frame;
	private JTextField address;
	private JFormattedTextField port;
	private JButton add, remove, choose, build;
	private JList<String> list;
	private JPanel leftPanel, rightPanel;
	private JSplitPane splitPane;
	
	private DefaultListModel<String> listModel;
	
	private static final Dimension SIZE = new Dimension(500, 300);
	private static final GridBagLayout LAYOUT_LEFT = new GridBagLayout();
	private static final BorderLayout LAYOUT_RIGHT = new BorderLayout();
	private static final EmptyBorder PADDING = new EmptyBorder(5, 5, 5, 5);
	private static final NumberFormat PORT_NUMBER_FORMAT = NumberFormat.getInstance();
	
	private static final String ADDRESS = LANGUAGE.getString("builder.address");
	private static final String PORT = LANGUAGE.getString("builder.port");
	
	private static final TitledBorder ADDRESS_BORDER = new TitledBorder(ADDRESS);
	private static final TitledBorder PORT_BORDER = new TitledBorder(PORT);
	
	public static final String ADD = LANGUAGE.getString("builder.add");
	public static final String REMOVE = LANGUAGE.getString("builder.remove");
	public static final String CHOOSE = LANGUAGE.getString("builder.choose");
	public static final String BUILD = LANGUAGE.getString("builder.build");
	
	static {
		PORT_NUMBER_FORMAT.setGroupingUsed(false);
	}
	
	public BuilderGui() {
		frame = new JFrame();
		address = new JTextField();
		port = new JFormattedTextField(PORT_NUMBER_FORMAT);
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
		address.setBorder(ADDRESS_BORDER);
		port.setBorder(PORT_BORDER);
		
		leftPanel = createLeftPanel();
		rightPanel = createRightPanel();
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
		
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setContentPane(splitPane);
		frame.setPreferredSize(SIZE);
		frame.pack();
		frame.setLocationRelativeTo(null);
	}
	
	private JPanel createLeftPanel() {
		final JPanel panel = new JPanel();
		final GridBagConstraints c = new GridBagConstraints();
		
		panel.setBorder(PADDING);
		panel.setLayout(LAYOUT_LEFT);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = c.weighty = 1;
		c.insets = new Insets(3, 3, 3, 3);
		
		c.gridy = 1;
		panel.add(address, c);
		
		c.gridy = 2;
		panel.add(port, c);
		
		c.gridy = 3;
		panel.add(add, c);
		
		c.gridy = 4;
		panel.add(choose, c);
		
		c.gridy = 5;
		c.anchor = GridBagConstraints.SOUTH;
		panel.add(build, c);
		
		return panel;
	}
	
	private JPanel createRightPanel() {
		final JPanel panel = new JPanel();
		
		panel.setLayout(LAYOUT_RIGHT);
		panel.add(list, BorderLayout.CENTER);
		panel.add(remove, BorderLayout.SOUTH);
		
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
