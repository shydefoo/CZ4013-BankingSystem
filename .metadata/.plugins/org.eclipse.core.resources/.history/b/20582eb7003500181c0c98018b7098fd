package services;

import java.io.IOException;

import main.Client;
import main.Console;
import message.BytePacker;
import message.ByteUnpacker;
import message.OneByteInt;

public class CloseAccountService extends Service {
	protected final static String NAME = "Name";
	protected final static String ACCOUNT_NUM = "AccountNumber";
	protected final static String PIN = "Pin";

	public CloseAccountService(){
		super(null);
	}

	@Override
	public void executeRequest(Console console, Client client) throws IOException {
		// TODO Auto-generated method stub
		Console.println("---------------------Account Closing---------------------------------");
		String name = console.askForString("Enter your name:");
		int accountNumber = console.askForInteger("Input Account Number:");
		int pin = console.askForInteger("Input Pin:");
		int message_id = client.getMessage_id();	/*This should only be called once for each executeRequest as the message_id will be incremented each time  this method is called*/
		BytePacker packer = new BytePacker.Builder()
								.setProperty("ServiceId", new OneByteInt(Client.CLOSE_ACCOUNT))
								.setProperty("messageId", message_id)
								.setProperty("Name", name)
								.setProperty("accountNumber", accountNumber)
								.setProperty("Pin", pin)
								.build();
		client.send(packer);
		
		ByteUnpacker.UnpackedMsg unpackedMsg = receivalProcedure(client, packer, message_id);
		if(checkStatus(unpackedMsg)){
			String accNum = unpackedMsg.getString(Service.REPLY);
			Console.println("Account successfully created.");
			Console.println("Account number: " + accNum);	
		}
		else{
			Console.println("Account create failed");
		}
		
	}

}
