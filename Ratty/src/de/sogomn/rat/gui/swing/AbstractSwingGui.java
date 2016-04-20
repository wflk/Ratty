/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat.gui.swing;

import java.awt.Image;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import de.sogomn.engine.util.AbstractListenerContainer;
import de.sogomn.rat.gui.IGui;
import de.sogomn.rat.gui.IGuiController;

public abstract class AbstractSwingGui extends AbstractListenerContainer<IGuiController> implements IGui {
	
	protected JFrame frame;
	
	public AbstractSwingGui() {
		frame = new JFrame();
	}
	
	private int showOptionDialog(final int messageType, final String message, final int optionType, final String... options) {
		final int input = JOptionPane.showOptionDialog(frame, message, null, optionType, messageType, null, options, null);
		
		return input;
	}
	
	private void showMessageDialog(final int messageType, final String message) {
		JOptionPane.showMessageDialog(frame, message, null, messageType, null);
	}
	
	@Override
	public void update() {
		//...
	}
	
	@Override
	public void close() {
		SwingUtilities.invokeLater(() -> {
			frame.setVisible(false);
			frame.dispose();
		});
	}
	
	@Override
	public void setVisible(final boolean visible) {
		SwingUtilities.invokeLater(() -> {
			frame.setVisible(visible);
		});
	}
	
	@Override
	public void setTitle(final String title) {
		frame.setTitle(title);
	}
	
	@Override
	public void setIcons(final List<? extends Image> icons) {
		frame.setIconImages(icons);
	}
	
	@Override
	public void showWarning(final String message) {
		showMessageDialog(JOptionPane.WARNING_MESSAGE, message);
	}
	
	@Override
	public int showWarning(final String message, final String yes, final String no) {
		return showOptionDialog(JOptionPane.WARNING_MESSAGE, message, JOptionPane.YES_NO_OPTION, yes, no);
	}
	
	@Override
	public void showError(final String message) {
		showMessageDialog(JOptionPane.ERROR_MESSAGE, message);
	}
	
	@Override
	public void showMessage(final String message) {
		showMessageDialog(JOptionPane.INFORMATION_MESSAGE, message);
	}
	
	@Override
	public int showOptions(final String message, final String yes, final String no, final String cancel) {
		return showOptionDialog(JOptionPane.QUESTION_MESSAGE, message, JOptionPane.YES_NO_CANCEL_OPTION, yes, no, cancel);
	}
	
	@Override
	public String getInput(final String message) {
		final String input = JOptionPane.showInputDialog(frame, message);
		
		return input;
	}
	
	@Override
	public boolean isVisible() {
		return frame.isVisible();
	}
	
}
