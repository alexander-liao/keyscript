package test;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import core.Core;
import core.LevenshteinDistance;

public class Test {
	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("Please place the cursor below this line\n---------------------------------------");
		Thread.sleep(2500);
		String string = " !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
		new Timer().schedule(new TimerTask() {
			public void run() {
				for (char character : string.toCharArray()) {
					Core.sequenceType(Core.typechain(character), 50, 50);
				}
				Core.press(KeyEvent.VK_ENTER);
			}
		}, 2500);
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String compar = reader.readLine();
		Core.release(KeyEvent.VK_ENTER);
		System.out.println("Return Code: " + LevenshteinDistance.levenshteinDistance(string, compar));
		System.exit(0);
	}
}