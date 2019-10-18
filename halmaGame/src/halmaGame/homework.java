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
		
	}
	
	public static void main(String args[]) throws FileNotFoundException {
		State state = readInput();
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
}
