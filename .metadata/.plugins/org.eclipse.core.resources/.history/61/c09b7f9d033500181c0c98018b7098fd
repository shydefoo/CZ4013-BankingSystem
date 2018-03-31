package services;

import java.io.IOException;
import java.net.DatagramPacket;

import main.Client;
import main.Console;
import message.BytePacker;
import message.ByteUnpacker;
import message.OneByteInt;

public abstract class Service {
	
	private final ByteUnpacker unpacker;
	protected static final String STATUS = "status";
    protected static final String MESSAGE_ID = "messageId";
    protected static final String REPLY = "reply";
	
	protected Service(ByteUnpacker unpacker){
		this.unpacker = new ByteUnpacker.Builder()
						.setType(STATUS, ByteUnpacker.TYPE.ONE_BYTE_INT)
						.setType(MESSAGE_ID, ByteUnpacker.TYPE.INTEGER)
						.setType(REPLY, ByteUnpacker.TYPE.STRING)
						.build()
						.defineComponents(unpacker);
	}
	
	public final ByteUnpacker.UnpackedMsg receivalProcedure(Client client, BytePacker packer, int message_id ) throws IOException{
		while(true){
			DatagramPacket reply = client.receive();
			ByteUnpacker.UnpackedMsg unpackedMsg = this.unpacker.parseByteArray(reply.getData());
			if(checkMsgId(message_id,unpackedMsg)) return unpackedMsg;
			else{
				Console.println("Message Id not the same");
				client.send(packer);
			}
			
		}
	}
	public final boolean checkMsgId(Integer message_id, ByteUnpacker.UnpackedMsg unpackedMsg){
		Integer return_message_id = unpackedMsg.getInteger(MESSAGE_ID);
		if(return_message_id != null){
			return message_id == return_message_id;
		}
		return false;
	}
	
	public final boolean checkStatus(ByteUnpacker.UnpackedMsg unpackedMsg){
		OneByteInt status = unpackedMsg.getOneByteInt(STATUS);
		if(status.getValue()==0)return true;
		return false;
	}
	
	
	public abstract void executeRequest(Console console, Client client) throws IOException;
	
	
}
