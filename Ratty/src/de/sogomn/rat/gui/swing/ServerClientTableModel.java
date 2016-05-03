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

import static de.sogomn.rat.util.Constants.LANGUAGE;

import java.util.ArrayList;
import java.util.function.Function;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import de.sogomn.rat.gui.ServerClient;

final class ServerClientTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 919111102883611810L;
	
	private ArrayList<ServerClient> serverClients;
	private ArrayList<Column<?>> columns;
	
	private static final Column<String> NAME = new Column<String>(LANGUAGE.getString("column.name"), String.class, ServerClient::getName);
	private static final Column<ImageIcon> LOCATION = new Column<ImageIcon>(LANGUAGE.getString("column.location"), ImageIcon.class, ServerClient::getFlag);
	private static final Column<String> IP_ADDRESS = new Column<String>(LANGUAGE.getString("column.address"), String.class, ServerClient::getAddress);
	private static final Column<String> PORT = new Column<String>(LANGUAGE.getString("column.port"), String.class, ServerClient::getPort);
	private static final Column<String> OS = new Column<String>(LANGUAGE.getString("column.os"), String.class, ServerClient::getOs);
	private static final Column<String> VERSION = new Column<String>(LANGUAGE.getString("column.version"), String.class, ServerClient::getVersion);
	private static final Column<Boolean> STREAMING_DESKTOP = new Column<Boolean>(LANGUAGE.getString("column.desktop"), Boolean.class, ServerClient::isStreamingDesktop);
	private static final Column<Boolean> STREAMING_VOICE = new Column<Boolean>(LANGUAGE.getString("column.voice"), Boolean.class, ServerClient::isStreamingVoice);
	private static final Column<Long> PING = new Column<Long>(LANGUAGE.getString("column.ping"), Long.class, ServerClient::getPing);
	
	public ServerClientTableModel() {
		serverClients = new ArrayList<ServerClient>();
		columns = new ArrayList<Column<?>>();
		
		addColumn(NAME);
		addColumn(LOCATION);
		addColumn(IP_ADDRESS);
		addColumn(PORT);
		addColumn(OS);
		addColumn(VERSION);
		addColumn(STREAMING_DESKTOP);
		addColumn(STREAMING_VOICE);
		addColumn(PING);
	}
	
	public void addColumn(final Column<?> column) {
		columns.add(column);
	}
	
	public void removeColumn(final Column<?> column) {
		columns.remove(column);
	}
	
	@Override
	public String getColumnName(final int columnIndex) {
		final int columnCount = columns.size();
		
		if (columnIndex <= columnCount - 1 && columnIndex >= 0) {
			final Column<?> column = columns.get(columnIndex);
			
			return column.name;
		}
		
		return super.getColumnName(columnIndex);
	}
	
	@Override
	public Class<?> getColumnClass(final int columnIndex) {
		final int columnCount = columns.size();
		if (columnIndex <= columnCount - 1 && columnIndex >= 0) {
			final Column<?> column = columns.get(columnIndex);
			
			return column.clazz;
		}
		
		return super.getColumnClass(columnIndex);
	}
	
	@Override
	public boolean isCellEditable(final int rowIndex, final int columnIndex) {
		return false;
	}
	
	@Override
	public Object getValueAt(final int rowIndex, final int columnIndex) {
		final ServerClient serverClient = getServerClient(rowIndex);
		final int columnCount = columns.size();
		
		if (serverClient == null || columnIndex > columnCount - 1 || columnIndex < 0) {
			return null;
		}
		
		final Column<?> column = columns.get(columnIndex);
		final Function<ServerClient, ?> value = column.value;
		
		return value.apply(serverClient);
	}
	
	@Override
	public int getColumnCount() {
		return columns.size();
	}
	
	@Override
	public int getRowCount() {
		return serverClients.size();
	}
	
	public void addServerClient(final ServerClient client) {
		SwingUtilities.invokeLater(() -> {
			serverClients.add(client);
			fireTableDataChanged();
		});
	}
	
	public void removeServerClient(final ServerClient client) {
		SwingUtilities.invokeLater(() -> {
			serverClients.remove(client);
			fireTableDataChanged();
		});
	}
	
	public ServerClient getServerClient(final int rowIndex) {
		if (rowIndex <= serverClients.size() - 1 && rowIndex >= 0) {
			return serverClients.get(rowIndex);
		}
		
		return null;
	}
	
	public ServerClient[] getServerClients(final int... rows) {
		final ServerClient[] clients = new ServerClient[rows.length];
		
		for (int i = 0; i < rows.length; i++) {
			final int row = rows[i];
			
			clients[i] = getServerClient(row);
		}
		
		return clients;
	}
	
	public static final class Column<T> {
		
		final String name;
		final Class<T> clazz;
		final Function<ServerClient, T> value;
		
		public Column(final String name, final Class<T> clazz, final Function<ServerClient, T> value) {
			this.name = name;
			this.clazz = clazz;
			this.value = value;
		}
		
	}
	
}
