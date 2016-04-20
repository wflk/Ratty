/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.rat.gui;


public interface IChatGui {
	
	String MESSAGE_SENT = "Message sent";
	
	public void appendLine(final String line);
	
	public String getMessageInput();
	
}
