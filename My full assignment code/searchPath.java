import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class searchPath {

	public static Set<Node> nodes;

	public searchPath(Map<Integer, Node> oldNodes){
		nodes = new HashSet<Node>();

		for(Node n : oldNodes.values())
			nodes.add(n);	

		initilize();
	}


	/** 
	 * Initializes all nodes to be ready for a path search
	 */
	public static void initilize(){

		for(Node n : nodes){ 
			n.visited = false;
			n.pathFrom = null;
			n.cost = 0;
		}
	}

	// next i have to calculate start node and goal node using A* search
	
		// TODO Auto-generated method stub
		/** 
		 * Calculates the shortest path between two different nodes using A* search
		 * @param start
		 * @param goal
		 * @return shortest distance
		 */
		public double calculateDistance(Node start, Node goal){

			initilize();

			// declare priority queue (fringe) of nodes (by total cost to goal)
			PriorityQueue<queueSetup> fringe = new PriorityQueue<queueSetup>();	

			// add initial node (start, null, 0, estimate(start,goal)
			queueSetup initial = new queueSetup(start, null, 0.0, estimate(start, goal), null, null);
			fringe.add(initial);

			//while fringe != null
			while(!fringe.isEmpty()){

				// dequeue 1st one
				queueSetup queueStruct = (queueSetup) fringe.poll();

				Node node = queueStruct.node;
				queueSetup structFrom = queueStruct;
				Node lastNodeFrom = queueStruct.from;
				structFrom.node = lastNodeFrom;


				if(!node.visited){

					// node.visited ←true, node.pathFrom←from, node.cost←costToHere
					node.visited = true;
					node.pathFrom = queueStruct.from;
					node.cost = queueStruct.costToHere;

					// if node = goal then exit			
					if(node.equals(goal)){
						pathTaken(queueStruct);
						return node.cost;
					}

					//for each edge to neigh out of node
					for(Segment s : node.getSegment()){
						int otherNodeID  = s.getOtherNodeId(node.getID());
						Node neighbour  = Application.getNode(otherNodeID);
						Road segmentRoad = Application.getRoad(s.getID());
						boolean oneWayRoad = segmentRoad.oneway == 1 ? true : false;

						// if not neigh.visited then
						if(!neighbour.visited){

							boolean okPath = true;
							
							if(okPath){
								
							}
								if( (oneWayRoad && s.node1 == otherNodeID) || !oneWayRoad ){ // is the segment one way?

								// costToNeigh ← costToHere + edge.weight
								double costToNeigh = queueStruct.costToHere + s.length;

								// estTotal ← costToNeigh + estimate(neighbour, goal)
								double estTotal = costToNeigh + estimate(neighbour, goal);

								// fringe.enqueue(Neighbor, node, costToNeigh, estTotal)

								
								fringe.add(new queueSetup(neighbour, node, costToNeigh, estTotal, structFrom, s));
								}
							}
						}
					} 
				}
			
		


	return -1; // no path was found
	}


		private void pathTaken(queueSetup queueStruct) {
			// TODO Auto-generated method stub
			
		}


		private double estimate(Node start, Node goal) {
			// TODO Auto-generated method stub
			return 0;
		}
		
	
	
	
	
	
	
}

