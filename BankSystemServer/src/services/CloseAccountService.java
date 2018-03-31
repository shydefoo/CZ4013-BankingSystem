package services;

import java.net.InetAddress;

import bank.Bank;
import message.BytePacker;
import message.ByteUnpacker;
import message.OneByteInt;
import socket.Socket;

public class CloseAccountService extends Service {
	protected final static String NAME = "Name";
	protected final static String ACCOUNT_NUM = "AccountNumber";
	protected final static String PIN = "Pin";
	public CloseAccountService(){
		super(new ByteUnpacker.Builder()
						.setType(NAME, ByteUnpacker.TYPE.STRING)
						.setType(ACCOUNT_NUM, ByteUnpacker.TYPE.INTEGER)
						.setType(PIN, ByteUnpacker.TYPE.INTEGER)
						.build());
						
	}
	@Override
	public BytePacker handleService(InetAddress clientAddress, int clientPortNumber, byte[] dataFromClient,
			Socket socket) {
		ByteUnpacker.UnpackedMsg unpackedMsg = this.unpacker.parseByteArray(dataFromClient);
		String accHolderName = unpackedMsg.getString(NAME);
		int accPin = unpackedMsg.getInteger(PIN);
		int accNum = unpackedMsg.getInteger(ACCOUNT_NUM);
		int messageId = unpackedMsg.getInteger(super.MESSAGE_ID);
		
		
		OneByteInt status = new OneByteInt(0);
		BytePacker replyMessage = super.generateReply(status, messageId, String.valueOf(accNum));
		
		return replyMessage;
	}

}
