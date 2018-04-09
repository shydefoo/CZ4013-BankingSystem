package services;

import java.io.IOException;
import java.net.InetAddress;

import bank.Bank;
import message.BytePacker;
import message.ByteUnpacker;
import message.OneByteInt;
import socket.Socket;

/**
 * This class handles Balance Update requests (i.e. deposit and withdraw request)
 *
 */
public class BalanceUpdate extends Service {
	protected final static String NAME = "Name";
	protected final static String ACCNUM = "accNum";
	protected final static String PIN = "Pin";
	protected final static String CHOICE = "choice";
	protected final static String AMOUNT = "amount";
	private CallbackHandlerClass callbackHandler;
	
	/**
	 * Class constructor for BalanceUpdate
	 * @param callbackHandler Callbackhandler instance to handle callback service for clients that subscribe to
	 * this service
	 */
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
			String choiceType = "";
			String choiceType2 = "";
			if(choice == 1){
				choiceType = "Deposit Funds";
				choiceType2 = "Amount Deposited";
			} 
			else if(choice==0){
				choiceType = "Withdraw Funds";
				choiceType = "Amount Withdrawn";
			}
			reply = String.format("---------------------\n%s \n%s: %f \nCurrent account balance: %f\n------------------" ,choiceType, choiceType2, amount, accBalance);
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
