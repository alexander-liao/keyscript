package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import core.Core;
import interpreter.Interpreter;

public class KeyScript {
	public static void main(String[] args) throws IOException {
		List<String> list = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new FileReader(new File(args[0])));
		String line;
		while ((line = reader.readLine()) != null) {
			list.add(line);
		}
		reader.close();
		Interpreter interpreter = new Interpreter(list.toArray(new String[list.size()]));
		for (int i = 1; i < args.length; i++) {
			interpreter.push(Core.unescape(args[i]));
		}
		interpreter.run();
	}
}