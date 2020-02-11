package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Random;

import net.automatalib.automata.simple.SimpleDeterministicAutomaton.IntAbstraction;
import net.automatalib.automata.transout.impl.compact.CompactMealy;
import net.automatalib.util.automata.conformance.WMethodTestsIterator;
import net.automatalib.util.automata.conformance.WpMethodTestsIterator;

public class Operations {

	public void GenerateTestSuite(Graph g, int size, ArrayList<ArrayList<IOPair>> TS, boolean repTests) {
	
		int length = 0;
		int tam  = 0;
		if (size < 1) {
			size = 1;
		}
		ArrayList<IOPair> T;
		while (length < size) {
			do {
				T = new ArrayList<IOPair>();
				tam = GenerateTest(g, size - length, T);
			} while (!repTests && repeated(T, TS));
			TS.add(new ArrayList<IOPair>(T));
			length += tam;
		}
	}

	public void GenerateTestSuite(ArrayList<ArrayList<IOPair>> TS, int ver) {
		IOPair IO;
		ArrayList<IOPair> T;
		if (ver == 0) {
			T = new ArrayList<IOPair>();
			IO = new IOPair("a", "x");
			T.add(IO.copy());
			IO = new IOPair("b", "z");
			T.add(IO.copy());
			IO = new IOPair("c", "w");
			T.add(IO.copy());
			IO = new IOPair("d", "y");
			T.add(IO.copy());
			IO = new IOPair("e", "z");
			T.add(IO.copy());
			IO = new IOPair("f", "z");
			T.add(IO.copy());
			TS.add(new ArrayList<IOPair>(T));
		} else if (ver == 1) {
			T = new ArrayList<IOPair>();
			IO = new IOPair("a", "x");
			T.add(IO.copy());
			IO = new IOPair("b", "z");
			T.add(IO.copy());
			IO = new IOPair("i", "z");
			T.add(IO.copy());
			TS.add(new ArrayList<IOPair>(T));
			T = new ArrayList<IOPair>();
			IO = new IOPair("a", "x");
			T.add(IO.copy());
			IO = new IOPair("j", "u");
			T.add(IO.copy());
			IO = new IOPair("i", "z");
			T.add(IO.copy());
			TS.add(new ArrayList<IOPair>(T));
		} else if (ver == 2) {
			T = new ArrayList<IOPair>();
			IO = new IOPair("a", "x");
			T.add(IO.copy());
			IO = new IOPair("b", "z");
			T.add(IO.copy());
			IO = new IOPair("i", "z");
			T.add(IO.copy());
			IO = new IOPair("a", "x");
			T.add(IO.copy());
			IO = new IOPair("j", "u");
			T.add(IO.copy());
			IO = new IOPair("i", "z");
			T.add(IO.copy());
			TS.add(new ArrayList<IOPair>(T));
		} else if (ver == 3) {
			T = new ArrayList<IOPair>();
			IO = new IOPair("a", "x");
			T.add(IO.copy());
			IO = new IOPair("b", "z");
			T.add(IO.copy());
			IO = new IOPair("b", "z");
			T.add(IO.copy());
			TS.add(new ArrayList<IOPair>(T));
			T = new ArrayList<IOPair>();
			IO = new IOPair("a", "x");
			T.add(IO.copy());
			IO = new IOPair("j", "u");
			T.add(IO.copy());
			IO = new IOPair("i", "z");
			T.add(IO.copy());
			TS.add(new ArrayList<IOPair>(T));
		}
	}

	public double MutualInformation(Graph g, ArrayList<ArrayList<IOPair>> TS) {
	
		ArrayList<ArrayList<IOPair>> aux = TS;
		double MI = 0.0;
		ListIterator<ArrayList<IOPair>> iter1 = aux.listIterator();
		ListIterator<ArrayList<IOPair>> iter2;
		ArrayList<IOPair> nex;
	
		while (iter1.hasNext()) {
			iter2 = aux.listIterator(iter1.nextIndex());
			nex = iter1.next();
			while (iter2.hasNext()) {
				MI += MutualInformation(g.getIOmap(), nex, iter2.next());
			}
		}
		return MI;
	}

	public double MutualInformation(HashMap<IOPair, Integer> IOmap, ArrayList<IOPair> T1, ArrayList<IOPair> T2) {
	
		double MI = 0.0;
		ArrayList<IOPair> aux1 = T1;
		ArrayList<IOPair> aux2 = T2;
		Iterator<IOPair> iter1;
		Iterator<IOPair> iter2;
		IOPair aux;
		IOPair nex1;
		IOPair nex2;
		int c = 0;
		int c1 = 0;
		int c2 = 0;
		int size1 = aux1.size();
		int size2 = aux2.size();
		ArrayList<Integer> n1 = new ArrayList<Integer>();
		ArrayList<Integer> n2 = new ArrayList<Integer>();
		ArrayList<Integer> mx1 = new ArrayList<Integer>();
		ArrayList<Integer> mx2 = new ArrayList<Integer>();
		ArrayList<Integer> count1 = new ArrayList<Integer>();
		ArrayList<Integer> count2 = new ArrayList<Integer>();
		ArrayList<Integer> mx = new ArrayList<Integer>();
		Iterator<Integer> naux1;
		Iterator<Integer> naux2;
		Iterator<Integer> naux3;
	
		aux1.sort(new IOPairComp());
		aux2.sort(new IOPairComp());
		
		iter1 = aux1.iterator();
		iter2 = aux2.iterator();

		nex1 = iter1.next();
		nex2 = iter2.next();
		
		while (size1 > 0 && size2 > 0) {
			c = 0;
			
			if (nex2.less(nex1)) {
				aux = nex2.copy();
				mx2.add(new Integer(IOmap.get(aux)));
				while(nex2.equals(aux) && iter2.hasNext()) {
					c++;
					nex2 = iter2.next();
				}
				if (nex2.equals(aux)) {
					c++;
				}
				size2 -= c;
				n2.add(new Integer(c));
			} else  if (nex1.less(nex2)) {
				aux = nex1.copy();
				mx1.add(new Integer(IOmap.get(aux)));
				while(nex1.equals(aux) && iter1.hasNext()) {
					c++;
					nex1 = iter1.next();
				}
				if (nex1.equals(aux)) {
					c++;
				}
				size1 -= c;
				n1.add(new Integer(c));
			} else {
				aux = nex1.copy();
				mx.add(new Integer(IOmap.get(aux)));
				while(nex1.equals(aux) && nex2.equals(aux) && iter1.hasNext() && iter2.hasNext()) {
					c++;
					nex1 = iter1.next();
					nex2 = iter2.next();
				}
				c1 = c;
				c2 = c;
				while (nex1.equals(aux) && iter1.hasNext()) {
					c1++;
					nex1 = iter1.next();
				}
				if (nex1.equals(aux)) {
					c1++;
				}
				while (nex2.equals(aux) && iter2.hasNext()) {
					c2++;
					nex2 = iter2.next();
				}
				if (nex2.equals(aux)) {
					c2++;
				}
				size1 -= c1;
				size2 -= c2;
				count1.add(new Integer(c1));
				count2.add(new Integer(c2));
			}
		}
	
		while (size1 > 0) {
			c = 0;
			aux = nex1.copy();
			mx1.add(new Integer(IOmap.get(aux)));
			while(nex1.equals(aux) && iter1.hasNext()) {
				c++;
				nex1 = iter1.next();
			}
			if (nex1.equals(aux)) {
				c++;
			}
			size1 -= c;
			n1.add(new Integer(c));
		}
	
		while (size2 > 0) {
			c = 0;
			aux = nex2.copy();
			mx2.add(new Integer(IOmap.get(aux)));
			while(nex2.equals(aux) && iter2.hasNext()) {
				c++;
				nex2 = iter2.next();
			}
			if (nex2.equals(aux)) {
				c++;
			}
			size2 -= c;
			n2.add(new Integer(c));
		}
	
		naux1 = count1.iterator();
		naux2 = count2.iterator();
		naux3 = mx.iterator();
		MI = 0.0;
		while (naux1.hasNext() && naux2.hasNext() && naux3.hasNext()) {
			MI += MutualInformation(naux1.next(), naux2.next(), naux3.next(), T1.equals(T2));
		}
		return MI;
	}
	
	public WMethodTestsIterator<String> WMethod(Graph g, int size) {
		WMethodTestsIterator<String> w = new WMethodTestsIterator<String>(g.getMachine(), g.getMachine().getInputAlphabet(), size);
//		while (w.hasNext()) {
//			w.next();
//		}
		return w;
	}
	
	public WpMethodTestsIterator<String> WpMethod(Graph g, int size) {
		WpMethodTestsIterator<String> wp = new WpMethodTestsIterator<String>(g.getMachine(), g.getMachine().getInputAlphabet(), size);
//		while (wp.hasNext()) {
//			wp.next();
//		}
		return wp;
	}

	private int GenerateTest(Graph g, int size, ArrayList<IOPair> T) {
	
		CompactMealy<String,String> mm = g.getMachine();
		IOPair IO = new IOPair();
		int length = 0;
		int state = mm.getInitialState()+1;
		Random rand = new Random();
		int input;
		Iterator<String> initer;
		String in;
		String out;
		int succ = 0;
		for (String a : mm.getInputAlphabet()) {
			if (mm.getSuccessor(state, a) != IntAbstraction.INVALID_STATE) {
				succ++;
			}
		}
		while (length < size && succ > 0) {
			input = rand.nextInt(mm.getInputAlphabet().size());
			initer = mm.getInputAlphabet().iterator();
			for (int i = 0; i < input; i++) {
				initer.next();
			}
			in = initer.next();
			out = mm.getOutput(state, in);
			
			while (out == null) {
				input = rand.nextInt(mm.getInputAlphabet().size());
				initer = mm.getInputAlphabet().iterator();
				for (int i = 0; i < input; i++) {
					initer.next();
				}
				in = initer.next();
				out = mm.getOutput(state, in);
			}
			
			IO = new IOPair(in, out);
			T.add(IO.copy());
			length++;
			state = mm.getTransition(state, in).getSuccId();
			succ = 0;
			for (String a : mm.getInputAlphabet()) {
				if (mm.getSuccessor(state, a) != IntAbstraction.INVALID_STATE) {
					succ++;
				}
			}
		}
		return length;
	}

	private double MutualInformation(double n1, double n2, double mx, boolean eq) {
	
		if (eq) {
			return (n1*(n1-1)/2)*log2(mx+1)/mx;
		} else {
			return n1*n2*log2(mx+1)/mx;
		}
	}

	private boolean repeated(ArrayList<IOPair> T, ArrayList<ArrayList<IOPair>> TS) {
	
		boolean repeat = false;
		Iterator<ArrayList<IOPair>> iter = TS.iterator();
		while(!repeat && iter.hasNext()) {
			if(iter.next() == T) {
				repeat = true;
			}
		}
		return repeat;
	}
	
	private double log2(double val) {
		return Math.log(val)/Math.log(2.0);
	}
}
