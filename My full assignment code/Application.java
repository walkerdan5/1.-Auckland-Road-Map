import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;




/**
 * This is a small example class to demonstrate extending the GUI class and
 * implementing the abstract methods. Instead of doing anything maps-related, it
 * draws some squares to the drawing area which are removed when clicked. Some
 * information is given in the text area, and pressing any of the navigation
 * buttons makes a new set of squares.
 * 
 * @author Daniel Walker
 */
public class Application extends GUI {

	public static Map<Integer,Node> nodes = new HashMap<Integer,Node>();
	private static Map<Integer,Road> roads = new HashMap<Integer,Road>();
	public static List<Segment> segments = new ArrayList<Segment>();
	private List<Polygon> polygons = new ArrayList<Polygon>();
	private List<Set<Segment>> selectedSeg = new ArrayList<Set<Segment>>(); // roads to highlight from search
	public static HashSet<Segment> selectedPath = new HashSet<Segment>(); // roads to highlight for path finding
	private Location origin = Location.newFromLatLon(-36.847622 , 174.763444); // Center of Auckland location
	private Trie trie;
	public searchPath searchPath;
	private boolean awaitingClick = false; // used for map directions
	private Articulation aPoints; // used for finding the articulation points

	private boolean drawArticulationClicked = false;
	public Node transitOne;
	public Node transitTwo;
	private double scale = 20; //zoom function


	public Application() {

	}

	/**
	 * Redraws the graphics
	 */
	@Override
	protected void redraw(Graphics g1D){
		Graphics2D g = (Graphics2D) g1D;
		drawPolygons(g);
		drawSeg(g);
		drawPathSegments(g);
		drawSearchSegments(g);
		if(drawArticulationClicked)
			drawArticulation(g);
	}

	/**
	 * Draw the segments
	 */

	public void drawSeg(Graphics g){
		g.setColor(Color.red);
		float size = (float) (scale/100.0);
		((Graphics2D) g).setStroke(new BasicStroke(size));

		for (Segment s : segments){
			ArrayList<Location> segLocation = s.getLocation();
			for(int i = 1; i < segLocation.size(); i++){

				Location first = segLocation.get(i-1);
				Location second = segLocation.get(i);
				Point p1 = first.asPoint(origin, scale);
				Point p2 = second.asPoint(origin, scale);

				switch(s.road.getType()){
				case 1 : g.setColor(new Color(105, 105,105)); break; // highway
				case 6 : g.setColor(new Color(185, 175,175)); break; // road
				case 9 : g.setColor(new Color(105, 105,105)); break; // highway
				case 22 : g.setColor(new Color(255, 255,255)); break; //walkway
				default : g.setColor(new Color(0,0,255)); break;


				}
				g.drawLine((int)p1.getX(), (int)p1.getY(), (int)p2.getX(), (int)p2.getY());
			}
		}
	}

	/**
	 * Draw Polygons (water, parks etc)
	 */

	public void drawPolygons(Graphics2D g){
		for(Polygon p : polygons){
			g.setColor(Color.DARK_GRAY);
			List<Location> locations = p.getCo();
			int [] x = new int [locations.size()];
			int [] y = new int [locations.size()];
			int ArraySize = 0;

			for(Location l : locations){
				Point pointOfLoc = l.asPoint(origin , scale);
				x[ArraySize] = (int) pointOfLoc.getX();
				y[ArraySize] = (int) pointOfLoc.getY();
				ArraySize++;

			}
			String polygonShapes = p.getType();
			Color col = null;
			switch(polygonShapes){
			case "Type=0x28": col = new Color(174,209,255); break; //water
			case "Type=0x3c" : col = new Color(50,50,255); break;  // lake 
			case "Type=0x3d" : col = new Color(50,50,255); break;  // lake 
			case "Type=0x3e" : col = new Color(50,50,255); break;  // lake 
			case "Type=0x29" : col = new Color(50,50,255); break; // lake 
			case "Type=0x32" : col = new Color(50,50,255); break;  // lake 
			case "Type=0x16" : col = new Color(202,223,255); break; // forest				
			case "Type=0x17" : col = new Color(202,223,170); break; // reserve	
			case "Type=0xe" : col = new Color(211,202,189); break; // airstrip
			case "Type=0x7" : col = new Color(223,219,212); break;  // terminal
			case "Type=0x18" : col = new Color(202,218,167); break;  // golf course 
			case "Type=0x14" : col = new Color(239,210,224); break;  // national park	
			case "Type=0x15" : col = new Color(239,210,224); break;  // national park
			case "Type=0x47" : col = new Color(0,0,0); break;  // auckland 

			default :  col = new Color(202,200,170); break;
			}
			g.setColor(col);	
			g.fillPolygon(x,y,locations.size());
		}
	}

	/** 
	 * Draws the segment(s) that have been searched in a thick red colour
	 * @param g
	 */	
	public void drawSearchSegments(Graphics2D g){
		g.setColor(Color.red);
		g.setStroke(new BasicStroke(3));
		for (Set<Segment> setOfSeg : selectedSeg) {
			for(Segment s1 : setOfSeg){
				ArrayList<Location> segLoc1 = s1.getLocation();
				for(int i = 1; i < segLoc1.size(); i++){
					Location one = segLoc1.get(i-1);
					Location two = segLoc1.get(i);
					Point p1 = one.asPoint(origin, scale);
					Point p2 = two.asPoint(origin, scale);
					g.drawLine((int)p1.getX(), (int)p1.getY(), (int)p2.getX(), (int)p2.getY());
				} 
			} 
		}
	}



	@Override
	protected void onMove(Move m) {
		double shift = 35/scale;	//how far the map moves with each click

		switch(m){
		case NORTH: origin = origin.moveBy(0,shift); break;
		case SOUTH: origin = origin.moveBy(0, -shift); break; 
		case EAST: origin = origin.moveBy(shift, 0); break; 
		case WEST: origin = origin.moveBy(-shift, 0); break; 
		case ZOOM_IN: scale = scale * 1.5 ; break;  
		case ZOOM_OUT: scale = scale / 1.5; break; 
		}

	}

	//@return String[] of roadNames

	@Override
	protected String[] onSearch() {
		selectedSeg.clear();
		String inputText = getSearchBox();
		String[] results =  trie.getWord(inputText);
		ArrayList<String> selectedRoadName = new ArrayList<String>();

		for(int i = 0; i < results.length; i ++) // adds from array to array list
			selectedRoadName.add(results[i]);

		for(Map.Entry<Integer, Road> entry : roads.entrySet()){
			if(selectedRoadName.contains(entry.getValue().getLabel() ))
				selectedSeg.add(entry.getValue().getSeg());
		}

		getTextOutputArea().setText(inputText + " successfully found");
		return results;

	}	

	/** 
	 * Loads the nodes, roads, segments, polygons
	 */
	@Override
	protected void onLoad(File nodesFile, File roadsFile, File segmentsFile, File polygonsFile)  {

		loadNodes(nodesFile);
		loadRoads(roadsFile);
		loadSegments(segmentsFile);
		loadTrie();
		loadPoly(polygonsFile);
		addNodeNeighbours();


		getTextOutputArea().append("Preparing to index the path finding algorithm \n"); 
		searchPath = new searchPath(nodes);
		getTextOutputArea().append("Path finding indexed successfully \n"); 

		getTextOutputArea().setText("Sucessfully loaded \n"); 


	}

	/**
	 * Loads the road intersections
	 * @param nodesFile
	 */
	public void loadNodes(File nodesFile){
		BufferedReader data;
		try{
			String line = null;
			data = new BufferedReader(new FileReader(nodesFile));
			line = data.readLine();
			while(line != null){
				String[] values = line.split("\t");
				int nodeID = Integer.parseInt(values[0]);
				double lat = Double.parseDouble(values[1]);
				double lng = Double.parseDouble(values[2]);
				Node node = new Node(nodeID, lat, lng);
				Application.nodes.put(nodeID, node);
				line = data.readLine();

			}
			data.close();
			getTextOutputArea().append("Loaded intersections sucesssfully \n"); 

		}
		catch (FileNotFoundException e) {e.printStackTrace();}

		catch (IOException e){e.printStackTrace();}

	}

	/**
	 * Loads the main roads (not segments)
	 * @param roadsFile
	 */
	public void loadRoads(File roadsFile){
		getTextOutputArea().append("Loading roads \n"); 
		try {
			String line = null;
			BufferedReader data = new BufferedReader( new FileReader(roadsFile));
			line = data.readLine(); // skips file names

			while((line = data.readLine())!=null){


				String[] values = line.split("\t");
				int roadID = (int) Integer.parseInt(values[0]);
				int type = Integer.parseInt(values[1]);
				String label = values[2];
				String city = values [3];
				int oneWay = Integer.parseInt(values[4]);
				int speed = Integer.parseInt(values[5]);
				int roadClass = Integer.parseInt(values[6]);
				int notForCar = Integer.parseInt(values[7]);
				int notForPde = Integer.parseInt(values[8]);
				int notForBicy = Integer.parseInt(values[9]);
				//adds the road to Road Class
				Road road = new Road(roadID, type, label, city, oneWay, speed, 
						roadClass, notForCar, notForPde, notForBicy );
				roads.put(roadID, road);
			}
			data.close();
			getTextOutputArea().append("Loaded roads sucesssfully \n");
		}
		catch (FileNotFoundException e){e.printStackTrace();}
		catch (IOException e){e.printStackTrace();}
	}



	/**
	 * Loads road segments
	 * @param segmentsFile
	 */
	public void loadSegments(File segmentsFile){
		try{
			getTextOutputArea().append("Loading segments \n"); 
			String line = null;
			BufferedReader data = new BufferedReader(new FileReader(segmentsFile));
			line = data.readLine(); //skips filename

			while((line = data.readLine()) != null){


				String[] values = line.split("\t");
				int roadID = Integer.parseInt(values[0]);
				double len = Double.parseDouble(values[1]);
				int nodeOne = Integer.parseInt(values[2]);
				int nodeTwo = Integer.parseInt(values[3]);
				ArrayList<Double> segLocation = new ArrayList<Double>(); // co-ordinates of segments
				int count = 4; //sets the count to be the four parameters required for the segments

				while(count < values.length){ //stops loop once the four parameters are added
					segLocation.add( (Double) Double.parseDouble(values[count]));
					count++;
				}

				Road rd = null;
				for(Map.Entry<Integer, Road> entry : roads.entrySet()){
					if(entry.getKey() == roadID){
						rd = entry.getValue();
						break;
					}
				}
				// adds a new segment to the segment class
				Segment newSegment = new Segment(roadID, len, nodeOne, nodeTwo, rd, segLocation);
				rd.addSeg(newSegment);
				segments.add(newSegment); //creates a new segment to add a segment to

				for (Map.Entry<Integer, Road> entry : roads.entrySet()) {  // checks for matching roadID and adds the segment to the corresponding road
					if(entry.getKey() == roadID){
						entry.getValue().addSeg(newSegment);
						break;
					}
				}

				int addedBoth = 0;
				for (Map.Entry<Integer, Node> entry : nodes.entrySet()) {   // adds the segments to the appropriate nodes
					if(entry.getKey() == nodeOne){ // checks for matching Node 1
						entry.getValue().addSeg(newSegment);
						addedBoth++;
					}
					if(entry.getKey() == nodeTwo){ // checks for matching Node 2
						entry.getValue().addSeg(newSegment);
						addedBoth++;
					}
					if(addedBoth == 2)
						break;
				}
			}
			data.close();
			getTextOutputArea().append("Loaded segments sucesssfully\n"); 
		}
		catch(FileNotFoundException e ){e.printStackTrace();}
		catch(IOException e){e.printStackTrace();}

	}


	/** 
	 * Displays the connecting nodes from the parameters.
	 * @param closestNode
	 */
	public void connectedRoads(Node closestNode){
		Set<Segment> segments = closestNode.getSegment(); // gets segments related to that node
		Set<Integer> roadIDs = new HashSet<Integer>(); // for the road id's related to that segment
		String printText = "Connecting roads are: "; // text that will display connecting roads
		for(Segment s : segments)
			roadIDs.add(s.getID()); // fills the set roadIDs with the relevant road ID


		Set<String> labels = new HashSet<String>();
		for (Map.Entry<Integer, Road> entry : roads.entrySet() ) {  // checks for matching roadID
			if(roadIDs.contains(entry.getKey() )){
				labels.add(entry.getValue().getLabel());
				printText += entry.getValue().getLabel() + ",  ";
			}
		}
		printText = printText.substring(0, printText.length() - 3);

		getTextOutputArea().setText(printText);

	}






	/** 
	 * Abstract method declared in GUI.
	 * Called on the event of a click, delegates the work to the onMove method.
	 * @param e
	 */
	@Override
	protected void onClick(MouseEvent e) {

		double closestDistance = Double.MAX_VALUE;
		Node closestNode = null;
		Location click = Location.newFromPoint(new Point(e.getX() , e.getY()), origin, scale);

		for (Map.Entry<Integer, Node> entry : nodes.entrySet()) {  
			Node tempNode = entry.getValue();
			Location tempNodeLoc = tempNode.getNodeLocation(); 	
			if(tempNodeLoc.distance(click) < closestDistance){ // if its closer from the current node compared to the current shortest
				closestDistance = tempNodeLoc.distance(click);
				closestNode = tempNode;				
			}
		}

		if(closestNode == null) 
			getTextOutputArea().setText("No intersections found here."); 

		if(awaitingClick){
			transit(closestNode);
		}
		else{
			connectedRoads(closestNode);
		}

	}

	/** 
	 * Loads the polygon data (grass/water shapes on the map)
	 * @param polygonsFile
	 */
	public void loadPoly(File polygonsFile){
		getTextOutputArea().append("Loading objects file \n"); 
		try {
			@SuppressWarnings("unused")
			String line = null;

			BufferedReader br = new BufferedReader(new FileReader(polygonsFile));

			while ((line = br.readLine()) != null) {
				//initialise polygon data points 
				String endLevel = "";
				String label = "";
				String cityID = "";
				String polygonData = "";
				boolean hasData = false;

				ArrayList<Location> coords = new ArrayList<Location>();

				String type = br.readLine();
				if(!type.startsWith("Type=")){ //if string does not start with type, it must be label
					label = type; //assigns label to equal the read string 
					type = "";
				}

				label = br.readLine();
				if(!label.startsWith("Label=")){
					endLevel = label;
					label = "";
				}

				endLevel = br.readLine();
				if(!endLevel.startsWith("EndLevel=")){
					cityID = endLevel;
					endLevel = "";

				}
				cityID = br.readLine();
				if(!cityID.startsWith("CityIdx")){
					polygonData = cityID;
					cityID = "";
				}
				else{
					polygonData = br.readLine();
				}

				if(polygonData.startsWith("Data"))
					hasData = true;

				if(hasData){

					polygonData = polygonData.substring(6, polygonData.length()-1);  // removes the "Data=" string
					String[] co = polygonData.split(","); 
					for(int i = 0; i < co.length; i++){
						if(co[i].startsWith("(")) 
							co[i] = co[i].substring(1, co[i].length()-1); // removes open bracket
						else if(co[i].endsWith(")"))
							co[i] = co[i].substring(0, co[i].length()-2); // removes closed bracket
					}

					for(int i = 1; i < co.length; i+=2){ // gets the co ordinates as doubles from co
						double one = Double.parseDouble(co[i-1]);
						double two = Double.parseDouble(co[i]);
						Location l = Location.newFromLatLon(one, two);
						coords.add(l);
					}
				}

				String end = br.readLine();
				while(true){ // skips [end] string
					if(end.compareTo("[END]") == 0)
						break;
					end = br.readLine();
				}	

				br.readLine(); // removes whitespace between chunks of poly data
				polygons.add(new Polygon(type, endLevel, cityID, coords)); // creates the new polygon, adds it to list of polygons					
			}
			br.close();
			getTextOutputArea().append("Loaded objects sucesssfully \n"); 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}	

	}


	/**
	 * Indexes the search
	 */
	public void loadTrie(){
		getTextOutputArea().append("Loading search tree\n"); 
		trie = new Trie(); // global variable declared in header
		for (Map.Entry<Integer, Road> entry : roads.entrySet()) {
			String roadName = entry.getValue().getLabel();
			trie.addWord(roadName);
		}
		getTextOutputArea().append("Search Tree loaded sucessfully \n"); 

	}

	/**
	 * @return
	 */
	public void lookForArticulation(){
		if(aPoints == null){
			aPoints = new Articulation(); //create new articulation object
			aPoints.searchForPoints(nodes.values()); //call articulation search method
			getTextOutputArea().append("Displaying all Articulation points in red dots");
		}
		drawArticulationClicked = !drawArticulationClicked;
		redraw();
	}

	/**
	 * Draw Articulation Points
	 */
	public void drawArticulation(Graphics g){
		g.setColor(Color.RED); // colour of the art points 

		if(drawArticulationClicked){ //draw articulation button
			getTextOutputArea().append("\n" + "Draw Called");
			System.out.println(aPoints.getArticulationPoints());
			for(Node node : aPoints.getArticulationPoints()){
				Location nodeLoc = node.getNodeLocation();
				Point p = nodeLoc.asPoint(origin, scale);
				g.drawOval((int) p.getX(), (int) p.getY(),3,3);
			}
		}
	}


	/**
	 * looks through all segments and adds neighbours to neighbour set
	 */
	public void addNodeNeighbours(){
		for(Map.Entry<Integer, Node> entry : nodes.entrySet()){
			Node node = entry.getValue();
			for (Segment s : node.getSegment()){
				int otherID = s.getOtherNodeId(node.getID());
				Node nextNeighbourNode = getNode(otherID);
				node.addNeighbour(nextNeighbourNode);

			}
		}

	}

	/** 
	 * Calculates the shortest from one intersection to another
	 */
	public void calculateShortestPath() {
		double dist = -2; // shortest path using the roads
		double direct = -2; // direct path from first click to second click

		if(transitOne != null && transitTwo != null){
			dist = searchPath.calculateDistance(transitOne, transitTwo);
			direct = transitOne.getNodeLocation().distance(transitTwo.getNodeLocation());



			getTextOutputArea().append("\nThe shortest path is " + dist + " km.");
			getTextOutputArea().append("\nIn a direct line the path is " + direct + " km.");
		}
	}


	/** 
	 * Draws each individual segment of the roads involved on the selected path to a destination in a highlighted colour.
	 * @param g
	 */		
	public void drawPathSegments(Graphics2D g){
		g.setColor(Color.red);
		g.setStroke(new BasicStroke(3));
		for (Segment s : selectedPath) {
			ArrayList<Location> segLoc = s.getLocation();
			for(int i = 1; i < segLoc.size(); i++){
				Location one = segLoc.get(i-1);
				Location two = segLoc.get(i);
				Point p1 = one.asPoint(origin, scale);
				Point p2 = two.asPoint(origin, scale);
				g.drawLine((int)p1.getX(), (int)p1.getY(), (int)p2.getX(), (int)p2.getY());
			} 
		}
	}


	/** 
	 * Used for the transit between the two nodes selected 
	 * @param closestNode
	 */
	public void transit(Node closestNode){

		if(transitOne == null && transitTwo == null){
			transitOne = closestNode;
			getTextOutputArea().setText("Start Point is: " + closestNode.getID());
		}
		else if(transitOne != null && transitTwo == null){
			transitTwo = closestNode;
			getTextOutputArea().setText("End Point is: " + closestNode.getID());
			calculateShortestPath();

			String pathTaken = "";
			double totalLength = 0;

			Set<Integer> roadIdsOnPath = new HashSet<Integer>();

			for(Segment s : selectedPath){

				Road road = getRoad(s.roadID);

				if(!roadIdsOnPath.contains(road.getID())) // prevents duplicates
					pathTaken += "\n" + road.getLabel() + " ( " +  s.length*1000 + " m )";

				roadIdsOnPath.add(road.getID());
				totalLength += s.length;
			}

			
			getTextOutputArea().append(pathTaken);

		}

	}


	/**
	 * Used to toggle whether the user wants transit instructions or not
	 */
	@Override
	protected void directions(){
		transitOne = transitTwo = null;
		if(awaitingClick){
			selectedPath.clear();
			getTextOutputArea().setText("");
		}

		this.awaitingClick = !this.awaitingClick;
	}


	/**
	 * @param id
	 * @return the corresponding Node to the id
	 */
	public static Node getNode(int id) {

		return nodes.get(id);
	}


	/**
	 * @param id
	 * @return the corresponding Road to the id
	 */
	public static Road getRoad(int id){		
		return roads.get(id);			
	}


	public static void main(String[] args) {
		new Application();
	}



}

// code for COMP261 assignments
