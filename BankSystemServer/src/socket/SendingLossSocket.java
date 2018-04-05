package socket;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Random;

import main.Console;
import message.BytePacker;

/**
 * 
 * Simulate packet loss when sending packets out, based on a defined probability
 * @author Shide
 *
 */
public class SendingLossSocket extends WrapperSocket {
	
	private final Random random;
	private final double probability;
	public SendingLossSocket(Socket socket, double probability) {
		super(socket);
		this.random = new Random();
		this.probability = probability;
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Use probability to simulate packetloss
	 * higher prob --> higher probability of sending. lower packetloss
	 * 
	 */
	public void send(BytePacker msg, InetAddress address, int port) throws IOException {
		if(random.nextDouble()<1-probability){
			super.send(msg, address, port);
		}
		else{
			try{
				Thread.sleep(1000);
				Console.debug("Simulate Packet loss");
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	

}
