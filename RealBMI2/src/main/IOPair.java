package main;

public class IOPair implements Comparable<IOPair> {
	

	public IOPair() {
	}

	public IOPair(String in, String out) {
		input = in;
		output = out;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String inp) {
		input = inp;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String out) {
		output = out;
	}
	
	@Override
	public boolean equals(Object other) {
		return equal(this, (IOPair)other);
	}

	public boolean equal(IOPair first, IOPair second) {
		if (first.input.equals(second.input)) {
			if (first.output.equals(second.output)) {
				return true;
			}
		}
		return false;
	}

	public boolean less(IOPair io) {
		if (this.input.compareTo(io.input) < 0) {
			return true;
		} else if (this.input.equals(io.input)) {
			if (this.output.compareTo(io.output) < 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int compareTo(IOPair o) {
		return this.input.compareTo(o.input)*10 + this.output.compareTo(o.output);
	}

	public IOPair copy() {
		return new IOPair(input, output);
	}
	  
    @Override    
    public int hashCode() {        
        int[] val = {input.toCharArray()[0], output.toCharArray()[0]};
        return val[0] + val[1];
    }    
	
	private String input;
	private String output;

}
