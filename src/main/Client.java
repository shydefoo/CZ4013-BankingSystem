package main;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;

import message.BytePacker;
import services.Service;
import socket.NormalSocket;
import socket.Socket;

public class Client {
	public static final int CREATE_ACCOUNT = 0; /*Service ID used to associate which service to call when msg gets transmitted*/
	public static final int CLOSE_ACCOUNT = 1;
	public static final int MAKE_DEPOSIT = 2;
	public static final int MAKE_WITHDRAWL = 3;
	public static final int BUFFER_SIZE = 2048;
	
	private Socket socket = null;
	private HashMap<Integer, Service> idToServiceMap; /*Hashmap containing the serviceId and corresponding service*/
	private String serverIpAddress = null;
	private int serverPortNumber = 0;
	private InetAddress InetIpAddress = null;
	private int message_id = 0;
	//buffer
    private byte[] buffer = new byte[BUFFER_SIZE];
	
	
	
	

	public Client(String ipAddress, int portNumber) throws UnknownHostException, SocketException{
		this.idToServiceMap = new HashMap<>();
		this.serverIpAddress = ipAddress;
		this.InetIpAddress = InetAddress.getByName(this.serverIpAddress);
		this.socket = new NormalSocket(new DatagramSocket());
	}
	
	public int getMessage_id() {
		return message_id++;
	}

	public void setMessage_id(int message_id) {
		this.message_id = message_id;
	}
	
	
	public void addService(int serviceId, Service service){
		idToServiceMap.put(serviceId, service);
	}
	
	public void execute(int id,Console console) throws IOException{
		if(idToServiceMap.containsKey(id)){
			Service service = this.idToServiceMap.get(id);
			service.executeRequest(console, this);
		}
	}
	public void send(BytePacker packer) throws IOException{
		this.socket.send(packer, this.InetIpAddress, this.serverPortNumber);
	}
	
	public DatagramPacket receive() throws IOException{
		clearBuffer();
		DatagramPacket p = new DatagramPacket(buffer,buffer.length);
		this.socket.receive(p);
		return p;
		
	}
	
	public void clearBuffer(){
		 Arrays.fill(buffer,(byte) 0);
	}
	
	
	
}
