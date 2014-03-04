package Graph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class BasicCCRP {

	private Graph graph = new Graph();
	private static double humanWalkingSpeed = 1.5;			//in m/s
	private int pathId = 0;
	private ArrayList<Route> pathList = new ArrayList<Route>();
	private Set<String> distinctRoutes = new LinkedHashSet<String>();
	private int egressTime = 0;
	private File file;
	FileWriter fw;
	BufferedWriter bw;
	
	public Set<String> getDistinctRoutes() {
		return distinctRoutes;
	}

	public void setDistinctRoutes(Set<String> distinctRoutes) {
		this.distinctRoutes = distinctRoutes;
	}

	public int getPathId() {
		return pathId;
	}

	public void setPathId(int pathId) {
		this.pathId = pathId;
	}

	public ArrayList<Route> getPathList() {
		return pathList;
	}

	public void setPathList(ArrayList<Route> pathList) {
		this.pathList = pathList;
	}

	public static double getHumanWalkingSpeed() {
		return humanWalkingSpeed;
	}

	public static void setHumanWalkingSpeed(double humanWalkingSpeed) {
		BasicCCRP.humanWalkingSpeed = humanWalkingSpeed;
	}

	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	//finding the vertex by name
	public Node getNode(String nodeName) {
		Iterator<Node> it = this.getGraph().getNodeList().iterator();
		while (it.hasNext()) 
		{
			// System.out.println("getNode : 1");
			Node temp = it.next();
			if (temp.getNodeName().equals(nodeName))
				return temp;
		}
		return null;
	}
	
	public void addNodeToGraph(String nodeId, String nodeName, double x, double y, 
			int maxCapacity, int initialOccupancy, int nodeType)
	{
		Node node = new Node();
		node.setNodeId(nodeId);
		node.setNodeName(nodeName);
		node.setX(x);
		node.setY(y);
		node.setMaxCapacity(maxCapacity);
		node.setInitialOccupancy(initialOccupancy);
		node.setCurrentOccupancy(initialOccupancy);
		node.setNodeType(nodeType);
		node.setAdjacencies(null);
		node.setAdjacentScannedList(null);
		node.setParent(null);
		node.setParentEdge(null);
//		node.setPathUptoPreviousNode(null);
		node.setScanned(false);
		node.setChildList(null);
		if(nodeType == Node.DESTINATION)
			node.setMaxCapacity(Integer.MAX_VALUE);
		if(nodeType == Node.SOURCE)
			node.setTravelTime(0);
		else
			node.setTravelTime(Integer.MAX_VALUE);
		ArrayList<Integer> nodeCapacityAtTime = new ArrayList<Integer>();
		nodeCapacityAtTime.add(node.getMaxCapacity());
		node.setNodeCapacityAtTime(nodeCapacityAtTime);
		this.getGraph().addNode(node);
	}
	
	public void addNodeToGraph(String nodeId, String nodeName, 
			int maxCapacity, int initialOccupancy, int nodeType)
	{
		Node node = new Node();
		node.setNodeId(nodeId);
		node.setNodeName(nodeName);
		node.setMaxCapacity(maxCapacity);
		node.setInitialOccupancy(initialOccupancy);
		node.setCurrentOccupancy(initialOccupancy);
		node.setNodeType(nodeType);
		node.setAdjacencies(null);
		node.setAdjacentScannedList(null);
		node.setParent(null);
		node.setParentEdge(null);
//		node.setPathUptoPreviousNode(null);
		node.setScanned(false);
		node.setChildList(null);
		if(nodeType == Node.DESTINATION)
			node.setMaxCapacity(Integer.MAX_VALUE);
		if(nodeType == Node.SOURCE)
			node.setTravelTime(0);
		else
			node.setTravelTime(Integer.MAX_VALUE);
		ArrayList<Integer> nodeCapacityAtTime = new ArrayList<Integer>();
		nodeCapacityAtTime.add(node.getMaxCapacity());
		node.setNodeCapacityAtTime(nodeCapacityAtTime);
		this.getGraph().addNode(node);
	}
	
	public void addEdgeToGraph(int edgeID, String edgeName, String sourceName, 
			String targetName, int maxIntakeCapacity)
	{
		Edge edge = new Edge();
		edge.setEdgeID(edgeID);
		edge.setEdgeName(edgeName);
		edge.setMaxIntakeCapacity(maxIntakeCapacity);
		Node src = this.getNode(sourceName);
		Node target = this.getNode(targetName);
		int travelTime = (int) Math.ceil(Node.calculateDistance(src, target)/humanWalkingSpeed);
		edge.setTravelTime(travelTime);
		edge.setSource(src);
		edge.setTarget(target);
		
		edge.setEdgeCapacity(new ArrayList<Integer>());
		for (int i = 0; i < travelTime; i++)
		{
			edge.getEdgeCapacity().add(maxIntakeCapacity);
		}
		graph.addEdge(edge);
		
		src.addAdjacentEdge(edge);
		target.addAdjacentEdge(edge);
	}
	
	public void addEdgeToGraph(int edgeID, String edgeName, String sourceName, 
			String targetName, int maxIntakeCapacity, int travelTime)
	{
		Edge edge = new Edge();
		edge.setEdgeID(edgeID);
		edge.setEdgeName(edgeName);
		edge.setMaxIntakeCapacity(maxIntakeCapacity);
		Node src = this.getNode(sourceName);
		Node target = this.getNode(targetName);
		edge.setTravelTime(travelTime);
		edge.setSource(src);
		edge.setTarget(target);
		
		edge.setEdgeCapacity(new ArrayList<Integer>());
		
		//For each section of edge, initially(t=0) capacity is its maximum
		for (int i = 0; i < travelTime; i++)
		{
			edge.getEdgeCapacity().add(maxIntakeCapacity);
		}
		
		graph.addEdge(edge);
		
		src.addAdjacentEdge(edge);
		target.addAdjacentEdge(edge);
	}
	
	public void addEdgeToGraph(int edgeID, String edgeName, String sourceName, 
			String targetName, int maxIntakeCapacity, int travelTime, int x)
	{
		Edge edge = new Edge();
		edge.setEdgeID(edgeID);
		edge.setEdgeName(edgeName);
		edge.setMaxIntakeCapacity(maxIntakeCapacity);
		Node src = this.getNode(sourceName);
		Node target = this.getNode(targetName);
		edge.setTravelTime(travelTime);
		edge.setSource(src);
		edge.setTarget(target);
		
		edge.setEdgeCapacity(new ArrayList<Integer>());
		
		//For each section of edge, initially(t=0) capacity is its maximum
		for (int i = 0; i < travelTime; i++)
		{
			edge.getEdgeCapacity().add(maxIntakeCapacity);
		}
		
		graph.addEdge(edge);
		
		src.addAdjacentEdge(edge);
		//target.addAdjacentEdge(edge);
	}
	
	public void CCRPEvacuationPlanner(String fileName) throws IOException
	{
		file = new File(fileName);
		file.createNewFile();
		fw = new FileWriter(file.getAbsoluteFile());
		bw = new BufferedWriter(fw);
		
		Route.setTotalHops(0);
		PriorityQueue<Node> priorityQueue = new PriorityQueue<Node>();
		int evacueeCount = 0;
		int evacuationTime = 0;
		for(int index = 0;index<graph.getNodeList().size();index++)
		{
			Node node = graph.getNodeList().get(index);
			if(node.getNodeType() == Node.SOURCE)
			{
				//count people
				evacueeCount = evacueeCount + node.getInitialOccupancy();
			}
		}
		int tempcount = 0;
		//while there are people in any source node
		while(evacueeCount > 0)
		{
			priorityQueue.clear();
			//Adding all source nodes to priority queue
			for(int index = 0;index<graph.getNodeList().size();index++)
			{
				Node node = graph.getNodeList().get(index);
				if(node.getNodeType() == Node.SOURCE)
				{
					//Add source list to priority queue
					priorityQueue.add(node);
				}
			}
			
			Node u = priorityQueue.poll();
			
			//while u is not a destination
			while(u.getNodeType() != Node.DESTINATION)
			{
				//for all adjacent vertices of u
				for(int index = 0; index < u.getAdjacencies().size(); index++)
				{
					Edge edge = u.getAdjacencies().get(index);
					Node v;
					if(edge.getSource() == u)
						v = edge.getTarget();
					else
						v = edge.getSource();
					if (v.isScanned())
					{
						//Already visited , Do Nothing
					}
					else
					{
						simpleDijkstra(u, v, edge, priorityQueue);
					}
				}
				u.setScanned(true);
				u = priorityQueue.poll();
				
			}
			/*We have a destination node now with smallest distance*/
			evacuationTime += u.getTravelTime();
			//Find the minimum capacity cmin and the edge e where this bottleneck occurs
			Node source = getSourceFromDestination(u);
			int sourceOccupancy = source.getCurrentOccupancy();
			MinimumCapacityAndEdge minCapacityAndEdge = findMinimumCapacityAndEdge(u);
			
			boolean sourceEmpty = false;
			int groupSize = 0;
			if(sourceOccupancy <= minCapacityAndEdge.getMinCapacity())
			{
				//Source should get empty
				sourceEmpty = true;
				groupSize = sourceOccupancy;
			}
			else
			{
		//		nodeToReset = minCapacityAndEdge.getNodeToReset();
				groupSize = minCapacityAndEdge.getMinCapacity();
			}
			//Reserve the path and assign the route to a group of size
			//min(cmin, evacuees in source of path found) from the source
			reservePath(u, groupSize);
			tempcount = tempcount + groupSize;
			//System.out.println(tempcount);
			//if(tempcount>32535)
				//break;
			if(sourceEmpty)
			{
				if(evacueeCount != groupSize)
				{
					//resetNodes(source, priorityQueue);
					//Source node becomes a normal node after getting empty
					source.setNodeType(Node.NORMAL);
					graph.clearNodeData();
				}
				else
				{
					//Evacuee count is equal to the group size means this is the last group.
					//Hence, no need to reset since the tree wont be used anymore.
					break;
				}
			}
			else
			{
				graph.clearNodeData();
			}

			evacueeCount = evacueeCount - groupSize;
		}
		bw.close();
		//System.out.println(tempcount);
		System.out.println("Egress Time : " + egressTime);
		System.out.println("RouteList Size : " + (pathId));
		System.out.println("Average Evacuation Time : " + 1.0*evacuationTime/tempcount);
		System.out.println("Avg Hops : " + 1.0*Route.getTotalHops()/pathId);
		System.out.println("Max Hops : " + Route.getMaxHops());
		System.out.println("No of Distinct Routes : " + this.getDistinctRoutes().size());
		int maxWaitingTimeAtANode = 0;
		String nodeName = "";
		double totalWaitingTime = 0;
		for(int i=0;i<graph.getNodeList().size();i++)
		{
			totalWaitingTime += graph.getNodeList().get(i).getWaitingTimeAtThisNode();
			if(graph.getNodeList().get(i).getWaitingTimeAtThisNode() > maxWaitingTimeAtANode)
			{
				maxWaitingTimeAtANode = graph.getNodeList().get(i).getWaitingTimeAtThisNode();
				nodeName = graph.getNodeList().get(i).getNodeName();
			}
		}
		System.out.println("Max. Waiting Time at a node : " + maxWaitingTimeAtANode 
				+ ", Node Name : " + nodeName);
		System.out.println("Average. Waiting Time at a node : " 
				+ totalWaitingTime/graph.getNodeList().size());
	}
	
	public Node getSourceFromDestination(Node dest)
	{
		while(dest.getParent()!=null)
		{
			dest = dest.getParent();
		}
		return dest;
	}
	
	/*
	 * This will first reserve path from second node upto last edge
	 * Then it will increase capacity of source at departure time from the source node
	 * Then it will decrease occupancy of source node
	 * Add route to pathList
	 * Display the route
	 */
	public void reservePath(Node destination, int groupSize) throws IOException
	{
		egressTime = destination.getTravelTime();
		String routeDetails1 = destination.getTravelTime() + "," + groupSize + ",";
		String routeDetails2 = "";
		int hops = 0;
		do
		{
			hops++;
			Node parentNode = destination.getParent();
			Edge parentEdge = destination.getParentEdge();
			
			//DepartureTime from parent
			int depart = destination.getTravelTime() - parentEdge.getTravelTime();
			int edgeTravelTime = parentEdge.getTravelTime();
			boolean sameFlowDirection = false;
			
			if(parentNode == parentEdge.getSource())
				sameFlowDirection = true;
			//Edge reservation
			int newCapacity = 0;
			
			if(sameFlowDirection)
			{
				newCapacity = parentEdge.getEdgeCapacity().get(depart) - groupSize;
				parentEdge.getEdgeCapacity().set(depart, newCapacity);
			}
			else
			{
				newCapacity = parentEdge.getEdgeCapacity().get(depart + edgeTravelTime) - groupSize;
				parentEdge.getEdgeCapacity().set(depart + edgeTravelTime, newCapacity);
			}
			//For eg : Departure time at 15, travel time = 5, Hence slot 'time' should be 
			//booked at time 15 + time(0,1,2,3,4) = (15,16,17,18,19)
			
			parentNode.setNoOfPathsThroughThisNode(parentNode.getNoOfPathsThroughThisNode() + 1);
			parentNode.setNoOfPeopleThroughThisNode(parentNode.getNoOfPeopleThroughThisNode() + groupSize);
			
			int parentNodeArrival = 0;
			if(parentNode.getParent()!=null)
				parentNodeArrival = (int)parentNode.getTravelTime();

			parentNode.setWaitingTimeAtThisNode(depart-parentNodeArrival);
			
			for(int i=parentNodeArrival;i<depart;i++)
			{
				int newNodeCapacity = parentNode.getNodeCapacityAtTime().get(i) - groupSize;
				parentNode.getNodeCapacityAtTime().set(i, newNodeCapacity);
			}
			routeDetails2 = "--" + destination.getNodeName() + routeDetails2;
			destination=parentNode;
		}while(destination.getParent()!=null);
		routeDetails2 = destination.getNodeName() + routeDetails2;
		bw.write(routeDetails1 + routeDetails2);
		destination.setCurrentOccupancy(destination.getCurrentOccupancy() - groupSize);
		
		//Add path to pathList
		
		Route.addTotalHops(hops);
		if(Route.getMaxHops() < hops)
			Route.setMaxHops(hops);
		
		//route.displayRoute2();
		distinctRoutes.add(routeDetails2);
		this.pathId++;
	}
	public MinimumCapacityAndEdge findMinimumCapacityAndEdge(Node destination)
	{
		int minCapacity = Integer.MAX_VALUE;
		Edge minCutEdge = null;
		Node nodeToReset = null;
		do
		{
			//Comparing minCapaciy, edge and nextNode
			Node parentNode = destination.getParent();
			Edge parentEdge = destination.getParentEdge();
			int depart = destination.getTravelTime() - parentEdge.getTravelTime();
			int edgeCapacity = Integer.MAX_VALUE;
			int edgeTravelTime = parentEdge.getTravelTime();
			int nodeCapacity = Integer.MAX_VALUE;
			boolean sameFlowDirection = false;
			if(parentNode==parentEdge.getSource())
				sameFlowDirection = true;
			
			//For eg : Departure time at 15, travel time = 5, Hence check capacity booked at  
			//slot 'time' for time 15 + time(0,1,2,3,4) = (15,16,17,18,19)
			int newCapacity = 0;
			if(sameFlowDirection)
			{
				newCapacity = parentEdge.getEdgeCapacity().get(depart);
			}
			else
			{
				newCapacity = parentEdge.getEdgeCapacity().get(depart + edgeTravelTime);
			}
			
			if(newCapacity < edgeCapacity)
			{
				edgeCapacity = newCapacity;
			}
			int arrival = 0;
			if(parentNode.getParent()!=null)
				arrival = parentNode.getTravelTime();
			int nodeCap = parentNode.getNodeCapacityAtTime().get(arrival);
			if(nodeCap < nodeCapacity)
			{
				nodeCapacity = nodeCap;
			}
			
			if(edgeCapacity < minCapacity && edgeCapacity < nodeCapacity)
			{
				minCapacity = edgeCapacity;
			}
			else if(nodeCapacity < minCapacity && nodeCapacity < edgeCapacity)
			{
				minCapacity = nodeCapacity;
			}
			destination = parentNode;
		}while(destination.getParent()!=null);
		
		MinimumCapacityAndEdge minimumCapacityAndEdge = 
				new MinimumCapacityAndEdge(minCapacity, minCutEdge, nodeToReset);
		return minimumCapacityAndEdge;
	}
	
	public void simpleDijkstra(Node u, Node v, Edge edge, PriorityQueue<Node> priorityQueue)
	{
		int departureTimeFromU;
		if(edge.getSource() == u)
			departureTimeFromU = u.getTravelTime();
		else
			departureTimeFromU = u.getTravelTime() + edge.getTravelTime(); //means we will reach source after travel time
		/* Time instances to add to an edge
		 * We add at least that many time instances as the travel time 
		 * of node u
		*/

		double timeInstancesToAdd = departureTimeFromU - edge.getEdgeCapacity().size() + 2;

		
		for(int i = 0; i < timeInstancesToAdd; i++)
			edge.addEdgeCapacity();
		
		//while the capacity in first section of this edge is not available
		//we will wait here only
		int delay = 0;
		while(edge.getEdgeCapacity().get((int)Math.ceil(departureTimeFromU)) <= 0)
		{
			delay++;
			departureTimeFromU++;
			if (edge.getEdgeCapacity().size() <= departureTimeFromU)
				edge.addEdgeCapacity();
		}

		//Adding Time instances for u
		timeInstancesToAdd = u.getTravelTime() + delay - 
				u.getNodeCapacityAtTime().size() + 1;
		for(int i = 0; i < timeInstancesToAdd; i++)
			u.getNodeCapacityAtTime().add(u.getMaxCapacity());
		
		int distanceToVThroughU = u.getTravelTime() + delay + edge.getTravelTime();

		/* Time instances to add to far vertex v
		 * We add at least that many time instances as the travel time 
		 * to node v
		*/
		timeInstancesToAdd = distanceToVThroughU - v.getNodeCapacityAtTime().size() + 2;

		for(int i = 0; i < timeInstancesToAdd; i++)
			v.getNodeCapacityAtTime().add(v.getMaxCapacity());

		//Capacity should be available at both the edge and at the vertex 
		while((v.getNodeCapacityAtTime().get((int)Math.ceil(distanceToVThroughU)) <= 0)
				|| edge.getEdgeCapacity().get((int)Math.ceil(departureTimeFromU)) <= 0)
		{
			delay++;
			distanceToVThroughU++;
			departureTimeFromU++;
			//Add time instance to node v
			v.getNodeCapacityAtTime().add(v.getMaxCapacity());
			u.getNodeCapacityAtTime().add(u.getMaxCapacity());
			//Add time instances to edge uv
			while(edge.getEdgeCapacity().size() <=
					(departureTimeFromU + 1))
			{
				edge.addEdgeCapacity();
			}
		}

		if(distanceToVThroughU < v.getTravelTime())
		{

			if(v.getParent() != null)
			{
				//v has Parent
				v.getParent().removeChild(v);
			}
			v.setParent(u);
			u.addChild(v);
			v.setParentEdge(edge);
//			PathUptoNode pathUptoPreviousNode = null;
//			if(u.getPathUptoPreviousNode() == null)
//			{
//				//u is source node
//				pathUptoPreviousNode = new PathUptoNode();
//			}
//			else
//			{
//				pathUptoPreviousNode = new PathUptoNode(u.getPathUptoPreviousNode());
//			}
//			pathUptoPreviousNode.add(u, edge, (int)Math.ceil(u.getTravelTime() + delay));
//			v.setPathUptoPreviousNode(pathUptoPreviousNode);
			v.setTravelTime(distanceToVThroughU);
			if(priorityQueue.contains(v))
				priorityQueue.remove(v);
			priorityQueue.add(v);
			/* Time instances to add to an edge
			 * We add at least that many time instances as the travel time 
			 * of node v at end
			*/
			timeInstancesToAdd = distanceToVThroughU - 
					edge.getEdgeCapacity().size() + 2;

			for(int i = 0; i < timeInstancesToAdd; i++)
				edge.addEdgeCapacity();
		}
	}
	
	public void displayNodeEdgeStats()
	{
		FileWriter writer = null;
		try
		{
		    writer = new FileWriter("/Users/MLGupta/Documents/BasicNodeDataKr2.csv");
		    writer.append("NodeName,NoOfPaths,NoOfPeople,WaitingTime\n");
			for(int i=0;i<graph.getNodeList().size();i++)
			{
				Node n = graph.getNodeList().get(i);
				String str = "";
				str+=n.getNodeName() + "," + n.getNoOfPathsThroughThisNode() + "," + n.getNoOfPeopleThroughThisNode() + "," + n.getWaitingTimeAtThisNode() + "\n";
				writer.write(str);
				//System.out.println(str);
			}
			writer.close();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		try
		{
		    writer = new FileWriter("/Users/MLGupta/Documents/BasicRouteListKr2.csv");
		    writer.append("RouteList\n");
			for (Iterator<String> iterator = this.distinctRoutes.iterator(); iterator.hasNext();) {
				String route = (String) iterator.next();
				writer.write(route + "\n");
			}
			writer.close();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
}
