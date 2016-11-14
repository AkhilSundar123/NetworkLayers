import java.io.IOException;
import java.io.PrintWriter;

public class Node extends Thread
{
	private IPAddress	srcIPAddress;
	private Medium	mediumObject;
	private int		TX_CHAR = 0;
	private int		MTU = 0;
	private String	sendingMessage = null;	
	private volatile boolean	running = true;
	
	private IPAddress destIPAddress;
	
	private Layer5 layer5;
	
		
	public Node(IPAddress srcIPAddress, Medium mediumObject, int TX_CHAR, int MTU) 
	{
		this.srcIPAddress = srcIPAddress;
		this.mediumObject = mediumObject;
		this.TX_CHAR = TX_CHAR;
		this.MTU = MTU;
		layer5 = new Layer5(this, srcIPAddress, mediumObject, TX_CHAR, MTU);
	}
	
	public Layer1 getLayer1Object()
	{
		return layer5.getLayer1Object();
	}


	public void setTask(String fileName, IPAddress destIPAddress)
	{
		this.sendingMessage = fileName;
		this.destIPAddress = destIPAddress;
	}
	
	public void receive(String fileName)
	{		
		Log.logActivity(srcIPAddress.stringFormat, "(Received at node) filename= " + fileName);
	}
	
		
	@Override
	public void run()
	{
		while(running)
		{
			if (sendingMessage != null)
			{	
				Log.logActivity(srcIPAddress.stringFormat, 
						"(Sent from node) filename= " + sendingMessage +
						" destIP= " + destIPAddress.stringFormat);
				try {
					layer5.send(sendingMessage, destIPAddress);
				} catch (IOException e) { e.printStackTrace(); }				
				sendingMessage = null;
				destIPAddress = null;
			}
		}
	
	}

}
