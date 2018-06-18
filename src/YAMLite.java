import java.io.*;

public class YAMLite {
	
	public static void usage() {
		System.out.println("usage: yamlite [option [value]] file");
		System.out.println("option:");
		System.out.println("           -parse : parse YAMLite, print 'valid' if there's no syntax error, "
				+ "report error otherwise");
		System.out.println("            -json : parse YAMLite to json format into a file with same name except its extension name is .json,");
		System.out.println("                    report error otherwise");
		System.out.println("       -find path : find the element specified by path and print it in json format. "
				+ "Print null if there's no such element,");
		System.out.println("                    report error otherwise");
	}

	public static void main(String[] args) throws IOException {
		
		
		if(args.length == 1) { // yamlite file
			Parser.parse(args[0]);
			System.out.println("valid");
		}else if(args.length == 2 && args[0].equals("-parse")) { // yamlite -parse file
			Parser.parse(args[1]);
			System.out.println("valid");
		}else if(args.length == 2 && args[0].equals("-json")) { // yamlite -json file
			String outfile;
			int index = args[1].lastIndexOf('.');
			if(index == -1)
				outfile = args[1] + ".json";
			else
				outfile = args[1].substring(0, index) + ".json";
			PrintStream printer = new PrintStream(outfile);
			System.setOut(printer);
			Parser.parse(args[1]);
			Parser.print();
		}else if(args.length == 3 && args[0].equals("-find")) { // yamlite -find path file
			Parser.parse(args[2]);
			Parser.find(args[1]);
			System.out.println();
		}else {
			usage();
		}

		
	}

}
