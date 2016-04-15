package de.sogomn.rat.gui.server;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.jnativehook.keyboard.NativeKeyEvent;

final class LoggingGui {
	
	private JFrame frame;
	private JTextArea textArea;
	private JScrollPane scrollPane;
	
	private static final Dimension SIZE = new Dimension(500, 500);
	private static final String KEY_MODIFIER_TEXT_FORMAT = " [%s] ";
	
	public LoggingGui() {
		frame = new JFrame();
		textArea = new JTextArea();
		scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setContentPane(scrollPane);
		frame.setPreferredSize(SIZE);
		frame.pack();
		frame.setLocationRelativeTo(null);
	}
	
	public void log(final int keyCode) {
		String text = NativeKeyEvent.getKeyText(keyCode);
		
		if (text == null) {
			return;
		}
		
		if (text.length() == 1) {
			textArea.append(text);
		} else {
			text = String.format(KEY_MODIFIER_TEXT_FORMAT, text);
			
			textArea.append(text);
		}
	}
	
	public void setVisible(final boolean visible) {
		SwingUtilities.invokeLater(() -> {
			frame.setVisible(visible);
		});
	}
	
	public void close() {
		SwingUtilities.invokeLater(() -> {
			frame.setVisible(false);
			frame.dispose();
		});
	}
	
	public void setTitle(final String title) {
		frame.setTitle(title);
	}
	
}
