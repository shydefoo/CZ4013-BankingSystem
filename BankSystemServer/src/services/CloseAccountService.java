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
		String reply = "";
		ByteUnpacker.UnpackedMsg unpackedMsg = this.getUnpacker().parseByteArray(dataFromClient);
		String accHolderName = unpackedMsg.getString(NAME);
		int accPin = unpackedMsg.getInteger(PIN);
		int accNum = unpackedMsg.getInteger(ACCNUM);
		int messageId = unpackedMsg.getInteger(super.getMessageId());
		int ret = Bank.closeAccount(accHolderName,accNum , accPin);
		OneByteInt status = new OneByteInt(0); 
		if (ret == 1){
			 reply = String.format("---------------\nAccount %d successfully deleted\n--------------- ", accNum);
			 BytePacker replyMessageSubscribers = super.generateReply(status, messageId, reply);
			callbackHandler.broadcast(replyMessageSubscribers);
		}
		else if(ret==-1){
			 reply = String.format("Invalid Account Number. Please try again.");
		}
		else if(ret == -2){
			reply = String.format("Invalid Pin number. Please try again");
		}
		
		BytePacker replyMessageClient = super.generateReply(status, messageId, reply);
		
		return replyMessageClient;
	}
	
	
	@Override
	public String ServiceName() {
		return "Close Account";
	}
}
