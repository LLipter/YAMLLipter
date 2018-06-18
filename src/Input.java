import java.io.*;

public class Input {
	private static Reader reader ;
	private static int ch;
	private static int line;
	private static int pos;
	
	
	public static int getCh() {
		return ch;
	}

	public static int getLine() {
		return line;
	}

	public static int getPos() {
		return pos;
	}

	public static void initWithFilename(String inputFile){
		try {
			reader = new InputStreamReader(new FileInputStream(inputFile));
			ch = -1;
			line = 1;
			pos = 0;
			next();
		} catch (FileNotFoundException e) {
			System.err.println("no such file as " + inputFile);
		}
	}
	
	public static void initWithString(String inputFile){
		reader = new StringReader(inputFile);
		ch = -1;
		line = 1;
		pos = 0;
		next();
	}
	
	public static void close(){
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void next(){
		try {
			if(ch == '\n') {
				line++;
				pos = 0;
			}
			ch = reader.read();
			pos++;
			
			if(ch == '#') { // ignore all comments
				while(ch != '\n' && ch != -1)
					next();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// nws stands for 'NOT white space'
	public static void nextnws() {
		while(Input.getCh() == ' ' || Input.getCh() == '\n')
			Input.next();
	}
	
	// ns stands for 'NOT space'
	public static void nextns() {
		while(Input.getCh() == ' ')
			Input.next();
	}
	
	// nd stands for 'NOT dot'
	public static void nextnd() {
		while(Input.getCh() == '.')
			Input.next();
	}
	
	// nws stands for 'NOT white space'
	public static boolean nws() {
		if(Input.getCh() == ' ')
			return false;
		if(Input.getCh() == '\n')
			return false;
		return true;
	}
	
	public static boolean space() {
		return Input.getCh() == ' ';
	}
	
	// nl stands for 'new line'
	public static boolean nl() {
		return Input.getCh() == '\n';
	}
	
	// eof stands for 'end of file'
	public static boolean eof() {
		return Input.getCh() == -1;
	}
	
	public static boolean letter() {
		return Character.isLetter(Input.getCh());
	}
	
	

}
