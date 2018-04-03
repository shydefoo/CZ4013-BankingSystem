package services;

import java.io.IOException;
import java.net.InetAddress;

import bank.Bank;
import message.BytePacker;
import message.ByteUnpacker;
import message.OneByteInt;
import socket.Socket;

public class CloseAccountService extends Service {
	protected final static String NAME = "Name";
	protected final static String PIN = "Pin";
	protected final static String ACCNUM = "accNum";
	private CallbackHandlerClass callbackHandler;
	//private CallbackHandlerClass callbackHandler;
	public CloseAccountService(CallbackHandlerClass callbackHandler){
		super(new ByteUnpacker.Builder()
						.setType(NAME, ByteUnpacker.TYPE.STRING)
						.setType(PIN, ByteUnpacker.TYPE.INTEGER)
						.setType(ACCNUM, ByteUnpacker.TYPE.INTEGER)
						.build());
		this.callbackHandler = callbackHandler;
		//this.callbackHandler = callbackHandler;
						
	}
	
	@Override
	public BytePacker handleService(InetAddress clientAddress, int clientPortNumber, byte[] dataFromClient, Socket socket) {
		String reply;
		ByteUnpacker.UnpackedMsg unpackedMsg = this.unpacker.parseByteArray(dataFromClient);
		String accHolderName = unpackedMsg.getString(NAME);
		int accPin = unpackedMsg.getInteger(PIN);
		int accNum = unpackedMsg.getInteger(ACCNUM);
		int messageId = unpackedMsg.getInteger(super.MESSAGE_ID);
		int ret = Bank.closeAccount(accHolderName,accNum , accPin);
		
		OneByteInt status = new OneByteInt(0); 
		if (ret == 1) 
		{
			 reply = String.format("Account deleted " + ret);
			}
		else 
		{
			 reply = String.format("Account deletion failed " + ret);
			}
		
		BytePacker replyMessageClient = super.generateReply(status, messageId, reply);
		BytePacker replyMessageSubscribers = super.generateReply(status, messageId, reply);
		callbackHandler.broadcast(replyMessageSubscribers);
		return replyMessageClient;
	}
}
