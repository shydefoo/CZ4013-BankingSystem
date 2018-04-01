package services;

import java.io.IOException;

import main.Client;
import main.Console;
import message.BytePacker;
import message.ByteUnpacker;
import message.OneByteInt;

public class BalanceUpdate extends Service {
		
	public BalanceUpdate() {
		super(null);
	}
	
	public void executeRequest(Console console, Client client) throws IOException{
		Console.println("---------------------Balance Update---------------------------------");
		String name = console.askForString("Enter your name:");
		int accNum = console.askForInteger("Enter your Account Number:");
		int pin = console.askForInteger("Input your 6 digit pin-number:");
		String currency = console.askForString("Specify currency type:");
		int choice = console.askForInteger("Do you want to deposit(1) or withdraw(0):"); 
		double amount = console.askForDouble("Enter amount to deposit/withdraw:");
		int message_id = client.getMessage_id();	/*This should only be called once for each executeRequest as the message_id will be incremented each time  this method is called*/
		
		BytePacker packer = new BytePacker.Builder()
								.setProperty("ServiceId", new OneByteInt(Client.UPDATE_BALANCE))
								.setProperty("messageId", message_id)
								.setProperty("Name", name)
								.setProperty("accNum", accNum)
								.setProperty("Pin", pin)
								.setProperty("Currency",currency)
								.setProperty("amount", amount)
								.setProperty("choice", choice)
								.build();
		client.send(packer);
		
		ByteUnpacker.UnpackedMsg unpackedMsg = receivalProcedure(client, packer, message_id);
		if(checkStatus(unpackedMsg)){
			String Balance = unpackedMsg.getString(Service.REPLY);
			Console.println("Current Balance: " + Balance);	
		}
		else{
			Console.println("Balance Update failed");
		}
	}
}