package grepy;

import java.util.ArrayList;

public class Grep {

	private final static String NFA_FILE_DELIMITER  = "-n";
	private final static String DFA_FILE_DELIMITER = "-d";
	private static String nfa_file;
	private static String dfa_file;
	private static String regex;
	private static String file;
	
	public static void main(String[] args) {
		// parse input into our nfa, dfa and regex files
		parseCommandArgs(args);
		
		System.out.println("nfa file: " + nfa_file);
		System.out.println("dfa file: " + dfa_file);
		System.out.println("regex: " + regex);
		System.out.println("file name: " + file);
		
		FileManager fm = new FileManager();
		NFA nfa = new NFA(regex);
		
		//try to find a match for each line in the input file 
		findMatches(nfa, fm.readFile(file));
		
		
		//write the dfa to the specified file 
		if(dfa_file != null) {
			fm.writeDotFile(dfa_file, nfa.getNFA());
		}
	}
	
	// try to find a match for each element in the input array
	private static void findMatches(NFA nfa, ArrayList<String> input) {
		for(int i =0; i < input.size(); i++) {
			// when we have a match print it to the console
			if(nfa.processInput(input.get(i))) {
				System.out.println(input.get(i));
			}
		}
	}
	
	// parses the command line inputs
	private static void parseCommandArgs(String[] args) {
		// parse the input
		if(args.length < 2) { // jsut making sure we have enough command line arguements
			inputErrorExitGracefully();
		}
		
		// nfa first file case
		if(args[0].equals(NFA_FILE_DELIMITER)) {
			if(args.length < 4) {
				inputErrorExitGracefully();
			}
			
			nfa_file = args[1];
			
			// dfa case
			if(args[2].equals(DFA_FILE_DELIMITER)) {
				if(args.length < 6) {
					inputErrorExitGracefully();
				}
				
				dfa_file = args[3];
				regex = args[4];
				file = args[5];
			}
			
			// no dfa
			else {
				if(args[2].startsWith("-")) { // make sure we're not giving bad input arguements 
					inputErrorExitGracefully();
				}
				regex = args[2];
				file = args[3];
			}
			
		}
		
		// dfa first file case
		else if(args[0].equals(DFA_FILE_DELIMITER)) {
			if(args.length < 4) {
				inputErrorExitGracefully();
			}
			
			dfa_file = args[1];
			
			//nfa case
			if(args[2].equals(NFA_FILE_DELIMITER)) {
				if(args.length < 4) {
					inputErrorExitGracefully();
				}
				nfa_file = args[3];
				regex = args[4];
				file = args[5];
			}
			
			//no nfa
			else {
				if(args[2].startsWith("-")) { // make sure we're not giving bad input arguements 
					inputErrorExitGracefully();
				}
				regex = args[2];
				file = args[3];
			}
		}
		
		// no nfa or dfa
		else {
			if(args[0].startsWith("-")) { // make sure we're not giving bad input arguements 
				inputErrorExitGracefully();
			}
			regex = args[0];
			file = args[1];
		}
		
	} // end parse input
	
	
	// on error input cases, just tell them its wrong and exit gracefully. 
	private static void inputErrorExitGracefully() {
		System.out.println("Error incorrect input parameters.\n an example call is: java grepy.Grep [-n NFA-FILE] [-d DFA-FILE] REGEX FILE");
		System.exit(0); // cant do anything, so exit gracefully
	}
	
}
