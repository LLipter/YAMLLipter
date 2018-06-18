package node;

import java.util.ArrayList;

public abstract class Internal extends Node{
	protected ArrayList<Node> children;
	
	public Internal() {
		children = new ArrayList<Node>();
	}
	
	public void addChild(Node node) {
		children.add(node);
	}
	
	public int size() {
		return children.size();
	}
	
	public Node getChild(int index) {
		return children.get(index);
	}

}
