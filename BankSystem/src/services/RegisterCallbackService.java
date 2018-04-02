package services;

import java.io.IOException;

import main.Client;
import main.Console;
import message.BytePacker;
import message.ByteUnpacker;
import message.OneByteInt;

public class RegisterCallbackService extends Service{
	
	protected final static String TIMEOUT = "timeout";
	public RegisterCallbackService(){
		super(null);
	}

	@Override
	public void executeRequest(Console console, Client client) throws IOException {
		Console.println("---------------------Register Auto-monitoring---------------------------------");
		int timeout = console.askForInteger("Enter monitor interval:");
		int message_id = client.getMessage_id();	/*This should only be called once for each executeRequest as the message_id will be incremented each time  this method is called*/
		BytePacker packer = new BytePacker.Builder()
								.setProperty(Service.SERVICE_ID, new OneByteInt(Client.REGISTER_CALLBACK))
								.setProperty(Service.MESSAGE_ID, message_id)
								.setProperty(TIMEOUT, timeout)
								.build();
		client.send(packer);
		
		//Wait for reply from server that says callback registered, then enter auto monitoring state
		ByteUnpacker.UnpackedMsg unpackedMsg = receivalProcedure(client, packer, message_id);
		if(checkStatus(unpackedMsg)){
			String reply = unpackedMsg.getString(Service.REPLY);
			Console.println(reply);
			
			/*
			 * Inside here, have while loop that runs infinitely, 
			 * call receive receive receive until 1 msg that has status 4 which means auto-monitoring expired.
			 * */
			while(true){
				ByteUnpacker.UnpackedMsg callbackMsg = receivalProcedure(client, packer, message_id);
				String callbackMsgReply = callbackMsg.getString(Service.REPLY);
				Console.println(callbackMsgReply);
				if(checkStatus(callbackMsg,4)){
					break;
				}
			}
		}
	}
	
	

}
