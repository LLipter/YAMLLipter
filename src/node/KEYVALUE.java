package node;

public class KEYVALUE extends Internal {
	
	public KEYVALUE(String key,Node value) {
		super();
		this.children.add(new LEAF(key));
		this.children.add(value);
	}

	@Override
	public void print() {
		this.children.get(0).print();
		System.out.print(":");
		this.children.get(1).print();
	}
	
	
}
