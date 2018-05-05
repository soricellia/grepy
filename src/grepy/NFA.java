package grepy;

import java.util.ArrayList;

public class NFA {
	private ArrayList<ArrayList<String>> nfa;
	private String regex;
	private ArrayList<String> ops;
	private int currentState;
	private int currentOp;
	private ArrayList<String> errors;
	private final String or = "+";
	private final String star = "*";
	private ArrayList<String> accepting;
	
	public NFA(String regex) {
		setRegex(regex);
		nfa = new ArrayList<ArrayList<String>>();
		ops = new ArrayList<String>();
		accepting = new ArrayList<String>();
		currentState = 0;
		currentOp = 0;
		makeStates(regex);
	}
	
	
	public void parseInput(String input) {
		if(input.isEmpty()) {
			return; // base case
		}
		
		for(int i =0; i < nfa.size(); i++) {
			if(!ops.isEmpty()) {
				//take the current op and apply it to the state group
				applyOp(nfa.get(i), ops.get(currentOp), input);
			}
			else{
				// we have to see if it matches our states
				if(!processStates(nfa.get(i), input)) {
					return; 
				}
				else System.out.println("did it");
			}
			
			
		}
		
		//make a state for the input
		
	}
	
	private boolean processStates(ArrayList<String> states, String input) {
		for(int i = 0; i < states.size(); i++) {
			if(!states.get(i).equals(input.charAt(i)+ "")) {
				return false; // error state
			}
		}
		return true;
	}
	
	
	private void applyOp(ArrayList<String> states, String op, String input) {
		// reset the current state
		currentState = 0;
		
		
		// apply the correct operation
		if(op.equals(star)) {
			for(int i = 0; i < input.length(); i ++) {
				if(states.get(currentState).equals(input.charAt(i)+"")) {
					// good, keep going till the end
					currentState++;
					
					// if we have more input and were at the end of the state groupings, we have to check if we should loop back around
					if( (states.size()-1 == currentState) && i+1 != input.length() ) {
						// check if the first element in this group of states matches what the next character is
						if(states.get(0).equals(input.charAt(i)+ "")) {
							// start the process over again 
							applyOp(states, op, input.substring(i, input.length()));
						}
					}
				}else {
					return; // no good, we're done
				}
			}
		}
		else if(op.equals(or)) {
			
		}
		
		currentState = 0;
	}
	
	private void makeStates(String regex) {
		ArrayList<String> states = new ArrayList<String>();
		for(int i = 0; i < regex.length() ; i ++) {
			// we add to the state the operation needed for each state
				
			switch(regex.charAt(i)) {
				case '+': 
					ops.add(this.or);
					nfa.add(states);
					states= new ArrayList<String>();
					break;
				case '*': 
					ops.add(this.star);
					nfa.add(states);
					states= new ArrayList<String>();
					break;
				case '(': 
					nfa.add(states);
					states= new ArrayList<String>();
					break;
				case ')': 
					nfa.add(states);
					states= new ArrayList<String>();
					break;
				default:
					// we add to our states
					states.add(regex.charAt(i)+"");
					break;
			}
			System.out.println("States: " + states);
			
			
		}
		// add this group of states to the dfa
		nfa.add(states);
		System.out.println(nfa);
	}
	
	
	public void setRegex(String regex) {
		this.regex = regex;
	}
	public String getRegex() {
		return this.regex;
	}
	public ArrayList<ArrayList<String>> getNFA(){
		return nfa;
	}
	
}
