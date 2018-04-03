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

	public BalanceTransfer(){
		super(new ByteUnpacker.Builder()
						.setType(NAME, ByteUnpacker.TYPE.STRING)
						.setType(ACCNUM, ByteUnpacker.TYPE.INTEGER)
						.setType(PIN, ByteUnpacker.TYPE.INTEGER)
						.setType(RECEIVER, ByteUnpacker.TYPE.INTEGER)
						.setType(AMOUNT, ByteUnpacker.TYPE.DOUBLE)
						.build());			
	}
	
	@Override
	public BytePacker handleService(InetAddress clientAddress, int clientPortNumber, byte[] dataFromClient, Socket socket) {
		// TODO Auto-generated method stub
		ByteUnpacker.UnpackedMsg unpackedMsg = this.unpacker.parseByteArray(dataFromClient);
		String accHolderName = unpackedMsg.getString(NAME);
		int accNum = unpackedMsg.getInteger(ACCNUM);
		int accPin = unpackedMsg.getInteger(PIN);
		int receiver = unpackedMsg.getInteger(RECEIVER);
		double amount = unpackedMsg.getDouble(AMOUNT);
		int messageId = unpackedMsg.getInteger(super.MESSAGE_ID);
		
		double accBalance = Bank.transferBalance(accHolderName, accNum, receiver, accPin, amount);
		OneByteInt status = new OneByteInt(0);
		BytePacker replyMessage = super.generateReply(status, messageId, String.valueOf(accBalance));
		
		return replyMessage;
		
	}
}
