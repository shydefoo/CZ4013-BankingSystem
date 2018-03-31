package main;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

import services.CreateAccountService;

public class Application {
	public static void main(String[] args){
		String serverIpAddress = "127.0.0.1"; //need to change if using a different computer on the network
		int serverPortNumber = 8000; //designated port number
		Console console = new Console(new Scanner(System.in));
				
		try {
			Client client = new Client(serverIpAddress, serverPortNumber);
			
			//add available service
			client.addService(0, new CreateAccountService());
			
			//hardcoded to execute service 0
			client.execute(0, console);
		} catch (UnknownHostException | SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
