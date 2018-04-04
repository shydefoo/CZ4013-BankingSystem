package main;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

import main.History.Client;
import message.BytePacker;
import message.ByteUnpacker;
import services.Service;
import socket.Socket;

public class AtMostOnceServer extends Server {
	
	private History history;

	public AtMostOnceServer(Socket socket) throws SocketException {
		super(socket);
		history = new History();
		// TODO Auto-generated constructor stub
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
					ByteUnpacker.UnpackedMsg unpackedMsg = service.getUnpacker().parseByteArray(data);
					int messageId = unpackedMsg.getInteger(Service.getMessageId());
					Client client = history.findClient(clientAddress, clientPortNumber);
					BytePacker replyToServicedReq = client.searchForDuplicateRequest(messageId);
					if(replyToServicedReq == null){
						replyToServicedReq = service.handleService(clientAddress, clientPortNumber, data, this.designatedSocket);
						client.addServicedReqToMap(messageId, replyToServicedReq);
					}
					this.designatedSocket.send(replyToServicedReq, clientAddress, clientPortNumber);
					
				}	
			}else{
				Console.debug("Nothing received");
			}
			
		}
	}
	

	

}
