package socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

import message.BytePacker;

public interface Socket {
	void send(BytePacker msg ,InetAddress address, int port) throws IOException;
	void receive(DatagramPacket p) throws IOException;
	void close();
	void setTimeOut();
}
