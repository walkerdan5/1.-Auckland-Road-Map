import java.util.*;


public class StackSetup {
	public Node node;
	public int reach;
	public StackSetup parent;
	public int depth;
	public Queue<Node>children;

	public StackSetup(Node node, int reach, StackSetup parent){
		this.node = node;
		this.reach = reach;
		this.parent = parent;
	}

}
