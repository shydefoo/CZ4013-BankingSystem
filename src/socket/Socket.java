package socket;

import java.net.DatagramPacket;
import java.net.InetAddress;

import message.BytePackerClass;

public interface Socket {
	void send(BytePackerClass msg ,InetAddress address, int port);
	void receive(DatagramPacket p);
	void close();
	void setTimeOut();
}
