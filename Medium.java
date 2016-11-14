import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Medium extends Thread
{
	List<Layer1> nodes = null;
	byte[]	message = null;
	int		TX_CHAR = 0;
	int		timestamp;
	int		NoOfNodesAccessing = 0;
	volatile boolean running = true;
		
	public Medium(int TX_CHAR)
	{
		nodes = new ArrayList<Layer1>();
		this.TX_CHAR = TX_CHAR;
	}
	
	public void openConnection(Layer1 layer1Object)
	{
		nodes.add(layer1Object);
	}
	
	public  void send(byte[] message)
	{
		NoOfNodesAccessing++;	/* No.of nodes currently accessing the medium */
		this.message = message;
	}
	
	public boolean isBusy()
	{
		return false;		
	}
	
	public void closeConnection()
	{
		running = false;
	}
	
	@Override
	public void run() 
	{			
		while (running)
		{
			if (message != null)
			{
				try 
				{
					Thread.sleep((message.length-10)*TX_CHAR);
					
					Log.logActivity("Medium", "(Received a packet) ");
				
				
					if(NoOfNodesAccessing == 1)		/* No collision case */		
					{
						for(Layer1 node : nodes)
						{
							Log.logActivity("Medium", "(Sent the packet to a node) ");
							node.receive(message);
						}
					}
					else
					{						/* Broadcasting scrambled msg */	
						for(Layer1 node : nodes)
						{
							Log.logActivity("Medium", "(Sent the packet to a node) ");
							node.receive("XXXXX".getBytes());
						}
					}
				}
				catch (InterruptedException e) {	e.printStackTrace();} 
				catch (IOException e) {	e.printStackTrace();}
				message = null;
				NoOfNodesAccessing = 0;
			}
		}
	}

}
