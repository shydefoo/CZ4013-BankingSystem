package services;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import message.BytePacker;
import message.OneByteInt;
import socket.Socket;

public class CallbackHandlerClass extends Service {
	private Socket designatedSocket;
	private static ArrayList<Subscriber> allTheSubscribers;
	
	public CallbackHandlerClass(Socket designatedSocket){
		super(null);
		this.designatedSocket = designatedSocket;
		allTheSubscribers = new ArrayList<>();
		
	}
	
	public static void registerSubscriber(InetAddress address, int portNumber, int messageId, int timeout){
		Subscriber subscriber = new Subscriber(address, portNumber, messageId, timeout);
		
		//lets keep it simple for now, just add the subscriber in.
		//To check if same port and address later. 
		allTheSubscribers.add(subscriber); 
		System.out.println("New subscriber added");
		subscriber.printSubscriberInfo();
	}
	
	
	public void checkValidity() throws IOException{
		Date now = new GregorianCalendar().getTime();
		ArrayList<Subscriber> temp = new ArrayList<>();
		for (Subscriber s: allTheSubscribers){
			if(now.after(s.expireTime.getTime())){
				System.out.println("Removing:");
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
	
	public void sendTerminationMessage(Subscriber s,OneByteInt status) throws IOException{
		System.out.println("sending termination message");
		String reply = "Auto monitoring expired.";
		System.out.println("subscriber messageId: " + s.messageId);
		BytePacker replyMessage = new BytePacker.Builder()
				.setProperty(Service.STATUS, status)
				.setProperty(Service.MESSAGE_ID, s.messageId)
				.setProperty(Service.REPLY, reply)
				.build();
		designatedSocket.send(replyMessage, s.address, s.portNumber);
	}
	
	public void broadcast(BytePacker msg) throws IOException{
		checkValidity();
		if(((OneByteInt)msg.getPropToValue().get(Service.getStatus())).getValue()==0){ //Only if reply status is 0, then broadcast out. 
			for (Subscriber s: allTheSubscribers){
				msg.getPropToValue().put(Service.MESSAGE_ID, s.messageId); //replace msgId of reply to whoever that made the action of with msgId of subscriber.
				designatedSocket.send(msg, s.address, s.portNumber);
			}
		}
		
	}
	@Override
	public BytePacker handleService(InetAddress clientAddress, int clientPortNumber, byte[] dataFromClient,
			Socket socket) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static class Subscriber{
		private InetAddress address;
		private int portNumber;
		private Calendar expireTime;
		private int messageId; //Store messageId, such that each msg(the update) sent to subscriber has same msgId of request? Hmmmm okay.
		//think of a way to manage monitor interval. timeout should be associated with Subscriber object. 
		//private 
		public Subscriber(InetAddress address, int portNumber, int messageId, int timeLimit){
			this.address = address;
			this.portNumber = portNumber;
			this.messageId = messageId;
			
			//Calculate time to remove subscriber.
			expireTime = Calendar.getInstance();
			expireTime.add(Calendar.MINUTE, timeLimit);
			System.out.println("expireTime: " + expireTime.getTime());
		}
		
		
		public void printSubscriberInfo(){
			System.out.println("Address: " + address.toString() + ", portNumber: " + portNumber + ", messageId: " + messageId + ", expireTime: " + expireTime.getTime());
		}
	}

	

}
