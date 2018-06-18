import node.*;
public class Parser {
	
	private static Node ASTree;
	
	// entrance point for 'parse' operation
	public static void parse(String filename) {
		Input.initWithFilename(filename);
		ASTree = node(-1);
		Input.nextnws();
		if(!Input.eof())
			SyntaxError.stop("end of file");
		Input.close();
	}
	
	// entrance point for 'find' operation
	public static void find(String path) {
		Input.initWithString(path);
		walk(ASTree,key());	
		Input.close();
	}
	
	// print YAML into json format into standard output (can be redirected)
	public static void print() {
		if(ASTree != null)
			ASTree.print();
	}
	
	// method for recognize integers
	private static long integer() {
		long ret = 0;
		int sign;
		if(Input.getCh() == '-') {
			Input.next();
			sign = -1;
		}else
			sign = 1;
		while(Input.getCh() >= '0' && Input.getCh() <= '9') {
			ret = ret * 10 + Input.getCh() - '0';
			Input.next();
		}
		ret *= sign;
		return ret;
	}
	
	// method for recognize all numbers, including integers, float point number and scientific notation number
	private static Object real() {
		Object ret = integer();
		if(Input.getCh() == '.') {
			double temp = (double)(Long)ret;
			Input.next();
			double fractional = integer();
			if(fractional < 0)
				SyntaxError.stop("a positive fractional part");
			while(fractional > 1)
				fractional /= 10;
			if(temp >= 0)
				temp += fractional;
			else
				temp -= fractional;
			ret = temp;
		}
		if(Input.getCh() == 'E' || Input.getCh() == 'e') {
			double temp;
			if(ret instanceof Long)
				temp = (double)(Long)ret;
			else
				temp = (Double)ret;
			Input.next();
			long pow = integer();
			if(pow < -38)
				SyntaxError.stop("a greater exponent");
			if(pow > 38)
				SyntaxError.stop("a smaller exponent");
			temp *= Math.pow(10, pow);
			ret = temp;
		}
		return ret;
	}
	
	// method for recognize a string delimited by "
	private static String string() {
		String ret = "";
		Input.next();
		while(Input.getCh() != '"') {
			if(Input.getCh() == '\n' || Input.eof())
				SyntaxError.stop("\"");
			ret = ret + (char)Input.getCh();
			Input.next();
		}
		Input.next();
		return ret;
	}
	
	// method for recognize identifiers
	private static String identifier() {
		String ret = "";
		while(Character.isLetterOrDigit(Input.getCh()) || Input.getCh() == '_') {
			ret += (char)Input.getCh();
			Input.next();
		}
		if(ret.endsWith("_"))
			SyntaxError.stop("digit or alphabet letter");
		return ret;
	}
	
	// method for recognize atomic values like number, string, boolean value, identifier and null
	private static LEAF leaf() {
		if(Input.getCh() == '"')
			return new STRING(string());
		if(Character.isDigit(Input.getCh()) || Input.getCh() == '+' || Input.getCh() == '-')
			return new LEAF(real());
		String id = identifier();
		if(id.equals("true"))
			return new LEAF(true);
		if(id.equals("false"))
			return new LEAF(false);
		if(id.equals("null"))
			return new LEAF(null);
		return new LEAF(id);
	}
	
	// method for recognize complex values like ARRAY and DICTIONARY
	private static Node node(int identation) {
		Input.nextnws();
		if(Input.eof())
			SyntaxError.stop("value");
		if(Input.getPos() < identation + 2)
			SyntaxError.stop("more identations");
		if(Input.getCh() == '-')
			return array(Input.getPos());
		else if(Input.letter())
			return dictionary(Input.getPos());
		else
			SyntaxError.stop("alphabet letter");
		return null;// never executed
	}
	
	// DICTIONARY is a group of several KEYVALUE pairs.
	private static DICTIONARY dictionary(int identation) {
		DICTIONARY ret = new DICTIONARY();
		Node subnode;
		while((subnode = keyvalue(identation)) != null)
			ret.addChild(subnode);
		return ret;
	}
	
	// This method will recognize KEYVALUE pairs. 
	// KEYVALUE pairs consist of a key and a value.
	// The key is a atomic value --- identifier
	// The value can be either atomic value or complex value
	private static KEYVALUE keyvalue(int identation) {
		Input.nextnws();
		if(Input.eof())
			return null;
		if(!Input.letter())
			return null;
		if(Input.getPos() != identation)
			return null;
		String key = identifier();
		Input.nextns();
		if(Input.getCh() != ':')
			SyntaxError.stop(":");
		int pos = Input.getPos();
		Input.next();
		Input.nextns();
		if(Input.eof())
			SyntaxError.stop("value");
		if(Input.getPos() <= pos + 1 && !Input.nl())
			SyntaxError.stop("white space");
		if(Input.nl()) // ARRAY or DICTIONARY
			return new KEYVALUE(key,node(identation));
		else// LEAF 
			return new KEYVALUE(key,leaf());
	}
	
	// ARRAY is a group of items, each item can either be atomic value or complex value
	private static ARRAY array(int identation) {
		ARRAY ret = new ARRAY();
		Node subnode;
		while((subnode = item(identation)) != null)
			ret.addChild(subnode);
		return ret;
	}

	// method for recognize item, basic unit for a ARRAY
	private static Node item(int identation) {
		Input.nextnws();
		if(Input.eof())
			return null;
		if(Input.getCh() != '-')
			return null;
		if(Input.getPos() != identation)
			return null;
		int pos = Input.getPos();
		Input.next();
		Input.nextns();
		if(Input.eof())
			SyntaxError.stop("value");
		if(Input.getPos() <= pos + 1 && !Input.nl())
			SyntaxError.stop("white space");
		if(Input.nl()) // ARRAY or DICTIONARY
			return node(identation);
		else// LEAF 
			return leaf();
	}
	

	
	
	// method for implement 'find'
	private static int index() {
		Input.next();
		long ret = integer();
		if(Input.getCh() != ']')
			SyntaxError.invalidPath();
		Input.next();
		return (int)ret;
	}
	
	private static Object key() {
		Input.nextnd();
		if(Input.eof())
			return null;
		if(Input.getCh() == '[')
			return index();
		else if(Input.letter())
			return identifier();
		else
			SyntaxError.invalidPath();
		return null;// never used
	}
	
	// we walk from root of ASTree downwards to the node we are looking for
	private static void walk(Node cur,Object key) {
		if(key instanceof Integer && cur instanceof ARRAY && ((ARRAY)cur).size() >= (Integer)key) {// ARRAY index
			cur = ((ARRAY)cur).getChild(((Integer) key-1));
			Object nextKey = key();
			if(nextKey == null) {// found 
				cur.print();
				return;
			}else {
				walk(cur,nextKey);
			}
		}else if(key instanceof String && cur instanceof DICTIONARY) {// DICTIONARY key
			DICTIONARY dic = (DICTIONARY)cur;
			for(int i=0;i<dic.size();i++) {
				KEYVALUE kv = (KEYVALUE)dic.getChild(i);
				LEAF key_lf = (LEAF)kv.getChild(0);
				if(key_lf.getValue().equals((String)key)){ // find matched key
					cur = kv.getChild(1);
					Object nextKey = key();
					if(nextKey == null) { // found
						cur.print();
						return;
					}else {
						walk(cur,nextKey);
					}
					return;
				}
			}
		}else
			SyntaxError.invalidPath();
	}
	

	

}
