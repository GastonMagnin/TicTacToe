
import java.util.Scanner;

public class TicTacToe {
	public class myBoolean {
		public boolean value;

		myBoolean(boolean val) {
			this.value = val;
		}
	}

	public static void main(String[] args) {
		TicTacToe game = new TicTacToe();
		game.run();
	}

	public void run() {
		// Create Scanner
		Scanner sc = new Scanner(System.in);
		String input = "";
		do {
			System.out.println("Wollen sie ein Spiel beginnen j/n");
			input = sc.next().toLowerCase();
		} while (!(input.equals("j") || input.equals("n")));
		if (input.equals("n")) {
			System.out.println("Bye");
			sc.close();
			return;
		}
		int rows = 0;
		do {
			rows = getNextInt(sc,
					"Wieviele Zeilen soll das Spielfeld haben?\nBitte geben sie eine ganze Zahl ueber 0 ein");
		} while (rows <= 0);
		int columns = 0;
		do {
			columns = getNextInt(sc,
					"Wieviele Spalten soll das Spielfeld haben?\nBitte geben sie eine ganze Zahl ueber 0 ein");
		} while (columns <= 0);

		// Initialize variables
		myBoolean playing = new myBoolean(true);
		myBoolean checkHorizontal = new myBoolean(true);
		myBoolean checkVertical = new myBoolean(true);
		myBoolean checkDiagonal = new myBoolean((rows == columns) ? true : false);

		int[][] field = new int[rows][columns];
		int[][] rowsPosibilities = new int[rows][columns];
		int[][] columnsPosibilities = new int[columns][rows];
		generatePosibilities(rows, columns, rowsPosibilities, columnsPosibilities);
		System.out.println("Wollen sie gegen einen Computer spielen?");
		do {
			input = "";
			try {
				input = sc.next();
			} catch (java.util.InputMismatchException e) {
				// Clear Scanner
				while (sc.hasNext())
					sc.nextLine();
			}
		} while (!(input.toLowerCase().contains("j") || input.toLowerCase().contains("n"))
				&& !(input.toLowerCase().contains("j") && input.toLowerCase().contains("n")));
		boolean computerOpponent = (input.contains("j")) ? true : false;
		// TODO add CPU opponent
		boolean currentPlayer = true;
		int inputRow;
		int inputColumn;
		do {
			printField(field);
			if (!currentPlayer && computerOpponent) {
				computerTurn(field, rowsPosibilities, columnsPosibilities);
				printField(field);
				checkPosibilities(field, playing, checkHorizontal, checkVertical, checkDiagonal, columnsPosibilities,
						rowsPosibilities);
				currentPlayer = !currentPlayer;
				continue;

			}
			boolean valid = false;
			do {
				try {
					System.out.println((currentPlayer ? "Spieler1" : "Spieler2") + " ist am Zug");
					inputRow = getNextInt(sc, "Geben sie die Zeile ihrer Auswahl ein") - 1;
					inputColumn = getNextInt(sc, "Geben sie die Spalte ihrer Auswahl ein") - 1;
					if (field[inputRow][inputColumn] == 0) {
						field[inputRow][inputColumn] = (currentPlayer) ? 1 : 2;
					} else {
						System.out.println("Dieses Feld ist bereits besetzt bitte geben sie ein leeres Feld ein");
						continue;
					}

					checkPosibilities(field, playing, checkHorizontal, checkVertical, checkDiagonal,
							columnsPosibilities, rowsPosibilities);
					currentPlayer = !currentPlayer;
					valid = true;
				} catch (java.lang.ArrayIndexOutOfBoundsException e) {
					System.out.println("Bitte geben sie einen Wert ein der innerhalb des Spielfelds liegt");
				}
			} while (!valid);
		} while (playing.value);

		sc.close();
	}

	public static void generatePosibilities(int rows, int columns, int[][] rowsPosibilities,
			int[][] columnsPosibilities) {

		for (int i = 0; i < columnsPosibilities.length; i++) {
			for (int j = 0; j < rows; j++) {
				columnsPosibilities[i][j] = j;
			}
		}

		for (int i = 0; i < columnsPosibilities.length; i++) {
			for (int j = 0; j < columns; j++) {
				rowsPosibilities[i][j] = j;
			}
		}

	}

	public static void checkPosibilities(int[][] field, myBoolean playing, myBoolean checkHorizontal,
			myBoolean checkVertical, myBoolean checkDiagonal, int[][] columnsPosibilities, int[][] rowsPosibilities) {
		boolean rowsLeft = false;
		boolean columnsLeft = false;

		if (checkVertical.value) {
			for (int column = 0; column < columnsPosibilities.length; column++) {
				if (columnsPosibilities[column] == null)
					continue;
				String columnString = "";
				for (int row : columnsPosibilities[column]) {
					columnString += field[row][column];
				}
				if (columnString.contains("1") && columnString.contains("2")) {
					columnsPosibilities[column] = null;
					continue;
				}
				columnsLeft = true;
				if (!columnString.contains("0")) {
					if (columnString.contains("1")) {
						System.out.println("Player 1 wins");
						playing.value = false;
					} else {
						System.out.println("Player 2 wins");
						playing.value = false;
					}
				}
			}
		}

		if (checkHorizontal.value) {
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
						playing.value = false;
					} else {
						System.out.println("Player 2 wins");
						playing.value = false;
					}
				}
			}
		}

		checkHorizontal.value = rowsLeft;
		checkVertical.value = columnsLeft;
		if (checkDiagonal.value)
			checkDiagonalPosibilities(field, checkDiagonal, playing);
		if (!(checkHorizontal.value || checkVertical.value || checkDiagonal.value)) {
			playing.value = false;
			System.out.println("Draw, there is no posibility to win left");
		}
	}

	public static void checkDiagonalPosibilities(int[][] field, myBoolean checkDiagonal, myBoolean playing) {

		boolean checkDia = true;
		String diaString = "";
		String diaString2 = "";

		for (int i = 0, j = field[0].length - 1; i < field[0].length; i++, j--) {
			diaString += field[i][i];
			diaString2 += field[i][j];

		}
		for (String diagonalString : (new String[] { diaString, diaString2 })) {
			if (diagonalString.contains("1") && diagonalString.contains("2")) {
				continue;
			} else if (!diagonalString.contains("0")) {
				if (diagonalString.contains("1")) {
					System.out.println("Player 1 wins");
					playing.value = false;
				} else {
					System.out.println("Player 2 wins");
					playing.value = false;
				}
			}
			checkDia = true;
		}

		checkDiagonal.value = checkDia;

	}

	public void printField(int[][] field) {
		for (int i = 0; i < field[0].length; i++) {
			System.out.print("____");
		}
		System.out.print("\n");
		for (int[] row : field) {
			System.out.print("| ");
			for (int entry : row) {
				System.out.print(entry + " | ");
			}
			System.out.print("\n|");
			for (int i = 0; i < row.length; i++) {
				System.out.print("___|");
			}
			System.out.print("\n");
		}

	}

	public void computerTurn(int[][] field, int[][] rowsPosibilities, int[][] columnsPosibilities) {
		boolean rows = (((int) (Math.random() * 2)) != 0) ? true : false;
		int[] entry;
		int row;
		int column;
		if (rows) {
			do {
				do {
					row = (int) (Math.random() * rowsPosibilities.length);
					entry = rowsPosibilities[row];
					System.out.println(row);
				} while (entry == null);

				column = (int) (Math.random() * rowsPosibilities[row].length);
			} while (field[row][column] != 0);
		} else {
			do {
				do {
					column = (int) (Math.random() * rowsPosibilities.length);
					entry = columnsPosibilities[column];
				} while (entry == null);

				row = (int)( Math.random() * rowsPosibilities[column].length);
			} while (field[row][column] != 0);
		}
		field[row][column] = 2;
	}

	public int getNextInt(Scanner sc, String prompt) {
		boolean valid = false;
		int input = 0;
		do {
			try {
				System.out.println(prompt);
				input = sc.nextInt();
				valid = true;
			} catch (java.util.InputMismatchException e) {
				// Clear Scanner
				if (sc.hasNext())
					sc.nextLine();
				System.out.println("Ungueltige Eingabe, bitte geben sie eine ganze Zahl ein");
			}
		} while (!valid);
		return input;
	}
}