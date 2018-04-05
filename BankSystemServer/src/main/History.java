package main;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import message.BytePacker;


/**
 * History of clients to perform at-most-once semantics. 
 *
 */
public class History {
	private ArrayList<Client> clientList;
	public static final int HISTORY_RECORD_SIZE = 10;
	
	/**
	 * Class constructor of History
	 */
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
	 * Represents each client that has sent the server a request before. 
	 */
	public class Client{
		private InetAddress address;
		private int portNumber;
		private HashMap<Integer, BytePacker> messageIdToReplyMap;
		private int[] historyRecord;
		private int count;
		public Client(InetAddress address, int portNumber){
			this.address = address;
			this.portNumber = portNumber;
			this.messageIdToReplyMap = new HashMap<>();
			historyRecord = new int[HISTORY_RECORD_SIZE]; //keep 10 messages in history
			count = 0;
			Arrays.fill(historyRecord, -1);
			
		}
		
		/**
		 * Searches if messageID exist in client hashmap
		 * @param messageId - messageId of incoming request
		 * @return reply to request if messageID does exists in the hashmap, null otherwise
		 */
		public BytePacker searchForDuplicateRequest(int messageId){
			BytePacker reply = this.messageIdToReplyMap.get(messageId);
			if(reply!=null){
				Console.debug("Request already serviced. Resending reply");
			}
			return reply;
		}
		
		/**
		 * Adds a messageId and reply to hashmap after request is serviced.
		 * @param messageId - messageId of incoming request
		 * @param replyToServicedReq - reply sent to client for this request
		 */
		public void addServicedReqToMap(int messageId, BytePacker replyToServicedReq) {
			if(historyRecord[count] !=-1){
				messageIdToReplyMap.remove(historyRecord[count]);
			}
			this.messageIdToReplyMap.put(messageId, replyToServicedReq);
			historyRecord[count] = messageId; 
			count = (count + 1) % HISTORY_RECORD_SIZE;
			
		}
	}


	
}
