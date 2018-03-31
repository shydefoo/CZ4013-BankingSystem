package main;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import message.BytePacker;
import socket.Socket;

public class CallbackHandlerClass {
	private Socket designatedSocket;
	private ArrayList<Subscriber> allTheSubscribers;
	public CallbackHandlerClass(Socket designatedSocket){
		this.designatedSocket = designatedSocket;
		allTheSubscribers = new ArrayList<>();
		
	}
	
	public void registerSubscriber(InetAddress address, int portNumber, int messageId, int timeLimit){
		Subscriber subscriber = new Subscriber(address, portNumber, messageId, timeLimit);
		allTheSubscribers.add(subscriber); //lets keep it simple for now, just add the subscriber in.
	}
	public void removeSubscriber(Subscriber s){
		allTheSubscribers.remove(s);
	}
	public void checkValidity(){
		Date now = new GregorianCalendar().getTime();
		for (Subscriber s: allTheSubscribers){
			if(now.before(s.getExpireTime().getTime())){
				System.out.println("Removing:");
				s.printSubscriberInfo();
				//Before removing, need to send termination message!
				allTheSubscribers.remove(s);
			}
		}
	}
	
	public void broadcast(BytePacker msg) throws IOException{
		for (Subscriber s: allTheSubscribers){
			designatedSocket.send(msg, s.getAddress(), s.getPortNumber());
		}
	}
	
	public class Subscriber{
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
		
		public InetAddress getAddress(){
			return this.address;
		}
		public int getPortNumber(){
			return this.portNumber;
		}
		
		public Calendar getExpireTime(){
			return this.expireTime;
		}
		public void printSubscriberInfo(){
			System.out.println("Address: " + address.toString() + "portNumber: " + portNumber + "messageId: " + messageId + "expireTime" + expireTime.getTime());
		}
	}

}
