package Graph;

import java.util.ArrayList;

public class Edge {

	private Node source;
	private Node target;
	private int travelTime;
	private int maxIntakeCapacity;
	private String edgeName;
	private int edgeID;
	private int edgeUsage;
	private ArrayList<Integer> edgeCapacity;

	
	public ArrayList<Integer> getEdgeCapacity() {
		return edgeCapacity;
	}

	public void setEdgeCapacity(ArrayList<Integer> edgeCapacity) {
		this.edgeCapacity = edgeCapacity;
	}

	public void addEdgeCapacity() {
		this.edgeCapacity.add(maxIntakeCapacity);
	}
	
	public Node getSource() {
		return source;
	}

	public void setSource(Node source) {
		this.source = source;
	}

	public int getEdgeUsage() {
		return edgeUsage;
	}

	public void setEdgeUsage(int edgeUsage) {
		this.edgeUsage = edgeUsage;
	}

	public Node getTarget() {
		return target;
	}

	public void setTarget(Node target) {
		this.target = target;
	}
	
	public int getTravelTime() {
		return travelTime;
	}

	public void setTravelTime(int travelTime) {
		this.travelTime = travelTime;
	}

	public int getMaxIntakeCapacity() {
		return maxIntakeCapacity;
	}

	public void setMaxIntakeCapacity(int maxIntakeCapacity) {
		this.maxIntakeCapacity = maxIntakeCapacity;
	}

	public String getEdgeName() {
		return edgeName;
	}

	public void setEdgeName(String edgeName) {
		this.edgeName = edgeName;
	}

	public int getEdgeID() {
		return edgeID;
	}

	public void setEdgeID(int edgeID) {
		this.edgeID = edgeID;
	}
}