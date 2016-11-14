import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class SimulateBroadcast
{	
	public static void main(String[] args) throws IOException, InterruptedException 
	{
		String	line;
		String	message = null;
		String	subString;
		int		nodeID;		
		int		TX_CHAR = 0;
		int		MTU = 0;
		BufferedReader	inputStream = null;
		
		/** Parse entire input file to get config details of the broadcast system */
		inputStream = new BufferedReader(new FileReader(args[0]));	
		if ((line = inputStream.readLine()) != null)
		{
			subString = getWord(line, 2);
			TX_CHAR = Integer.parseInt(subString);
		}
		if ((line = inputStream.readLine()) != null) 
		{
			subString = getWord(line, 2);
			MTU = Integer.parseInt(subString);
		}			
		if (inputStream != null)			
			inputStream.close();
		
		/** Setting up Broadcast system */
		int		prevTimestamp = 0, timestamp;
		Node	sourceNode, node;	
		IPAddress nodeIPAddress;
		IPAddress srcIPAddress = new IPAddress(10, 10, 10, 1);
		IPAddress destIPAddress = new IPAddress(10, 10, 10, 2);
		
		Medium	mediumObject = new Medium(TX_CHAR);
		List<Node>	nodes = new ArrayList<Node>();
		inputStream = new BufferedReader(new FileReader(args[0]));
				
		/** Starting all nodes and medium threads */
		nodeIPAddress = new IPAddress(10, 10, 10, 1);	
		node = new Node(nodeIPAddress, mediumObject, TX_CHAR, MTU);
		nodes.add(node);
		mediumObject.openConnection(node.getLayer1Object());
		node.start();
		sourceNode = node;
		
		nodeIPAddress = new IPAddress(10, 10, 10, 2);	
		node = new Node(nodeIPAddress, mediumObject, TX_CHAR, MTU);
		nodes.add(node);
		mediumObject.openConnection(node.getLayer1Object());
		node.start();
		
		mediumObject.start();
		
		Log.logFileName = args[1];
		Log.startTime = System.currentTimeMillis();
		
		/** Starting Broadcast */		
		line = inputStream.readLine();		/** Skipping starting 2 lines of input file */
		line = inputStream.readLine();
		while ((line = inputStream.readLine()) != null)
		{
			subString = getWord(line, 1);
			timestamp = Integer.parseInt(subString);
			
						
			message = getWord(line, 3);
												/** Keeping main thread in sleep until */
			Thread.sleep(timestamp - prevTimestamp);/** the time to send msg to the node has reached */
			
			
			if(message.contains("."))
			sourceNode.setTask(message, destIPAddress);			
			
			prevTimestamp = timestamp;	
		}
		
		/** Stopping Boardcast */
		if(message != null)
			Thread.sleep(TX_CHAR * 2200);		
		mediumObject.closeConnection();
			
	
		if (inputStream != null)			
			inputStream.close();

		
		System.out.println("Program End");
		
	}
	
	/** Function to get individual words in a line */
	private static String getWord(String inputString, int wordPosition)
	{
		String subString = null ;
		int startIndex = 0;
		int endIndex = 0;
		
		while (wordPosition != 0)
		{			
			endIndex = inputString.indexOf(' ', startIndex);
			
			wordPosition--;
			if (wordPosition == 0)
			{
				if (endIndex == -1)
					subString = inputString.substring(startIndex);
				else
					subString = inputString.substring(startIndex, endIndex);				
			}
			
			if (endIndex == -1)
				break;
			else
				startIndex = endIndex + 1;				
		}
		
		return subString;		
	}	
}
