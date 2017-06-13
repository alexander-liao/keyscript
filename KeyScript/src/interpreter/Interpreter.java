package interpreter;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import core.Core;
import core.LevenshteinDistance;

public class Interpreter {
	private final String[] lines;
	private final List<Object> stack = new ArrayList<>();
	private boolean stackmode = true;

	public Interpreter(String... lines) {
		this.lines = lines;
	}

	private Object pop() {
		if (stackmode) {
			return stack.remove(stack.size() - 1);
		} else {
			return stack.remove(0);
		}
	}

	private Object[] require(int length, String[] line) {
		Object[] array = new Object[Math.max(length, line.length - 1)];
		System.arraycopy(line, 1, array, 0, line.length - 1);
		for (int i = line.length - 1; i < array.length; i++) {
			array[i] = pop();
		}
		return array;
	}

	private String requireString(String[] delimited, String line) {
		if (delimited.length > 1) {
			return line.split("\\s+", 2)[1];
		} else {
			return pop().toString();
		}
	}

	private static int parseInt(Object string) {
		return Integer.parseInt(string.toString().replaceAll("\\.\\d+", ""));
	}

	private static long parseLong(Object string) {
		return Long.parseLong(string.toString().replaceAll("\\.\\d+", ""));
	}

	private static double parseDouble(Object string) {
		return Double.parseDouble(string.toString());
	}

	private static float parseFloat(Object string) {
		return Float.parseFloat(string.toString());
	}

	private static short parseShort(Object string) {
		return (short) (parseInt(string) % (Short.MAX_VALUE - Short.MIN_VALUE + 1));
	}

	private static byte parseByte(Object string) {
		return (byte) (parseInt(string) % 256);
	}

	private static char parseChar(Object string) {
		if (string instanceof Character) {
			return (Character) string;
		} else {
			return (char) Math.abs(parseInt(string));
		}
	}

	private static int parseBool(Object object) {
		if (object == null) {
			return 0;
		} else if (object instanceof String) {
			return ((String) object).isEmpty() ? 0 : 1;
		} else if (object instanceof Integer) {
			return ((Integer) object) == 0 ? 0 : 1;
		} else if (object instanceof Long) {
			return ((Long) object) == 0 ? 0 : 1;
		} else if (object instanceof Double) {
			return ((Double) object) == 0.0 ? 0 : 1;
		} else if (object instanceof Float) {
			return ((Float) object) == 0.0f ? 0 : 1;
		} else if (object instanceof Short) {
			return ((Short) object) == 0 ? 0 : 1;
		} else if (object instanceof Byte) {
			return ((Byte) object) == 0 ? 0 : 1;
		} else if (object instanceof Boolean) {
			return (Boolean) object ? 1 : 0;
		} else if (object instanceof Character) {
			return Character.isWhitespace((Character) object) ? 0 : 1;
		} else {
			return 0; // Should never happen!
		}
	}

	public void push(Object object) {
		stack.add(object);
	}

	private static String reverse(String string) {
		return new StringBuffer(string).reverse().toString();
	}

	private static Object add(Object a, Object b) {
		if (a == null || b == null) {
			return 0;
		} else if (a instanceof String || b instanceof String) {
			return a.toString() + b.toString();
		} else if (a instanceof Integer || b instanceof Integer) {
			return (Integer) a + (Integer) b;
		} else if (a instanceof Long || b instanceof Long) {
			return (Long) a + (Long) b;
		} else if (a instanceof Double || b instanceof Double) {
			return (Double) a + (Double) b;
		} else if (a instanceof Float || b instanceof Float) {
			return (Float) a + (Float) b;
		} else if (a instanceof Short || b instanceof Short) {
			return (Short) a + (Short) b;
		} else if (a instanceof Byte || b instanceof Byte) {
			return (Byte) a + (Byte) b;
		} else if (a instanceof Character && b instanceof Character) {
			return (Character) a + (Character) b;
		} else {
			return a.toString() + b.toString();
		}
	}

	private static Object subtract(Object a, Object b) {
		if (a == null || b == null) {
			return 0;
		} else if (a instanceof String || b instanceof String) {
			return LevenshteinDistance.levenshteinDistance(a.toString(), b.toString());
		} else if (a instanceof Integer || b instanceof Integer) {
			return (Integer) a - (Integer) b;
		} else if (a instanceof Long || b instanceof Long) {
			return (Long) a - (Long) b;
		} else if (a instanceof Double || b instanceof Double) {
			return (Double) a - (Double) b;
		} else if (a instanceof Float || b instanceof Float) {
			return (Float) a - (Float) b;
		} else if (a instanceof Short || b instanceof Short) {
			return (Short) a - (Short) b;
		} else if (a instanceof Byte || b instanceof Byte) {
			return (Byte) a - (Byte) b;
		} else if (a instanceof Character && b instanceof Character) {
			return (Character) a - (Character) b;
		} else {
			return LevenshteinDistance.levenshteinDistance(a.toString(), b.toString());
		}
	}

	private static Object absolute_difference(Object a, Object b) {
		if (a == null || b == null) {
			return 0;
		} else if (a instanceof String || b instanceof String) {
			return LevenshteinDistance.levenshteinDistance(a.toString(), b.toString());
		} else if (a instanceof Integer || b instanceof Integer) {
			return Math.abs((Integer) a - (Integer) b);
		} else if (a instanceof Long || b instanceof Long) {
			return Math.abs((Long) a - (Long) b);
		} else if (a instanceof Double || b instanceof Double) {
			return Math.abs((Double) a - (Double) b);
		} else if (a instanceof Float || b instanceof Float) {
			return Math.abs((Float) a - (Float) b);
		} else if (a instanceof Short || b instanceof Short) {
			return Math.abs((Short) a - (Short) b);
		} else if (a instanceof Byte || b instanceof Byte) {
			return Math.abs((Byte) a - (Byte) b);
		} else if (a instanceof Character && b instanceof Character) {
			return Math.abs((Character) a - (Character) b);
		} else {
			return LevenshteinDistance.levenshteinDistance(a.toString(), b.toString());
		}
	}

	private static Object repeat(String string, double times) {
		if (times < 0) {
			return repeat(reverse(string), -times);
		}
		StringBuffer sbuf = new StringBuffer();
		for (int i = 0; i < times; i++) {
			sbuf.append(string);
		}
		sbuf.append(string.substring(0, (int) (string.length() * times)));
		return sbuf.toString();
	}

	private static Object multiply(Object a, Object b) {
		if (a == null || b == null) {
			return 0;
		} else if (a instanceof String) {
			return repeat(a.toString(), parseDouble(b));
		} else if (b instanceof String) {
			return repeat(b.toString(), parseDouble(a));
		} else if (a instanceof Integer || b instanceof Integer) {
			return (Integer) a * (Integer) b;
		} else if (a instanceof Long || b instanceof Long) {
			return (Long) a * (Long) b;
		} else if (a instanceof Double || b instanceof Double) {
			return (Double) a * (Double) b;
		} else if (a instanceof Float || b instanceof Float) {
			return (Float) a * (Float) b;
		} else if (a instanceof Short || b instanceof Short) {
			return (Short) a * (Short) b;
		} else if (a instanceof Byte || b instanceof Byte) {
			return (Byte) a * (Byte) b;
		} else if (a instanceof Character && b instanceof Character) {
			return (Character) a * (Character) b;
		} else {
			return parseDouble(a) * parseDouble(b);
		}
	}

	private static Object divide(Object a, Object b) {
		if (a == null || b == null) {
			return 0;
		} else if (a instanceof Integer || b instanceof Integer) {
			return (Integer) a / (Integer) b;
		} else if (a instanceof Long || b instanceof Long) {
			return (Long) a / (Long) b;
		} else if (a instanceof Double || b instanceof Double) {
			return (Double) a / (Double) b;
		} else if (a instanceof Float || b instanceof Float) {
			return (Float) a / (Float) b;
		} else if (a instanceof Short || b instanceof Short) {
			return (Short) a / (Short) b;
		} else if (a instanceof Byte || b instanceof Byte) {
			return (Byte) a / (Byte) b;
		} else if (a instanceof Character && b instanceof Character) {
			return (Character) (char) ((Character) a / (Character) b);
		} else {
			return parseDouble(a) / parseDouble(b);
		}
	}

	private static Object exponentiate(Object a, Object b) {
		if (a == null || b == null) {
			return 0;
		} else if (a instanceof Integer || b instanceof Integer) {
			return (Integer) (int) Math.pow((Integer) a, (Integer) b);
		} else if (a instanceof Long || b instanceof Long) {
			return (Long) (long) Math.pow((Long) a, (Long) b);
		} else if (a instanceof Double || b instanceof Double) {
			return (Double) (double) Math.pow((Double) a, (Double) b);
		} else if (a instanceof Float || b instanceof Float) {
			return (Float) (float) Math.pow((Float) a, (Float) b);
		} else if (a instanceof Short || b instanceof Short) {
			return (Short) (short) Math.pow((Short) a, (Short) b);
		} else if (a instanceof Byte || b instanceof Byte) {
			return (Byte) (byte) Math.pow((Byte) a, (Byte) b);
		} else if (a instanceof Character && b instanceof Character) {
			return (Character) (char) Math.pow((Character) a, (Character) b);
		} else {
			return (Double) Math.pow(parseDouble(a), parseDouble(b));
		}
	}

	public void run() {
		run(0, lines.length);
	}

	public void run(int start, int end) {
		for (int number = start; number < end; number++) {
			final int current = number;
			String[] line = lines[number].split("\\s+");
			String command = line[0].toLowerCase().replaceAll("-", "");
			if (command.startsWith("#")) {
				// This would be a comment
			} else if (command.equals("sleep")) {
				Object[] arguments = require(1, line);
				long delay = parseLong(arguments[0]);
				for (long progress = 0; progress < delay; progress++) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						progress--;
					}
				}
			} else if (command.equals("delay")) {
				final Object[] arguments = require(2, line);
				final int lines = parseInt(arguments[0]);
				new Timer().schedule(new TimerTask() {
					public void run() {
						Interpreter.this.run(current + 1, current + lines + 1);
					}
				}, parseLong(arguments[1]));
				number += lines;
			} else if (command.equals("press")) {
				Object[] arguments = require(1, line);
				if (arguments.length > 0) {
					int[] codes = new int[arguments.length - 1];
					for (int i = 1; i < arguments.length; i++) {
						codes[i - 1] = parseInt(arguments[i]);
					}
					Core.sequencePress(codes, parseInt(arguments[0]));
				}
			} else if (command.equals("release")) {
				Object[] arguments = require(1, line);
				if (arguments.length > 0) {
					int[] codes = new int[arguments.length - 1];
					for (int i = 1; i < arguments.length; i++) {
						codes[i - 1] = parseInt(arguments[1]);
					}
					Core.sequenceRelease(codes, parseInt(arguments[0]));
				}
			} else if (command.equals("sequence")) {
				Object[] arguments = require(2, line);
				if (arguments.length > 1) {
					int[] codes = new int[arguments.length - 2];
					for (int i = 2; i < arguments.length; i++) {
						codes[i - 2] = parseInt(arguments[i]);
					}
					Core.sequenceType(codes, parseInt(arguments[0]), parseInt(arguments[1]));
				}
			} else if (command.equals("type")) {
				Object[] arguments = require(3, line);
				long downdelay = parseLong(arguments[0]);
				long updelay = parseLong(arguments[1]);
				if (line.length > 3) {
					for (char character : lines[number].split("\\s+", 4)[3].toString().toCharArray()) {
						Core.sequenceType(Core.typechain(character), downdelay, updelay);
					}
				} else {
					for (char character : arguments[2].toString().toCharArray()) {
						Core.sequenceType(Core.typechain(character), downdelay, updelay);
					}
				}
			} else if (command.equals("move")) {
				Object[] arguments = require(2, line);
				Core.mouseMove(parseInt(arguments[0]), parseInt(arguments[1]));
			} else if (command.equals("drag")) {
				Object[] arguments = require(2, line);
				Core.mouseDrag(MouseEvent.BUTTON1_MASK, parseInt(arguments[0]), parseInt(arguments[1]));
			} else if (command.equals("rightdrag")) {
				Object[] arguments = require(2, line);
				Core.mouseDrag(MouseEvent.BUTTON2_MASK, parseInt(arguments[0]), parseInt(arguments[1]));
			} else if (command.equals("animatemove")) {
				Object[] arguments = require(4, line);
				Core.mouseMove(parseInt(arguments[0]), parseInt(arguments[1]), parseInt(arguments[2]),
						parseLong(arguments[3]));
			} else if (command.equals("animatedrag")) {
				Object[] arguments = require(6, line);
				Core.mouseDrag(MouseEvent.BUTTON1_MASK, parseInt(arguments[0]), parseInt(arguments[1]),
						parseInt(arguments[2]), parseLong(arguments[4]), parseLong(arguments[3]),
						parseLong(arguments[5]));
			} else if (command.equals("animaterightdrag")) {
				Object[] arguments = require(6, line);
				Core.mouseDrag(MouseEvent.BUTTON2_MASK, parseInt(arguments[0]), parseInt(arguments[1]),
						parseInt(arguments[2]), parseLong(arguments[4]), parseLong(arguments[3]),
						parseLong(arguments[5]));
			} else if (command.equals("mousedown")) {
				Object[] arguments = require(1, line);
				Core.mouseDown(new int[] { MouseEvent.BUTTON1_MASK, MouseEvent.BUTTON2_MASK,
						MouseEvent.BUTTON3_MASK }[parseInt(arguments[0]) - 1]);
			} else if (command.equals("mouseup")) {
				Object[] arguments = require(1, line);
				Core.mouseUp(new int[] { MouseEvent.BUTTON1_MASK, MouseEvent.BUTTON2_MASK,
						MouseEvent.BUTTON3_MASK }[parseInt(arguments[0]) - 1]);
			} else if (command.equals("mouseclick")) {
				Object[] arguments = require(2, line);
				Core.mouseClick(new int[] { MouseEvent.BUTTON1_MASK, MouseEvent.BUTTON2_MASK,
						MouseEvent.BUTTON3_MASK }[parseInt(arguments[0]) - 1], parseLong(arguments[1]));
			} else if (command.equals("leftclick")) {
				Object[] arguments = require(1, line);
				Core.mouseClick(MouseEvent.BUTTON1_MASK, parseLong(arguments[0]));
			} else if (command.equals("rightclick")) {
				Object[] arguments = require(1, line);
				Core.mouseClick(MouseEvent.BUTTON2_MASK, parseLong(arguments[0]));
			} else if (command.equals("wheelclick")) {
				Object[] arguments = require(1, line);
				Core.mouseClick(MouseEvent.BUTTON3_MASK, parseLong(arguments[0]));
			} else if (command.equals("location")) {
				int[] location = Core.location();
				push(location[1]);
				push(location[0]);
			} else if (command.equals("goto")) {
				Object[] arguments = require(1, line);
				number = parseInt(arguments[0]) - 2;
			} else if (command.equals("input")) {
				try {
					push(Core.reader.readLine());
				} catch (IOException e) {
					System.err.println("READ_FAIL");
				}
			} else if (command.equals("rawinput")) {
				try {
					push(Core.unescape(Core.reader.readLine()));
				} catch (IOException e) {
					System.err.println("READ_FAIL");
				}
			} else if (command.equals("int")) {
				Object[] arguments = require(1, line);
				push(parseInt(arguments[0]));
			} else if (command.equals("long")) {
				Object[] arguments = require(1, line);
				push(parseLong(arguments[0]));
			} else if (command.equals("double")) {
				Object[] arguments = require(1, line);
				push(parseDouble(arguments[0]));
			} else if (command.equals("float")) {
				Object[] arguments = require(1, line);
				push(parseFloat(arguments[0]));
			} else if (command.equals("short")) {
				Object[] arguments = require(1, line);
				push(parseShort(arguments[0]));
			} else if (command.equals("byte")) {
				Object[] arguments = require(1, line);
				push(parseByte(arguments[0]));
			} else if (command.equals("bool")) {
				Object[] arguments = require(1, line);
				push(parseBool(arguments[0]));
			} else if (command.equals("string")) {
				Object[] arguments = require(1, line);
				push(arguments[0].toString());
			} else if (command.equals("char")) {
				Object[] arguments = require(1, line);
				push(parseChar(arguments[0]));
			} else if (command.equals("chars")) {
				for (char character : requireString(line, lines[number]).toCharArray()) {
					push(character);
				}
			} else if (command.equals("code")) {
				Object[] arguments = require(1, line);
				String string;
				if (arguments[0] instanceof Character) {
					push((int) (Character) (arguments[0]));
				} else if ((string = arguments[0].toString()).length() == 1) {
					push((int) string.charAt(0));
				} else {
					throw new RuntimeException("CHAR expected.");
				}
			} else if (command.equals("bury")) {
				stack.add(0, pop());
			} else if (command.equals("dig")) {
				stack.add(stack.remove(0));
			} else if (command.equals("flip")) {
				Collections.reverse(stack);
			} else if (command.equals("stack")) {
				stackmode = true;
			} else if (command.equals("queue")) {
				stackmode = false;
			} else if (command.equals("isstack")) {
				push(stackmode ? 1 : 0);
			} else if (command.equals("isqueue")) {
				push(stackmode ? 0 : 1);
			} else if (command.equals("pop")) {
				pop();
			} else if (command.equals("push")) {
				push(requireString(line, lines[number]));
			} else if (command.equals("copy")) {
				Object object = pop();
				push(object);
				push(object);
			} else if (command.equals("stretch")) {
				Object[] arguments = require(1, line);
				int times = parseInt(arguments[0]);
				arguments = stack.toArray();
				stack.clear();
				for (Object object : arguments) {
					for (int i = 0; i < times; i++) {
						push(object);
					}
				}
			} else if (command.equals("add")) {
				Object[] arguments = require(2, line);
				push(add(arguments[0], arguments[1]));
			} else if (command.equals("subtract")) {
				Object[] arguments = require(2, line);
				push(subtract(arguments[0], arguments[1]));
			} else if (command.equals("multiply")) {
				Object[] arguments = require(2, line);
				push(multiply(arguments[0], arguments[1]));
			} else if (command.equals("divide")) {
				Object[] arguments = require(2, line);
				push(divide(arguments[0], arguments[1]));
			} else if (command.equals("exponentiate")) {
				Object[] arguments = require(2, line);
				push(exponentiate(arguments[0], arguments[1]));
			} else if (command.equals("difference")) {
				Object[] arguments = require(2, line);
				push(absolute_difference(arguments[0], arguments[1]));
			} else if (command.equals("absolutevalue")) {
				Object[] arguments = require(1, line);
				push(absolute_difference(arguments[0], 0));
			} else if (command.equals("print")) {
				System.out.print(Core.unescape(requireString(line, lines[number])));
			} else if (command.equals("printraw")) {
				System.out.print(requireString(line, lines[number]));
			} else if (command.equals("reverse")) {
				push(reverse(requireString(line, lines[number])));
			} else if (command.equals("quit")) {
				System.exit(0);
			} else if (command.equals("if")) {
				Object[] arguments = require(2, line);
				if (parseBool(arguments[0]) == 0) {
					number += parseInt(arguments[1]);
				}
			} else if (command.equals("unescape")) {
				push(Core.unescape(requireString(line, lines[number])));
			} else if (command.equals("loop")) {
				final Object[] arguments = require(2, line);
				final int size = parseInt(arguments[1]);
				new Thread() {
					public void run() {
						for (int i = parseInt(arguments[0]); i > 0; i--) {
							Interpreter.this.run(current + 1, current + size + 1);
						}
					}
				}.start();
				number += size;
			} else if (command.equals("random")) {
				push(Math.random());
			} else if (command.equals("randint")) {
				final Object[] arguments = require(2, line);
				double min = parseDouble(arguments[0]);
				double max = parseDouble(arguments[1]);
				push((int) (Math.random() * (max - min + 1) + min));
			}
		}
	}
}