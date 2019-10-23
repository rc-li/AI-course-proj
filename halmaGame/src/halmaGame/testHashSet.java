package halmaGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class testHashSet {
	public static void main(String args[]) {
		HashSet<int[]> set = new HashSet<int[]>();
		int[] a = {1,2};
		int[] b = {1,2};
		System.out.println("a == b? "+ (a==b));
		
		set.add(a);
		System.out.println("set contains a? "+set.contains(a));
		System.out.println("set contains b? "+set.contains(b));
		
		HashSet<String> stringset = new HashSet<String>();
		String c = "hello";
		String d = "hello";
		stringset.add(c);
		System.out.println("c == d? "+ (c==d));
		System.out.println("set contains c? "+stringset.contains(c));
		System.out.println("set contains d? "+stringset.contains(d));
		
		System.out.println("a.equals b? "+ a.equals(b));
		
		System.out.println("Arrays.equals(a,b)? "+Arrays.equals(a, b));
		
		ArrayList<int[]> list = new ArrayList<int[]>();
		list.add(a);
		list.add(b);
		for (int[] is : list) {
			System.out.println(is);
		}
//		
//		int e = 10;
//		int f = -e;
//		System.out.println(f);
		
		int[] g = {1,2};
		
		String str = "hello";
		System.out.println(str=="hello");
		
		int[] obj1 = {1,2};
		int[] obj2 = obj1;
		obj1[1] = 100;
		System.out.println("value of obj2 is "+ obj2[1]);
		
		System.out.println("c equals d? "+ c.equals(d));
	}
}
