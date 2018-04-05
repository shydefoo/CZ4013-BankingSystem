package services;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

import main.Client;
import main.Console;
import message.BytePacker;
import message.ByteUnpacker;
import message.OneByteInt;
import socket.NormalSocket;
import socket.Socket;
import socket.WrapperSocket;
/**
 * Abstract class for all other services that extend the Service class
 * Abstract methods need to be implemented by daughter Service classes
 * @author Shide
 *
 */
public abstract class Service {
	
	private final ByteUnpacker unpacker;
	protected static final String STATUS = "status";
	protected static final String SERVICE_ID = "serviceId";
    protected static final String MESSAGE_ID = "messageId";
    protected static final String REPLY = "reply";
    /**
     * Superclass Constructor of Service object
     * @param unpacker Unpacker object with predefined message format 
     */
	protected Service(ByteUnpacker unpacker){
		this.unpacker = new ByteUnpacker.Builder()
						.setType(STATUS, ByteUnpacker.TYPE.ONE_BYTE_INT)
						.setType(MESSAGE_ID, ByteUnpacker.TYPE.INTEGER)
						.setType(REPLY, ByteUnpacker.TYPE.STRING)
						.build()
						.defineComponents(unpacker);
	}
	
	/**
	 * Makes sure reply's message ID matches request message ID
	 * 
	 * @param client
	 * @param packer request that was just sent out
	 * @param message_id id of reply received
	 * @return Unpacked message once received from server. message_id in msg from server must match param message_id
	 * @throws IOException
	 */
	public final ByteUnpacker.UnpackedMsg receivalProcedure(Client client, BytePacker packer, int message_id ) throws IOException{
		while(true){
			try{
				DatagramPacket reply = client.receive();
				ByteUnpacker.UnpackedMsg unpackedMsg = this.getUnpacker().parseByteArray(reply.getData());
				if(checkMsgId(message_id,unpackedMsg)){
					return unpackedMsg;
				}		
			}catch (SocketTimeoutException e){
				Console.debug("Socket timeout.");
				client.send(packer);
			}
		}
	}
	/**
	 * Checks value of message ID from reply message
	 * @param message_id id of reply received
	 * @param unpackedMsg reply message received 
	 * @return boolean showing whether message id of request and reply match
	 * 
	 */
	public final boolean checkMsgId(Integer message_id, ByteUnpacker.UnpackedMsg unpackedMsg){
		Integer return_message_id = unpackedMsg.getInteger(MESSAGE_ID);
		Console.debug("return_message_id: " + return_message_id);
		Console.debug("message_id: " + message_id);
		if(return_message_id != null){
			return message_id == return_message_id;
		}
		return false;
	}
	/**
	 * Checks status from reply message
	 * 
	 * @param unpackedMsg reply message from server
	 * @return true if no error, false if status indicates error
	 */
	public final boolean checkStatus(ByteUnpacker.UnpackedMsg unpackedMsg){
		OneByteInt status = unpackedMsg.getOneByteInt(STATUS);
		Console.debug("Status: " + status.getValue());
		if(status.getValue()==0)return true; //0 means no error? okay. 
		return false;
	}
	/**
	 * Same as above, except checks with a certain status value
	 * 
	 * @param unpackedMsg reply message from server
	 * @param replyStatus value of status to compare with
	 * @return true if both are the same, false otherwise
	 */
	public final boolean checkStatus(ByteUnpacker.UnpackedMsg unpackedMsg, int replyStatus){
		OneByteInt status = unpackedMsg.getOneByteInt(STATUS);
		if(status.getValue()==replyStatus)return true; 
		return false;
	}
	
	
	public ByteUnpacker getUnpacker() {
		return unpacker;
	}
	
	public abstract void executeRequest(Console console, Client client) throws IOException;
	public abstract String ServiceName(); 
	
}
