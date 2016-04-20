/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat.gui.swing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import de.sogomn.rat.gui.ILoggingGui;

public final class LoggingSwingGui extends AbstractSwingGui implements ILoggingGui {
	
	private JTextArea textArea;
	private JScrollPane scrollPane;
	private JButton clear;
	
	private static final Dimension SIZE = new Dimension(500, 500);
	
	public LoggingSwingGui() {
		textArea = new JTextArea();
		scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		clear = new JButton(CLEAR);
		
		clear.setActionCommand(CLEAR);
		clear.addActionListener(this::buttonClicked);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
		final Container contentPane = frame.getContentPane();
		
		contentPane.add(scrollPane, BorderLayout.CENTER);
		contentPane.add(clear, BorderLayout.SOUTH);
		
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setPreferredSize(SIZE);
		frame.pack();
		frame.setLocationRelativeTo(null);
	}
	
	private void buttonClicked(final ActionEvent a) {
		final String command = a.getActionCommand();
		
		notifyListeners(controller -> controller.userInput(command, this));
	}
	
	public void log(final String message) {
		textArea.append(message);
	}
	
	public void clear() {
		textArea.setText("");
	}
	
}
