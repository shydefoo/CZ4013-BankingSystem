package message;

public class OneByteInt {
	private int value;
	
	public OneByteInt(int value){
		this.value = value;
	}
	public void setValue(int value){
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}
}
