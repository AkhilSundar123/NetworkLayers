import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class Layer3 
{
	private IPAddress srcIPAddress;
	private Medium	mediumObject;
	private int		TX_CHAR;
	private int		MTU;
	private byte[]	packet;
	private Layer2 layer2;
	private Layer4 layer4;
	
	private Map<String, IPAddress> routingTable;
	
		
	public Layer3(Layer4 layer4, IPAddress srcIPAddress, Medium mediumObject, 
			int TX_CHAR, int MTU)
	{
		this.srcIPAddress = srcIPAddress;
		this.mediumObject = mediumObject;
		this.TX_CHAR = TX_CHAR;
		this.MTU = MTU;
		this.layer4 = layer4;
		packet = new byte[MTU - 12];
		
		routingTable = new HashMap<String, IPAddress>();
		IPAddress node1IPAddress = new IPAddress(10, 10, 10, 01);
		IPAddress node2IPAddress = new IPAddress(10, 10, 10, 02);
		IPAddress node3IPAddress = new IPAddress(10, 10, 10, 03);
		IPAddress node4IPAddress = new IPAddress(10, 10, 10, 04);
		IPAddress node5IPAddress = new IPAddress(10, 10, 10, 05);
		IPAddress gateWay = new IPAddress(10, 10, 10, 03);
		
		if(srcIPAddress.stringFormat.equals(node1IPAddress.stringFormat) ||
				srcIPAddress.stringFormat.equals(node2IPAddress.stringFormat))
		{
			routingTable.put(node1IPAddress.stringFormat, null);
			routingTable.put(node2IPAddress.stringFormat, null);
			routingTable.put(node3IPAddress.stringFormat, null);
			routingTable.put(node4IPAddress.stringFormat, gateWay);
			routingTable.put(node5IPAddress.stringFormat, gateWay);
		}
		else if(srcIPAddress.stringFormat.equals(node3IPAddress.stringFormat))
		{
			routingTable.put(node1IPAddress.stringFormat, null);
			routingTable.put(node2IPAddress.stringFormat, null);
			routingTable.put(node3IPAddress.stringFormat, null);
			routingTable.put(node4IPAddress.stringFormat, null);
			routingTable.put(node5IPAddress.stringFormat, null);
		}
		else if(srcIPAddress.stringFormat.equals(node4IPAddress.stringFormat) ||
				srcIPAddress.stringFormat.equals(node5IPAddress.stringFormat))
		{
			routingTable.put(node1IPAddress.stringFormat, gateWay);
			routingTable.put(node2IPAddress.stringFormat, gateWay);
			routingTable.put(node3IPAddress.stringFormat, null);
			routingTable.put(node4IPAddress.stringFormat, null);
			routingTable.put(node5IPAddress.stringFormat, null);
		}
		
		layer2 = new Layer2(this, srcIPAddress, mediumObject, TX_CHAR, MTU);
	}
	
	public Layer1 getLayer1Object()
	{
		return layer2.getLayer1Object();
	}
	
	public void send(byte[] segment, IPAddress destIPAddress)
	{
		int	destPos = 0;
		
		System.arraycopy(srcIPAddress.byteFormat, 0, packet, destPos, 4);
		destPos += 4;
		System.arraycopy(destIPAddress.byteFormat, 0, packet, destPos, 4);
		destPos += 4;
		System.arraycopy(segment, 0, packet, destPos, segment.length);	
		
		IPAddress gateWay = routingTable.get(destIPAddress.stringFormat);
		
		
		String event;
		if(gateWay == null)
		{
			event = "(Sent from L3) destIP= " + destIPAddress.stringFormat +
					" GateWayIP= null";
		}
		else
		{
			event = "(Sent from L3) destIP= " + destIPAddress.stringFormat +
					" GateWayIP= " + gateWay.stringFormat;
		}
		Log.logActivity(srcIPAddress.stringFormat, event);
		
		if(segment.length == MTU - 20)
			layer2.send(packet, destIPAddress, gateWay);
		else
			layer2.send(Arrays.copyOf(packet, segment.length + 8), destIPAddress, gateWay);
	}
	
	public void receive(byte[] packet) throws IOException
	{
		byte[] destIPAddress = new byte[4];
		System.arraycopy(packet, 4, destIPAddress, 0, 4);
		
		if(Arrays.equals(destIPAddress,srcIPAddress.byteFormat))
		{
			Log.logActivity(srcIPAddress.stringFormat, "(Received at L3) IP Matched");
			layer4.receive(Arrays.copyOfRange(packet, 8, packet.length));
			return;
		}	
		Log.logActivity(srcIPAddress.stringFormat, "(Received at L3) IP Not Matched");
	}

}
