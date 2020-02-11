package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

public class Main {
	protected static int REP = 50;
	protected static int LEN = 50;
	protected static int MUT = 1000;
	protected static int TESTS = 50;
	protected static int EXP = 100;
	protected static int INI = 0;
	protected static double PROB_OUTPUT = 0.5;
	protected static int MAX = 2;

	@SuppressWarnings({ "resource", "unchecked" })
	public static void main(String[] args) {

		//Initialization
		IOHandler IOH = new IOHandler();
		Mutations Mutator = new Mutations();
		Checkups Checker = new Checkups();
		Operations Ops = new Operations();
		Random rand = new Random();

		File folder;
		String Ofile;
		FileWriter OFile;
        Graph G;
		ArrayList<ArrayList<IOPair>> TS[];
    	double MI[];
    	Graph GM[];
    	boolean detected[][];
    	double count[];
    	double Pc;
    	double Sc;
    	double Pcorr;
    	double Scorr;
    	double meanP;
    	double meanS;
		
        for (int A = 1; A < MAX; A++) {
			try {
				Ofile = "Results_"+ String.valueOf(A) +".txt";
				OFile = new FileWriter(Ofile);
				OFile.write("| #Test | Percentage of success |\n");
				OFile.flush();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			
			TS = new ArrayList[TESTS];
			MI = new double[TESTS];
			GM = new Graph[MUT];
			detected = new boolean[TESTS][MUT];
			count = new double[TESTS];
			meanP = 0;
			meanS = 0;
			
			for (int I = 0; I < REP; I++) {
//				folder = new File("./Benchmarks/BenchmarkCircuits");
				folder = new File("./SuperBenchmark");
				int J = 0;
				Pcorr = 0;
				Scorr = 0;
//				for (int J = INI; J < INI + EXP; J++) {
				for (File Ifile : folder.listFiles()) {
//					Ifile = new File("./models.Mealy/Mealy/principle/BenchmarkCoffeeMachine/coffeemachine.dot");

					G = IOH.readGraph(Ifile.toString());
//					G = IOH.buildCoffeeMachine();
//					G = IOH.buildPhone();

					if (G == null) {
						System.err.println(Ifile.toString() + ": Failled to load the automaton.");
						return;
					}

					if (!Checker.is_valid(G.getMachine())) {
						System.err.println(Ifile.toString() + ": Non-valid graph.");
						return;
					}
					
					//Generate Test Suites
					for (int i = 0; i < TESTS; i++) {
						TS[i] = new ArrayList<ArrayList<IOPair>>();
					}
					for (int i = 0; i < TESTS; i++) {
						Ops.GenerateTestSuite(G, G.getMachine().size()*G.getMachine().numInputs()*A/25, TS[i], false);
//						Ops.GenerateTestSuite(TS[i], i);
					}
					
					//Check Mutual Information
					for (int i = 0; i < TESTS; i++) {
						MI[i] = 0;
					}
					for (int i = 0; i < TESTS; i++) {
						MI[i] = Ops.MutualInformation(G, TS[i]);
					}
					
					//Generate Mutants
					for (int i = 0; i < MUT; i++) {
						GM[i] = Mutator.mutateState(G, PROB_OUTPUT, rand);
						while (!Checker.is_validMutation(GM[i].getMachine(), G.getMachine())) {
							GM[i] = Mutator.mutateState(G, PROB_OUTPUT, rand);
						}
					}
					
					//Check Fail Detection
					for (int i = 0; i < TESTS; i++) {
						for (int j = 0; j < MUT; j++) {
							detected[i][j] = Checker.checkMutation(GM[j], TS[i]);
						}
					}

					//Count fail detection
					for (int i = 0; i < TESTS; i++) {
						count[i] = 0;
					}
					for (int i = 0; i < TESTS; i++) {
						for (int j = 0; j < MUT; j++) {
							if (detected[i][j]) {
								count[i]++;
							}
						}
					}
					Pc = new PearsonsCorrelation().correlation(MI,count);
					Sc = new SpearmansCorrelation().correlation(MI,count);
					if (Pc == Pc && Sc == Sc) {
						Pcorr += Pc;
						Scorr += Sc;
						J++;
					}
					System.out.println("run " + String.valueOf(J) + " --> " + String.valueOf(Pcorr/J) + " --> " + String.valueOf(Scorr/J));
					System.out.flush();
				}

				try {
					OFile.write(String.valueOf(I + 1) + " & " + String.valueOf(Pcorr/J) + " & " + String.valueOf(Scorr/J) + " \\\\\n");
					OFile.write("\\hline\n");
					OFile.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				meanP += Pcorr/J;
				meanS += Scorr/J;

				System.out.println("test " + String.valueOf(I + 1) + " --> " + String.valueOf(meanP/(I+1)) + " --> " + String.valueOf(meanS/(I+1)));
				System.out.flush();
			}
			try {
				OFile.write("Mean & " + String.valueOf(meanP/REP) + " & " + String.valueOf(meanS/REP) + " \\\\\n");
				OFile.write("\\hline\n");
				OFile.flush();
				OFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		return;
	}
}
