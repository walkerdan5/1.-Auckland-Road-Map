import java.util.Comparator;

public class queueSetup implements Comparator<queueSetup>, Comparable<queueSetup> {
	public Node node;
	public queueSetup setupFrom;
	public Segment segmentFrom;
	public Node from;
	public double costToHere;
	public double estimateTotal;
	
	public queueSetup(Node node, Node from, double cost, double estTotal, queueSetup setupFrom, Segment s){
		this.node = node;
		this.from = from;
		this.setupFrom = setupFrom;
		this.segmentFrom = s;
		this.costToHere = cost;
		this.estimateTotal = estTotal;	
	}	



	// used for priority queue comparator
	@Override
	public int compareTo(queueSetup o) {
		return  (int) (this.estimateTotal - o.estimateTotal);
	}
	
	// used for priority queue comparator
	@Override
	public int compare(queueSetup a, queueSetup b) {
		return (int) (a.estimateTotal - b.estimateTotal);
	}

}
