package services;

import java.net.InetAddress;

import bank.Bank;
import message.BytePacker;
import message.ByteUnpacker;
import message.OneByteInt;
import socket.Socket;

public class CheckBalanceService extends Service {
	protected final static String ACC_NUMBER = "AccountNumber";
	protected final static String PIN = "Pin";
	private CallbackHandlerClass callbackHandler;

	public CheckBalanceService(CallbackHandlerClass callbackHandler) {
		super(new ByteUnpacker.Builder()
				.setType(ACC_NUMBER,ByteUnpacker.TYPE.INTEGER)
				.setType(PIN, ByteUnpacker.TYPE.INTEGER)
				.build());
		this.callbackHandler = callbackHandler;
		
	}

	@Override
	public BytePacker handleService(InetAddress clientAddress, int clientPortNumber, byte[] dataFromClient,
			Socket socket) {
		ByteUnpacker.UnpackedMsg unpackedMsg = this.unpacker.parseByteArray(dataFromClient);
		int accNum = unpackedMsg.getInteger(ACC_NUMBER);
		int pin = unpackedMsg.getInteger(PIN);
		int messageId = unpackedMsg.getInteger(MESSAGE_ID);
		double balance = Bank.checkBalance(accNum,pin);
		String reply = "";
		OneByteInt status = new OneByteInt(0);
		if(balance==-1){
			reply = "Invalid account number. Please try again.";
		}
		else if(balance ==-2){
			reply = "Invalid pin number . Please try again";
		}
		else{
			reply = "Current account balance: " + Double.toString(balance);
			BytePacker replyMessageSubscriber = super.generateReply(status, messageId, reply);
			callbackHandler.broadcast(replyMessageSubscriber);
		}
		BytePacker replyMessageClient = super.generateReply(status, messageId, reply);
		
		return replyMessageClient;
	}

	@Override
	public String ServiceName() {
		return "Check Balance";
	}
	
}
