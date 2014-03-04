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

public class CCRPWithReuse {

	private Graph graph = new Graph();
	private Set<Node> PQAdditionList = new LinkedHashSet<Node>();
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

	public Set<Node> getPQAdditionList() {
		return PQAdditionList;
	}

	public void setPQAdditionList(Set<Node> pQAdditionList) {
		PQAdditionList = pQAdditionList;
	}

	public static double getHumanWalkingSpeed() {
		return humanWalkingSpeed;
	}

	public static void setHumanWalkingSpeed(double humanWalkingSpeed) {
		CCRPWithReuse.humanWalkingSpeed = humanWalkingSpeed;
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
		//System.out.println(edge.getEdgeID());
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
	
	public void modifiedCCRPEvacuationPlanner(String fileName) throws Exception
	{
		file = new File(fileName);
		file.createNewFile();
		fw = new FileWriter(file.getAbsoluteFile());
		bw = new BufferedWriter(fw);
		PriorityQueue<Node> priorityQueue = new PriorityQueue<Node>();
		int evacueeCount = 0;
		int evacuationTime = 0;
		boolean sameTime = false;
		for(int index = 0;index<graph.getNodeList().size();index++)
		{
			Node node = graph.getNodeList().get(index);
			if(node.getNodeType() == Node.SOURCE)
			{
				//Add source list to priority queue
				priorityQueue.add(node);
				//count people
				evacueeCount = evacueeCount + node.getInitialOccupancy();
				
			}
		}
		//System.out.println(evacueeCount);
		//System.out.println(priorityQueue.size());
		int tempcount = 0;
		//while there are people in any source node
		while(evacueeCount > 0)
		{
			Node u = priorityQueue.poll();
			
			//while u is not a destination
			while(u.getNodeType() != Node.DESTINATION)
			{
				//System.out.println(u.getNodeName());
				//for all adjacent vertices of u
				for(int index = 0; index < u.getAdjacencies().size(); index++)
				{
					Edge edge = u.getAdjacencies().get(index);
					Node v;
					if(edge.getSource() == u)
						v = edge.getTarget();
					else
						v = edge.getSource();
					//System.out.println(u.getNodeName() + "---" + v.getNodeName());
					
					if (v.isScanned())
					{
						if(v.getParent() == u || u.getParent() == v)
						{
							//Do Nothing
						}
						else
						{
							u.addAdjacentScannedEdge(v);
							v.addAdjacentScannedEdge(u);
						}
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
//			Node source = u.getPathUptoPreviousNode().getNodeList().get(0);
			Node source  = getSourceFromDestination(u);
			//Check for another destination node with same travel time
			evacuationTime+= u.getTravelTime();
			if(priorityQueue.size()>1)
			{
				double tempTime = u.getTravelTime();
				
				//Queue q = priorityQueue;
				Node secondElem = (Node)priorityQueue.toArray()[1];
				if(!sameTime && secondElem.getTravelTime()==tempTime && secondElem.getNodeType() == Node.DESTINATION)
				{
//					if(source == secondElem.getPathUptoPreviousNode().getNodeList().get(0))
					if(source == getSourceFromDestination(secondElem))	
						sameTime = true;
				}
			}
			
			//Find the minimum capacity cmin and the edge e where this bottleneck occurs
			int sourceOccupancy = source.getCurrentOccupancy();
			MinimumCapacityAndEdge minCapacityAndEdge = findMinimumCapacityAndEdge(u);
			
			boolean sourceEmpty = false;
			Node nodeToReset = null;
			int groupSize = 0;
			if(sourceOccupancy <= minCapacityAndEdge.getMinCapacity())
			{
				//Source should get empty
				sourceEmpty = true;
				groupSize = sourceOccupancy;
			}
			else
			{
				nodeToReset = minCapacityAndEdge.getNodeToReset();
				groupSize = minCapacityAndEdge.getMinCapacity();
			}
			minCapacityAndEdge = null;
			//Reserve the path and assign the route to a group of size
			//min(cmin, evacuees in source of path found) from the source
			reservePath(u, groupSize);
			tempcount = tempcount + groupSize;
			if(sourceEmpty)
			{
				if(evacueeCount != groupSize)
				{
					resetNodes(source, priorityQueue);
					//Source node becomes a normal node after getting empty
					source.setNodeType(Node.NORMAL);
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
				Node parent = nodeToReset.getParent();
				resetNodes(nodeToReset, priorityQueue);
				priorityQueue.add(parent);
			}
			Iterator<Node> PQAdditionListIterator = PQAdditionList.iterator();
			while(PQAdditionListIterator.hasNext())
			{
				Node n = PQAdditionListIterator.next();
				n.setScanned(false);
				priorityQueue.add(n);
			}
			PQAdditionList.clear();
			evacueeCount = evacueeCount - groupSize;
			System.out.println(evacueeCount + " " + pathId);
		}
		//System.gc();
		bw.close();
		System.out.println("Egress Time : " + egressTime);
		System.out.println("RouteList Size : " + pathId);
		System.out.println("Average Evacuation Time : " + 1.0*evacuationTime/tempcount);
		System.out.println("Flag : " + sameTime);
		System.out.println("Avg Hops : " + 1.0*Route.getTotalHops()/pathId);
		System.out.println("Max Hops : " + Route.getMaxHops());
		System.out.println("No of Distinct Routes : " + this.getDistinctRoutes().size());
		int maxWaitingTimeAtANode = 0;
		//String nodeName;
		double totalWaitingTime = 0;
		for(int i=0;i<graph.getNodeList().size();i++)
		{
			totalWaitingTime += graph.getNodeList().get(i).getWaitingTimeAtThisNode();
			if(graph.getNodeList().get(i).getWaitingTimeAtThisNode() > maxWaitingTimeAtANode)
			{
				maxWaitingTimeAtANode = graph.getNodeList().get(i).getWaitingTimeAtThisNode();
			//	nodeName = graph.getNodeList().get(i).getNodeName();
			}
		}
		System.out.println("Max. Waiting Time at a node : " + maxWaitingTimeAtANode);
		System.out.println("Average. Waiting Time at a node : " + totalWaitingTime/graph.getNodeList().size());
	}
	
	public Node getSourceFromDestination(Node dest)
	{
		while(dest.getParent()!=null)
		{
			dest = dest.getParent();
		}
		return dest;
	}
	
	public void resetNodes(Node n, PriorityQueue<Node> priorityQueue)
	{
		if(!n.isScanned() && n.getChildList() == null)
		{
			for(int index = 0; index < n.getAdjacencies().size(); index++)
			{
				Edge edge = n.getAdjacencies().get(index);
				Node v;
				if(edge.getSource() == n)
					v = edge.getTarget();
				else
					v = edge.getSource();
				if (v.isScanned() && n.getParent() != v)
				{
					PQAdditionList.add(v);
				}
			}
		
			n.setTravelTime(Integer.MAX_VALUE);
			n.setParent(null);
			n.setParentEdge(null);
//			n.setPathUptoPreviousNode(null);
			priorityQueue.remove(n);
		}
		else
		{
			/*Resetting scanned node */
			if(n.getChildList() != null)
			{
				Iterator<Node> childListIterator = n.getChildList().iterator();
				while(childListIterator.hasNext())
				{
					Node v = childListIterator.next();
					resetNodes(v, priorityQueue);
				}
			}
			if(n.getAdjacentScannedList() != null)
			{
				Iterator<Node> adjacentScannedListIterator = n.getAdjacentScannedList().keySet().iterator();
				while(adjacentScannedListIterator.hasNext())
				{
					Node u = adjacentScannedListIterator.next();
					PQAdditionList.add(u);
					u.getAdjacentScannedList().remove(n);
					n.getAdjacentScannedList().remove(u);
				}
			}
			n.setScanned(false);
			n.setChildList(null);
			n.setParent(null);
			n.setParentEdge(null);
//			n.setPathUptoPreviousNode(null);
			n.setTravelTime(Integer.MAX_VALUE);
			PQAdditionList.remove(n);
		}
	}
	
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
		bw.write(routeDetails1 + routeDetails2 + "\n");
		destination.setCurrentOccupancy(destination.getCurrentOccupancy() - groupSize);
		
		//Add path to pathList
		Route.addTotalHops(hops);
		if(Route.getMaxHops() < hops)
			Route.setMaxHops(hops);
		//route.displayRoute2();
		distinctRoutes.add(routeDetails2);
		this.pathId++;
	}
	
	/*
	 * This will first reserve path from second node upto last edge
	 * Then it will increase capacity of source at departure time from the source node
	 * Then it will decrease occupancy of source node
	 * Add route to pathList
	 * Display the route
	 */
	/*public void reservePath(Node destination, int groupSize)
	{
		PathUptoNode pathUptoNode = destination.getPathUptoPreviousNode();
		int noOfNodes = pathUptoNode.getNodeList().size();
		for(int n=0; n<noOfNodes; n++)
		{
			Node tempNode = pathUptoNode.getNodeList().get(n);
			Edge tempEdge = pathUptoNode.getEdgeList().get(n);
			
			int depart = pathUptoNode.getDepartureTime().get(n);
			int edgeTravelTime = (int)Math.ceil(tempEdge.getTravelTime());
			boolean sameFlowDirection = false;
			
			if(tempNode == tempEdge.getSource())
				sameFlowDirection = true;
			//Edge reservation
			int newCapacity = 0;
			
			if(sameFlowDirection)
			{
				newCapacity = tempEdge.getEdgeCapacity().get(depart) - groupSize;
				tempEdge.getEdgeCapacity().set(depart, newCapacity);
			}
			else
			{
				newCapacity = tempEdge.getEdgeCapacity().get(depart + edgeTravelTime) - groupSize;
				tempEdge.getEdgeCapacity().set(depart + edgeTravelTime, newCapacity);
			}
			//For eg : Departure time at 15, travel time = 5, Hence slot 'time' should be 
			//booked at time 15 + time(0,1,2,3,4) = (15,16,17,18,19)
			
			Node currNode = pathUptoNode.getNodeList().get(n);
			currNode.setNoOfPathsThroughThisNode(currNode.getNoOfPathsThroughThisNode() + 1);
			currNode.setNoOfPeopleThroughThisNode(currNode.getNoOfPeopleThroughThisNode() + groupSize);
			
			int currNodeArrival = 0;
			if(n!=0)
				currNodeArrival = (int)currNode.getTravelTime();
			
			int departure = pathUptoNode.getDepartureTime().get(n);
			currNode.setWaitingTimeAtThisNode(departure-currNodeArrival);
			
			for(int i=currNodeArrival;i<departure;i++)
			{
				int newNodeCapacity = tempNode.getNodeCapacityAtTime().get(i) - groupSize;
				tempNode.getNodeCapacityAtTime().set(i, newNodeCapacity);
			}
		}
		
		Node src = destination.getPathUptoPreviousNode().getNodeList().get(0);
		src.setCurrentOccupancy(src.getCurrentOccupancy() - groupSize);
		
		//Add path to pathList
		Route route = new Route();
		route.setRouteId(pathId);
		route.setGroupSize(groupSize);
		route.getArrivalTime().add(0);	//Source node arrival time
		for(int index = 0; index < pathUptoNode.getNodeList().size(); index++)
		{
			Node temp = pathUptoNode.getNodeList().get(index);
			route.getRouteNodeList().add(temp);
			if(index != 0)
				route.getArrivalTime().add((int)Math.ceil(temp.getTravelTime()));
			
			Edge tempEdge = pathUptoNode.getEdgeList().get(index);
			route.getRouteEdgeList().add(tempEdge);
			
			route.getDepartureTime().add(pathUptoNode.getDepartureTime().get(index));
		}
		route.getRouteNodeList().add(destination);
		route.getArrivalTime().add((int)Math.ceil(destination.getTravelTime()));
		route.getDepartureTime().add(-1);  //Destination departure time
		this.pathList.add(route);
		
		Route.addTotalHops(route.getRouteNodeList().size());
		if(Route.getMaxHops() < route.getRouteNodeList().size())
			Route.setMaxHops(route.getRouteNodeList().size());
		
		if(destination.getTravelTime() > Route.getMaxLength())
			Route.setMaxLength(destination.getTravelTime());
		
		//route.displayRoute2();
		distinctRoutes.add(route.getRouteString());
		this.pathId++;
	}*/
	public MinimumCapacityAndEdge findMinimumCapacityAndEdge(Node destination) throws Exception
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
			
			if(newCapacity <= edgeCapacity)
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
			if(edgeCapacity <= minCapacity && edgeCapacity < nodeCapacity)
			{
				minCapacity = edgeCapacity;
				minCutEdge = parentEdge;
				nodeToReset = destination;
			}
			else if(nodeCapacity <= minCapacity && nodeCapacity <= edgeCapacity)
			{
				minCapacity = nodeCapacity;
				minCutEdge = parentEdge;
				nodeToReset = parentNode;
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

		int timeInstancesToAdd = departureTimeFromU - edge.getEdgeCapacity().size() + 2;

		
		for(int i = 0; i < timeInstancesToAdd; i++)
			edge.addEdgeCapacity();
		
		//while the capacity in first section of this edge is not available
		//we will wait here only
		int delay = 0;
		while(edge.getEdgeCapacity().get(departureTimeFromU) <= 0)
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
		while((v.getNodeCapacityAtTime().get(distanceToVThroughU) <= 0)
				|| edge.getEdgeCapacity().get(departureTimeFromU) <= 0)
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
		    writer = new FileWriter("/Users/MLGupta/Documents/ModifiedNodeDataKr2.csv");
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
		    writer = new FileWriter("/Users/MLGupta/Documents/ModifiedRouteListKr2.csv");
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