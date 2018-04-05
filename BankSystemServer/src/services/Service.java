package services;

import java.io.IOException;
import java.net.InetAddress;

import message.BytePacker;
import message.ByteUnpacker;
import message.OneByteInt;
import socket.Socket;

/**
 * Abstract class for all other services that extend the Service class
 * Abstract methods need to be implemented by daughter Service classes
 * @author Shide
 *
 */
public abstract class Service {
	
	/*
	 * Need to come up with a list of statuses that are standardized!!!
	 * 0 - success
	 * 1 - Fail
	 * 2 - Auto monitoring update
	 * 4 - Auto monitoring expired
	 * */
	private ByteUnpacker unpacker;
	protected static final String SERVICE_ID = "serviceId";
	private static final String MESSAGE_ID = "messageId";
	protected static final String STATUS = "status";
    protected static final String REPLY = "reply";
    
    /**
     * Superclass Constructor of Service object
     * @param unpacker Unpacker object with predefined message format 
     */
	public Service(ByteUnpacker unpacker){
		this.setUnpacker(new ByteUnpacker.Builder()
						.setType(SERVICE_ID, ByteUnpacker.TYPE.ONE_BYTE_INT)
						.setType(getMessageId(), ByteUnpacker.TYPE.INTEGER)
						.build()
						.defineComponents(unpacker));
	}
	
	/**
	 * Creates BytePacker object from string input. Inherited by all services that extend Service class
	 * @param status - status of message
	 * @param messageId - messageId of reply
	 * @param reply - String, actual reply for client
	 * @return BytePacker object
	 */
	public BytePacker generateReply(OneByteInt status, int messageId, String reply){
		BytePacker replyMessage = new BytePacker.Builder()
							.setProperty(STATUS, status)
							.setProperty(getMessageId(), messageId)
							.setProperty(REPLY, reply)
							.build();
		return replyMessage;
	}
	public static String getStatus(){
		return STATUS;
	}
	/**
	 * Compulsory method to be implemented by all daughter classes that extend this class. 
	 * Each subclass must implement how it will handle the incoming request. This includes unmarshalling of data, 
	 * calling Bank methods and creating BytePacker object to be sent back.
	 * @param clientAddress client IP address
	 * @param clientPortNumber client port number
	 * @param dataFromClient byte array in data of DatagramPacket
	 * @param socket Socket that server is using 
	 * @return BytePacker object
	 * @throws IOException
	 * @throws NullPointerException
	 */
	public abstract BytePacker handleService(InetAddress clientAddress, int clientPortNumber, byte[] dataFromClient, Socket socket) throws IOException, NullPointerException;
	public abstract String ServiceName();

	public ByteUnpacker getUnpacker() {
		return unpacker;
	}

	public void setUnpacker(ByteUnpacker unpacker) {
		this.unpacker = unpacker;
	}

	public static String getMessageId() {
		return MESSAGE_ID;
	}
}
