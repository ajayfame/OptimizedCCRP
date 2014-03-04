package Graph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MainProgram {
	
	private static CCRPWithReuse ccrp;
	private static BasicCCRP basicCCRP;
	private static CCRP_PlusPlus ccrpPlusPlus;
	
	public static void readNodeAndEdgeFiles(String nodeFile, String edgeFile)
	{
		String csvFile = nodeFile;
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		try 
		{
	 
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) 
			{
				String[] stringNode = line.split(cvsSplitBy);
				String nodeId = stringNode[0];
				String nodeName = stringNode[1];
				double x = Double.parseDouble(stringNode[2]);
				double y = Double.parseDouble(stringNode[3]);
				int maxCapacity = (int)Double.parseDouble(stringNode[4]);
				int initialOccupancy = (int)Double.parseDouble(stringNode[5]);
				String nodeTypeC = stringNode[6];
				int nodeType = 2;
				if(nodeTypeC.equals("S"))
					nodeType = Node.SOURCE;
				else if(nodeTypeC.equals("D"))
					nodeType = Node.DESTINATION;
				else
					nodeType = Node.NORMAL;
				ccrp.addNodeToGraph(nodeId, nodeName, x, y, maxCapacity, initialOccupancy, nodeType);
				basicCCRP.addNodeToGraph(nodeId, nodeName, x, y, maxCapacity, initialOccupancy, nodeType);
				ccrpPlusPlus.addNodeToGraph(nodeId, nodeName, x, y, maxCapacity, initialOccupancy, nodeType);
			}
			csvFile = edgeFile;
			br.close();
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) 
			{
				String[] stringEdge = line.split(cvsSplitBy);
				int edgeID = Integer.parseInt(stringEdge[0]);
				String edgeName = stringEdge[1];
				String sourceName = stringEdge[2];
				String targetName = stringEdge[3];
				int travelTime = (int)Double.parseDouble(stringEdge[4]);
				int maxCapacity = (int)Double.parseDouble(stringEdge[5]);
				ccrp.addEdgeToGraph(edgeID, edgeName, sourceName, targetName, maxCapacity, travelTime);
				basicCCRP.addEdgeToGraph(edgeID, edgeName, sourceName, targetName, maxCapacity, travelTime);
				ccrpPlusPlus.addEdgeToGraph(edgeID, edgeName, sourceName, targetName, maxCapacity, travelTime);
			}
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			if (br != null) {
				try 
				{
					br.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}

	public static void readNodeAndEdgeFiles(String nodeFile, String edgeFile, int overload)
	{
		String csvFile = nodeFile;
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		try 
		{
	 
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) 
			{
				String[] stringNode = line.split(cvsSplitBy);
				String nodeId = stringNode[0];
				String nodeName = stringNode[1];
				int maxCapacity = (int)Double.parseDouble(stringNode[2]);
				int initialOccupancy = (int)Double.parseDouble(stringNode[3]);
				String nodeTypeC = stringNode[4];
				int nodeType = 2;
				if(nodeTypeC.equals("S"))
					nodeType = Node.SOURCE;
				else if(nodeTypeC.equals("D"))
					nodeType = Node.DESTINATION;
				else
					nodeType = Node.NORMAL;
				ccrp.addNodeToGraph(nodeId, nodeName, maxCapacity, initialOccupancy, nodeType);
				//basicCCRP.addNodeToGraph(nodeId, nodeName, maxCapacity, initialOccupancy, nodeType);
				//ccrpPlusPlus.addNodeToGraph(nodeId, nodeName, maxCapacity, initialOccupancy, nodeType);
			}
			csvFile = edgeFile;
			br.close();
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) 
			{
				String[] stringEdge = line.split(cvsSplitBy);
				int edgeID = Integer.parseInt(stringEdge[0]);
				String edgeName = stringEdge[1];
				String sourceName = stringEdge[2];
				String targetName = stringEdge[3];
				int travelTime = (int)Double.parseDouble(stringEdge[6]);
				int maxCapacity = (int)Double.parseDouble(stringEdge[7]);
				ccrp.addEdgeToGraph(edgeID, edgeName, sourceName, targetName, maxCapacity, travelTime);
				//basicCCRP.addEdgeToGraph(edgeID, edgeName, sourceName, targetName, maxCapacity, travelTime);
				//ccrpPlusPlus.addEdgeToGraph(edgeID, edgeName, sourceName, targetName, maxCapacity, travelTime);
				
			}
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			if (br != null) {
				try 
				{
					br.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
	public static void main(String args[]) throws Exception
	{
		ccrp = new CCRPWithReuse();
		basicCCRP = new BasicCCRP();
		ccrpPlusPlus = new CCRP_PlusPlus();
		String nodeFile = "C:/Users/Mukesh/Google Drive/Account2/nodeSF.csv";
		String edgeFile = "C:/Users/Mukesh/Google Drive/Account2/edgeSF.csv";
		String outputFile1 = "C:/Users/Mukesh/Google Drive/Account2/modCCRPout_SF.csv";
		String outputFile2 = "C:/Users/Mukesh/Google Drive/Account2/basicCCRPout_SF.csv";
		//String outputFile3 = "C:/Users/Mukesh/Google Drive/Account2/CCRPplusPlusOut_SF.csv";
		
		//System.out.println("ABC");
		readNodeAndEdgeFiles(nodeFile, edgeFile, 1);
		//System.out.println("BCD");
		System.out.println("Modified CCRP");
		long startTime1 = System.currentTimeMillis();
		ccrp.modifiedCCRPEvacuationPlanner(outputFile1);
		long endTime1 = System.currentTimeMillis();
		System.out.println("Running time :" + (endTime1-startTime1) );
		//ccrp.displayNodeEdgeStats();
		System.out.println("***********");
		System.out.println("Basic CCRP");
		long startTime = System.currentTimeMillis();
		//basicCCRP.CCRPEvacuationPlanner(outputFile2);
		long endTime = System.currentTimeMillis();
		System.out.println("Running time :" + (endTime-startTime) );
		//basicCCRP.displayNodeEdgeStats();
		System.out.println("***********");
		System.out.println("CCRP Plus Plus");
		startTime1 = System.currentTimeMillis();
		//ccrpPlusPlus.ccrpPlusPlus();
		endTime1 = System.currentTimeMillis();
		System.out.println("Running time :" + (endTime1-startTime1) );
	}
}
