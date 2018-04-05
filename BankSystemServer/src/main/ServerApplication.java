package main;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Scanner;

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
	private static int portNumber;
	public static void main(String[] args){
		Console console = new Console(new Scanner(System.in));
		try {
			System.out.println("Starting server");
			bank = new Bank();
			
			/*Start of code to set server configurations*/
			String addressInput = console.askForString("Input IP address hosting server on:");
			address = InetAddress.getByName(addressInput);
			portNumber = console.askForInteger("Input port number for server to listen at:");
			socket = new NormalSocket(new DatagramSocket(portNumber,address));
			/*Specify type of server*/
			int serverChoice = console.askForInteger(1, 2, "Select Server type: \n1)At-Least-Once\n2)At-Most-Once");
			if(serverChoice==1){
				server = new Server(socket); //at-least-once server
			}
			else if(serverChoice==2){
				server = new AtMostOnceServer(socket); //at-most-once server
			}
			/*Specify what type of socket to use*/
			int socketType = console.askForInteger(1, 3, "Select Socket Type: \n1)Normal Socket\n2)SendingLossSocket \n3)CorruptedSocket");
			 if(socketType==2){
				double probability = 1 - console.askForDouble(0.0, 1.0, "Probability of packetloss:");
				server.useSendingLossSocket(probability);
			 }
			 else if(socketType==3){
				 double probability = 1 - console.askForDouble(0.0, 1.0, "Probability of packetloss:");
				 server.useCorruptedSocket(probability);
			 }
			//Console.debug_info = false;		
			
			/*End of code to set server configurations*/	
			
			callbackHandler = new CallbackHandlerClass(socket);
			Thread validityCheck = new Thread(callbackHandler);
			validityCheck.start();
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
