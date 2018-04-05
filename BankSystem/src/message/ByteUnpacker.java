package message;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
/**
 * The ByteUnpacker class handles the unmarshalling of incoming messages
 * The message will be converted into from a byte array to the type of object in the message
 * @author Shide
 *
 */
public class ByteUnpacker {
	private ArrayList<String> properties;
	private HashMap <String, TYPE> propToValue;
	/**
	 * Class constructor of ByteUnpacker
	 */
	public ByteUnpacker(){
		properties = new ArrayList<>();
		propToValue = new HashMap<>();
	}
	/**
	 * This method will include all the values
     * and properties
	 * @param unpacker ByteUnpacker object
	 * @return ByteUnpacker object itself
	 */
	public ByteUnpacker defineComponents(ByteUnpacker unpacker){
		if(unpacker!=null){
			properties.addAll(unpacker.properties);
			propToValue.putAll(unpacker.propToValue);
		}
		return this;
	}
	/***
	 *Identify data structure type for each property using hashmap built from defineComponents()
	 *Scan byte array using hashmap to build a new hashmap containing key and actual values
	 * 
	 * @param data byte array from DatagramPacket
	 * @return UnpackedMsg object
	 */
	public UnpackedMsg parseByteArray(byte[] data){
		int offset = 0;
		HashMap<String, Object> map = new HashMap<>();
		try{
			for(String property: properties){
				TYPE value = propToValue.get(property);
				switch(value){
				case INTEGER:
					map.put(property, parseInt(data, offset));
					offset+=4;
					break;
				case DOUBLE:
					map.put(property, parseDouble(data,offset));
					offset+=8;
					break;
				case STRING:
					int length = parseInt(data, offset);
                    map.put(property, parseString(data, offset + 4, length));
                    offset += 4 + length;
                    break;
				case BYTE_ARRAY:
					int byte_length = parseInt(data, offset);
                    map.put(property, Arrays.copyOfRange(data, offset + 4, offset + 4 + byte_length));
					break;
				case ONE_BYTE_INT:
					map.put(property, new OneByteInt(data[offset] & 0xFF));
                    offset += 1;
                    break;
				}
			}
			UnpackedMsg result = new UnpackedMsg(map);
			return result;
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * Converts bytes from byte array into a string
	 * @param data - byte array from the DatagramPacket
	 * @param offset - offset from the first index position of the byte array
	 * @param length - length of the string to be parsed
	 * @return
	 */
	
	private String parseString(byte[] data, int offset, int length) {
		try{
			StringBuilder sb = new StringBuilder();
			for(int i=0;i<length;i++,offset++){
				sb.append((char)data[offset]);
			}
			return sb.toString();
		}catch(IndexOutOfBoundsException e){
			return null;
		}
		
	}
	/**
	 * Converts bytes from byte arry into a data structure of type double
	 * @param data - byte array from the DatagramPacket
	 * @param offset - offset from the first index position of the byte array
	 * @return double data type
	 */
	private Double parseDouble(byte[] data, int offset) {
		int doubleSize = 8;
		byte[] temp = new byte[doubleSize];
		for(int i =0;i<doubleSize;i++){
			temp[i] = data[offset+i];
		}
		double value = ByteBuffer.wrap(temp).getDouble();
		return value;
	}
	/**
	 * Converts bytes from byte arry into a data structure of type integer
	 * @param data - byte array from the DatagramPacket
	 * @param offset - offset from the first index position of the byte array
	 * @return integer data type
	 */
	private Integer parseInt(byte[] data, int offset) {
		int intSize = 4;
		byte[] temp = new byte[intSize];
		for(int i=0;i<intSize;i++){
			temp[i] = data[offset+i];
		}
		
		int value = ByteBuffer.wrap(temp).getInt();
		return value;
	}

	
	/**
	 * UnpackedMsg class is the handler to retrieve the contents on the message 
	 * by looking up a hashmap.
	 * Keys of the hashmap represent the fields of the message.
	 * Values at the keys represent the contents for that field
	 * @author Shide
	 *
	 */
	public static class UnpackedMsg{
		private HashMap<String, Object> map;
		/**
		 * Class constructor of UnpackedMsg
		 * @param map
		 */
		public UnpackedMsg(HashMap<String,Object> map){
			this.map = map;
		}
		
		public Integer getInteger(String key){
			if(map.containsKey(key) && (map.get(key) instanceof Integer)){
				return (Integer) map.get(key);
			}
			return null;
		}
		public String getString(String key){
			if(map.containsKey(key) && map.get(key) instanceof String){
				return (String) map.get(key);
			}
			return null;
		}
		public Double getDouble(String key){
			if(map.containsKey(key) && map.get(key) instanceof Double){
				return (Double) map.get(key);
			}
			return null;
		}
		public byte[] getByteArray(String value) {
            if (map.containsKey(value) && map.get(value) instanceof byte[]) {
                return (byte[]) map.get(value);
            }
            return null;
        }

        public OneByteInt getOneByteInt(String value) {
            if (map.containsKey(value) && map.get(value) instanceof OneByteInt) {
                return (OneByteInt) map.get(value);
            }
            return null;
        }
	}
	
	
	
	
	public enum TYPE {
        INTEGER, DOUBLE, STRING, BYTE_ARRAY, ONE_BYTE_INT
    }
	/**
	 * Builder class for the ByteUnpacker class
	 *
	 */
	public static class Builder{
		private ByteUnpacker unpacker;
		public Builder(){
			unpacker = new ByteUnpacker();
			
		}
		public Builder setType(String property, TYPE type){
			unpacker.properties.add(property);
			unpacker.propToValue.put(property, type);
			return this;
		}
		public ByteUnpacker build(){
			return unpacker;
		}
	}
}
