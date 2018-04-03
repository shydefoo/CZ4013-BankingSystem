package services;

import java.io.IOException;
import java.net.InetAddress;

import bank.Bank;
import message.BytePacker;
import message.ByteUnpacker;
import message.OneByteInt;
import socket.Socket;

public class BalanceTransfer extends Service {
	protected final static String NAME = "Name";
	protected final static String ACCNUM = "accNum";
	protected final static String PIN = "Pin";
	protected final static String RECEIVER = "receiver";
	protected final static String AMOUNT = "amount";
	private CallbackHandlerClass callbackHandler;
	public BalanceTransfer(CallbackHandlerClass callbackHandler){
		super(new ByteUnpacker.Builder()
						.setType(NAME, ByteUnpacker.TYPE.STRING)
						.setType(ACCNUM, ByteUnpacker.TYPE.INTEGER)
						.setType(PIN, ByteUnpacker.TYPE.INTEGER)
						.setType(RECEIVER, ByteUnpacker.TYPE.INTEGER)
						.setType(AMOUNT, ByteUnpacker.TYPE.DOUBLE)
						.build());		
		this.callbackHandler = callbackHandler;
	}	
	
	@Override
	public BytePacker handleService(InetAddress clientAddress, int clientPortNumber, byte[] dataFromClient, Socket socket) {
		String reply = "";
		ByteUnpacker.UnpackedMsg unpackedMsg = this.getUnpacker().parseByteArray(dataFromClient);
		String accHolderName = unpackedMsg.getString(NAME);
		int accNum = unpackedMsg.getInteger(ACCNUM);
		int accPin = unpackedMsg.getInteger(PIN);
		int receiver = unpackedMsg.getInteger(RECEIVER);
		double amount = unpackedMsg.getDouble(AMOUNT);
		int messageId = unpackedMsg.getInteger(super.getMessageId());
		
		double accBalance = Bank.transferBalance(accHolderName, accNum, receiver, accPin, amount);
		OneByteInt status = new OneByteInt(0);
		if(accBalance==-1){
			reply = "Invalid account number. ";
		}
		else if (accBalance == -2){
			reply = "Invalid pin number . Please try again";
		}
		else if(accBalance==-3){
			reply = "Insufficient funds. Current balance: " + Bank.checkBalance(accNum, accPin);
		}
		else{
			reply = String.format("--------------------\nFunds Transfer\nFrom: %d\nTo: %d\nAmount:%.2f\nBalance: %.2f\n---------------", accNum,receiver,amount,accBalance);
			String replyToSubscribers = String.format("%.2f transferred from acc number: %d to acc number: %d", amount, accNum,receiver);
			BytePacker replyMessageToSubcribers = super.generateReply(status, messageId, replyToSubscribers);
			callbackHandler.broadcast(replyMessageToSubcribers);
			
		}
		BytePacker replyMessage = super.generateReply(status, messageId, reply);
		return replyMessage;
		
	}
	public String ServiceName(){
		return "Transfer funds";
	}
}
