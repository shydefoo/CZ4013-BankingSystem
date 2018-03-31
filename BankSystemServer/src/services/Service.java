package services;

import java.net.InetAddress;

import message.BytePacker;
import message.ByteUnpacker;
import message.OneByteInt;
import socket.Socket;

public abstract class Service {
	
	/*
	 * Need to come up with a list of statuses that are standardized!!!
	 * 0 - success
	 * 1 - Fail
	 * 2 - Auto monitoring update
	 * 4 - Auto monitoring expired
	 * */
	ByteUnpacker unpacker;
	protected static final String SERVICE_ID = "serviceId";
	protected static final String MESSAGE_ID = "messageId";
	protected static final String STATUS = "status";
    protected static final String REPLY = "reply";
    
	public Service(ByteUnpacker unpacker){
		this.unpacker = new ByteUnpacker.Builder()
						.setType(SERVICE_ID, ByteUnpacker.TYPE.ONE_BYTE_INT)
						.setType(MESSAGE_ID, ByteUnpacker.TYPE.INTEGER)
						.build()
						.defineComponents(unpacker);
	}
	
	public BytePacker generateReply(OneByteInt status, int messageId, String reply){
		BytePacker replyMessage = new BytePacker.Builder()
							.setProperty(STATUS, status)
							.setProperty(MESSAGE_ID, messageId)
							.setProperty(REPLY, reply)
							.build();
		return replyMessage;
	}
	public static String getStatus(){
		return STATUS;
	}
	
	public abstract BytePacker handleService(InetAddress clientAddress, int clientPortNumber, byte[] dataFromClient, Socket socket);
	
}
