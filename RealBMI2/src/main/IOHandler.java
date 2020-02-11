package main;

import java.util.ArrayList;
import java.util.HashMap;

import net.automatalib.automata.transout.impl.compact.CompactMealy;
import net.automatalib.words.Alphabet;
import net.automatalib.words.impl.Alphabets;
import statemachine.model.fsm.mealy.MealyNonDetModel;
import statemachine.model.fsm.mealy.file.Import;
import statemachine.model.elements.action.InputAction;
import statemachine.model.elements.location.LocationState;
import statemachine.model.elements.transition.MealyTransition;
import statemachine.model.fsm.mealy.MealyDetModel;
import statemachine.model.fsm.mealy.MealyModel;

public class IOHandler {
	
	public IOHandler() {
		
	}

	public Graph readGraph(String file) {
		MealyNonDetModel mndm = null;
		MealyDetModel mdm = null;
		CompactMealy<String, String> mm = null;
		
		try {
			mndm = Import.dot2MealyModel(file);
			mdm = MealyDetModel.fromMealyModel(mndm);
			mm = convert2automatalibMealy(mdm);
		} catch (Exception e) {
			System.err.println("Unable to load the automaton.");
			e.printStackTrace();
			return null;
		}

		Graph g = new Graph(mm);
		return g;
	}

    public Graph buildCoffeeMachine() {

        String WATER = "WATER";
        String POD = "POD";
        String BUTTON = "BUTTON";
        String CLEAN = "CLEAN";

        String out_ok = "ok";
        String out_error = "error";
        String out_coffee = "coffee";

        Alphabet<String> inputAlphabet = Alphabets.fromArray(WATER, POD, BUTTON, CLEAN);

        CompactMealy<String, String> machine = new CompactMealy<String, String>(inputAlphabet);

        Integer a = machine.addInitialState(), b = machine.addState(), c = machine.addState(), d = machine.addState(),
                e = machine.addState(), f = machine.addState();

        machine.addTransition(a, WATER, c, out_ok);
        machine.addTransition(a, POD, b, out_ok);
        machine.addTransition(a, BUTTON, f, out_error);
        machine.addTransition(a, CLEAN, a, out_ok);

        machine.addTransition(b, WATER, d, out_ok);
        machine.addTransition(b, POD, b, out_ok);
        machine.addTransition(b, BUTTON, f, out_error);
        machine.addTransition(b, CLEAN, a, out_ok);

        machine.addTransition(c, WATER, c, out_ok);
        machine.addTransition(c, POD, d, out_ok);
        machine.addTransition(c, BUTTON, f, out_error);
        machine.addTransition(c, CLEAN, a, out_ok);

        machine.addTransition(d, WATER, d, out_ok);
        machine.addTransition(d, POD, d, out_ok);
        machine.addTransition(d, BUTTON, e, out_coffee);
        machine.addTransition(d, CLEAN, a, out_ok);

        machine.addTransition(e, WATER, f, out_error);
        machine.addTransition(e, POD, f, out_error);
        machine.addTransition(e, BUTTON, f, out_error);
        machine.addTransition(e, CLEAN, a, out_ok);

        machine.addTransition(f, WATER, f, out_error);
        machine.addTransition(f, POD, f, out_error);
        machine.addTransition(f, BUTTON, f, out_error);
        machine.addTransition(f, CLEAN, f, out_error);

        return new Graph(machine);
    }

    public Graph buildPhone() {

    	// Inputs.
        String a = "a";
        String b = "b";
        String c = "c";
        String d = "d";
        String e = "e";
        String f = "f";
        String g = "g";
        String h = "h";
        String i = "i";
        String j = "j";
        String k = "k";
        String l = "l";

        // Outputs.
        String s = "s";
        String t = "t";
        String u = "u";
        String v = "v";
        String w = "w";
        String x = "x";
        String y = "y";
        String z = "z";

        Alphabet<String> inputAlphabet = Alphabets.fromArray(a, b, c, d, e, f, g, h, i, j, k, l);

        CompactMealy<String, String> machine = new CompactMealy<String, String>(inputAlphabet);

        Integer s0 = machine.addInitialState(),
        		s1 = machine.addState(),
        		s2 = machine.addState(),
        		s3 = machine.addState(),
                s4 = machine.addState(),
                s5 = machine.addState(),
                s6 = machine.addState(),
                s7 = machine.addState(),
                s8 = machine.addState(),
                s9 = machine.addState(),
                s10 = machine.addState();

        machine.addTransition(s0, a, s1, x);
        machine.addTransition(s0, c, s1, z);
        
        machine.addTransition(s1, b, s2, z);
        machine.addTransition(s1, j, s3, u);
        machine.addTransition(s1, i, s0, z);

        machine.addTransition(s2, b, s2, z);
        machine.addTransition(s2, j, s3, u);
        machine.addTransition(s2, c, s4, w);
        machine.addTransition(s2, g, s5, v);
        machine.addTransition(s2, i, s0, z);

        machine.addTransition(s3, i, s0, z);
        
        machine.addTransition(s4, d, s6, y);
        machine.addTransition(s4, k, s7, s);
        machine.addTransition(s4, l, s8, t);
        machine.addTransition(s4, i, s0, z);

        machine.addTransition(s5, h, s10, z);
        machine.addTransition(s5, i, s0, z);
        
        machine.addTransition(s6, e, s9, z);
        machine.addTransition(s6, f, s0, z);

        machine.addTransition(s7, i, s0, z);
        
        machine.addTransition(s8, i, s0, z);
        
        machine.addTransition(s9, f, s10, z);
        machine.addTransition(s9, i, s0, z);

        machine.addTransition(s10, i, s0, z);

        return new Graph(machine);
    }
    
    static private CompactMealy<String, String> convert2automatalibMealy(MealyModel model) {


		ArrayList<String> inputs=new ArrayList<>();
		for ( InputAction input: model.getInputAlphabet() ) {
			inputs.add(input.getName());
		}

		Alphabet<String> inputAlphabet = Alphabets.fromList(inputs);

		CompactMealy<String, String> machine = new CompactMealy<String,String>(inputAlphabet);

		// map locations
		HashMap<LocationState,Integer> statelib2autolibLocation = new HashMap<>();
		LocationState startLocation=model.getStartLocation();
		Integer startState = machine.addInitialState();
		statelib2autolibLocation.put(startLocation,startState);

		for ( LocationState l: model.getLocations() ) {
			if ( !l.equals(startLocation) ) {
				Integer state=machine.addState();   // => cannot add label LocationState , no label supported by automatalib (internal uses integer though)
				statelib2autolibLocation.put(l,state);
			}
		}

		for ( MealyTransition t: model.getModelTransitions()) {
			Integer source = statelib2autolibLocation.get(t.getSource());
			Integer destination = statelib2autolibLocation.get(t.getDestination());
			machine.addTransition(source, t.getInput().getName(), destination, t.getOutput().getName());
		}

		return machine;
	}

}
