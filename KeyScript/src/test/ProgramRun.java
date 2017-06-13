package test;

import interpreter.Interpreter;

public class ProgramRun {
	public static void main(String[] args) {
		String[] program = {
				"randint 250 700",
				"randint 250 850",
				"move",
				"mouseclick 1 100",
				"sleep 500",
				"goto 1"
		};
		new Interpreter(program).run();
		// 250 250
		// 850 700
	}
}