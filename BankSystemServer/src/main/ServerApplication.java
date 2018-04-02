package main;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import bank.Bank;
import services.CallbackHandlerClass;
import services.CheckBalanceService;
import services.CreateAccountService;
import services.RegisterCallbackService;
import socket.NormalSocket;
import socket.Socket;

public class ServerApplication {
	private static Server server;
	private static Bank bank;
	private static CallbackHandlerClass callbackHandler;
	private static InetAddress address;
	private static Socket socket;
	private static final int PORT_NUMBER = 8000;
	public static void main(String[] args){
		try {
			System.out.println("Starting server");
			bank = new Bank();
			address = InetAddress.getByName("127.0.0.1");
			socket = new NormalSocket(new DatagramSocket(PORT_NUMBER,address));
			server = new Server(socket);
			server.useSendingLossSocket(0.5);
			callbackHandler = new CallbackHandlerClass(socket);
			//Services to be added to server
			server.addServiceToServer(0, new CreateAccountService(callbackHandler));
			server.addServiceToServer(4, new RegisterCallbackService(callbackHandler));
			server.addServiceToServer(5, new CheckBalanceService(callbackHandler));
			////////////////
			
			server.start();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			Console.debug("Server error");
			e.printStackTrace();
		} 
		
	}
}
