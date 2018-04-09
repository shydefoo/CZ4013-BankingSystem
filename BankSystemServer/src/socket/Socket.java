package socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;

import message.BytePacker;


/**
 * This class provides the interface for the Socket that the server uses. 
 * Each subclass that extends the Socket interface has to implement the 4 methods of the interface to carry out basic
 * socket functions. 
 * @author Shide
 *
 */
public interface Socket {
	void send(BytePacker msg ,InetAddress address, int port) throws IOException;
	void receive(DatagramPacket p) throws IOException;
	void close();
	void setTimeOut(int timeout) throws SocketException;
}
