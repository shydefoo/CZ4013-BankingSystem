

public class Application {
	public static void main(String[] args){
		String string = "apple";
		byte[] x = string.getBytes();
		System.out.println(x.length);
	}
	static void  binary(int num)
	{
	   int i = 0;    
	   int  bin[]=new int[100]; 
	    bin[0]=0;
	   while(num>0)
	    {
	    bin[i++] = num%2;
	    num = num/2;
	    } 
	    for(int j =i-1;j >= 0;j--)
	    {
	       System.out.print(bin[j]);
	    }
	    System.out.println("\n");
	}
	static int intToByte(int i, byte[] buffer, int index) {
        buffer[index++] = (byte) ((i >> 24) & 0xFF);         /* most significant byte, byte 3, most sig fig byte goes in first. */
        buffer[index++] = (byte) ((i >> 16) & 0xFF);         /* byte 2 */
        buffer[index++] = (byte) ((i >> 8) & 0xFF);          /* byte 1 */
        buffer[index++] = (byte) ((i) & 0xFF);               /* least significant byte, byte 0 */
        return index;                                       /* update and return current index number */
    }
	
	static int parseInt(byte[] data, int offset){
		int i = (int)(data[offset++] & 0xFF)<<24;
		i+= (int) (data[offset++] & 0xFF) <<16;
		i+= (int) (data[offset++] & 0xFF) <<8;
		i+= (int) (data[offset++]& 0xFF);
		
		return i;
	}
}
