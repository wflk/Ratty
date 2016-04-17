package de.sogomn.rat.gui;

import java.awt.Image;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import de.sogomn.engine.util.AbstractListenerContainer;

public abstract class AbstractGui extends AbstractListenerContainer<IGuiController> {
	
	protected JFrame frame;
	
	public AbstractGui() {
		frame = new JFrame();
	}
	
	private int showOptionDialog(final int messageType, final String message, final int optionType, final String... options) {
		final int input = JOptionPane.showOptionDialog(frame, message, null, optionType, messageType, null, options, null);
		
		return input;
	}
	
	private void showMessageDialog(final int messageType, final String message) {
		JOptionPane.showMessageDialog(frame, message, null, messageType, null);
	}
	
	public void update() {
		//...
	}
	
	public void close() {
		SwingUtilities.invokeLater(() -> {
			frame.setVisible(false);
			frame.dispose();
		});
	}
	
	public void setVisible(final boolean visible) {
		SwingUtilities.invokeLater(() -> {
			frame.setVisible(visible);
		});
	}
	
	public void setTitle(final String title) {
		frame.setTitle(title);
	}
	
	public void setIcons(final List<? extends Image> icons) {
		frame.setIconImages(icons);
	}
	
	public void showWarning(final String message) {
		showMessageDialog(JOptionPane.WARNING_MESSAGE, message);
	}
	
	public int showWarning(final String message, final String yes, final String no) {
		return showOptionDialog(JOptionPane.WARNING_MESSAGE, message, JOptionPane.YES_NO_OPTION, yes, no);
	}
	
	public void showError(final String message) {
		showMessageDialog(JOptionPane.ERROR_MESSAGE, message);
	}
	
	public void showMessage(final String message) {
		showMessageDialog(JOptionPane.INFORMATION_MESSAGE, message);
	}
	
	public int showOptions(final String message, final String yes, final String no, final String cancel) {
		return showOptionDialog(JOptionPane.QUESTION_MESSAGE, message, JOptionPane.YES_NO_CANCEL_OPTION, yes, no, cancel);
	}
	
	public String getInput(final String message) {
		final String input = JOptionPane.showInputDialog(frame, message);
		
		return input;
	}
	
	public String getInput() {
		return getInput(null);
	}
	
}
