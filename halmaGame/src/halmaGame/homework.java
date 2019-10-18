package halmaGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class homework {
	static class State {
		String gameMode;
		String colorUPlay;
		String colorOpponent;
		float timeRemaining;
		char[][] board = new char[16][16];
		ArrayList<int[]> yourMinions = new ArrayList<int[]>();
		ArrayList<int[]> opponentMinions = new ArrayList<int[]>();
		int eval_value;
		
		@Override
		protected Object clone() throws CloneNotSupportedException {
			// TODO Auto-generated method stub
			return super.clone();
		}
	}
	
	static class Node {
		int x;
		int y;
	}
	
	public static void main(String args[]) throws FileNotFoundException, CloneNotSupportedException {
		State state = readInput();
		State[] actions = actions(state);
		System.out.println("hw2 terminated");
	}
	
	private static State readInput() throws FileNotFoundException {
		File file = new File("C:\\Users\\Ruicheng\\Documents\\GitHub\\CSCI-561-updated\\halmaGame\\src\\input.txt");
		State state = new State();
		Scanner s = new Scanner(file);
		state.gameMode = s.next();
		state.colorUPlay = s.next();
		state.timeRemaining = Float.parseFloat(s.next());
		String currString;
		char curr;
		for (int i = 0; i < 16; i++) {
			currString = s.next();
			for (int j = 0; j < 16; j++) {
				curr = currString.charAt(j);
				if (curr == 'B' && state.colorUPlay.equals("BLACK")) {
					int[] coordinate = {j,i};
					state.yourMinions.add(coordinate);
				}
				if (curr == 'B' && state.colorUPlay.equals("WHITE")) {
					int[] coordinate = {j,i};
					state.opponentMinions.add(coordinate);
				}
				if (curr == 'W' && state.colorUPlay.equals("WHITE")) {
					int[] coordinate = {j,i};
					state.yourMinions.add(coordinate);
				}
				if (curr == 'W' && state.colorUPlay.equals("BLACK")) {
					int[] coordinate = {j,i};
					state.opponentMinions.add(coordinate);
				}
				state.board[i][j] = curr;
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
//		make a new state like that 
//		Evaluate it and put the eval value inside the state
//		and add that to collection
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
	
	private static State[] actions(State state) throws CloneNotSupportedException {
		int[][] directions = {{1,1},{1,0},{1,-1},{0,1},{0,-1},{-1,1},{-1,0},{-1,-1}};
		for (int[] minion : state.yourMinions) {
			for (int[] direction : directions) {
				int neighborX = minion[0]+direction[0];
				int neighborY = minion[1]+direction[1];
				if (0 < neighborX && neighborX < 16 && 0 < neighborY && neighborY < 16 ) {
					char neighbor = state.board[neighborX][neighborY];
					if (neighbor == '.') {
						State newState = (State) state.clone();
						newState.board[neighborX][neighborY] = state.board[minion[0]][minion[1]];
						newState.board[minion[0]][minion[1]] = '.';
					}
					
				}
				
			}
		}
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
