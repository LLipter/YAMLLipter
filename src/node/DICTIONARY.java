package node;

public class DICTIONARY extends Internal {
	

	@Override
	public void print() {
		System.out.print("{");
		for(int i=0;i<children.size();i++) {
			if(i>0)
				System.out.print(", ");
			children.get(i).print();
		}
		System.out.print("}");
	}

}
