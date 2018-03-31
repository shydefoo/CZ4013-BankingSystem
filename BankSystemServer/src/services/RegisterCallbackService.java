package services;

import java.net.InetAddress;

import message.BytePacker;
import message.ByteUnpacker;
import socket.Socket;

public class RegisterCallbackService extends Service {

	public RegisterCallbackService(ByteUnpacker unpacker) {
		super(unpacker);
		
	}

	@Override
	public BytePacker handleService(InetAddress clientAddress, int clientPortNumber, byte[] dataFromClient,
			Socket socket) {
		// TODO Auto-generated method stub
		return null;
	}

}
