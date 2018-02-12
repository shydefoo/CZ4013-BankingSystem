package message;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

/***
 * BytePackerClass is the class that does the marshalling. 
 * Converts objects into bytes before storing the bytes in a byte array. 
 * Order at which objects are stored in byte[] depends on the order of the Keys in the ArrayList properties
 * @author Shide
 *
 */
public class BytePackerClass {
	private ArrayList<String> properties;
	private HashMap<String, Object> propToValue;
	
	public BytePackerClass(){
		properties = new ArrayList<>();
		propToValue = new HashMap<>();
		
	}
	public void setValue(String key, Object value){
		properties.add(key);
		propToValue.put(key,value);
	}
	
	public Object getValue(String key){
		return propToValue.get(key);
	}
	
	public byte[] getByteArray(){
		/*
        Calculate the size required for the byte array
        based on the type of the object
        Integer     4 bytes
        String      4 + length of string bytes (+4 in front is to store the length of the string)
        Long        8 bytes
        Byte Array  4 + length of byte array
        float 		4 bytes
        double 		8 bytes
        OneByteInt  1 byte
		*/
		int size = 1;
		for(Object value: propToValue.values()){
			if(value instanceof Integer){
				size+=4;
			}
			else if(value instanceof String){
				size += 4 + ((String) value).length(); 
			}
			else if (value instanceof Double){
				size+= 8;
			}
			else if(value instanceof byte[]){
				size+= 4 + ((byte[]) value).length;
			}
			else if(value instanceof OneByteInt){
				size +=1;
			}
		}
		byte[] buffer = new byte[size];
		
		
		int index = 0;
		for(String property: properties){
			Object value = propToValue.get(property);
			if(value instanceof Integer){
				index = intToByte((Integer)value, buffer, index); /*Converts Integer to byte*/
			}
			else if(value instanceof String){
				index = intToByte(((String) value).length(), buffer, index); /*1st integer is length of string*/
				index = stringToByte(((String)value), buffer,index);		/*Convert the content of string to bytes*/
			}
			else if(value instanceof Double){
				index = doubleToByte((Double)value,buffer,index);		/*Convert double to bytes*/
			}
			else if(value instanceof byte[]){
				index = intToByte(((byte[]) value).length, buffer,index); /*First int is length of byte array*/
				System.arraycopy(value, 0, buffer, index, ((byte[])value).length); /*Copy contents of byte array into buffer*/
				index+= ((byte[]) value).length;
			}else if(value instanceof OneByteInt){
				int OBIValue = ((OneByteInt) value).getValue();
				buffer[index++] = (byte) (OBIValue & 0xFF);
			}
			
		}
		
		return buffer;
	}
	
	private int intToByte(int i, byte[] buffer, int index){
		/*buffer[index++] = (byte)((i>>24) & 0xFF);
		buffer[index++] = (byte)((i>>16) & 0xFF);
		buffer[index++] = (byte)((i>>8) & 0xFF);
		buffer[index++] = (byte)((i) & 0xFF);*/
		
		byte[] temp = new byte[4];
		ByteBuffer.wrap(temp).putInt(i);
		for(byte b: temp){
			buffer[index++] = b;
		}
		
		return index;
	}
	private int stringToByte(String s, byte[] buffer, int index){
		for(byte b: s.getBytes()){
			buffer[index++] = b;
		}
		return index;
	}
	private int doubleToByte(Double d, byte[] buffer, int index){
		byte[] temp = new byte[8];
		ByteBuffer.wrap(temp).putDouble(d);
		for(byte b: temp){
			buffer[index++] = b;
		}
		return index;
	}
	
	/***
	 * Builder class for BytePackerClass
	 * 
	 * 
	 */
	public static class Builder{
		private BytePackerClass packer;
		
		public Builder(){
			packer = new BytePackerClass();
		}
		
		public Builder setProperty(String key, int value){
			return set(key,value);
		}
		
		public Builder setProperty(String key, double value){
			return set(key, value);
		}
		
		public Builder setProperty(String key, String string){
			return set(key, string);
		}
		
		public Builder set(String key, Object value){
			packer.setValue(key,value);
			return this;
		}
		
		public BytePackerClass Build(){
			return packer;
		}
	}
}

