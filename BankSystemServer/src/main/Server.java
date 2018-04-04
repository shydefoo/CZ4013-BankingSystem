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
import socket.NormalSocket;
import socket.ReceivingLossSocket;
import socket.SendingLossSocket;
import socket.Socket;

public class Server {
	protected HashMap<Integer, Service> idToServiceMap;
	protected Socket designatedSocket;
	protected int portNumber;
	protected String ipAddress;
	protected final int bufferSize = 2048;
	protected byte[] buffer;
	
	public Server(Socket socket) throws SocketException{
		this.idToServiceMap = new HashMap<>();
		this.designatedSocket = socket;
		this.buffer = new byte[bufferSize];
		
		
	}
	
	public void addServiceToServer(int id, Service service){
		if(!this.idToServiceMap.containsKey(id)){
			this.idToServiceMap.put(id, service);
			System.out.println("Service added");
		}
		else{
			System.out.printf("There is no existing service using service id %d, please use a different id.\n",id);
		}		
	}
	
	public void start() throws IOException{
		while(true){
			DatagramPacket p = receive(); /*Create DatagramPacket to receive requests from clients, assumes that it has no problem receiving.*/
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
			}else{
				Console.debug("Nothing received");
			}
					
		}
	}
	
	public DatagramPacket receive() throws IOException{
		Arrays.fill(buffer, (byte) 0);	//empty buffer
		DatagramPacket p = new DatagramPacket(buffer, buffer.length);
		System.out.println("Waiting for request...");
		this.designatedSocket.receive(p);
		
		return p;
	}
	
	public void useSendingLossSocket(double probability){
		this.designatedSocket = new SendingLossSocket(this.designatedSocket, probability);
	}
	public void useReceivingLossSocket(double probability){
		this.designatedSocket = new ReceivingLossSocket(this.designatedSocket, probability);
	}
}
