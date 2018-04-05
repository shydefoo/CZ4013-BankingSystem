package services;

import java.io.IOException;

import main.Client;
import main.Console;
import message.BytePacker;
import message.ByteUnpacker;
import message.OneByteInt;

public class CloseAccountService extends Service {

	protected final static String NAME = "Name";
	protected final static String ACCNUM = "accNum";
	protected final static String PIN = "Pin";

	public CloseAccountService(){
		super(null);
	}
	
	/***
	 * Account closing: 
	 * Required details - 1) Name (string), 2) Account Number (5 digit integer), 3) Password (6 digit integer)
	 * @throws IOException 
	 */
	@Override
	public void executeRequest(Console console, Client client) throws IOException {
		Console.println("---------------------Account deletion---------------------------------");
		String name = console.askForString("Enter your name:");
		int accNum = console.askForInteger("Input your account number:");
		int pin = console.askForInteger("Input your 6 digit pin-number:");
		int message_id = client.getMessage_id();	/*This should only be called once for each executeRequest as the message_id will be incremented each time  this method is called*/
		
		/*Carry out marshalling, store values for each field in request*/
		BytePacker packer = new BytePacker.Builder()
								.setProperty(Service.SERVICE_ID, new OneByteInt(Client.CLOSE_ACCOUNT))
								.setProperty(Service.MESSAGE_ID, message_id)
								.setProperty(NAME, name)
								.setProperty(ACCNUM, accNum)
								.setProperty(PIN, pin)
								.build();
		client.send(packer);
		
		ByteUnpacker.UnpackedMsg unpackedMsg = receivalProcedure(client, packer, message_id);
		
		//if status == 0, checkStatus returns true
		if(checkStatus(unpackedMsg)){ 
			String reply = unpackedMsg.getString(Service.REPLY);
			Console.println(reply);
		}
		else{
			Console.println("Account close failed");
		}
	}
	
	@Override
	public String ServiceName() {
		return "Close Account";
	}
}
