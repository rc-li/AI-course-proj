package source;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PathLen_checker {
	public static void main(String[] args) throws FileNotFoundException {
//		Scanner sc = new Scanner("output_reference.txt");
//		for (int i=0;i<98;i++) {
//			try {
//				String line = sc.nextLine().trim();
//				System.out.println(line.length());
//			} catch (Exception e) {
//				// TODO: handle exception
//				System.out.println("FAIL");
//				sc.next
//			}
//		}
		
		check();
	}
	
	private static void check() throws FileNotFoundException {
		File output = new File("output.txt");
		File output_reference = new File("output_reference.txt");
		Scanner out = new Scanner(output);
		Scanner ref = new Scanner(output_reference);
		for (int i = 1; i <= 98; i++) {
			String line1 = out.nextLine().trim();
			String line2 = ref.nextLine().trim();
//			System.out.println(line2.length());
			if (line1.length() != line2.length()) {
				System.out.println("doesn't match at line£º" + i + ", ref len = " + line2.length() + ", my len " + line1.length());
			}
		}
		out.close();
		ref.close();
		
	}
}
