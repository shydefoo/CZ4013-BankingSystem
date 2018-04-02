package services;

import java.io.IOException;
import java.net.InetAddress;

import bank.Bank;
import message.BytePacker;
import message.ByteUnpacker;
import message.OneByteInt;
import socket.Socket;

public class CreateAccountService extends Service {
	protected final static String NAME = "Name";
	protected final static String PIN = "Pin";
	protected final static String CURRENCY = "Currency";
	protected final static String BALANCE = "Balance";
	private CallbackHandlerClass callbackHandler;
	//private CallbackHandlerClass callbackHandler;
	public CreateAccountService(CallbackHandlerClass callbackHandler){
		super(new ByteUnpacker.Builder()
						.setType(NAME, ByteUnpacker.TYPE.STRING)
						.setType(PIN, ByteUnpacker.TYPE.INTEGER)
						.setType(CURRENCY, ByteUnpacker.TYPE.STRING)
						.setType(BALANCE, ByteUnpacker.TYPE.DOUBLE)
						.build());
		this.callbackHandler = callbackHandler;
		//this.callbackHandler = callbackHandler;
						
	}
	
	@Override
	public BytePacker handleService(InetAddress clientAddress, int clientPortNumber, byte[] dataFromClient, Socket socket) {

		ByteUnpacker.UnpackedMsg unpackedMsg = this.unpacker.parseByteArray(dataFromClient);
		String accHolderName = unpackedMsg.getString(NAME);
		int accPin = unpackedMsg.getInteger(PIN);
		String accCurrency = unpackedMsg.getString(CURRENCY);
		double accBalance = unpackedMsg.getDouble(BALANCE);
		int messageId = unpackedMsg.getInteger(super.MESSAGE_ID);
		int accNum = Bank.createAccount(accHolderName, accPin, accCurrency, accBalance);
		
		OneByteInt status = new OneByteInt(0); 
		String reply = String.format("Account created, account number: %d", accNum);
		BytePacker replyMessage = super.generateReply(status, messageId, reply);
		callbackHandler.broadcast(replyMessage);
		return replyMessage;
		
		
	}
}
