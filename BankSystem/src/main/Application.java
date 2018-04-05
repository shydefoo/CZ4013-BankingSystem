package main;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

import services.CheckBalanceService;
import services.CloseAccountService;
import services.CreateAccountService;
import services.RegisterCallbackService;
import services.BalanceTransfer;
import services.BalanceUpdate;



public class Application {
	public static void main(String[] args){
		
		Console console = new Console(new Scanner(System.in));
		String serverIpAddress = console.askForString("Input server ip address:"); //need to change if using a different computer on the network
		int serverPortNumber = console.askForInteger("Input server port number"); //designated port number
		int timeout = console.askForInteger("Input socket timeout");
				
		try {
			Client client = new Client(serverIpAddress, serverPortNumber, timeout*1000);
			
			//add available service
			client.addService(0, new CreateAccountService());
			client.addService(1, new CloseAccountService());
			client.addService(2, new BalanceUpdate());
			client.addService(3, new BalanceTransfer());
			client.addService(4, new RegisterCallbackService());
			client.addService(5, new CheckBalanceService());
			
			//Console.debug_info = false;
			/*Specify what type of socket to use*/
			int socketType = console.askForInteger(1, 4, "Select Socket Type: \n1)Normal Socket\n2)SendingLossSocket\n3)ReceivingLossSocket\n4)CorruptedSocket");
			if(socketType!=1){
				 double probability = 1.0 - console.askForDouble(0.0, 1.0, "Probability of packetloss:");
				 //System.out.println("socketType:" + socketType);
				 if(socketType == 2){
					 client.useSendingLossSocket(probability);
				 } 
				 else if(socketType==3){
					 client.useReceivingLossSocket(probability);
				 }else if(socketType == 4){
					 client.useCorruptedSocket(probability);
				 }
			 }	

			
			while(true){
				client.printMenu();
				int serviceNumber = console.askForInteger("Enter service request: ");
				if(serviceNumber ==-1) break;
				client.execute(serviceNumber, console);
			}
			
		} catch (UnknownHostException | SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
