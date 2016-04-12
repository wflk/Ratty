package de.sogomn.rat.gui.server;

import static de.sogomn.rat.util.Constants.LANGUAGE;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import de.sogomn.engine.util.AbstractListenerContainer;
import de.sogomn.rat.gui.IGuiController;

final class BuilderGui extends AbstractListenerContainer<IGuiController> {
	
	private JFrame frame;
	private JLabel addressLabel, portLabel, fileLabel;
	private JTextField address;
	private JFormattedTextField port;
	private JButton choose, build;
	
	private static final Dimension SIZE = new Dimension(500, 200);
	private static final GridBagLayout LAYOUT = new GridBagLayout();
	private static final EmptyBorder PADDING = new EmptyBorder(5, 5, 5, 5);
	private static final NumberFormat PORT_NUMBER_FORMAT = NumberFormat.getInstance();
	
	private static final String ADDRESS = LANGUAGE.getString("builder.address");
	private static final String PORT = LANGUAGE.getString("builder.port");
	
	public static final String CHOOSE = LANGUAGE.getString("builder.choose");
	public static final String BUILD = LANGUAGE.getString("builder.build");
	
	static {
		PORT_NUMBER_FORMAT.setGroupingUsed(false);
	}
	
	public BuilderGui() {
		frame = new JFrame();
		addressLabel = new JLabel(ADDRESS);
		portLabel = new JLabel(PORT);
		fileLabel = new JLabel();
		address = new JTextField();
		port = new JFormattedTextField(PORT_NUMBER_FORMAT);
		choose = new JButton(CHOOSE);
		build = new JButton(BUILD);
		
		choose.setActionCommand(CHOOSE);
		choose.addActionListener(this::buttonClicked);
		build.setActionCommand(BUILD);
		build.addActionListener(this::buttonClicked);
		
		final JPanel contentPane = createPanel();
		
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setContentPane(contentPane);
		frame.setPreferredSize(SIZE);
		frame.pack();
		frame.setLocationRelativeTo(null);
	}
	
	private JPanel createPanel() {
		final JPanel panel = new JPanel();
		final GridBagConstraints c = new GridBagConstraints();
		
		panel.setBorder(PADDING);
		panel.setLayout(LAYOUT);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = c.weighty = 1;
		panel.add(addressLabel, c);
		
		c.gridx = 1;
		c.weightx = 3;
		panel.add(address, c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		panel.add(portLabel, c);
		
		c.gridx = 1;
		c.weightx = 3;
		panel.add(port, c);
		
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1;
		panel.add(fileLabel, c);
		
		c.gridx = 1;
		panel.add(choose, c);
		
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		c.weighty = 5;
		c.insets = new Insets(10, 0, 0, 0);
		panel.add(build, c);
		
		return panel;
	}
	
	private void buttonClicked(final ActionEvent a) {
		final String command = a.getActionCommand();
		
		notifyListeners(controller -> controller.userInput(command, this));
	}
	
	public void setVisible(final boolean visible) {
		frame.setVisible(true);
	}
	
	public void close() {
		frame.setVisible(false);
		frame.dispose();
	}
	
	public void setFileLabel(final String text) {
		fileLabel.setText(text);
	}
	
	public String getAddressInput() {
		return address.getText();
	}
	
	public String getPortInput() {
		return port.getText();
	}
	
}
