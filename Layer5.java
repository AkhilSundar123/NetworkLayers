import java.io.IOException;


public class Layer5 
{
	private IPAddress srcIPAddress;
	private Medium	mediumObject;
	private int		TX_CHAR;
	private int		MTU;
	
	private Layer4 layer4;
	private Node nodeObject;
	
	public Layer5(Node nodeObject, IPAddress srcIPAddress, Medium mediumObject, 
			int TX_CHAR, int MTU)
	{
		this.srcIPAddress = srcIPAddress;
		this.mediumObject = mediumObject;
		this.TX_CHAR = TX_CHAR;
		this.MTU = MTU;
		this.nodeObject = nodeObject;
		layer4 = new Layer4(this, srcIPAddress, mediumObject, TX_CHAR, MTU);
	}
	
	public Layer1 getLayer1Object()
	{
		return layer4.getLayer1Object();
	}
	
	public void send(String fileName, IPAddress destIPAddress) throws IOException
	{
		Log.logActivity(srcIPAddress.stringFormat, 
				"(Sent from L5) filename= " + fileName +
				" destIP= " + destIPAddress.stringFormat);
		layer4.send(fileName, destIPAddress);
	}	
	
	public void receive(String fileName)
	{
		Log.logActivity(srcIPAddress.stringFormat, 
				"(Received at L5) filename= " + fileName);
		nodeObject.receive(fileName);
	}
}
