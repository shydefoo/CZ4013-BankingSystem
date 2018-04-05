package main;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.HashMap;

import message.BytePacker;
import services.CallbackHandlerClass;
import services.Service;
import socket.CorruptedSocket;
import socket.NormalSocket;
import socket.ReceivingLossSocket;
import socket.SendingLossSocket;
import socket.Socket;

/**
 * Server that performs at-least-once semantics
 * @author Shide
 *
 */
public class Server {
	protected HashMap<Integer, Service> idToServiceMap;
	protected Socket designatedSocket;
	protected int portNumber;
	protected String ipAddress;
	protected final int bufferSize = 2048;
	protected byte[] buffer;
	/**
	 * Class constructor of Server	
	 * @param socket - socket object used to send and receive messages
	 * @throws SocketException
	 */
	public Server(Socket socket) throws SocketException{
		this.idToServiceMap = new HashMap<>();
		this.designatedSocket = socket;
		this.buffer = new byte[bufferSize];
		
		
	}
	
	/**
	 * Add a service to the hashmap containing all services
	 * @param id - Service ID
	 * @param service - Type of service
	 */
	public void addServiceToServer(int id, Service service){
		if(!this.idToServiceMap.containsKey(id)){
			this.idToServiceMap.put(id, service);
			System.out.println("Service added");
		}
		else{
			System.out.printf("There is no existing service using service id %d, please use a different id.\n",id);
		}		
	}
	
	/**
	 * Server starts listening for incoming request once called
	 */
	@SuppressWarnings("finally")
	public void start(){
		while(true){
			try{
				DatagramPacket p = receive(); 
				if(p.getLength()!=0){
					byte[] data = p.getData();
					InetAddress clientAddress = p.getAddress();
					int clientPortNumber = p.getPort();
					//Service ID from client is the first byte in the byte array sent from client
					int serviceRequested = data[0];
					Service service = null;
					if(idToServiceMap.containsKey(serviceRequested)){
						service = idToServiceMap.get(serviceRequested);
						System.out.println("Service Requested: " + service.ServiceName());
						BytePacker replyToRequest = service.handleService(clientAddress,clientPortNumber, data, this.designatedSocket);
						this.designatedSocket.send(replyToRequest, clientAddress, clientPortNumber);					
					}	
				}
			}catch(IOException e){
				e.printStackTrace();
				
			}catch(NullPointerException e){
				Console.debug("Received corrupted data");
				e.printStackTrace();
			}finally{
				continue;
			}	
		}
	}
	
	/**
	 * Listen for incoming messages
	 * @return DatagramPacket with new message
	 * @throws IOException
	 */
	public DatagramPacket receive() throws IOException{
		Arrays.fill(buffer, (byte) 0);	//empty buffer
		DatagramPacket p = new DatagramPacket(buffer, buffer.length);
		//System.out.println("Waiting for request...");
		this.designatedSocket.receive(p);
		
		return p;
	}
	
	/**
	 * Used to simulate packet loss when sending
	 * @param probability - probability of message being sent out
	 */
	public void useSendingLossSocket(double probability){
		this.designatedSocket = new SendingLossSocket(this.designatedSocket, probability);
	}
	/**
	 * Used to simulate packet loss when receiving
	 * @param probability - probability message is received
	 */
	public void useReceivingLossSocket(double probability){
		this.designatedSocket = new ReceivingLossSocket(this.designatedSocket, probability);
	}
	
	/**
	 * Used to create corrupted messages to be sent or received. 
	 * @param probability - probability messages are corrupted
	 */
	public void useCorruptedSocket(double probability){
		this.designatedSocket = new CorruptedSocket(this.designatedSocket, probability);
	}
}
