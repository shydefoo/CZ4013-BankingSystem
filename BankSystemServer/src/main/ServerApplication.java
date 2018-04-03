package main;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import bank.Bank;
import services.BalanceTransfer;
import services.BalanceUpdate;
import services.CallbackHandlerClass;
import services.CheckBalanceService;
import services.CloseAccountService;
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
			Console.debug_info = false;
			
			/*Specify type of server*/
			//server = new Server(socket); //at-least-once server 
			server = new AtMostOnceServer(socket); //at-most-once server
			
			/*Specify what type of socket to use*/
			double probability = 0.5;
			server.useSendingLossSocket(probability);
			
			
			
			callbackHandler = new CallbackHandlerClass(socket);
			//Services to be added to server
			server.addServiceToServer(0, new CreateAccountService(callbackHandler));
			server.addServiceToServer(1, new CloseAccountService(callbackHandler));
			server.addServiceToServer(2, new BalanceUpdate(callbackHandler));
			server.addServiceToServer(3, new BalanceTransfer(callbackHandler));
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
