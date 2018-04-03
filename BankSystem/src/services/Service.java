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

public abstract class Service {
	
	private final ByteUnpacker unpacker;
	protected static final String STATUS = "status";
	protected static final String SERVICE_ID = "serviceId";
    protected static final String MESSAGE_ID = "messageId";
    protected static final String REPLY = "reply";
	
	protected Service(ByteUnpacker unpacker){
		this.unpacker = new ByteUnpacker.Builder()
						.setType(STATUS, ByteUnpacker.TYPE.ONE_BYTE_INT)
						.setType(MESSAGE_ID, ByteUnpacker.TYPE.INTEGER)
						.setType(REPLY, ByteUnpacker.TYPE.STRING)
						.build()
						.defineComponents(unpacker);
	}
	/**
	 * 
	 * @param client
	 * @param packer request that was just sent out, now waiting for reply from server
	 * @param message_id id of request sent out
	 * @return Unpacked message once received from server. message_id in msg from server must match param message_id
	 * @throws IOException
	 */
	public final ByteUnpacker.UnpackedMsg receivalProcedure(Client client, BytePacker packer, int message_id ) throws IOException{
		while(true){
			try{
				//DatagramSocket tempSocket = ((NormalSocket) ((WrapperSocket)client.getDesignatedSocket()).getSocket()).getSocket();
				//Console.debug("bufferSize: "+ tempSocket.getReceiveBufferSize());
				DatagramPacket reply = client.receive();
				ByteUnpacker.UnpackedMsg unpackedMsg = this.getUnpacker().parseByteArray(reply.getData());
				if(checkMsgId(message_id,unpackedMsg)) return unpackedMsg;
			}catch (SocketTimeoutException e){
				//If socket receive function timeout, catch exception, resend request. Stays here until reply received? okay. 
				Console.debug("Socket timeout.");
				client.send(packer);
			}
			
			
		}
	}
	public final boolean checkMsgId(Integer message_id, ByteUnpacker.UnpackedMsg unpackedMsg){
		Integer return_message_id = unpackedMsg.getInteger(MESSAGE_ID);
		Console.debug("return_message_id: " + Integer.toString(return_message_id));
		Console.debug("message_id: " + Integer.toString(message_id));
		if(return_message_id != null){
			return message_id == return_message_id;
		}
		return false;
	}
	
	public final boolean checkStatus(ByteUnpacker.UnpackedMsg unpackedMsg){
		OneByteInt status = unpackedMsg.getOneByteInt(STATUS);
		Console.debug("Status: " + status.getValue());
		if(status.getValue()==0)return true; //0 means no error? okay. 
		return false;
	}
	
	public final boolean checkStatus(ByteUnpacker.UnpackedMsg unpackedMsg, int replyStatus){
		OneByteInt status = unpackedMsg.getOneByteInt(STATUS);
		if(status.getValue()==replyStatus)return true; //0 means no error? okay. 
		return false;
	}
	
	
	public ByteUnpacker getUnpacker() {
		return unpacker;
	}
	
	public abstract void executeRequest(Console console, Client client) throws IOException;
	public abstract String ServiceName(); 
	
}
