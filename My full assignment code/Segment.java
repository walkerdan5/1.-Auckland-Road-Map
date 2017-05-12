import java.util.ArrayList;

public class Segment{
	
	int roadID;
	double length;
	int node1;
	int node2;
	public Road road;
	public ArrayList<Location> locations = new ArrayList<Location>();
	
	
	
	public Segment(int id, double len, int n1, int n2, Road rd, ArrayList<Double> segLocation){
		
		this.roadID = id;
		this.length = len;
		this.node1 = n1;
		this.node2 = n2;
		for(int i = 1; i<segLocation.size(); i+=2){
			double lat = segLocation.get(i-1);
			double lon = segLocation.get(i);
			Location tempLoc = Location.newFromLatLon(lat, lon);
			locations.add(tempLoc);
		}
		road = rd;		
				
		
	}
	
	//getter and setter methods
	
	public int getID() {return this.roadID;}
	public double getLength() {return this.length;}
	public int getNode1() {return this.node1;}
	public int getNode2() {return this.node2;}
	public ArrayList<Location> getLocation(){ return this.locations;}
	public int getOtherNodeId(int id){
		return this.node1 == id ? node2 : node1;
	}
	public boolean containsBothNodes(int node1, int node2){
		return (this.node1 == node1 && this.node2 == node2) || (this.node1 == node2 && this.node2 == node1);
	}
	
			
	}

