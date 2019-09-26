package source;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class BFS_pathLen_checker {
	public static void main(String[] args) throws FileNotFoundException {
		File output = new File("output.txt");
		File output_reference = new File("output_reference.txt");
		Scanner out = new Scanner(output);
		Scanner ref = new Scanner(output_reference);
		for (int i = 0; i < 98; i++) {
			String line1 = out.nextLine().trim();
			String line2 = ref.nextLine().trim();
			if (line1.length() != line2.length()) {
				System.out.println("doesn't match at line£º" + i);
			}
		}
		out.close();
		ref.close();
		System.out.println("they prob have the same length");
	}
}
