package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Main {
	protected static int REP = 50;
	protected static int LEN = 50;
	protected static int MUT = 100;
	protected static int LOOPS = 10;
	protected static int TESTS = 2;
	protected static int EXP = 100;
	protected static int INI = 0;
	protected static double PROB_OUTPUT = 0;
	protected static int MAX = 3;

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
    	double wins;
    	double valid;
		ArrayList<ArrayList<IOPair>> TS[];
    	double MI[];
    	Graph GM[];
    	boolean detected[][];
    	int count[];
    	double mean;
    	double total;
		
        for (int A = 2; A < MAX; A++) {
			try {
				Ofile = "Results_V4_"+ String.valueOf(A) +".txt";
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
			count = new int[TESTS];
			mean = 0;
			total = 0;
			
			for (int I = 0; I < REP; I++) {
				wins = 0;
				valid = 0;
//				folder = new File("./Benchmarks/BenchmarkCircuits");
				folder = new File("./SuperBenchmark");
				int J = 0;
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

					for (int K = 0; K < LOOPS; K++) {
						
						//Generate Test Suites
						for (int i = 0; i < TESTS; i++) {
							TS[i] = new ArrayList<ArrayList<IOPair>>();
						}
						for (int i = 0; i < TESTS; i++) {
							// 2/5 for alphabets of size 5, 2/25 for alphabets of size 25.
							Ops.GenerateTestSuite(G, G.getMachine().size()*G.getMachine().numInputs()*A/25, TS[i], false);
//							Ops.GenerateTestSuite(TS[i], i);
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

						//Check if our measure detected the best test suite
						if ((count[0] > count[1] && MI[0] < MI[1]) || (count[1] > count[0] && MI[1] < MI[0])) {
							wins++;
						}
						if (count[0] != count[1] && MI[0] != MI[1]) {
							valid++;
						}
					}
					System.out.println("run " + String.valueOf(J) + " --> " + String.valueOf(wins / valid));
					System.out.flush();
					J++;
				}

				try {
					if (valid != 0) {
						OFile.write(String.valueOf(I + 1) + " & " + String.valueOf(wins / valid) + " \\\\\n");
					} else {
						OFile.write(String.valueOf(I + 1) + " & ? \\\\\n");
					}
					OFile.write("\\hline\n");
					OFile.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}

				mean += wins;
				total += valid;
				System.out.println("test " + String.valueOf(I + 1) + " --> " + String.valueOf(mean / total));
				System.out.flush();
			}
			try {
				OFile.write("Mean & " + String.valueOf(mean / total) + " \\\\\n");
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
