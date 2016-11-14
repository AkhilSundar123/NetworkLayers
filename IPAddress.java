import java.util.Arrays;


public class IPAddress 
{
	byte[] byteFormat = new byte[4];
	String stringFormat;
	
	public IPAddress(int b1, int b2, int b3, int b4) 
	{
		byteFormat[0] = (byte) b1;
		byteFormat[1] = (byte) b2;
		byteFormat[2] = (byte) b3;
		byteFormat[3] = (byte) b4;
		stringFormat = String.format("%d.%d.%d.%d",b1,b2,b3,b4);
	}
}
