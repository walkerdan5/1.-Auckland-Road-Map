import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;


public class Articulation {

	private Set<Node> articulationPoints;
	//private Set<Node> unvisitedNodes;
	private Stack<StackSetup> stack = new Stack<StackSetup>();
	public int numberOfPoints;


	public Articulation(){
	}

	public Set<Node> searchForPoints(Collection<Node> nodes){

		initialise(nodes); // gives nodes neighbours

		this.articulationPoints = new HashSet<>();
		//this.unvisitedNodes = new HashSet<>(nodes);

		int numSubtrees = 0;
		Node startNode = nodes.iterator().next();
		startNode.depth = 0;

		// where the recursive method is called

		for(Node neigh : startNode.getNeighbours()){
			if(neigh.depth == Node.MAX_DEPTH){
				recursePoints(neigh,1, startNode);
				numSubtrees++;
			}	
		}

		if(numSubtrees>1){
			articulationPoints.add(startNode);
		}	

		//unvisitedNodes.remove(startNode);



		return articulationPoints;
	}

//
	private void initialise(Collection<Node> nodes) {
		for(Node node : nodes){
			node.setDepth(Node.MAX_DEPTH);
		}
	}


	/**
	 * Recusive method for finding articulation points
	 * @param node
	 * @param depth
	 * @param from
	 * @param artPoints
	 * @param unvisitedNodes
	 * @return
	 */
	private int recursePoints(Node node, int depth, Node from){
		node.depth = depth;
		int reachBack = depth;

		for(Node neigh : node.getNeighbours()){
			// Skip from node
			if(neigh.getNodeLocation() == from.getNodeLocation()){
				continue;
			}

			// If it has been visited, set new reach back
			if(neigh.depth < Node.MAX_DEPTH){
				reachBack = Math.min(reachBack, neigh.depth);
			} 
			else{
				int childReach = recursePoints(neigh, depth+1, node);
				if(childReach >= depth){
					articulationPoints.add(node);
				}
				reachBack = Math.min(reachBack, childReach);
			}
			//unvisitedNodes.remove(neigh);
		}
		return reachBack;

	}


	/**
	 * iterative version of finding articulation points
	 * @param firstNode
	 * @param root
	 */
	public void iteratePoints(Node firstNode, Node root){
		stack.push(new StackSetup(firstNode, 1, new StackSetup(root, 0, null)));

		while (!stack.empty()){
			//peek at first node
			StackSetup look = stack.peek();
			Node node = look.node;

			if(look.children == null){
				node.depth = look.depth;
				look.reach = look.depth;
				look.children = new LinkedList<Node>();


				for(Node neigh : node.getNeighbours()){
					if(!neigh.equals(look.parent.node)){
						//add neighbour to look.children
						look.children.add(neigh);
					}
				}
			}

			else if(!look.children.isEmpty()){
				Node child = look.children.poll();
				if(child.depth<Integer.MAX_VALUE){
					look.reach = Math.min(look.reach, child.depth);
				}
				else{
					stack.push( new StackSetup(child, node.depth +1, look));
				}
			}

			else{
				if(node != firstNode){
					if(look.reach >= look.parent.depth){
						getArticulationPoints().add(look.parent.node);
					}
					look.parent.reach = Math.min(look.parent.reach, look.reach);		
				}
				stack.pop();
			}
		}
	}

	//getter methods

	public Set<Node> getArticulationPoints() {
		return articulationPoints;
	}

	public void setArticulationPoints(Set<Node> articulationPoints) {
		this.articulationPoints = articulationPoints;
	}

}


