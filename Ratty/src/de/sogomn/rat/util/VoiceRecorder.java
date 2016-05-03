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

package de.sogomn.rat.util;

import java.util.function.Consumer;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;

public final class VoiceRecorder {
	
	private TargetDataLine line;
	private Thread thread;
	private boolean running;
	
	private byte[] data;
	
	private Consumer<VoiceRecorder> observer;
	
	public VoiceRecorder(final int bufferSize) {
		data = new byte[bufferSize];
	}
	
	public void start() {
		if (running) {
			return;
		}
		
		final Runnable runnable = () -> {
			while (running) {
				line.read(data, 0, data.length);
				
				if (observer != null) {
					observer.accept(this);
				}
			}
		};
		
		try {
			line = AudioSystem.getTargetDataLine(null);
			thread = new Thread(runnable);
			running = true;
			
			line.open();
			line.start();
			thread.start();
		} catch (final Exception ex) {
			stop();
			
			data = new byte[0];
			
			ex.printStackTrace();
		}
	}
	
	public void stop() {
		if (!running) {
			return;
		}
		
		running = false;
		
		try {
			thread.interrupt();
			line.stop();
			line.close();
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
		
		thread = null;
		line = null;
	}
	
	public void setObserver(final Consumer<VoiceRecorder> observer) {
		this.observer = observer;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public byte[] getLastRecord() {
		return data;
	}
	
}
