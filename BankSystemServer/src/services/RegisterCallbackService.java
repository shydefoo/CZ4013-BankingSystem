package services;

import java.net.InetAddress;

import message.BytePacker;
import message.ByteUnpacker;
import message.OneByteInt;
import services.CallbackHandlerClass.Subscriber;
import socket.Socket;

public class RegisterCallbackService extends Service {
	
	protected final static String TIMEOUT = "timeout";

	public RegisterCallbackService() {
		super(new ByteUnpacker.Builder()
				.setType(TIMEOUT, ByteUnpacker.TYPE.INTEGER)
				.build());
		
	}

	@Override
	public BytePacker handleService(InetAddress clientAddress, int clientPortNumber, byte[] dataFromClient,
			Socket socket) {
		//clientAddress, portnumber not in used at the moment...used to check at most once semantics/at least once? Hmmm
		ByteUnpacker.UnpackedMsg unpackedMsg = this.unpacker.parseByteArray(dataFromClient);
		int messageId = unpackedMsg.getInteger(Service.MESSAGE_ID);
		int timeout = unpackedMsg.getInteger(TIMEOUT);
		//Create subscriber object
		CallbackHandlerClass.registerSubscriber(clientAddress, clientPortNumber, messageId, timeout);
		OneByteInt status = new OneByteInt(2);
		String reply = "Auto-monitoring registered, waiting for updates...";
		BytePacker replyMessage = super.generateReply(status, messageId, reply);
		return replyMessage;
		
	}

}
