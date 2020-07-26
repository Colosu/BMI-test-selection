/*
 * main.cpp
 *
 *  Created on: 19 sept. 2017
 *      Author: colosu
 */

#include <iostream>
#include <fstream>
#include <string>
#include <cstdlib>
#include <ctime>
#include <pthread.h>
#include <semaphore.h>
#include <fst/fst-decl.h>
#include <fst/fstlib.h>
#include <chrono>
#include "src/SqueezinessLib.h"
#include <unistd.h>

using namespace fst;

#define REP 50
#define LEN 100
#define MUT 1000
#define TESTS 2
#define EXP 100
#define INI 0
#define TIME 0
#define POOL 10

int main(int argc, char * argv[]) {

	srand(time(NULL));

	//Initialization
	IOHandler* IOH = new IOHandler();
	Mutations* Mutator = new Mutations();
	Checkups* Checker = new Checkups();
	Operations* Ops = new Operations();

	std::string Ifile = "binary.fst";
	std::string Ofile = "Results.txt";

	std::ofstream OFile;

	OFile.open(Ofile);
	if (!OFile.is_open()) {
		std::cerr << "I can't create the output file." << std::endl;
		return 1;
	}

	OFile << "| #Test | Percentage of success MIGA | Percentage of success TSDm | Percentage of ties winning | Percentage of ties loosing |" << std::endl;


	Graph* G;
	double winsMIGA;
	double winsTSDm;
	double tiesWin;
	double tiesLose;
	list<list<IOpair>>* TS[TESTS];
	Graph* GM[MUT];
	bool detected[TESTS][MUT];
	int count[TESTS];
	double BMI0 = 0;
	double BMI1 = 0;
	int BMI = 0;
	double TSDm0 = 0;
	double TSDm1 = 0;
	int TSDm = 0;
	double meanMIGA = 0;
	double meanTSDm = 0;
	double meanWin = 0;
	double meanLose = 0;
	int size = LEN;
	std::chrono::duration<double> elapsed;
	auto start = std::chrono::high_resolution_clock::now();
	auto finish = std::chrono::high_resolution_clock::now();
	double timeMIGA = 0;
	double timeTSDm = 0;
	int ticks = 0;

	for (int J = 0; J < REP; J++) {

		winsMIGA = 0;
		winsTSDm = 0;
		tiesWin = 0;
		tiesLose = 0;

		for (int I = INI; I < INI + EXP; I++) {

			Ifile = "./Tests/test" + to_string(I+1) + "/binary.fst";
//			Ifile = "./Tests/Phone/binary.fst";
//			Ifile = "./War of the Worlds/binary.fst";

			ticks = 0;
			G = IOH->readGraph(Ifile, ticks);

			if (G == NULL) {
				return 1;
			}

			if (!Checker->is_valid(G)) {
				std::cerr << "Not valid graph." << std::endl;
				return 1;
			}

			try {
				do {
					//Generate Test Suites
					for (int i = 0; i < TESTS; i++) {
						TS[i] = new list<list<IOpair>>();
					}
					for (int i = 0; i < TESTS; i++) {
						Ops->GenerateRandomTestSuite(G, size, *TS[i], true, false);
					}

					//TODO: Compute BMI and TSDm of both TS.


					start = std::chrono::high_resolution_clock::now();
					Ops->MutualInformation(G, *TS[0], BMI0);
					Ops->MutualInformation(G, *TS[1], BMI1);
					finish = std::chrono::high_resolution_clock::now();
					elapsed = finish - start;
					timeMIGA += elapsed.count();

					start = std::chrono::high_resolution_clock::now();
					Ops->TestSetDiameter(G, *TS[0], TSDm0);
					Ops->TestSetDiameter(G, *TS[1], TSDm1);
					finish = std::chrono::high_resolution_clock::now();
					elapsed = finish - start;
					timeTSDm += elapsed.count();

					if (BMI0 < BMI1) {
						BMI = 0;
					} else if (BMI0 > BMI1) {
						BMI = 1;
					} else {
						BMI = -1;
					}

					if (TSDm0 > TSDm1) {
						TSDm = 0;
					} else if (TSDm0 < TSDm1) {
						TSDm = 1;
					} else {
						TSDm = -1;
					}

					//Generate Mutants
					for (int i = 0; i < MUT; i++) {
						GM[i] = Mutator->mutateState(G);
						while (!Checker->is_validMutation(GM[i])) {
							delete GM[i];
							GM[i] = Mutator->mutateState(G);
						}
					}


					//Check Fail Detection
					ticks = 0;
					for (int i = 0; i < TESTS; i++) {
						for (int j = 0; j < MUT; j++) {
							detected[i][j] = Checker->checkMutation(GM[j], *TS[i], ticks);
						}
					}

					//Delete test suites
					for (int i = 0; i < TESTS; i++) {
						delete TS[i];
					}

					//Delete mutants
					for (int i = 0; i < MUT; i++) {
						delete GM[i];
					}

					//Count fail detection
					for (int i = 0; i < TESTS; i++) {
						count[i] = 0;
					}
					for (int i = 0; i < TESTS; i++) {
						for (int j = 0; j < MUT; j++) {
							if(detected[i][j]) {
								count[i]++;
							}
						}
					}
				} while (BMI == -1 || TSDm == -1 || count[0] == count[1]);

				if (BMI != TSDm) {
					if (count[BMI] > count[TSDm]) {
						winsMIGA++;
					} else {
						winsTSDm++;
					}
				} else {
					if (count[BMI] > count[(BMI+1)%2]) {
						tiesWin++;
					} else {
						tiesLose++;
					}
				}

			} catch (exception &e) {
				cout << "Exception: " << e.what() << endl;
			}

			delete G;
			cout << I << endl;
		}

		cout << "test " << to_string(J+1) << endl;
		OFile << J+1 << " & " << winsMIGA/EXP  << "\\% & " << winsTSDm/EXP << "\\% & " << tiesWin/EXP << "\\% & " << tiesLose/EXP << "\\% \\\\" << std::endl;
		OFile << "\\hline" << std::endl;

		meanMIGA += winsMIGA;
		meanTSDm += winsTSDm;
		meanWin += tiesWin;
		meanLose += tiesLose;
	}

	OFile << "Mean & " << meanMIGA/(EXP*REP) << "\\% & " << meanTSDm/(EXP*REP) << "\\% & " << meanWin/(EXP*REP) << "\\% & " << meanLose/(EXP*REP) << "\\% \\\\" << std::endl;
	OFile << "\\hline" << std::endl;
	OFile << "Time & " << timeMIGA/(EXP*REP) << " s & " << timeTSDm/(EXP*REP) << " s \\\\" << std::endl;
	OFile << "\\hline" << std::endl;

	OFile.close();

	delete IOH;
	delete Mutator;
	delete Checker;
	delete Ops;

	return 0;
}
