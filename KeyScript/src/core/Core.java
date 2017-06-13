package core;

import static java.awt.event.KeyEvent.VK_0;
import static java.awt.event.KeyEvent.VK_1;
import static java.awt.event.KeyEvent.VK_2;
import static java.awt.event.KeyEvent.VK_3;
import static java.awt.event.KeyEvent.VK_4;
import static java.awt.event.KeyEvent.VK_5;
import static java.awt.event.KeyEvent.VK_6;
import static java.awt.event.KeyEvent.VK_7;
import static java.awt.event.KeyEvent.VK_8;
import static java.awt.event.KeyEvent.VK_9;
import static java.awt.event.KeyEvent.VK_BACK_QUOTE;
import static java.awt.event.KeyEvent.VK_BACK_SLASH;
import static java.awt.event.KeyEvent.VK_CLOSE_BRACKET;
import static java.awt.event.KeyEvent.VK_COMMA;
import static java.awt.event.KeyEvent.VK_EQUALS;
import static java.awt.event.KeyEvent.VK_MINUS;
import static java.awt.event.KeyEvent.VK_OPEN_BRACKET;
import static java.awt.event.KeyEvent.VK_PERIOD;
import static java.awt.event.KeyEvent.VK_QUOTE;
import static java.awt.event.KeyEvent.VK_SEMICOLON;
import static java.awt.event.KeyEvent.VK_SHIFT;
import static java.awt.event.KeyEvent.VK_SLASH;
import static java.awt.event.KeyEvent.VK_SPACE;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Core {
	private static Robot robot;
	public static BufferedReader reader;

	static {
		reader = new BufferedReader(new InputStreamReader(System.in));
	}

	private Core() {

	}

	static {
		try {
			Core.robot = new Robot();
		} catch (AWTException e) {
			System.err.println("The robot could not be initialized; thus, execution will not work.");
			System.exit(-1);
		}
	}

	public static final int[] location() {
		Point location = MouseInfo.getPointerInfo().getLocation();
		return new int[] { location.x, location.y };
	}

	public static final void mouseDown(int button) {
		robot.mousePress(button);
	}

	public static final void mouseUp(int button) {
		robot.mouseRelease(button);
	}

	public static final void mouseClick(int button, long delay) {
		mouseDown(button);
		safeDelay(delay);
		mouseUp(button);
	}

	public static final void mouseMove(int x, int y) {
		mouseMove(x, y, 1, 0);
	}

	public static final void mouseDrag(int button, int x, int y) {
		mouseDrag(button, x, y, 1, 0, 0, 0);
	}

	public static final void mouseDrag(int button, int x, int y, int steps, long downdelay, long delay, long updelay) {
		mouseDown(button);
		safeDelay(downdelay);
		mouseMove(x, y, steps, delay);
		safeDelay(updelay);
		mouseUp(button);
	}

	public static final void mouseMove(int x, int y, int steps, long delay) {
		int[] location = location();
		float dx = (float) (x - location[0]) / (float) steps;
		float dy = (float) (y - location[1]) / (float) steps;
		for (int i = 1; i <= steps; i++) {
			robot.mouseMove((int) (dx * i + location[0]), (int) (dy * i + location[1]));
			safeDelay(delay);
		}
	}

	public static final void sequencePress(int[] keys, long delay) {
		for (int key : keys) {
			press(key);
			safeDelay(delay);
		}
	}

	public static final void sequenceRelease(int[] keys, long delay) {
		for (int i = keys.length - 1; i >= 0; i--) {
			release(keys[i]);
			safeDelay(delay);
		}
	}

	public static final void sequenceType(int[] keys, long downdelay, long updelay) {
		sequencePress(keys, downdelay);
		sequenceRelease(keys, updelay);
	}

	public static final void type(int key, long delay) {
		press(key);
		safeDelay(delay);
		release(key);
	}

	public static final void press(int key) {
		robot.keyPress(key);
	}

	public static final void release(int key) {
		robot.keyRelease(key);
	}

	public static final int[] typechain(char character) {
		int SHIFT = VK_SHIFT;
		if (character == ' ') {
			return new int[] { VK_SPACE };
		} else if (character >= 'a' && character <= 'z' || character >= '0' && character <= '9') {
			return new int[] { Character.toUpperCase(character) };
		} else if (character >= 'A' && character <= 'Z') {
			return new int[] { SHIFT, character };
		} else {
			int[][] output = new int[128][];
			System.arraycopy(new int[][] { { SHIFT, VK_1 }, { SHIFT, VK_QUOTE }, { SHIFT, VK_3 }, { SHIFT, VK_4 },
					{ SHIFT, VK_5 }, { SHIFT, VK_7 }, { VK_QUOTE }, { SHIFT, VK_9 }, { SHIFT, VK_0 }, { SHIFT, VK_8 },
					{ SHIFT, VK_EQUALS }, { VK_COMMA }, { VK_MINUS }, { VK_PERIOD }, { VK_SLASH } }, 0, output, 33, 15);
			System.arraycopy(new int[][] { { SHIFT, VK_SEMICOLON }, { VK_SEMICOLON }, { SHIFT, VK_COMMA },
					{ VK_EQUALS }, { SHIFT, VK_PERIOD }, { SHIFT, VK_SLASH }, { SHIFT, VK_2 } }, 0, output, 58, 7);
			System.arraycopy(new int[][] { { VK_OPEN_BRACKET }, { VK_BACK_SLASH }, { VK_CLOSE_BRACKET },
					{ SHIFT, VK_6 }, { SHIFT, VK_MINUS }, { VK_BACK_QUOTE } }, 0, output, 91, 6);
			System.arraycopy(new int[][] { { SHIFT, VK_OPEN_BRACKET }, { SHIFT, VK_BACK_SLASH },
					{ SHIFT, VK_CLOSE_BRACKET }, { SHIFT, VK_BACK_QUOTE } }, 0, output, 123, 4);
			try {
				return output[character];
			} catch (IndexOutOfBoundsException e) {
				return new int[0];
			}
		}
	}

	public static final void safeDelay(long delay) {
		while (delay >= 0) {
			try {
				Thread.sleep(1);
				delay--;
			} catch (InterruptedException e) {
				System.err.println("InterruptedException: Resetting 1 millisecond");
			}
		}
	}

	public static final String unescape(String string) {
		StringBuffer sbuf = new StringBuffer();
		boolean escapemode = false;
		for (char c : string.toCharArray()) {
			if (escapemode) {
				if (c == 's') {
					sbuf.append(' ');
				} else if (c == 'b') {
					sbuf.append('\b');
				} else if (c == 't') {
					sbuf.append('\t');
				} else if (c == 'n') {
					sbuf.append('\n');
				} else if (c == 'f') {
					sbuf.append('\f');
				} else if (c == 'r') {
					sbuf.append('\r');
				} else if (c == '\"') {
					sbuf.append('\"');
				} else if (c == '\'') {
					sbuf.append('\'');
				} else if (c == '\\') {
					sbuf.append('\\');
				}
				escapemode = false;
			} else {
				if (c == '\\') {
					escapemode = true;
				} else {
					sbuf.append(c);
				}
			}
		}
		return sbuf.toString();
	}
}