import java.util.Scanner;

public class TicTacToe {
	public static int[][] field = new int[3][3];
	public static final int rows = 3;
	public static final int columns = 3;
	public static boolean playing = true;
	public static int[][][] posibilities;

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String input = "";
		posibilities = generatePosibilities();
		checkPosibilities();
		do {
			System.out.println("Wollen sie ein Spiel beginnen j/n");
			input = sc.next().toLowerCase();
		} while (!(input.equals("j") || input.equals("n")));
		if (input.equals("n")) {
			System.out.println("Bye");
			return;
		}
		// TODO add CPU opponent
		boolean currentPlayer = true;
		int inputRow;
		do {
			for (int[] row : field) {
				for (int entry : row) {
					System.out.print(entry + " ");
				}
				System.out.print("\n");
			}
			// TODO better input/prompt Error handling
			System.out.println("Geben sie die Zeile ihrer Auswahl ein");
			inputRow = sc.nextInt();
			System.out.println("Geben sie die Spalte ihrer Auswahl ein");
			field[inputRow][sc.nextInt()] = (currentPlayer) ? 1 : 2;
			checkPosibilities();
			currentPlayer = !currentPlayer;
		} while (playing);

		sc.close();
	}

	public static int[][][] generatePosibilities() {
		int[][] columnsPosibilities = new int[columns][rows];
		for (int i = 0; i < columnsPosibilities.length; i++) {
			for (int j = 0; j < rows; j++) {
				columnsPosibilities[i][j] = j;
			}
		}
		int[][] rowsPosibilities = new int[rows][columns];
		for (int i = 0; i < columnsPosibilities.length; i++) {
			for (int j = 0; j < columns; j++) {
				rowsPosibilities[i][j] = j;
			}
		}
		int[][][] output = new int[][][] { columnsPosibilities, rowsPosibilities };
		return output;
	}

	public static void checkPosibilities() {
		int[][] columnsPosibilities = posibilities[0];
		int[][] rowsPosibilities = posibilities[1];
		boolean rowsLeft = false;
		boolean columnsLeft = false;

		for (int column = 0; column < columnsPosibilities.length; column++) {
			if (columnsPosibilities[column] == null)
				continue;
			String columnString = "";
			for (int row : columnsPosibilities[column]) {
				columnString += field[row][column];
			}
			System.out.println(columnString + "column");
			if (columnString.contains("1") && columnString.contains("2")) {
				columnsPosibilities[column] = null;
				continue;
			}
			columnsLeft = true;
			if (!columnString.contains("0")) {
				if (columnString.contains("1")) {
					System.out.println("Player 1 wins");
					playing = false;
				} else {
					System.out.println("Player 2 wins");
					playing = false;
				}
			}
		}
		posibilities[0] = columnsPosibilities;
		for (int row = 0; row < rowsPosibilities.length; row++) {
			if (rowsPosibilities[row] == null)
				continue;
			String rowString = "";
			for (int column : rowsPosibilities[row]) {
				rowString += field[row][column];
			}
			if (rowString.contains("1") && rowString.contains("2")) {
				columnsPosibilities[row] = null;
				continue;
			}
			rowsLeft = true;
			if (!rowString.contains("0")) {
				if (rowString.contains("1")) {
					System.out.println("Player 1 wins");
					playing = false;
				} else {
					System.out.println("Player 2 wins");
					playing = false;
				}
			}
			System.out.println(rowString);
		}
		posibilities[1] = rowsPosibilities;
		if (!(rowsLeft || columnsLeft || checkDiagonal())) {
			playing = false;
			System.out.println("Draw, there is no posibility to win left");
		}
	}

	public static boolean checkDiagonal() {
		boolean checkLtoR = true;
		boolean checkRtoL = true;
		String diagonalString = "";

		for (int i = 0; i < columns; i++) {
			diagonalString += field[i][i];
			System.out.println(diagonalString + "diagonal");
		}
		if (diagonalString.contains("1") && diagonalString.contains("2")) {
			checkLtoR = false;
		} else if (!diagonalString.contains("0")) {
			if (diagonalString.contains("1")) {
				System.out.println("Player 1 wins");
				playing = false;
			} else {
				System.out.println("Player 2 wins");
				playing = false;
			}
		}

		diagonalString = "";

		for (int i = 0; i < columns; i++) {
			diagonalString += field[i][i];
		}
		if (diagonalString.contains("1") && diagonalString.contains("2")) {
			checkRtoL = false;
		} else if (!diagonalString.contains("0")) {
			if (diagonalString.contains("1")) {
				System.out.println("Player 1 wins");
				playing = false;
			} else {
				System.out.println("Player 2 wins");
				playing = false;
			}
		}

		// return false if both are false
		return (checkRtoL || checkLtoR);

	}
}