
public class SyntaxError {
	// report error in 'parse' operation
	public static void stop(String expected) {
		System.err.printf("line %d, position %d:expected <%s>\n", Input.getLine(), Input.getPos(), expected);
		System.exit(1);
	}
	
	// report error in 'find' operation
	public static void invalidPath() {
		System.out.println("null");
		System.exit(2);
	}
}
