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

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

import de.sogomn.rat.gui.INotificationGui;

public final class NotificationSwingGui implements INotificationGui {
	
	private JDialog dialog;
	private JLabel label;
	
	private static final EmptyBorder PADDING = new EmptyBorder(10, 50, 10, 50);
	private static final int GAP = 30;
	private static final int INTERVAL = 3;
	private static final int WAIT_TIME = 3000;
	
	public NotificationSwingGui(final String text, final Icon icon) {
		dialog = new JDialog();
		label = new JLabel(text);
		
		label.setIcon(icon);
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		label.setBorder(PADDING);
		label.setIconTextGap(GAP);
		
		dialog.setUndecorated(true);
		dialog.setContentPane(label);
		dialog.pack();
		dialog.setLocation(-dialog.getWidth(), 0);
		dialog.setAlwaysOnTop(true);
	}
	
	private void moveForward() throws InterruptedException {
		while (dialog.getX() < 0) {
			final int x = dialog.getX() + 1;
			final int y = dialog.getY();
			
			dialog.setLocation(x, y);
			
			Thread.sleep(INTERVAL);
		}
	}
	
	private void moveBackwards() throws InterruptedException {
		while (dialog.getX() > -dialog.getWidth()) {
			final int x = dialog.getX() - 1;
			final int y = dialog.getY();
			
			dialog.setLocation(x, y);
			
			Thread.sleep(INTERVAL);
		}
	}
	
	public NotificationSwingGui(final String text) {
		this(text, null);
	}
	
	public NotificationSwingGui() {
		this("");
	}
	
	@Override
	public void trigger() {
		if (dialog.isVisible() || !dialog.isDisplayable()) {
			return;
		}
		
		final Runnable runnable = () -> {
			try {
				moveForward();
				Thread.sleep(WAIT_TIME);
				moveBackwards();
			} catch (final Exception ex) {
				ex.printStackTrace();
			}
			
			dialog.setVisible(false);
			dialog.dispose();
		};
		final Thread thread = new Thread(runnable);
		
		dialog.setVisible(true);
		
		thread.setDaemon(true);
		thread.start();
	}
	
}
