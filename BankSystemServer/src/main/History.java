package main;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

import message.BytePacker;

public class History {
	private ArrayList<Client> clientList;
	
	public History(){
		clientList = new ArrayList<>();
	}
	
	
	
	/**
	 * searches for existing client in clientList, else create a new client and insert into list
	 * @param address ipaddress
	 * @param port portnumber
	 * @return client object
	 */
	public Client findClient(InetAddress address, int port){
		for(Client c : clientList){
			if(c.address.equals(address) && c.portNumber==port){
				return c;
			}
		}
		Client newClient = new Client(address, port);
		clientList.add(newClient);
		return newClient;
	}
	
	/**
	 * Searches if hashmap in client object contains a request with the same messageId. (if same messageId means duplicate request)
	 * @param client
	 * @param messageId
	 * @return
	 */
	
	
	
	public class Client{
		private InetAddress address;
		private int portNumber;
		private HashMap<Integer, BytePacker> messageIdToReplyMap;
		public Client(InetAddress address, int portNumber){
			this.address = address;
			this.portNumber = portNumber;
			this.messageIdToReplyMap = new HashMap<>();
		}
		public BytePacker searchForDuplicateRequest(int messageId){
			BytePacker reply = this.messageIdToReplyMap.get(messageId);
			return reply;
		}
		public void addServicedReqToMap(int messageId, BytePacker replyToServicedReq) {
			this.messageIdToReplyMap.put(messageId, replyToServicedReq);
			
		}
	}


	
}
