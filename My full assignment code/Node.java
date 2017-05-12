import java.util.HashSet;
import java.util.Set;

public class Node {

	
	private double nodeX;
	private double nodeY;
	private Location nodeLocation;
	private int id;
	private Set<Segment> segments = new HashSet<Segment>();
	private Set<Node> neighbours = new HashSet<Node>();
	public static final int MAX_DEPTH = Integer.MAX_VALUE;
	
	
	public Node(int id, double nodeX, double nodeY){
		
		this.id = id;
		this.nodeX = nodeX;
		this.nodeY = nodeY;
		this.pathFrom = null;
		this.visited = false;
		this.cost = 0;
		nodeLocation = Location.newFromLatLon(nodeX, nodeY);	
		
	}
	
	public double getNodeX(){return this.nodeX;}
	
	public double getNodeY(){return this.nodeY;}
	
	public Location getNodeLocation(){return this.nodeLocation;}
	
	public int getID(){return this.id;}
	
	public void setLoc(Location l){this.nodeLocation = l;}
	
	public void addSeg(Segment s){segments.add(s);}
	
	public Set<Segment> getSegment(){return this.segments;}
	
	public Set<Node> getNeighbours() {return this.neighbours;}
	
	public void addNeighbour(Node neighbour) { neighbours.add(neighbour);}
	
	
	public void setDepth(int depth){this.depth = depth;}

	
	
	//path finder here
	
	
	
	
//fields for articulation

	public boolean visited;
	public Node pathFrom;
	public double cost;
	public int depth;
	public int numSubtrees;
	public static int visitedNodes;


	

}
