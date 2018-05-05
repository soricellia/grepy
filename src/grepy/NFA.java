package grepy;

import java.util.ArrayList;

public class NFA {
	
	// data members
	private String regex;
	
	private int currentState;
	private int currentOp;
	private boolean errorState; // used to tell if were in an error state
	
	private final String or = "+";
	private final String star = "*";
	
	private ArrayList<ArrayList<String>> nfa;
	private ArrayList<Operation> ops;
	private ArrayList<String> acceptingStates;
	private ArrayList<String> acceptedInput;
	
	
	
	public NFA(String regex) {
		setRegex(regex);
		nfa = new ArrayList<ArrayList<String>>();
		ops = new ArrayList<Operation>();
		acceptingStates = new ArrayList<String>();
		acceptedInput = new ArrayList<String>();
		currentState = 0;
		currentOp = 0;
		errorState = false;
		makeStates(regex);
	}
	
	// attempts to walk the NFA using the input string
	// returns true if it accepts the input string
	// false if it rejects the input string
	public boolean processInput(String input) {
		errorState = false;
		currentOp = 0;
		currentState = 0;
		
		if(input.isEmpty()) {
			// we might be able to take this, we have to check if were at an accepting state
			
		}
		
		for(int i =0; i < nfa.size(); i++) {
			if(inErrorState()) {
				return !inErrorState(); // reject the input string
			}
			if(ops.size() > currentOp) { // there is more ops to apply
				if(ops.get(currentOp).getState() == i) { // if were on the right state for the op, lets apply it
					//take the current op and apply it to the state group
					input = applyOp(nfa.get(i), ops.get(currentOp), input);
					currentOp++;
				}else {
					input = processStates(nfa.get(i), input);
				}
				
				
			}else {
				// there is no operation to apply to the states, so just walk the dfa like normal at the current spot in our input string
				input = processStates(nfa.get(i), input);
				
			}
		}
		
		return !inErrorState(); // accept the input string
	}
	
	// processes the grouping of states
	// ex (ab)*
	// ab is the grouping of states
	// returns true if the input string is accepted
	// returns false otherwise
	private String processStates(ArrayList<String> states, String input) {
		for(int i = 0; i < states.size(); i++) {
			if(inErrorState()) {
				return input; // reject the input string
			}
			if(input.isEmpty()) {
				// that means there is no more input and were not done with our states yet
				errorState();
				return input;
			}
			else if(!states.get(i).equals(input.charAt(0)+ "")) {
				errorState(); // reject the input string
				return input;
			}
			
			// move the input string along
			input = input.substring(1, input.length());
		}
		//if(!input.isEmpty()) {
			//errorState();
			//return false;
		//}
		return input; // accept the input string
	}
	
	
	// applies the operation on the state groupings
	// ex (ab)*
	// will apply the * operation to the states ab
	private String applyOp(ArrayList<String> states, Operation op, String input) {
		// reset the current state
		currentState = 0;
		
		// apply the correct operation
		if(op.getOp().equals(star)) {
			for(int i = 0; i < input.length(); i ++) {
				if(currentState >= states.size()) {
					return input;
				}
				if(states.get(currentState).equals(input.charAt(0)+"")) {
					
					// if we have more input and were at the end of the state groupings, we have to check if we should loop back around
					if( (states.size()-1 == currentState) && input.length() > 0 ) {
						
						// check if the first element in this group of states matches what the next character is
						if(states.get(0).equals(input.charAt(0)+ "")) {
							// start the process over again 
							input = applyOp(states, op, input.substring(1, input.length()));
						}
					}
				}else {
					
				}
				// move the input tape along
				input = input.substring(1, input.length());
				currentState++;
			}
		}
		else if(op.getOp().equals(or)) {
			
		}

		return input;
	}
	
	// creates the states in the NFA
	private void makeStates(String regex) {
		ArrayList<String> states = new ArrayList<String>();
		for(int i = 0; i < regex.length() ; i ++) {
			
			// we add to the state the operation needed for each state	
			switch(regex.charAt(i)) {
				case '+': 
					ops.add(new Operation(this.or, nfa.size()));
					nfa.add(states);
					states= new ArrayList<String>();
					break;
				case '*': 
					// aa*b
					// a*b
					// get the last state added, and make a new state on it
					if(states.isEmpty()) { // this is the case for (ab)*
						// the last group of states added to the nfa will be applied to star
						// we dont have to do anything
						
					}else {
						String starState = states.get(states.size()-1);
						states.remove(states.size()-1);
						if(!states.isEmpty()) {
							nfa.add(states);
						}
						
						states = new ArrayList<String>();
						states.add(starState);
					}
					
					if(!states.isEmpty()) {
						nfa.add(states);
						
					}
					
					Operation op = new Operation(this.star, nfa.size()-1);
					ops.add(op); // we will use the current size of the nfa to decide which state is the star state
					
					states= new ArrayList<String>();
					break;
				case '(':
					// were starting a new state here, so lets add everything we have to the nfa and state the new state
					if(!states.isEmpty()) { // for the edge case (ab*)
						nfa.add(states);
					}
					
					states = new ArrayList<String>();
					break;
				case ')':
					// same thing here, we're ending the states that were currently in, so add them to the nfa and start a new list of states
					if(!states.isEmpty()) { //for the edge case ()
						nfa.add(states);
					}
					
					states = new ArrayList<String>();
					break;
				default:
					// we add to our state grouping
					states.add(regex.charAt(i)+"");
					break;
			}
		}
		if(!states.isEmpty()) { // we dont want to add empty states (though we could, cause its an nfa)
			// add this group of states to the dfa
			nfa.add(states);
		}
		System.out.println(nfa);
	}
	
	private void errorState() {
		errorState = true;
	}
	
	private boolean inErrorState() {
		return errorState;
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
