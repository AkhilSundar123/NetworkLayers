import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;



public class Layer4 
{
	private IPAddress srcIPAddress;
	private Medium	mediumObject;
	private int		TX_CHAR;
	private int		MTU;
	private byte[]	segment;	
	private Layer3	layer3;
	private Layer5 layer5;
	
	private static int nodeIndex = 0;
	private int outputFileCounter = 1;
	private String outputFileName = null;
	private boolean isOutputFileOpen = false;
	private FileOutputStream outputStream = null;
	
	
	public Layer4(Layer5 layer5, IPAddress srcIPAddress, Medium mediumObject, 
			int TX_CHAR, int MTU)
	{
		this.srcIPAddress = srcIPAddress;
		this.mediumObject = mediumObject;
		this.TX_CHAR = TX_CHAR;
		this.MTU = MTU;
		this.layer5 = layer5;
		segment = new byte[MTU - 20];
		layer3 = new Layer3(this, srcIPAddress, mediumObject, TX_CHAR, MTU);
		nodeIndex++;
	}
	
	public Layer1 getLayer1Object()
	{
		return layer3.getLayer1Object();
	}
	
	public void send(String fileName, IPAddress destIPAddress) throws IOException
	{
		File file = new File(fileName);
		FileInputStream inputStream = new FileInputStream(file);		
		int bytesRead;
		
		while ((bytesRead = inputStream.read(segment)) > 0)
		{
			Log.logActivity(srcIPAddress.stringFormat, 
					"(Sent from L4) destIP= " + destIPAddress.stringFormat);
			if(bytesRead == MTU - 20)
				layer3.send(segment, destIPAddress);
			else
				layer3.send(Arrays.copyOf(segment, bytesRead), destIPAddress);
			
        }
	}
	
	public void receive(byte[] segment) throws IOException
	{
		Log.logActivity(srcIPAddress.stringFormat, "(Received at L4) ");
		if(isOutputFileOpen == false)
		{
			outputFileName = String.format("Node%d_file%d.txt", nodeIndex,outputFileCounter);
			outputFileCounter++;
			
			File file = new File(outputFileName);
			outputStream = new FileOutputStream(file);
			isOutputFileOpen = true;
		}
		
		outputStream.write(segment);
		if(segment.length != MTU - 20)
		{
			outputStream.close();
			outputStream = null;
			isOutputFileOpen = false;
			layer5.receive(outputFileName);
		}	
	}
}
