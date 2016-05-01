/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat.gui.swing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import de.sogomn.rat.gui.IChatGui;

public final class ChatSwingGui extends AbstractSwingGui implements IChatGui {
	
	private JTextArea chat;
	private JTextField submit;
	private JScrollPane scrollPane;
	
	private String message;
	
	private static final Dimension SIZE = new Dimension(500, 500);
	private static final String USER_PREFIX = "You: ";
	
	public ChatSwingGui() {
		chat = new JTextArea();
		submit = new JTextField();
		scrollPane = new JScrollPane(chat, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		final Container contentPane = frame.getContentPane();
		
		submit.addActionListener(this::messageSubmitted);
		chat.setEditable(false);
		chat.setLineWrap(true);
		chat.setWrapStyleWord(true);
		
		contentPane.add(scrollPane, BorderLayout.CENTER);
		contentPane.add(submit, BorderLayout.SOUTH);
		
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setPreferredSize(SIZE);
		frame.pack();
		frame.setLocationRelativeTo(null);
	}
	
	private void messageSubmitted(final ActionEvent a) {
		final String message = submit.getText();
		
		if (!message.isEmpty()) {
			this.message = message;
			
			notifyListeners(controller -> controller.userInput(MESSAGE_SENT, this));
			
			appendLine(USER_PREFIX + message);
		}
		
		submit.setText("");
	}
	
	@Override
	public void appendLine(String line) {
		line += System.lineSeparator();
		
		chat.append(line);
		
		final JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
		final int bottom = scrollBar.getMaximum();
		
		scrollBar.setValue(bottom);
	}
	
	@Override
	public String getMessageInput() {
		return message;
	}
	
}
