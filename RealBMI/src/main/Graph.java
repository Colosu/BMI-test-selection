package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.automatalib.automata.transout.impl.compact.CompactMealy;
import net.automatalib.words.Alphabet;
import net.automatalib.words.impl.Alphabets;

public class Graph {

	public Graph(CompactMealy<String,String> mema) {
		setMachine(mema);
	}

	public Graph(CompactMealy<String,String> mema, HashMap<IOPair, Integer> iomap) {
		setMachine(mema, iomap);
	}

	public CompactMealy<String,String> getMachine() {
		return mm;
	}

	public void setMachine(CompactMealy<String,String> mema) {
		mm = mema;
		generateIOmap();
	}

	public void setMachine(CompactMealy<String,String> mema, Integer ticks) {
		mm = mema;
		generateIOmap(ticks);
	}

	public void setMachine(CompactMealy<String,String> mema, HashMap<IOPair, Integer> iomap) {
		mm = mema;
		setIOmap(iomap);
	}

	public HashMap<IOPair, Integer> getIOmap() {
		return IOmap;
	}

	public void setIOmap(HashMap<IOPair, Integer> iomap) {
		IOmap = iomap;
	}

	public Alphabet<String> getOutputAlphabet() {
		return OutputAlphabet;
	}

	public void setOutputAlphabet(Alphabet<String> outputAlphabet) {
		OutputAlphabet = outputAlphabet;
	}

	private void generateIOmap() {
	
		IOmap = new HashMap<IOPair, Integer>();
		ArrayList<String> OA = new ArrayList<String>();
		IOPair IO;
		Iterator<Integer> iter = mm.getStates().iterator();
		Iterator<String> initer;
		int state;
		String in;
		String out;
		while (iter.hasNext()) {
			state = iter.next();
			initer = mm.getInputAlphabet().iterator();
			while (initer.hasNext()) {
				in = initer.next();
				out = mm.getOutput(state, in);
				if (out != null) {
					IO = new IOPair(in, out);
					if(IOmap.containsKey(IO)) {
						IOmap.put(IO, IOmap.get(IO) + 1);
					} else {
						IOmap.put(IO, 1);
					}
					if (!OA.contains(out)) {
						OA.add(out);
					}
				}
			}
		}
		OutputAlphabet = Alphabets.fromList(OA);
	}

	private void generateIOmap(int ticks) {
	
		IOmap = new HashMap<IOPair, Integer>();
		ArrayList<String> OA = new ArrayList<String>();
		IOPair IO;
		Iterator<Integer> iter = mm.getStates().iterator();
		Iterator<String> initer;
		int state;
		String in;
		String out;
		while (iter.hasNext()) {
			state = iter.next();
			initer = mm.getInputAlphabet().iterator();
			while (initer.hasNext()) {
				in = initer.next();
				out = mm.getOutput(state, in);
				if (out != null) {
					ticks++;
					IO = new IOPair(in, out);
					if(IOmap.containsKey(IO)) {
						IOmap.put(IO, IOmap.get(IO) + 1);
					} else {
						IOmap.put(IO, 1);
					}
					if (!OA.contains(out)) {
						OA.add(out);
					}
				}
			}
		}
		OutputAlphabet = Alphabets.fromList(OA);
	}

	private CompactMealy<String,String> mm;
	private HashMap<IOPair,Integer> IOmap;
	private Alphabet<String> OutputAlphabet;
}
