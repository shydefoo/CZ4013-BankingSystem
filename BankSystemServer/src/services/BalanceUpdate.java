package services;

import java.io.IOException;
import java.net.InetAddress;

import bank.Bank;
import message.BytePacker;
import message.ByteUnpacker;
import message.OneByteInt;
import socket.Socket;

public class BalanceUpdate extends Service {
	protected final static String NAME = "Name";
	protected final static String ACCNUM = "accNum";
	protected final static String PIN = "Pin";
	protected final static String CHOICE = "choice";
	protected final static String AMOUNT = "amount";
	private CallbackHandlerClass callbackHandler;
	public BalanceUpdate(CallbackHandlerClass callbackHandler){
		super(new ByteUnpacker.Builder()
						.setType(NAME, ByteUnpacker.TYPE.STRING)
						.setType(ACCNUM, ByteUnpacker.TYPE.INTEGER)
						.setType(PIN, ByteUnpacker.TYPE.INTEGER)
						.setType(CHOICE, ByteUnpacker.TYPE.INTEGER)
						.setType(AMOUNT, ByteUnpacker.TYPE.DOUBLE)
						.build());	
		this.callbackHandler = callbackHandler;
	}
	
	
	@Override
	public BytePacker handleService(InetAddress clientAddress, int clientPortNumber, byte[] dataFromClient, Socket socket) {
		// TODO Auto-generated method stub
		ByteUnpacker.UnpackedMsg unpackedMsg = this.getUnpacker().parseByteArray(dataFromClient);
		String accHolderName = unpackedMsg.getString(NAME);
		int accNum = unpackedMsg.getInteger(ACCNUM);
		int accPin = unpackedMsg.getInteger(PIN);
		int choice = unpackedMsg.getInteger(CHOICE);
		double amount = unpackedMsg.getDouble(AMOUNT);
		int messageId = unpackedMsg.getInteger(super.getMessageId());
		String reply = "";
		double accBalance = Bank.updateBalance(accHolderName,accNum, accPin, choice, amount);
		OneByteInt status = new OneByteInt(0);
		if(accBalance==-1){
			reply = "Invalid account number. Please try again.";
		}
		else if(accBalance ==-2){
			reply = "Invalid pin number . Please try again";
		}
		else if(accBalance==-3){
			reply = "Insufficient funds. Current balance: " + Bank.checkBalance(accNum, accPin);
		}
		else if(accBalance==-4){
			reply = "Invalid Choice. Please try again";
		}
		else{
			reply = String.format("---------------------\nWithdraw funds  \nAmount Withdrawn: %f \nCurrent account balance: %f\n------------------" , amount, accBalance);
			BytePacker replyMessageSubscriber = super.generateReply(status, messageId, reply);
			callbackHandler.broadcast(replyMessageSubscriber);
		}
		
		BytePacker replyMessageClient = super.generateReply(status, messageId, reply);
		return replyMessageClient;
		
	}

	@Override
	public String ServiceName() {
		return "Make Deposit/Withdrawal";
	}
	
	
}
