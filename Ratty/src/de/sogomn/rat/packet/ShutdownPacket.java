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

package de.sogomn.rat.packet;

import de.sogomn.rat.ActiveConnection;
import de.sogomn.rat.util.Constants;

public final class ShutdownPacket implements IPacket {
	
	public ShutdownPacket() {
		//...
	}
	
	@Override
	public void send(final ActiveConnection connection) {
		//...
	}
	
	@Override
	public void receive(final ActiveConnection connection) {
		//...
	}
	
	@Override
	public void execute(final ActiveConnection connection) {
		Constants.OS_SERVICE.shutDown();
	}
	
}
