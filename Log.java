import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Log 
{
	static String logFileName;
	static long startTime;	
	
	public static void logActivity(String callerIPAddress, String event)
	{
		PrintWriter	outputStream = null;
		try
		{
			outputStream = new PrintWriter(new FileWriter(logFileName, true));
		} catch (IOException e) { e.printStackTrace(); }
		
		
		outputStream.println(callerIPAddress + " " + (System.currentTimeMillis()- startTime) +" "+event);
		outputStream.close();
	}

}
