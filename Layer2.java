import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class Layer2 
{
	private IPAddress srcIPAddress;
	private MACAddress srcMACAddress;
	private Medium	mediumObject;
	private int		TX_CHAR;
	private int		MTU;
	private byte[]	frame;
	private Layer1 layer1;
	private Layer3	layer3;
	
	private Map<String, MACAddress> ARPTable;
	
	public Layer2(Layer3 layer3, IPAddress srcIPAddress, Medium mediumObject, 
			int TX_CHAR, int MTU)
	{
		this.srcIPAddress = srcIPAddress;
		this.mediumObject = mediumObject;
		this.TX_CHAR = TX_CHAR;
		this.MTU = MTU;
		this.layer3 = layer3;
		frame = new byte[MTU];
		
		ARPTable = new HashMap<String, MACAddress>();
		IPAddress node1IPAddress = new IPAddress(10, 10, 10, 01);
		IPAddress node2IPAddress = new IPAddress(10, 10, 10, 02);
		IPAddress node3IPAddress = new IPAddress(10, 10, 10, 03);
		IPAddress node4IPAddress = new IPAddress(10, 10, 10, 04);
		IPAddress node5IPAddress = new IPAddress(10, 10, 10, 05);
		MACAddress node1MACAddress = new MACAddress(10, 10, 10, 10, 10, 01);
		MACAddress node2MACAddress = new MACAddress(10, 10, 10, 10, 10, 02);
		MACAddress node3MACAddress = new MACAddress(10, 10, 10, 10, 10, 03);
		MACAddress node4MACAddress = new MACAddress(10, 10, 10, 10, 10, 04);
		MACAddress node5MACAddress = new MACAddress(10, 10, 10, 10, 10, 05);
					
		ARPTable.put(node1IPAddress.stringFormat, node1MACAddress);
		ARPTable.put(node2IPAddress.stringFormat, node2MACAddress);
		ARPTable.put(node3IPAddress.stringFormat, node3MACAddress);
		ARPTable.put(node4IPAddress.stringFormat, node4MACAddress);
		ARPTable.put(node5IPAddress.stringFormat, node5MACAddress);
		srcMACAddress = ARPTable.get(srcIPAddress.stringFormat);		
		
		layer1 = new Layer1(this, srcIPAddress, mediumObject, TX_CHAR);
			
	}
	
	public Layer1 getLayer1Object()
	{
		return layer1;
	}
	
	public void send(byte[] packet, IPAddress destIPAddress, IPAddress gateWay)
	{
		MACAddress destMACAddress;
		
		if(gateWay == null)
		{
			destMACAddress = ARPTable.get(destIPAddress.stringFormat);
		}
		else
		{
			destMACAddress = ARPTable.get(gateWay.stringFormat);
		}

		int	destPos = 0;
		System.arraycopy(srcMACAddress.byteFormat, 0, frame, destPos, 6);
		destPos += 6;
		System.arraycopy(destMACAddress.byteFormat, 0, frame, destPos, 6);
		destPos += 6;
		System.arraycopy(packet, 0, frame, destPos, packet.length);		
		
		Log.logActivity(srcIPAddress.stringFormat, 
				"(Sent from L2) destIP= " + destIPAddress.stringFormat 
				+ " destMAC= " + destMACAddress.stringFormat);
		
		if(packet.length == MTU - 12)
			layer1.send(frame);
		else
			layer1.send(Arrays.copyOf(frame, packet.length + 12));
	}
	
	public void receive(byte[] frame) throws IOException
	{
		byte[] destMACAddress = new byte[6];
		System.arraycopy(frame, 6, destMACAddress, 0, 6);
		
		if(Arrays.equals(destMACAddress,srcMACAddress.byteFormat))
		{
			Log.logActivity(srcIPAddress.stringFormat, "(Received at L2) MAC Matched");
			layer3.receive(Arrays.copyOfRange(frame, 12, frame.length));
			return;
		}	
		Log.logActivity(srcIPAddress.stringFormat, "(Received at L2) MAC Not Matched");
	}

	
}
