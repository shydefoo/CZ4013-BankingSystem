package socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Random;

import main.Console;
import message.BytePacker;

public class SendingCorruptedSocket extends WrapperSocket{
	
	private final double probability;
	private final Random random;
	public SendingCorruptedSocket(Socket socket, double probability) {
		super(socket);
		this.random = new Random();
		this.probability = probability;
	}
	
	public void corruptData(byte[] message){
		this.random.nextBytes(message);
	}
	
	@Override
	public void send(BytePacker msg, InetAddress address, int port) throws IOException {
		if(random.nextDouble()<probability){
			byte[] msgByte = msg.getByteArray();
			corruptData(msgByte);
			NormalSocket socket = (NormalSocket) (this.getSocket());
			DatagramPacket p = new DatagramPacket(msgByte, msgByte.length,address, port);
			socket.send(p);
		}
	}
	
}
