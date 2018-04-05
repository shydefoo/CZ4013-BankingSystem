package services;


import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import main.Console;
import message.BytePacker;
import message.OneByteInt;
import socket.Socket;

/**
 * This class handles the callback service. It keeps a list of subscribers and sends a message to these subscribers
 * whenever the broadcast method is called, using the server socket
 * @author Shide
 *
 */
public class CallbackHandlerClass implements Runnable {
	private Socket designatedSocket;
	private static ArrayList<Subscriber> allTheSubscribers;
	
	/**
	 * Class constructor for CallbackHandlerClass
	 * @param designatedSocket - socket that server is using to send and receive messages
	 */
	public CallbackHandlerClass(Socket designatedSocket){
		this.designatedSocket = designatedSocket;
		allTheSubscribers = new ArrayList<>();
		
	}
	
	/**
	 * Register subscriber to this callback service
	 * @param address - ip address of subscriber
	 * @param portNumber - port number of subscriber
	 * @param messageId - messageID of update message
	 * @param timeout - duration that subscriber wishes to receive updates for
	 */
	public void registerSubscriber(InetAddress address, int portNumber, int messageId, int timeout){
		Subscriber subscriber = new Subscriber(address, portNumber, messageId, timeout);
		
		//Check if client has already subscribed to callbackService or not 
		if(checkExisting(address,portNumber,messageId,timeout)){
			allTheSubscribers.add(subscriber); 
			Console.debug("New subscriber added");
			subscriber.printSubscriberInfo();
		}
		else{
			Console.debug("Client exists in list of subscribers");
		}
		
	}
	
	/**
	 * Checks if this subscriber already exists in list of subscribers. 
	 * @param address - ip address of subscriber
	 * @param portNumber - port number of subscriber
	 * @param messageId - messageID of update message
	 * @param timeout - duration that subscriber wishes to receive updates for
	 * @return
	 */
	public boolean checkExisting(InetAddress address, int portNumber, int messageId, int timeout){
		//Console.debug("Check Existing");
		boolean DoesNotExists = true; //set to true, means no such subscriber
		for(Subscriber s: allTheSubscribers){
			s.printSubscriberInfo();
			if(s.address.equals(address) && s.portNumber==portNumber && s.messageId == messageId){
				DoesNotExists = false;
				break;
			}
		}
		return DoesNotExists;
	}
	
	/**
	 * Monitors which subscribers have expired. A termination message is sent to a subscriber informing them that
	 * they are removed from this callback service, before the subscriber is removed from the listOfSubscribers.
	 * @throws IOException
	 */
	public void checkValidity() throws IOException{
		Date now = new GregorianCalendar().getTime();
		ArrayList<Subscriber> temp = new ArrayList<>();
		for (Subscriber s: allTheSubscribers){
			if(now.after(s.expireTime.getTime())){
				Console.debug("Removing:");
				s.printSubscriberInfo();
				//Before removing, need to send termination message!
				OneByteInt status = new OneByteInt(4);
				sendTerminationMessage(s,status);
				temp.add(s);
			}
		}
		for(Subscriber x: temp){
			if(allTheSubscribers.contains(x)){
				allTheSubscribers.remove(x);
			}
		}
	}
	
	/**
	 * Send message to a subscriber
	 * @param s - recipient subscriber 
	 * @param status - message status
	 * @throws IOException
	 */
	public void sendTerminationMessage(Subscriber s,OneByteInt status) throws IOException{
		Console.debug("Sending termination message");
		String reply = "Auto monitoring expired.";
		//System.out.println("subscriber messageId: " + s.messageId);
		BytePacker replyMessage = new BytePacker.Builder()
				.setProperty(Service.STATUS, status)
				.setProperty(Service.getMessageId(), s.messageId)
				.setProperty(Service.REPLY, reply)
				.build();
		designatedSocket.send(replyMessage, s.address, s.portNumber);
	}
	
	/**
	 * Checks which subscribers have not expired, then send update message to all subscribers
	 * @param msg - BytePacker message 
	 */
	public void broadcast(BytePacker msg){
		try {
			checkValidity();
			if(((OneByteInt)msg.getPropToValue().get(Service.getStatus())).getValue()==0){ //Only if reply status is 0, then broadcast out. 
				if(!allTheSubscribers.isEmpty()){
					Console.debug("Sending packets to subscribers:");
					for (Subscriber s: allTheSubscribers){
						msg.getPropToValue().put(Service.getMessageId(), s.messageId); //replace msgId of reply to whoever that made the action of with msgId of subscriber.
						designatedSocket.send(msg, s.address, s.portNumber);
					}
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error broadcasting");
			e.printStackTrace();
		}
		
		
	}
	/**
	 * checkValidity() is carried out on a separate thread. This method is called every 20s
	 */
	@Override
	public void run() {
		while(true){
			try {
				//Check validity every 10s on separate thread.
				checkValidity();
				Thread.sleep(10000);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * The subscriber class stores the information of clients that subscribe to the callback service. 
	 * @author Shide
	 *
	 */
	public static class Subscriber{
		private InetAddress address;
		private int portNumber;
		private Calendar expireTime;
		private int messageId; 
		
		/**
		 * Class constructor for Subscriber object
		 * @param address - ip address
		 * @param portNumber - port number
		 * @param messageId - message id of incoming request that registers for callback
		 * @param timeLimit - monitor interval
		 */
		public Subscriber(InetAddress address, int portNumber, int messageId, int timeLimit){
			this.address = address;
			this.portNumber = portNumber;
			this.messageId = messageId;
			
			//Calculate time to remove subscriber.
			expireTime = Calendar.getInstance();
			expireTime.add(Calendar.MINUTE, timeLimit);
			Console.debug("expireTime: " + expireTime.getTime());
		}
		
		
		public void printSubscriberInfo(){
			Console.debug("Address: " + address.toString() + ", portNumber: " + portNumber + ", messageId: " + messageId + ", expireTime: " + expireTime.getTime());
		}
	}

	

	

}
