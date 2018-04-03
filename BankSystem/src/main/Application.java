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
		String serverIpAddress = "127.0.0.1"; //need to change if using a different computer on the network
		int serverPortNumber = 8000; //designated port number
		int timeout = 5;
		Console console = new Console(new Scanner(System.in));
				
		try {
			Client client = new Client(serverIpAddress, serverPortNumber, timeout*1000);
			
			//add available service
			client.addService(0, new CreateAccountService());
			client.addService(1, new CloseAccountService());
			client.addService(2, new BalanceUpdate());
			client.addService(3, new BalanceTransfer());
			client.addService(4, new RegisterCallbackService());
			client.addService(5, new CheckBalanceService());
			//client.useReceivingLossSocket(0.1);
			//hardcoded to execute service 0
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
