package main;

import java.util.Comparator;

public class IOPairComp implements Comparator<IOPair> {

	@Override
	public int compare(IOPair arg0, IOPair arg1) {
		return arg0.getInput().compareTo(arg1.getInput())*1000 + arg0.getOutput().compareTo(arg1.getOutput());
	}

}
