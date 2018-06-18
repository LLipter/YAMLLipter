package node;

public class LEAF extends Node {
	
	protected Object value;
	
	public LEAF(Object value) {
		this.value = value;
	}

	@Override
	public void print() {
		System.out.print(value);
	}
	
	public Object getValue() {
		return value;
	}


}
