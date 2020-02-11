package main;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import net.automatalib.automata.simple.SimpleDeterministicAutomaton.IntAbstraction;
import net.automatalib.automata.transout.impl.compact.CompactMealy;
import net.automatalib.automata.transout.impl.compact.CompactMealyTransition;
import net.automatalib.util.automata.copy.AutomatonCopyMethod;
import net.automatalib.util.automata.copy.AutomatonLowLevelCopy;

public class Mutations {

	public Graph mutateState(Graph g, double prob_output, Random rand) {
		CompactMealy<String,String> mm = new CompactMealy<String,String>(g.getMachine().getInputAlphabet());
		AutomatonLowLevelCopy.copy(AutomatonCopyMethod.STATE_BY_STATE, g.getMachine(), g.getMachine().getInputAlphabet(), mm);
		HashMap<IOPair, Integer> iomap = new HashMap<IOPair, Integer>(g.getIOmap());
		int input;
		int output;
		Iterator<String> initer;
		Iterator<String> outiter;
		String in;
//		String out;
		String oldout;
		String newout;
		CompactMealyTransition<String> tr;
		int state;
		int oldstate;
		int newstate;
		
		state = rand.nextInt(mm.getStates().size());

		int succ = 0;
		for (String a : mm.getInputAlphabet()) {
			if (mm.getSuccessor(state, a) != IntAbstraction.INVALID_STATE) {
				succ++;
			}
		}
		while (succ < 1) {
			state = rand.nextInt(mm.getStates().size());
			succ = 0;
			for (String a : mm.getInputAlphabet()) {
				if (mm.getSuccessor(state, a) != IntAbstraction.INVALID_STATE) {
					succ++;
				}
			}
		}

		input = rand.nextInt(mm.getInputAlphabet().size());
		initer = mm.getInputAlphabet().iterator();
		for (int i = 0; i < input; i++) {
			initer.next();
		}
		in = initer.next();
		
		while (mm.getOutput(state, in) == null) {
			input = rand.nextInt(mm.getInputAlphabet().size());
			initer = mm.getInputAlphabet().iterator();
			for (int i = 0; i < input; i++) {
				initer.next();
			}
			in = initer.next();
		}
		
		tr = mm.getTransition(state, in);
		oldout = tr.getOutput();
		oldstate = tr.getSuccId();
		
		if (rand.nextDouble() > prob_output) {
			newstate = rand.nextInt(mm.getStates().size());
			
			while (newstate == state || newstate == oldstate) {
				newstate = rand.nextInt(mm.getStates().size());
			}
			
			mm.removeTransition(state, in, tr);
			tr = mm.createTransition(newstate, oldout);
			mm.addTransition(state, in, tr);
		} else {
			output = rand.nextInt(g.getOutputAlphabet().size());
			outiter = g.getOutputAlphabet().iterator();
			for (int i = 0; i < output; i++) {
				outiter.next();
			}
			newout = outiter.next();
			
			while (newout.equals(oldout)) {
				output = rand.nextInt(g.getOutputAlphabet().size());
				outiter = g.getOutputAlphabet().iterator();
				for (int i = 0; i < output; i++) {
					outiter.next();
				}
				newout = outiter.next();
			}
			
			mm.removeTransition(state, in, tr);
			tr = mm.createTransition(oldstate, newout);
			mm.addTransition(state, in, tr);
		}

		
	
		Graph G = new Graph(mm, iomap);
		return G;
	}

}
