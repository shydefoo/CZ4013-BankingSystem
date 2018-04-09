package services;

import java.io.IOException;
import java.net.InetAddress;

import bank.Bank;
import message.BytePacker;
import message.ByteUnpacker;
import message.OneByteInt;
import socket.Socket;

/**
 * This class handles requests to create an account. 
 * @author Shide
 *
 */
public class CreateAccountService extends Service {
	protected final static String NAME = "Name";
	protected final static String PIN = "Pin";
	protected final static String CURRENCY = "Currency";
	protected final static String BALANCE = "Balance";
	private CallbackHandlerClass callbackHandler;

	/**
	 * Class constructor for CloseAccountService
	 * @param callbackHandler Callbackhandler instance to handle callback service for clients that subscribe to
	 * this service
	 */
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

		ByteUnpacker.UnpackedMsg unpackedMsg = this.getUnpacker().parseByteArray(dataFromClient);
		String accHolderName = unpackedMsg.getString(NAME);
		int accPin = unpackedMsg.getInteger(PIN);
		String accCurrency = unpackedMsg.getString(CURRENCY);
		double accBalance = unpackedMsg.getDouble(BALANCE);
		int messageId = unpackedMsg.getInteger(super.getMessageId());
		int accNum = Bank.createAccount(accHolderName, accPin, accCurrency, accBalance);
		
		OneByteInt status = new OneByteInt(0); 
		String reply = String.format("Account created ----------- \n Account Holder name: %s \n Account number: %d \n Currency: %s \n Balance: %f \n --------- ",accHolderName, accNum, accCurrency, accBalance);
		BytePacker replyMessageClient = super.generateReply(status, messageId, reply);
		
		String toSubscribers = String.format("%s created an account. Account number: %d", accHolderName, accNum);
		BytePacker replyMessageSubscribers = super.generateReply(status, messageId, toSubscribers);
		callbackHandler.broadcast(replyMessageSubscribers);
		return replyMessageClient;
		
		
	}

	@Override
	public String ServiceName() {
		return "Create Account";
	}
}
