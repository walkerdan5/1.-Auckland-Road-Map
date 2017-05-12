import java.util.HashSet;

public class Road {

	private int roadID;
	private int type;
	private String label;
	public String city;
	public int oneway;
	public int speed;
	public int roadClass;
	public int notForCar;
	public int notForPede;
	public int notForBicy;
	private HashSet<Segment> segments = new HashSet<Segment>();



	public Road(int roadID, int type, String label, String city, int oneway, int speed, int roadClass, int notForCar,int notForPde, int notForBicy ){
		
		this.roadID = roadID;
		this.type = type;
		this.label = label;
		this.city = city;
		this.oneway = oneway;
		this.speed = speed;
		this.roadClass = roadClass;
		this.notForCar = notForCar;
		this.notForPede = notForPde;
		this.notForBicy = notForBicy;
	}
	
	public void addSeg(Segment s){ segments.add(s);}
	public int getID(){ return this.roadID; }
	public String getLabel() { return this.label; }
	public HashSet<Segment> getSeg() { return this.segments; }
	public int getType(){return this.type;}
	
}