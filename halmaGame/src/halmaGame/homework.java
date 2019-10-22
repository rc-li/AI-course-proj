package halmaGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class homework {
	private static int jumps = 0;

	static class State implements Cloneable {
		String gameMode;
		String colorUPlay;
		String colorOpponent;
		float timeRemaining;
		char[][] board = new char[16][16];
		ArrayList<int[]> yourMinions = new ArrayList<int[]>();
		ArrayList<int[]> opponentMinions = new ArrayList<int[]>();
		int eval_value;
		int currentX;
		int currentY;
		int neighborX;
		int neighborY;
		int jumpX;
		int jumpY;
		int previousX;
		int previousY;
		ArrayList<int[]> jumped = new ArrayList<int[]>();
		int minionExamined;
		int depthSearched;

		@SuppressWarnings("unchecked")
		@Override
		protected Object clone() throws CloneNotSupportedException {
			// TODO Auto-generated method stub
			State cloned = (State) super.clone();
			cloned.board = cloned.board.clone();
			for (int i = 0; i < 16; i++) {
				cloned.board[i] = cloned.board[i].clone();
			}
//			for (char[] line : cloned.board) {
//				char[] temp = line.clone();
//				line = temp;
//			}

			cloned.yourMinions = (ArrayList<int[]>) cloned.yourMinions.clone();
			for (int i = 0; i < 16; i++) {
				cloned.yourMinions.set(i, cloned.yourMinions.get(i).clone());
//				int[] minion = cloned.yourMinions.get(i);
//				minion = minion.clone();
			}
//			for (int[] minion : cloned.yourMinions) {
//				minion = minion.clone();
//			}

			cloned.opponentMinions = (ArrayList<int[]>) cloned.opponentMinions.clone();
			for (int i = 0; i < 16; i++) {
				cloned.opponentMinions.set(i, cloned.opponentMinions.get(i).clone());
//				int[] minion = cloned.opponentMinions.get(i);
//				minion = minion.clone();
			}
//			for (int[] minion : cloned.opponentMinions) {
//				minion = minion.clone();
//			}
			return cloned;
		}
	}

//	static class Node {
//		int x;
//		int y;
//	}

	public static void main(String args[]) throws FileNotFoundException, CloneNotSupportedException {
		State state = readInput();
//		ArrayList<State> actions = actions(state);
		abSearch(state);
		System.out.println("jumped " + jumps + " times");
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
					int[] coordinate = { j, i };
					state.yourMinions.add(coordinate);
				}
				if (curr == 'B' && state.colorUPlay.equals("WHITE")) {
					int[] coordinate = { j, i };
					state.opponentMinions.add(coordinate);
				}
				if (curr == 'W' && state.colorUPlay.equals("WHITE")) {
					int[] coordinate = { j, i };
					state.yourMinions.add(coordinate);
				}
				if (curr == 'W' && state.colorUPlay.equals("BLACK")) {
					int[] coordinate = { j, i };
					state.opponentMinions.add(coordinate);
				}
				state.board[i][j] = curr;
			}
		}
		s.close();
		return state;
	}

	private static State abSearch(State state) throws CloneNotSupportedException {
		int v = maxValue(state, Integer.MIN_VALUE, Integer.MAX_VALUE);
		ArrayList<State> states = actions(state);
//		for (State oneState : states) {
		for (int i = 0; i < states.size(); i++) {
			State oneState = states.get(i);
			if (oneState.eval_value == v) {
				return oneState;
			}
		}
		return state;
	}

	private static int maxValue(State state, int a, int b) throws CloneNotSupportedException {
		if (state.depthSearched > 5) {
			return state.eval_value;
		}
		int v = Integer.MIN_VALUE;
		ArrayList<State> states = actions(state);
//		for (State nextState : states) {
//		for (State nextState : actions(state)) {
		for (int i = 0; i < states.size(); i++) {
			State nextState = states.get(i);
			v = Math.max(v, minValue(nextState, a, b));
			if (v >= b) {
				return v;
			}
			a = Math.max(a, v);
		}
		return v;
	}

	private static int minValue(State state, int a, int b) throws CloneNotSupportedException {
		if (state.depthSearched > 5) {
			return state.eval_value;
		}
		int v = Integer.MAX_VALUE;
		ArrayList<State> states = actions(state);
//		for (State nextState : states) {
//			for (State nextState : actions(state)) {
		for (int i = 0; i < states.size(); i++) {
			State nextState = states.get(i);
			v = Math.min(v, maxValue(nextState, a, b));
			if (v <= a) {
				return v;
			}
		}
		return v;
	}

	private static ArrayList<State> actions(State state) throws CloneNotSupportedException {
		ArrayList<State> states = new ArrayList<homework.State>();
		int[][] directions = { { 1, 1 }, { 1, 0 }, { 1, -1 }, { 0, 1 }, { 0, -1 }, { -1, 1 }, { -1, 0 }, { -1, -1 } };
		int counter = 0;
//		for (int[] minion : state.yourMinions) {
		for (int i = 0; i < 19; i++) {
			int[] minion = state.yourMinions.get(i);
			state.minionExamined = i;
			for (int[] direction : directions) {
				int currentX = minion[0];
				int currentY = minion[1];
				int neighborX = minion[0] + direction[0];
				int neighborY = minion[1] + direction[1];
				int jumpX = currentX + direction[0] * 2;
				int jumpY = currentY + direction[1] * 2;
				state.currentX = minion[0];
				state.currentY = minion[1];
				state.neighborX = minion[0] + direction[0];
				state.neighborY = minion[1] + direction[1];
				state.jumpX = state.currentX + direction[0] * 2;
				state.jumpY = state.currentY + direction[1] * 2;

//	For each piece, 
//	For each direction
//	if the adjacent is empty, 
//		make a new state like that 
//		Evaluate it and put the eval value inside the state
//		and add that to collection

//				move directly to an adjacent spot
//				if (0 < neighborX && neighborX < 16 && 0 < neighborY && neighborY < 16 ) {
				if (0 <= neighborX && neighborX <= 16 && 0 <= neighborY && neighborY <= 16) {
					char neighbor = state.board[neighborY][neighborX];
					if (neighbor == '.') {
						State newState = (State) state.clone();
						newState.board[neighborY][neighborX] = state.board[minion[1]][minion[0]];
						newState.board[minion[1]][minion[0]] = '.';
//						int[] yourNewMinion = newState.yourMinions.get(i);
						int[] yourNewMinion = { neighborX, neighborY };
//						yourNewMinion[0] = neighborX;
//						yourNewMinion[1] = neighborY;
						newState.yourMinions.set(i, yourNewMinion);

						newState.eval_value = state.eval_value + (neighborX - currentX) + (neighborY - currentY);
						newState.previousX = currentX;
						newState.previousY = currentY;
						newState.currentX = neighborX;
						newState.currentY = neighborY;
						newState.neighborX = -1;
						newState.neighborY = -1;
						newState.jumpX = -1;
						newState.jumpY = -1;
						newState.depthSearched++;
						states.add(newState);

//						System.out.println("moved from "+currentX+","+currentY+" to "+neighborX+","+neighborY);

//						System.out.println("the old board");
//						printBoard(state);
//						System.out.println("the new board");
//						printBoard(newState);
						counter++;
					}
				}
//	Else (if the adjacent is occupied)
//	If the other side is empty
//		Jump(state) 
//
//Jump(state, otherside)
//	Make a new state of the other side and add that to collection
//	Evaluate it and put the eval value inside the state
//	If (an adjacent spot that has not been jumped over is jumpable)
//		Jump(new state, new otherside)
//	wow, just played with git checkout
//				($$$) i can actually use try catch to do this condition, if it saves time
				jump(state, jumpX, jumpY, states);

			}
		}
		System.out.println("minion " + counter + " completed!");
		return states;
	}

//	($$$) here jumpx and jumpY were passed as duplicate
//	($$$) i can actually make jump into a subclass of state
	private static void jump(State state, int jumpX, int jumpY, ArrayList<State> states)
			throws CloneNotSupportedException {
		int currentX = state.currentX;
		int currentY = state.currentY;
		int neighborX = state.neighborX;
		int neighborY = state.neighborY;
		int minionExamined = state.minionExamined;
		if (0 <= jumpX && jumpX <= 16 && 0 <= jumpY && jumpY <= 16) {
			if (state.board[jumpY][jumpX] == '.' && state.board[neighborY][neighborX] != '.') {
				State newState = (State) state.clone();
				newState.board[jumpY][jumpX] = state.board[currentY][currentX];
				newState.board[currentY][currentX] = '.';
				int[] yourNewMinion = { jumpX, jumpY };
				newState.yourMinions.set(minionExamined, yourNewMinion);
				newState.eval_value = state.eval_value + (jumpX - currentX) + (jumpY - currentY);
				newState.previousX = currentX;
				newState.previousY = currentY;
				newState.currentX = jumpX;
				newState.currentY = jumpY;
				newState.neighborX = -1;
				newState.neighborY = -1;
				newState.jumpX = -1;
				newState.jumpY = -1;
				newState.depthSearched++;
				int[] previousLocation = { newState.previousX, newState.previousY };
				newState.jumped.add(previousLocation);
				states.add(newState);

				System.out.println("minion " + state.minionExamined + " can jump from " + currentX + "," + currentY
						+ " to " + jumpX + "," + jumpY + " with eval value " + newState.eval_value);
				jumps++;

				jump(newState, jumpX, jumpY, states);
			}
		}

		if (state.jumpX == -1 && state.jumpY == -1) {
			int[][] directions = { { 1, 1 }, { 1, 0 }, { 1, -1 }, { 0, 1 }, { 0, -1 }, { -1, 1 }, { -1, 0 },
					{ -1, -1 } };
			for (int[] direction : directions) {
				neighborX = state.currentX + direction[0];
				neighborY = state.currentY + direction[1];
				jumpX = state.currentX + direction[0] * 2;
				jumpY = state.currentY + direction[1] * 2;
				int[] currentJump = { jumpX, jumpY };
				boolean skipDirection = false;
				for (int[] jump : state.jumped) {
					if (Arrays.equals(jump, currentJump)) {
						skipDirection = true;
					}
				}
				if (skipDirection) {
					continue;
				}
				if (!(jumpX == state.previousX && jumpY == state.previousY)) {
					if (0 <= jumpX && jumpX <= 16 && 0 <= jumpY && jumpY <= 16) {
						if (state.board[jumpY][jumpX] == '.' && state.board[neighborY][neighborX] != '.') {
							State newState = (State) state.clone();
							newState.board[jumpY][jumpX] = state.board[currentY][currentX];
							newState.board[currentY][currentX] = '.';
							int[] yourNewMinion = { jumpX, jumpY };
							newState.yourMinions.set(minionExamined, yourNewMinion);
							newState.eval_value = state.eval_value + (jumpX - currentX) + (jumpY - currentY);
							newState.previousX = currentX;
							newState.previousY = currentY;
							newState.currentX = jumpX;
							newState.currentY = jumpY;
							newState.neighborX = -1;
							newState.neighborY = -1;
							newState.jumpX = -1;
							newState.jumpY = -1;
							newState.depthSearched++;
							int[] previousLocation = { newState.previousX, newState.previousY };
							newState.jumped.add(previousLocation);
							states.add(newState);

							System.out.println(
									"minion " + state.minionExamined + " can jump from " + currentX + "," + currentY
											+ " to " + jumpX + "," + jumpY + " with eval value " + newState.eval_value);
							jumps++;

							jump(newState, jumpX, jumpY, states);
						}
					}

				}
			}
		}

	}

	private static void printBoard(State state) {
		for (char[] line : state.board) {
			System.out.println(line);
		}
	}

	private static void output() {

	}
}
