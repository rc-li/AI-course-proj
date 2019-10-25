package halmaGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class homework {
	private static int jumps = 0;
	private static int searchDepth = 1;
	private static String whichPlayer;

	static class State implements Cloneable {
		String gameMode;
		String colorUPlay;
		String colorOpponent;
		float timeRemaining;
		char[][] board = new char[16][16];
		ArrayList<int[]> yourMinions = new ArrayList<int[]>();
		ArrayList<int[]> opponentMinions = new ArrayList<int[]>();
		ArrayList<int[]> insideCamp = new ArrayList<int[]>();
		int eval_value;
		int currentX;
		int currentY;
		int neighborX;
		int neighborY;
		int jumpX;
		int jumpY;
		int previousX;
		int previousY;
		int previousDestX;
		int previousDestY;
		int jumpStartX;
		int jumpStartY;
		char moveMode;
		ArrayList<int[]> jumped = new ArrayList<int[]>();
		int minionExamined;
		int depthSearched;
		int v;
		State child;

		@SuppressWarnings("unchecked")
		@Override
		protected Object clone() throws CloneNotSupportedException {
			// TODO Auto-generated method stub
			State cloned = (State) super.clone();
			cloned.board = cloned.board.clone();
			for (int i = 0; i < 16; i++) {
				cloned.board[i] = cloned.board[i].clone();
			}

			cloned.yourMinions = (ArrayList<int[]>) cloned.yourMinions.clone();
			for (int i = 0; i < 16; i++) {
				cloned.yourMinions.set(i, cloned.yourMinions.get(i).clone());
			}

			cloned.opponentMinions = (ArrayList<int[]>) cloned.opponentMinions.clone();
			for (int i = 0; i < 16; i++) {
				cloned.opponentMinions.set(i, cloned.opponentMinions.get(i).clone());
			}
			return cloned;
		}
	}

	public static void main(String args[]) throws FileNotFoundException, CloneNotSupportedException {
		State state = readInput();
		State output = abSearch(state);
		output(output);
		System.out.println("jumped " + jumps + " times");
		System.out.println("hw2 terminated");
	}

	private static State readInput() throws FileNotFoundException {
		File file = new File("C:\\Users\\Ruicheng\\Documents\\GitHub\\CSCI-561-updated\\halmaGame\\src\\input.txt");
		State state = new State();
		Scanner s = new Scanner(file);
		state.gameMode = s.next();
		state.colorUPlay = s.next();
		if (state.colorUPlay.equals("WHITE")) {
			state.colorOpponent = "BLACK";
		} else {
			state.colorOpponent = "WHITE";
		}
		whichPlayer = state.colorUPlay;
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

	private static void printBoard(State state) {
		for (char[] line : state.board) {
			System.out.println(line);
		}
	}

	private static void output(State state) throws FileNotFoundException {
		File file = new File("C:\\Users\\Ruicheng\\Documents\\GitHub\\CSCI-561-updated\\halmaGame\\src\\output.txt");
		PrintWriter printer = new PrintWriter(file);
		char moveMode = state.moveMode;
		printer.write(moveMode + " " + state.previousX + "," + state.previousY + " " + state.previousDestX + ","
				+ state.previousDestY);
		printer.close();

	}

	private static State abSearch(State state) throws CloneNotSupportedException {
		State ret = maxValue(state, Integer.MIN_VALUE, Integer.MAX_VALUE);
		return ret.child;
	}

	private static State maxValue(State state, int a, int b) throws CloneNotSupportedException {
		if (state.depthSearched >= searchDepth) {
			state.v = state.eval_value;
			return state;
		}
		state.v = Integer.MIN_VALUE;
		ArrayList<State> states = actions(state);
		for (int i = 0; i < states.size(); i++) {
			State nextState = states.get(i);
			State retState = minValue(nextState, a, b);
			int retV = retState.v;
			state.v = Math.max(state.v, retV);
			if (state.v >= b) {
				for (State state2 : states) {
					if (state2.v == state.v) {
						state.child = state2;
						return state;
					}
				}
			}
			a = Math.max(a, state.v);
		}
		for (State state2 : states) {
			if (state2.v == state.v) {
				state.child = state2;
				return state;
			}
		}
		return null;
	}

	private static State minValue(State state, int a, int b) throws CloneNotSupportedException {
		if (state.depthSearched >= searchDepth) {
			state.v = state.eval_value;
			return state;
		}
		state.v = Integer.MAX_VALUE;
		ArrayList<State> states = actions(state);
		for (int i = 0; i < states.size(); i++) {
			State nextState = states.get(i);
			State retState = maxValue(nextState, a, b);
			int retV = retState.v;
			state.v = Math.min(state.v, retV);
			if (state.v <= a) {
				for (State state2 : states) {
					if (state2.v == state.v) {
						state.child = state2;
						return state;
					}
				}
			}
			b = Math.min(b, state.v);
		}
		for (State state2 : states) {
			if (state2.v == state.v) {
				state.child = state2;
				return state;
			}
		}
		return null;
	}

	private static ArrayList<State> actions(State state) throws CloneNotSupportedException {
		ArrayList<State> states = new ArrayList<homework.State>();
		int[][] directions = { { 1, 1 }, { 1, 0 }, { 1, -1 }, { 0, 1 }, { 0, -1 }, { -1, 1 }, { -1, 0 }, { -1, -1 } };
//		int[][] directions;
//		int[][] directionWHITE = { { -1, 0 }, { -1, -1 }, { 0, -1 } };
//		int[][] directionBLACK = { { 1, 0 }, { 1, 1 }, { 0, 1 } };
//		if (whichPlayer == "WHITE") {
//			directions = directionWHITE;
//		} else {
//			directions = directionBLACK;
//		}
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
				state.jumpStartX = currentX;
				state.jumpStartY = currentY;

				if (0 <= neighborX && neighborX < 16 && 0 <= neighborY && neighborY < 16) {
					char neighbor = state.board[neighborY][neighborX];
					if (neighbor == '.') {
						State newState = (State) state.clone();
						newState.board[neighborY][neighborX] = state.board[minion[1]][minion[0]];
						newState.board[minion[1]][minion[0]] = '.';
						int[] yourNewMinion = { neighborX, neighborY };
						newState.yourMinions.set(i, yourNewMinion);

						if (state.colorUPlay.equals(whichPlayer)) {
							if (state.colorUPlay.equals("WHITE")) {
								newState.eval_value = state.eval_value
										- ((neighborX + neighborY) - (currentX + currentY));
							} else if (state.colorUPlay.equals("BLACK")) {
								newState.eval_value = state.eval_value
										+ ((neighborX + neighborY) - (currentX + currentY));
							}
						}
						newState.previousX = currentX;
						newState.previousY = currentY;
						newState.previousDestX = neighborX;
						newState.previousDestY = neighborY;
						newState.depthSearched++;
						newState.moveMode = 'E';
//						expand neibor need to add it to jumped too
						int[] previousLocation = { currentX, currentY };
						newState.jumped.add(previousLocation);
						newState.colorUPlay = state.colorOpponent;
						newState.colorOpponent = state.colorUPlay;
						ArrayList<int[]> temp = newState.yourMinions;
						newState.yourMinions = newState.opponentMinions;
						newState.opponentMinions = temp;
						states.add(newState);

//						System.out.println("moved from "+currentX+","+currentY+" to "+neighborX+","+neighborY);
//
//						System.out.println("the old board");
//						printBoard(state);
//						System.out.println("the new board");
//						printBoard(newState);
					}
				}
//				($$$) i can actually use try catch to do this condition, if it saves time
				jump(state, jumpX, jumpY, states);

			}
		}
//		System.out.println("minion " + counter + " completed!");
		return states;
	}

//	($$$) here jumpx and jumpY were passed as duplicate
//	($$$) i can actually make jump into a subclass of state
	private static void jump(State state, int jumpX, int jumpY, ArrayList<State> states)
			throws CloneNotSupportedException {
//		spawning states of of jumping from where we 'E'ed in actions()
		int currentX = state.currentX;
		int currentY = state.currentY;
		int neighborX = state.neighborX;
		int neighborY = state.neighborY;
		int minionExamined = state.minionExamined;
		if (0 <= jumpX && jumpX < 16 && 0 <= jumpY && jumpY < 16) {
//			verify if it's a valid jump
			if (state.board[jumpY][jumpX] == '.' && state.board[neighborY][neighborX] != '.') {
				State newState = (State) state.clone();
				newState.board[jumpY][jumpX] = state.board[currentY][currentX];
				newState.board[currentY][currentX] = '.';
				int[] yourNewMinion = { jumpX, jumpY };
				newState.yourMinions.set(minionExamined, yourNewMinion);
				if (state.colorUPlay.equals(whichPlayer)) {
					if (state.colorUPlay.equals("WHITE")) {
						newState.eval_value = state.eval_value - ((jumpX + jumpY) - (currentX + currentY));
					} else if (state.colorUPlay.equals("BLACK")) {
						newState.eval_value = state.eval_value + ((jumpX + jumpY) - (currentX + currentY));
					}
				}
				newState.previousX = currentX;
				newState.previousY = currentY;
				newState.previousDestX = jumpX;
				newState.previousDestY = jumpY;
				newState.currentX = jumpX;
				newState.currentY = jumpY;
				newState.jumpX = -100;
				newState.jumpY = -100;
				newState.moveMode = 'J';
				newState.depthSearched++;
				newState.colorUPlay = state.colorOpponent;
				newState.colorOpponent = state.colorUPlay;
				int[] previousLocation = { currentX, currentY };
				newState.jumped.add(previousLocation);
				ArrayList<int[]> temp = newState.yourMinions;
				newState.yourMinions = newState.opponentMinions;
				newState.opponentMinions = temp;
				states.add(newState);

				System.out.println("minion " + state.minionExamined + " can jump from " + currentX + "," + currentY
						+ " to " + jumpX + "," + jumpY + " with eval value " + newState.eval_value);
				jumps++;

//				jump(newState, jumpX, jumpY, states);
				jump(newState, -100, -100, states);
			}
		}
//		insdie the recursive jump call
		if (state.jumpX == -100 && state.jumpY == -100) {
			int[][] directions = { { 1, 1 }, { 1, 0 }, { 1, -1 }, { 0, 1 }, { 0, -1 }, { -1, 1 }, { -1, 0 },
					{ -1, -1 } };
//			int[][] directions;
//			int[][] directionWHITE = { { -1, 0 }, { -1, -1 }, { 0, -1 } };
//			int[][] directionBLACK = { { 1, 0 }, { 1, 1 }, { 0, 1 } };
//			if (whichPlayer == "WHITE") {
//				directions = directionWHITE;
//			} else {
//				directions = directionBLACK;
//			}
			String t = state.colorUPlay;
			state.colorUPlay = state.colorOpponent;
			state.colorOpponent = t;
			
//			did i forget to switch the minions back?
			ArrayList<int[]> temp = state.yourMinions;
			state.yourMinions = state.opponentMinions;
			state.opponentMinions = temp;

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
//				previous state checking failed, so there is infinite loop
//				if (!(jumpX == state.previousX && jumpY == state.previousY)) {
				if (0 <= jumpX && jumpX < 16 && 0 <= jumpY && jumpY < 16) {
					if (state.board[jumpY][jumpX] == '.' && state.board[neighborY][neighborX] != '.') {
						State newState = (State) state.clone();
						newState.board[jumpY][jumpX] = state.board[currentY][currentX];
						newState.board[currentY][currentX] = '.';
						int[] yourNewMinion = { jumpX, jumpY };
						newState.yourMinions.set(minionExamined, yourNewMinion);
						if (state.colorUPlay.equals(whichPlayer)) {
							if (state.colorUPlay.equals("WHITE")) {
								newState.eval_value = state.eval_value - ((jumpX + jumpY) - (currentX + currentY));
							} else if (state.colorUPlay.equals("BLACK")) {
								newState.eval_value = state.eval_value + ((jumpX + jumpY) - (currentX + currentY));
							}
						}
//						newState.previousX = currentX;
//						newState.previousY = currentY;
						
//						update currentX is necessary for next recursive call of jump()
						newState.currentX = jumpX;
						newState.currentY = jumpY;
						newState.previousX = state.jumpStartX;
						newState.previousY = state.jumpStartY;
						newState.previousDestX = jumpX;
						newState.previousDestY = jumpY;
						newState.moveMode = 'J';
						newState.depthSearched++;
//							int[] previousLocation = { newState.previousX, newState.previousY };
						int[] previousLocation = { currentX, currentY };
						newState.jumped.add(previousLocation);
						newState.colorUPlay = state.colorOpponent;
						newState.colorOpponent = state.colorUPlay;
						temp = newState.yourMinions;
						newState.yourMinions = newState.opponentMinions;
						newState.opponentMinions = temp;
						states.add(newState);

						System.out.println("minion " + state.minionExamined + " can jump from " + currentX + ","
								+ currentY + " to " + jumpX + "," + jumpY + " with eval value " + newState.eval_value);
						jumps++;

						jump(newState, -100, -100, states);
					}
				}

//				}
			}
		}

	}
}
