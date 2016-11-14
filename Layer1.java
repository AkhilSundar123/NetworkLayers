import java.io.IOException;


public class Layer1 
{
	private IPAddress srcIPAddress;
	private Medium	mediumObject;
	private int		TX_CHAR;
	private Layer2 layer2;
	
	public Layer1(Layer2 layer2, IPAddress srcIPAddress, Medium mediumObject, int TX_CHAR)
	{
		this.srcIPAddress = srcIPAddress;
		this.mediumObject = mediumObject;
		this.TX_CHAR = TX_CHAR;
		this.layer2 = layer2;
	}
	
	public void send(byte[] frame)
	{
		Log.logActivity(srcIPAddress.stringFormat, "(Started Sending from L1) ");
		
		mediumObject.send(frame);
		try 
		{
			Thread.sleep((frame.length)*TX_CHAR);
		} catch (InterruptedException e) {	e.printStackTrace(); }
	}
	
	public void receive(byte[] bytes) throws IOException
	{
		Log.logActivity(srcIPAddress.stringFormat, "(Received at L1) ");
		layer2.receive(bytes);
	}
	
	public boolean isBusy()
	{
		return false;
	}

}
