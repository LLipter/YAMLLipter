package node;

public class STRING extends LEAF {

	public STRING(String value) {
		super(value);
	}
	
	@Override
	public void print() {
		String res = (String) value;
		res = res.replace("'", "\\'");
		System.out.printf("'%s'",res);
	}
	
}
