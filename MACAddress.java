import java.util.Arrays;


public class MACAddress 
{
	byte[] byteFormat = new byte[6];
	String stringFormat;
	
	public MACAddress(int b1, int b2, int b3, int b4, int b5, int b6) 
	{
		byteFormat[0] = (byte) b1;
		byteFormat[1] = (byte) b2;
		byteFormat[2] = (byte) b3;
		byteFormat[3] = (byte) b4;
		byteFormat[4] = (byte) b5;
		byteFormat[5] = (byte) b6;
		stringFormat = String.format("%d.%d.%d.%d.%d.%d",b1,b2,b3,b4,b5,b6);
	}
}
