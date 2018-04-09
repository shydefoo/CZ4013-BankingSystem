package socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketTimeoutException;
import java.util.Random;

import main.Console;

/**
 * This class simulates packet loss when receiving
 * 
 * @author Shide
 *
 */
public class ReceivingLossSocket extends WrapperSocket {
	private Random random;
	private double probability;
	
	public ReceivingLossSocket(Socket socket, double probability){
		super(socket);
		this.random = new Random();
		this.probability = probability;
	}
	
	/**
	 * Method to call receive depending based on a probability. 
	 * Higher probability --> higher probability of receiving.
	 */
	public void receive(DatagramPacket p) throws IOException, SocketTimeoutException{
		if(random.nextDouble()<this.probability){
			super.receive(p);
		}
		else{
			try {
				Thread.sleep(1000);
				Console.debug("Simulate packetloss when receiving");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}throw new SocketTimeoutException();
		}
	}
}
