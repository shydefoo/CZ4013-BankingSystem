package socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

import message.BytePackerClass;

public interface Socket {
	void send(BytePackerClass msg ,InetAddress address, int port) throws IOException;
	void receive(DatagramPacket p) throws IOException;
	void close();
	void setTimeOut();
}
