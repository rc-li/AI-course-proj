package halmaGame;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class homework {
	private static int jumps = 0;
	private static int searchDepth = 5;
	private static String whichPlayer;
	private static int[][] blackCampLocations = { { 0, 0 }, { 1, 0 }, { 2, 0 }, { 3, 0 }, { 4, 0 }, { 0, 1 }, { 1, 1 },
			{ 2, 1 }, { 3, 1 }, { 4, 1 }, { 0, 2 }, { 1, 2 }, { 2, 2 }, { 3, 2 }, { 0, 3 }, { 1, 3 }, { 2, 3 },
			{ 0, 4 }, { 1, 4 } };
	private static int[][] whiteCampLocations = { { 14, 11 }, { 15, 11 }, { 13, 12 }, { 14, 12 }, { 15, 12 },
			{ 12, 13 }, { 13, 13 }, { 14, 13 }, { 15, 13 }, { 11, 14 }, { 12, 14 }, { 13, 14 }, { 14, 14 }, { 15, 14 },
			{ 11, 15 }, { 12, 15 }, { 13, 15 }, { 14, 15 }, { 15, 15 } };

	private static DecimalFormat df = new DecimalFormat("#.##");
	
	static class State implements Cloneable {
		String gameMode;
		String colorUPlay;
		String colorOpponent;
		float timeRemaining;
		double eval_value;
		int currentX;
		int currentY;
		int neighborX;
		int neighborY;
		int jumpX;
		int jumpY;
		int jumpStartX;
		int jumpStartY;
		char moveMode;
		int minionExamined;
		int depthSearched;
		double v;
		State child;
		char[][] board = new char[16][16];
		ArrayList<int[]> yourMinions = new ArrayList<int[]>();
		ArrayList<int[]> opponentMinions = new ArrayList<int[]>();
		ArrayList<int[]> insideCamp = new ArrayList<int[]>();
		ArrayList<int[]> jumped = new ArrayList<int[]>();
		ArrayList<int[]> path = new ArrayList<int[]>();

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

			cloned.jumped = (ArrayList<int[]>) cloned.jumped.clone();
			int jumpedSize = jumped.size();
			for (int i = 0; i < jumpedSize; i++) {
				cloned.jumped.set(i, cloned.jumped.get(i).clone());
			}
			cloned.path = (ArrayList<int[]>) cloned.path.clone();
			int pathSize = path.size();
			for (int i = 0; i < pathSize; i++) {
				cloned.path.set(i, cloned.path.get(i).clone());
			}
			cloned.insideCamp = (ArrayList<int[]>) cloned.insideCamp.clone();
			int campSize = insideCamp.size();
			for (int i = 0; i < campSize; i++) {
				cloned.insideCamp.set(i, cloned.insideCamp.get(i).clone());
			}
			return cloned;
		}
	}

	static class Scan {
		ArrayList<int[]> blocks = new ArrayList<int[]>();
		ArrayList<int[]> insiders = new ArrayList<int[]>();
		ArrayList<int[]> outsiders = new ArrayList<int[]>();
	}
	
	public static void main(String args[]) throws FileNotFoundException, CloneNotSupportedException {
		State state = readInput();
		Scan scan = lateGameScan(state);
		if (scan.outsiders.size() > 4) {
			State output = abSearch(state);
			output(output);
		}
		else {
			int[][] move = lateGameMove(state, scan);
			File file = new File("C:\\Users\\Ruicheng\\Documents\\GitHub\\CSCI-561-updated\\halmaGame\\src\\output.txt");
			PrintWriter printer = new PrintWriter(file);
			printer.write("E " + move[0][0] + "," + move[0][1] + " " + move[1][0] + "," + move[1][1]);
			printer.close();
		}
	}
	
	private static int[][] lateGameMove(State state, Scan scan) {
//			pick the minion and the block to move
		int[] pickMinion = scan.outsiders.get(0);
		int[] pickBlock = scan.blocks.get(0);
		double minDistance = Point.distance(pickMinion[0], pickMinion[1], pickBlock[0], pickBlock[1]);
		double distance;
		int[] block = new int[2];
		int[] minion = new int[2];
		
		for (int i = 0; i < scan.outsiders.size(); i++) {
			minion = scan.outsiders.get(i);
			for (int i1 = 0; i1 < scan.blocks.size(); i1++) {
				block = scan.blocks.get(i1);
				distance = Point.distance(minion[0], minion[1], block[0], block[1]);
				if (distance < minDistance) {
					pickBlock = block;
					pickMinion = minion;
					minDistance = distance;
				}
			}
			
		}
//			determine direction to move the block
		int[] direction = new int[2];
		if (pickMinion[0] - pickBlock[0] < 0) {
			direction[0] = -1;
		}
		else if (pickMinion[0] - pickBlock[0] > 0) {
			direction[0] = 1;
		}
		else {
			direction[0] = 0;
		}
		
		if (pickMinion[1] - pickBlock[1] < 0) {
			direction[1] = -1;
		}
		else if (pickMinion[1] - pickBlock[1] > 0) {
			direction[1] = 1;
		}
		else {
			direction[1] = 0;
		}
		
		int[] pieceToMove = {pickBlock[0] + direction[0], pickBlock[1] + direction[1]};
		
//			check if pieceToMove is inside the camp
		boolean isInside = false;
		if (state.colorUPlay.equals("WHITE")) {
			for (int[] camp : blackCampLocations) {
				if (Arrays.equals(camp, pieceToMove)) {
					isInside = true;
					break;
				}
			}
		}
		else if (state.colorUPlay.equals("BLACK")) {
			for (int[] camp : whiteCampLocations) {
				if (Arrays.equals(camp,pieceToMove)) {
					isInside = true;
					break;
				}
			}
		}
		
		
//		if pieceToMove is inside the camp, return the block move
		if (isInside) {
			int[][] ret = {{pickBlock[0] + direction[0], pickBlock[1] + direction[1]}, {pickBlock[0], pickBlock[1]}};
			return ret;
		}
		else {
			int[][] ret = {{pickMinion[0],pickMinion[1]},{pickMinion[0]-direction[0],pickMinion[1]-direction[1]}};
			return ret;
		}
	}
	
	private static Scan lateGameScan(State state){
		Scan s = new Scan();
		if (state.colorUPlay.equals("WHITE")) {
			for (int[] camp : blackCampLocations) {
				if (state.board[camp[1]][camp[0]] == '.') {
					s.blocks.add(camp);
				}
				else if (state.board[camp[1]][camp[0]] == 'W') {
					s.insiders.add(camp);
				}
			}
		}
		else if (state.colorUPlay.equals("BLACK")) {
			for (int[] camp : whiteCampLocations) {
				if (state.board[camp[1]][camp[0]] == '.') {
					s.blocks.add(camp);
				}
				else if (state.board[camp[1]][camp[0]] == 'B') {
					s.insiders.add(camp);
				}
			}
		}
		for (int[] minion : state.yourMinions) {
			boolean isInside = false;
			for (int[] camp : s.insiders) {
				if (Arrays.equals(camp, minion)) {
					isInside = true;
					break;
				}
			}
			if (!isInside) {
				s.outsiders.add(minion);
			}
		}
		return s;
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
		ArrayList<int[]> jumped = state.child.path;
//		int[] lastPair = { state.child.currentX, state.child.currentY};
//		jumped.add(lastPair);
		int jumpedSize = jumped.size();
		for (int i = 0; i < jumpedSize - 1; i++) {
			if (i < jumpedSize - 1) {
				printer.write(state.child.moveMode + " " + jumped.get(i)[0] + "," + jumped.get(i)[1] + " "
						+ jumped.get(i + 1)[0] + "," + jumped.get(i + 1)[1] + "\n");
			} else {
				printer.write(state.child.moveMode + " " + jumped.get(i)[0] + "," + jumped.get(i)[1] + " "
						+ jumped.get(i + 1)[0] + "," + jumped.get(i + 1)[1]);
			}
		}
		printer.close();

	}

	private static State abSearch(State state) throws CloneNotSupportedException {
		State ret = maxValue(state, -2136483647.0, 2136483647.0);
		return ret;
	}

	private static State maxValue(State state, Double a, Double b) throws CloneNotSupportedException {
//		System.out.println("*** MaxValue is playing " + state.colorUPlay);
		if (state.depthSearched >= searchDepth) {
			state.v = state.eval_value;
			return state;
		}
		state.v = Integer.MIN_VALUE;
		ArrayList<State> states = actions(state, true, false, false);
		for (int i = 0; i < states.size(); i++) {
			State nextState = states.get(i);
			State retState = minValue(nextState, a, b);
			double retV = retState.v;
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

	private static State minValue(State state, Double a, Double b) throws CloneNotSupportedException {
//		System.out.println("*** MinValue is playing " + state.colorUPlay);
		if (state.depthSearched >= searchDepth) {
			state.v = state.eval_value;
			return state;
		}
		state.v = Integer.MAX_VALUE;
		ArrayList<State> states = actions(state, true, false, false);
		for (int i = 0; i < states.size(); i++) {
			State nextState = states.get(i);
			State retState = maxValue(nextState, a, b);
			double retV = retState.v;
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

	private static ArrayList<State> actions(State state, boolean needCampCheck, boolean needMoveAway, boolean outOfCampSearch)
			throws CloneNotSupportedException {
		ArrayList<State> states = new ArrayList<homework.State>();
		int[][] directions = { { 1, 1 }, { 1, 0 }, { 1, -1 }, { 0, 1 }, { 0, -1 }, { -1, 1 }, { -1, 0 }, { -1, -1 } };
		int[][] directionWHITE = { { -1, 0 }, { -1, -1 }, { 0, -1 }, { 1, -1 }, { -1, 1 } };
		int[][] directionBLACK = { { 1, 0 }, { 1, 1 }, { 0, 1 }, { 1, -1 }, { -1, 1 } };
		if (state.colorUPlay.equals("WHITE")) {
			directions = directionWHITE;
		} else {
			directions = directionBLACK;
		}

//		check if there is still minion in camp
		boolean campEmpty = true;
		int[][] yourCamp = null;
		int[][] opponentCamp = null;
		if (state.colorUPlay.equals("WHITE")) {
			yourCamp = whiteCampLocations;
			opponentCamp = blackCampLocations;
		} else if (state.colorUPlay.equals("BLACK")) {
			yourCamp = blackCampLocations;
			opponentCamp = whiteCampLocations;
		}
		if (needCampCheck && !needMoveAway) {
			for (int[] minion : state.yourMinions) {
				for (int[] camp : yourCamp) {
					if (minion[0] == camp[0] && minion[1] == camp[1]) {
						campEmpty = false;
						break;
					}
				}
				if (campEmpty == false) {
					break;
				}
			}
		}

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
				state.jumped = new ArrayList<int[]>();
				

				
				if (0 <= neighborX && neighborX < 16 && 0 <= neighborY && neighborY < 16) {
					char neighbor = state.board[neighborY][neighborX];
					if (neighbor == '.') {
						State newState = (State) state.clone();
						newState.board[neighborY][neighborX] = state.board[minion[1]][minion[0]];
						newState.board[minion[1]][minion[0]] = '.';
						int[] yourNewMinion = { neighborX, neighborY };
						newState.yourMinions.set(i, yourNewMinion);

//						check if it's a crossing border move
						boolean startInCamp = false;
						for (int[] camp : yourCamp) {
							if (currentX == camp[0] && currentY == camp[1]) {
								startInCamp = true;
								break;
							}
						}
						boolean endOutCamp = true;
						for (int[] camp : yourCamp) {
							if (neighborX == camp[0] && neighborY == camp[1]) {
								endOutCamp = false;
								break;
							}
						}
						boolean crossingBorder = false;
						if (startInCamp && endOutCamp) {
							crossingBorder = true;
						}
						
//						check if it's an escape move(moving out of the opponent camp)
						boolean startInOpponentCamp = false;
						for (int[] camp: opponentCamp) {
							if (currentX == camp[0] && currentY == camp[1]) {
								startInOpponentCamp = true;
								break;
							}
						}
						boolean endOutOpponentCamp = true;
						for (int[] camp : opponentCamp) {
							if (neighborX == camp[0] && neighborY == camp[1]) {
								endOutOpponentCamp = false;
								break;
							}
						}
						boolean escapeMove = false;
						if (startInOpponentCamp && endOutOpponentCamp) {
							escapeMove = true;
						}

//						check if it's a move-away move
						boolean awayMove = false;
						if (needMoveAway && startInCamp) {
							if (state.colorUPlay.equals("WHITE")) {
								if (neighborX + neighborY - currentX - currentY < 0) {
									awayMove = true;
								}
							} else if (state.colorUPlay.equals("BLACK")) {
								if (neighborX + neighborY - currentX - currentY > 0) {
									awayMove = true;
								}
							}
						}

//						update eval
						double diagLineDist = Math.abs(neighborX - neighborY) / Math.sqrt(2);
						double oriDist = Math.abs(currentX - currentY) / Math.sqrt(2);
						if (state.colorUPlay.equals(whichPlayer)) {
							if (state.colorUPlay.equals("WHITE")) {
								newState.eval_value = state.eval_value
										- ((neighborX + neighborY) - (currentX + currentY));
							} else if (state.colorUPlay.equals("BLACK")) {
								newState.eval_value = state.eval_value
										+ ((neighborX + neighborY) - (currentX + currentY));
							}
						}
						newState.currentX = neighborX;
						newState.currentY = neighborY;
						if (true) {
//							System.out.println("\n minion " + i + " currentX is " + currentX + " currentY is "+ currentY);
						}
						newState.depthSearched++;
						newState.moveMode = 'E';
						int[] newLocation = { neighborX, neighborY };
						int[] currentLocation = {currentX,currentY};
						if (newState.depthSearched == 1) {
							newState.path.add(currentLocation);
							newState.path.add(newLocation);
						}
						newState.jumped.add(currentLocation);
						newState.jumped.add(newLocation);
						newState.colorUPlay = state.colorOpponent;
						newState.colorOpponent = state.colorUPlay;
						ArrayList<int[]> temp = newState.yourMinions;
						newState.yourMinions = newState.opponentMinions;
						newState.opponentMinions = temp;
						if (!escapeMove) {
							if (campEmpty == false && needMoveAway == false) {
								if (crossingBorder) {
									states.add(newState);
//									System.out.println("minion " + i + " can E cross border from " + currentX + "," + currentY + " to " + neighborX + "," + neighborY + " with eval value " + df.format(newState.eval_value));
								}
							} else if (needMoveAway) {
								if (awayMove) {
									states.add(newState);
//									System.out.println("minion " + i + " can E move away from " + currentX + "," + currentY + " to " + neighborX + "," + neighborY + " with eval value " + df.format(newState.eval_value));
								}
							} else {
								states.add(newState);
//								System.out.println("minion " + i + " can E outside from " + currentX + "," + currentY + " to " + neighborX + "," + neighborY + " with eval value " + df.format(newState.eval_value));
							}
						}
					}
				}
//				($$$) i can actually use try catch to do this condition, if it saves time
				jump(state, jumpX, jumpY, states, campEmpty, yourCamp, needMoveAway);

			}
		}
		/*
		 * by default, needCampCheck is set to true, and actions() will check camp
		 * emptyness then only add crossing move if camp is not empty. if no crossing
		 * border states are found, set needMoveAway to true, so that program checks if
		 * it's an away move
		 */
		if (states.isEmpty() && needMoveAway == false) {
			states = actions(state, needCampCheck, true, outOfCampSearch);
		}
		/*
		 * if there is no move-away, then set both to false and perform whole board
		 * search
		 */
		if (states.isEmpty() && outOfCampSearch == false) {
			outOfCampSearch = true;
			states = actions(state, false, false, outOfCampSearch);
			if (states.isEmpty()) {
				State ret = new State();
				ret.eval_value = state.eval_value;
				ret.depthSearched = Integer.MAX_VALUE;
				states.add(ret);
			}
		}
//		System.out.println("minion " + counter + " completed!");
		return states;
	}

//	($$$) here jumpx and jumpY were passed as duplicate
//	($$$) i can actually make jump into a subclass of state
	private static void jump(State state, int jumpX, int jumpY, ArrayList<State> states, boolean campEmpty,
			int[][] yourCamp, boolean needMoveAway) throws CloneNotSupportedException {
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

//				check if it's a crossing border move
				boolean startInCamp = false;
				for (int[] camp : yourCamp) {
					if (state.jumpStartX == camp[0] && state.jumpStartY == camp[1]) {
						startInCamp = true;
						break;
					}
				}
				boolean endOutCamp = true;
				for (int[] camp : yourCamp) {
					if (jumpX == camp[0] && jumpY == camp[1]) {
						endOutCamp = false;
						break;
					}
				}
				boolean crossingBorder = false;
				if (startInCamp && endOutCamp) {
					crossingBorder = true;
				}

//				check if it's an away move
				boolean awayMove = false;
				if (needMoveAway && startInCamp) {
					if (state.colorUPlay.equals("WHITE")) {
						if (jumpX + jumpY - state.jumpStartX - state.jumpStartY < 0) {
							awayMove = true;
						}
					} else if (state.colorUPlay.equals("BLACK")) {
						if (jumpX + jumpY - state.jumpStartX - state.jumpStartY > 0) {
							awayMove = true;
						}
					}
				}
				
//				determine your camp
				int[][] opponentCamp = null;
				if (yourCamp == whiteCampLocations) {
					opponentCamp = blackCampLocations;
				}
				else {
					opponentCamp = whiteCampLocations;
				}
				
//				check if it's an escape move(moving out of the opponent camp)
				boolean startInOpponentCamp = false;
				for (int[] camp: opponentCamp) {
					if (currentX == camp[0] && currentY == camp[1]) {
						startInOpponentCamp = true;
						break;
					}
				}
				boolean endOutOpponentCamp = true;
				for (int[] camp : opponentCamp) {
					if (jumpX == camp[0] && jumpY == camp[1]) {
						endOutOpponentCamp = false;
						break;
					}
				}
				boolean escapeMove = false;
				if (startInOpponentCamp && endOutOpponentCamp) {
					escapeMove = true;
				}

//				update eval
				double diagLineDist = Math.abs(jumpX - jumpY) / Math.sqrt(2);
				double oriDist = Math.abs(currentX - currentY) / Math.sqrt(2);
				if (state.colorUPlay.equals(whichPlayer)) {
					if (state.colorUPlay.equals("WHITE")) {
						newState.eval_value = state.eval_value
								- ((jumpX + jumpY) - (currentX + currentY));
					} else if (state.colorUPlay.equals("BLACK")) {
						newState.eval_value = state.eval_value
								+ ((jumpX + jumpY) - (currentX + currentY));
					}
				}
				newState.currentX = jumpX;
				newState.currentY = jumpY;
				newState.jumpX = -100;
				newState.jumpY = -100;
				newState.moveMode = 'J';
				newState.depthSearched++;
				newState.colorUPlay = state.colorOpponent;
				newState.colorOpponent = state.colorUPlay;
				int[] newLocation = { jumpX, jumpY };
				int[] currentLocation = {currentX, currentY};
				if (newState.depthSearched == 1) {
					newState.path.add(currentLocation);
					newState.path.add(newLocation);
				}
				newState.jumped.add(currentLocation);
				newState.jumped.add(newLocation);
				ArrayList<int[]> temp = newState.yourMinions;
				newState.yourMinions = newState.opponentMinions;
				newState.opponentMinions = temp;
				
				if (!escapeMove) {
					if (campEmpty == false && needMoveAway == false) {
						if (crossingBorder) {
							states.add(newState);
//							System.out.println("minion " + state.minionExamined + " can J cross border from " + state.jumpStartX + "," + state.jumpStartY + " to " + jumpX + "," + jumpY + " with eval value " + df.format(newState.eval_value));
						}
					} else if (needMoveAway) {
						if (awayMove) {
							states.add(newState);
//							System.out.println("minion " + state.minionExamined + " can J move away from " + state.jumpStartX + "," + state.jumpStartY + " to " + jumpX + "," + jumpY + " with eval value " + df.format(newState.eval_value));
						}
					} else {
						states.add(newState);
//						System.out.println("minion " + state.minionExamined + " can J outside play from " + state.jumpStartX + "," + state.jumpStartY + " to " + jumpX + "," + jumpY + " with eval value " + df.format(newState.eval_value));
					}
				}

//				System.out.println("minion " + state.minionExamined + " can jump from " + currentX + "," + currentY
//						+ " to " + jumpX + "," + jumpY + " with eval value " + newState.eval_value);
//				jumps++;

//				jump(newState, jumpX, jumpY, states);
				
				State passState = (State) newState.clone();
				passState.colorOpponent = state.colorOpponent;
				passState.colorUPlay = state.colorUPlay;
				jump(passState, -100, -100, states, campEmpty, yourCamp, needMoveAway);
			}
		}
//		insdie the recursive jump call
		if (state.jumpX == -100 && state.jumpY == -100) {
			int[][] directions = { { 1, 1 }, { 1, 0 }, { 1, -1 }, { 0, 1 }, { 0, -1 }, { -1, 1 }, { -1, 0 },
					{ -1, -1 } };
			int[][] directionWHITE = { { -1, 0 }, { -1, -1 }, { 0, -1 }, { 1, -1 }, { -1, 1 } };
			int[][] directionBLACK = { { 1, 0 }, { 1, 1 }, { 0, 1 }, { 1, -1 }, { -1, 1 } };
			if (state.colorUPlay.equals("WHITE")) {
				directions = directionWHITE;
			} else {
				directions = directionBLACK;
			}
//			String t = state.colorUPlay;
//			state.colorUPlay = state.colorOpponent;
//			state.colorOpponent = t;

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

//						update eval
						double diagLineDist = Math.abs(jumpX - jumpY) / Math.sqrt(2);
						double oriDist = Math.abs(jumpX - jumpY) / Math.sqrt(2);
						if (state.colorUPlay.equals(whichPlayer)) {
							if (state.colorUPlay.equals("WHITE")) {
								newState.eval_value = state.eval_value - ((jumpX + jumpY) - (currentX + currentY));
							} else if (state.colorUPlay.equals("BLACK")) {
								newState.eval_value = state.eval_value + ((jumpX + jumpY) - (currentX + currentY));
							}
						}

//						check if it's a crossing border move
						boolean startInCamp = false;
						for (int[] camp : yourCamp) {
							if (state.jumpStartX == camp[0] && state.jumpStartY == camp[1]) {
								startInCamp = true;
								break;
							}
						}
						boolean endOutCamp = true;
						for (int[] camp : yourCamp) {
							if (jumpX == camp[0] && jumpY == camp[1]) {
								endOutCamp = false;
								break;
							}
						}
						boolean crossingBorder = false;
						if (startInCamp && endOutCamp) {
							crossingBorder = true;
						}

//						check if it's an away move
						boolean awayMove = false;
						if (needMoveAway && startInCamp) {
							if (state.colorUPlay.equals("WHITE")) {
								if (jumpX + jumpY - state.jumpStartX - state.jumpStartY < 0) {
									awayMove = true;
								}
							} else if (state.colorUPlay.equals("BLACK")) {
								if (jumpX + jumpY - state.jumpStartX - state.jumpStartY > 0) {
									awayMove = true;
								}
							}
						}
						
//						determine your camp
						int[][] opponentCamp = null;
						if (yourCamp == whiteCampLocations) {
							opponentCamp = blackCampLocations;
						}
						else {
							opponentCamp = whiteCampLocations;
						}
						
//						check if it's an escape move(moving out of the opponent camp)
						boolean startInOpponentCamp = false;
						for (int[] camp: opponentCamp) {
							if (currentX == camp[0] && currentY == camp[1]) {
								startInOpponentCamp = true;
								break;
							}
						}
						boolean endOutOpponentCamp = true;
						for (int[] camp : opponentCamp) {
							if (jumpX == camp[0] && jumpY == camp[1]) {
								endOutOpponentCamp = false;
								break;
							}
						}
						boolean escapeMove = false;
						if (startInOpponentCamp && endOutOpponentCamp) {
							escapeMove = true;
						}

//						update currentX is necessary for next recursive call of jump()
						newState.currentX = jumpX;
						newState.currentY = jumpY;
						newState.moveMode = 'J';
						int[] newLocation = { jumpX, jumpY };
						if (newState.depthSearched == 1) {
							newState.path.add(newLocation);
						}
						newState.jumped.add(newLocation);
						newState.colorUPlay = state.colorOpponent;
						newState.colorOpponent = state.colorUPlay;
						temp = newState.yourMinions;
						newState.yourMinions = newState.opponentMinions;
						newState.opponentMinions = temp;
						
						if (!escapeMove) {
							if (campEmpty == false && needMoveAway == false) {
								if (crossingBorder) {
									states.add(newState);
//									System.out.println("minion " + state.minionExamined + " can J cross border from " + state.jumpStartX + "," + state.jumpStartY + " to " + jumpX + "," + jumpY + " with eval value " + df.format(newState.eval_value));
								}
							} else if (needMoveAway) {
								if (awayMove) {
									states.add(newState);
//									System.out.println("minion " + state.minionExamined + " can J move away from " + state.jumpStartX + "," + state.jumpStartY + " to " + jumpX + "," + jumpY + " with eval value " + df.format(newState.eval_value));
								}
							} else {
								states.add(newState);
//								System.out.println("minion " + state.minionExamined + " can J outside play from " + state.jumpStartX + "," + state.jumpStartY + " to " + jumpX + "," + jumpY + " with eval value " + df.format(newState.eval_value));
							}
						}

//						System.out.println("minion " + state.minionExamined + " can jump from " + currentX + ","
//								+ currentY + " to " + jumpX + "," + jumpY + " with eval value " + newState.eval_value);
//						jumps++;

						State passState = (State) newState.clone();
						passState.colorOpponent = state.colorOpponent;
						passState.colorUPlay = state.colorUPlay;
						jump(passState, -100, -100, states, campEmpty, yourCamp, needMoveAway);
					}
				}

//				}
			}
		}

	}
}
