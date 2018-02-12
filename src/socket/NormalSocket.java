package socket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import message.BytePackerClass;

public class NormalSocket implements Socket {
	
	private DatagramSocket socket;
	
	@Override
	public void send(BytePackerClass msg, InetAddress address, int port) {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public void receive(DatagramPacket p) {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public void setTimeOut() {
		// TODO Auto-generated method stub
		return;
	}

	public DatagramSocket getSocket() {
		return socket;
	}

	public void setSocket(DatagramSocket socket) {
		this.socket = socket;
	}

	
	
}
