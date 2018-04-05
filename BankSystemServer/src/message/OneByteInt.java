package message;
/**
 * Class that holds integer that takes up 1 byte. 
 * @author Shide
 *
 */
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
