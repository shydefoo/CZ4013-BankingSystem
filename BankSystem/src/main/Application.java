package main;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

import services.CheckBalanceService;
import services.CreateAccountService;
import services.RegisterCallbackService;

public class Application {
	public static void main(String[] args){
		String serverIpAddress = "127.0.0.1"; //need to change if using a different computer on the network
		int serverPortNumber = 8000; //designated port number
		int timeout = 0;
		Console console = new Console(new Scanner(System.in));
				
		try {
			Client client = new Client(serverIpAddress, serverPortNumber, timeout);
			
			//add available service
			client.addService(0, new CreateAccountService());
			client.addService(4, new RegisterCallbackService());
			client.addService(5, new CheckBalanceService());
			
			//hardcoded to execute service 0
			while(true){
				int serviceNumber = console.askForInteger("Enter service request: ");
				if(serviceNumber ==-1) break;
				client.execute(serviceNumber, console);
			}
			
		} catch (UnknownHostException | SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
