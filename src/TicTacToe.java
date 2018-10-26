import java.util.Scanner;

public class TicTacToe {
	public static boolean playing = true;

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String input = "";
		do {
			System.out.println("Wollen sie ein Spiel beginnen j/n");
			input = sc.next().toLowerCase();
		} while (!(input.equals("j") || input.equals("n")));
		if (input.equals("n")) {
			System.out.println("Bye");
			return;
		}
		int rows = 0;
		do {
			rows = getNextInt(sc, "Wieviele Zeilen soll das Spielfeld haben?\nBitte geben sie eine ganze Zahl über 0 ein");
		} while (rows <= 0);
		int columns = 0;
		do {
			columns = getNextInt(sc, "Wieviele Spalten soll das Spielfeld haben?\nBitte geben sie eine ganze Zahl über 0 ein");
		} while (columns <= 0);

		int[][][] container = generatePosibilities(rows, columns);

		// TODO add CPU opponent
		boolean currentPlayer = true;
		int inputRow;
		do {
			printField(container);
			// TODO better input/prompt Error handling
			boolean valid = false;
			do {
				try {
					System.out.println((currentPlayer ? "Spieler1" : "Spieler2") + " ist am Zug"); 
					inputRow = getNextInt(sc, "Geben sie die Zeile ihrer Auswahl ein");
					container[0][inputRow + 1][getNextInt(sc, "Geben sie die Spalte ihrer Auswahl ein") + 1] = (currentPlayer) ? 1 : 2;
					container = checkPosibilities(container);
					currentPlayer = !currentPlayer;
					valid = true;
				} catch (java.util.InputMismatchException e) {//java.lang.ArrayIndexOutOfBoundsException e) {
					System.out.println(e.getMessage() + e.getLocalizedMessage());
					System.out.println("Bitte geben sie einen Wert ein der innerhalb des Spielfelds liegt");
				}
			} while (!valid);
		} while (container[3][0][0] != 0);

		sc.close();
	}

	public static int[][][] generatePosibilities(int rows, int columns) {
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
		int[][] field = new int[rows][columns];
		int checkDiagonal = (rows == columns) ? 1 : 0;
		int[][][] container = new int[][][] { field, columnsPosibilities, rowsPosibilities, { { 1, 1, 1, checkDiagonal } } };
		return container;
	}

	public static int[][][] checkPosibilities(int[][][] container) {
		int[][] columnsPosibilities = container[1];
		int[][] rowsPosibilities = container[2];
		boolean rowsLeft = false;
		boolean columnsLeft = false;
		
		if(container[3][0][1] == 1) {
			for (int column = 0; column < columnsPosibilities.length; column++) {
				if (columnsPosibilities[column] == null)
					continue;
				String columnString = "";
				for (int row : columnsPosibilities[column]) {
					columnString += container[0][row][column];
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
						container[3][0][0] = 0;
					} else {
						System.out.println("Player 2 wins");
						container[3][0][0] = 0;
					}
				}
			}
		}
		
		if(container[3][0][1] == 1) {
			for (int row = 0; row < rowsPosibilities.length; row++) {
				if (rowsPosibilities[row] == null)
					continue;
				String rowString = "";
				for (int column : rowsPosibilities[row]) {
					rowString += container[0][row][column];
				}
				if (rowString.contains("1") && rowString.contains("2")) {
					columnsPosibilities[row] = null;
					continue;
				}
				rowsLeft = true;
				if (!rowString.contains("0")) {
					if (rowString.contains("1")) {
						System.out.println("Player 1 wins");
						container[3][0][0] = 0;
					} else {
						System.out.println("Player 2 wins");
						container[3][0][0] = 0;
					}
				}
				System.out.println(rowString);
			}
		}
		
		container[1] = columnsPosibilities;
		container[2] = rowsPosibilities;
		container[3][0][1] = rowsLeft ? 1 : 0;
		container[3][0][2] = columnsLeft ? 1 : 0;
		container = (container[3][0][3] != 0) ? checkDiagonal(container) : container;
		if ((container[3][0][1] + container[3][0][2] + container[3][0][3]) == 0){
			container[3][0][0] = 0;
			System.out.println("Draw, there is no posibility to win left");
		}
		return container;
	}

	public static int[][][] checkDiagonal(int[][][] container) {

		boolean checkDia = true;
		String diaString = "";
		String diaString2 = "";

		for (int i = 0, j = container[0][0].length - 1; i < container[0][0].length; i++, j--) {
			diaString += container[0][i][i];
			diaString2 += container[0][i][j];

		}
		for (String diagonalString : (new String[] { diaString, diaString2 })) {
			if (diagonalString.contains("1") && diagonalString.contains("2")) {
				continue;
			} else if (!diagonalString.contains("0")) {
				if (diagonalString.contains("1")) {
					System.out.println("Player 1 wins");
					container[3][0][0] = 0;
				} else {
					System.out.println("Player 2 wins");
					container[3][0][0] = 0;
				}
			}
			checkDia = true;
		}

		container[3][0][3] = checkDia ? 1 : 0;
		return container;

	}

	public static void printField(int[][][] container) {
		for(int i = 0; i < container[0][0].length ; i++) {
			System.out.print("____");
		}
		System.out.print("\n");
		for (int[] row : container[0]) {
			System.out.print("| ");
			for (int entry : row) {
				System.out.print(entry + " | ");
			}
			System.out.print("\n|");
			for(int i = 0; i < row.length ; i++) {
				System.out.print("___|");
			}
			System.out.print("\n");
		}
		
	}
	public static int getNextInt(Scanner sc, String prompt) {
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
				System.out.println("Ungültige Eingabe, bitte geben sie eine ganze Zahl ein");
			}
		} while (!valid);
		return input;
	}
}