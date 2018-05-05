package grepy;

public class Operation {
	
	private String op;
	private int state;
	
	Operation(String op, int state){
		setOp(op);
		setState(state);
	}
	
	public void setOp(String operation) {
		op = operation;
	}
	
	public String getOp() {
		return op;
	}
	
	public void setState(int newState) {
		state = newState;
	}
	
	public int getState() {
		return state;
	}

}
