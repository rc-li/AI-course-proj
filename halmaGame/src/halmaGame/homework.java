package halmaGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class homework {
	static class State {
		String gameMode;
		String colorUPlay;
		float timeRemaining;
		char[][] board = new char[16][16];
		int eval_value;
		
	}
	
	public static void main(String args[]) throws FileNotFoundException {
		State state = readInput();
		System.out.println("hw2 terminated");
	}
	
	private static State readInput() throws FileNotFoundException {
		File file = new File("C:\\Users\\Ruicheng\\Documents\\GitHub\\CSCI-561-updated\\halmaGame\\src\\input.txt");
		State state = new State();
		Scanner s = new Scanner(file);
		state.gameMode = s.next();
		state.colorUPlay = s.next();
		state.timeRemaining = Float.parseFloat(s.next());
		String temp;
		for (int i = 0; i < 16; i++) {
			temp = s.next();
			for (int j = 0; j < 16; j++) {
				state.board[i][j] = temp.charAt(j);
			}
		}
		s.close();
		return state;
	}
	
	private static State abSearch(State state) {
		int v = maxValue(state, Integer.MIN_VALUE, Integer.MAX_VALUE);
		return state;
	}
	
//	For each piece, 
//	For each direction
//	if the adjacent is empty, 
//		make a new state like that and add that to collection
//		Evaluate it and put the eval value inside the state
//	Else (if the adjacent is occupied)
//	If the other side is empty
//		Jump(state) 
//
//Jump(state, otherside)
//	Make a new state of the other side that and add that to collection
//	Evaluate it and put the eval value inside the state
//	If (an adjacent spot that has not been jumped over is jumpable)
//		Jump(new state, new otherside)
//	wow, just played with git checkout
	
	private static State[] actions(State state) {
		
		return null;
	}
	
	private static int maxValue(State state, int a, int b) {
		int v = Integer.MAX_VALUE;
		return v;
	}
	
	private static int minValue(State state, int a, int b) {
		int v = Integer.MAX_VALUE;
		return v;
	}
	
	private static void output() {
		
	}
}
