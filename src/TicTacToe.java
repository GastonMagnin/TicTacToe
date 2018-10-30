
import java.util.Scanner;

public class TicTacToe {
	//Create own Boolean class that is passed by reference
	public class myBoolean {
		public boolean value;

		myBoolean(boolean val) {
			this.value = val;
		}
	}

	public static void main(String[] args) {
		//Create new TicTacToe object and run method
		TicTacToe game = new TicTacToe();
		game.run();
	}

	public void run() {
		// Create Scanner
		Scanner sc = new Scanner(System.in);
		//Create input String
		String input = "";
		//Get user input until a valid answer is entered
		do {
			System.out.println("Wollen sie ein Spiel beginnen j/n");
			input = sc.next().toLowerCase();
		} while (!(input.equals("j") || input.equals("n")));
		//Stop program if the answer is no
		if (input.equals("n")) {
			System.out.println("Bye");
			sc.close();
			return;
		}
		//Get the desired amount of rows and columns repeat until a valid input is entered
		int rows = 0;
		do {
			//Get input
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
		//Fill arrays
		generatePosibilities(rows, columns, rowsPosibilities, columnsPosibilities);
		//Determine if playing vs another player or a computer repeat until valid answer is entered
		
		do {
			input = "";
			try {
				System.out.println("Wollen sie gegen einen Computer spielen?");
				input = sc.next();
			} catch (java.util.InputMismatchException e) {
				// Clear Scanner
				while (sc.hasNext())
					sc.nextLine();
			}
		} while (!(input.toLowerCase().contains("j") || input.toLowerCase().contains("n"))
				&& !(input.toLowerCase().contains("j") && input.toLowerCase().contains("n")));
		boolean computerOpponent = (input.contains("j")) ? true : false;
		//Initialize variables
		boolean currentPlayer = true;
		int inputRow;
		int inputColumn;
		//
		do {
			//print the current version of the playing field
			printField(field);
			//Computers turn
			if (!currentPlayer && computerOpponent) {
				//get a value for the comnputers turn
				computerTurn(field, rowsPosibilities, columnsPosibilities);
				//print the field again
				printField(field);
				//Check if the computer won or there's a draw
				checkPosibilities(field, playing, checkHorizontal, checkVertical, checkDiagonal, columnsPosibilities,
						rowsPosibilities);
				//Switch current Player 
				currentPlayer = !currentPlayer;
				continue;

			}
			boolean valid = false;
			do {
				try {
					//Get player input repeat until a empty cell is entered
					System.out.println((currentPlayer ? "Spieler1" : "Spieler2") + " ist am Zug");
					inputRow = getNextInt(sc, "Geben sie die Zeile ihrer Auswahl ein") - 1;
					inputColumn = getNextInt(sc, "Geben sie die Spalte ihrer Auswahl ein") - 1;
					if (field[inputRow][inputColumn] == 0) {
						field[inputRow][inputColumn] = (currentPlayer) ? 1 : 2;
					} else {
						System.out.println("Dieses Feld ist bereits besetzt bitte geben sie ein leeres Feld ein");
						continue;
					}
					//check if the player won or ther's a draw
					checkPosibilities(field, playing, checkHorizontal, checkVertical, checkDiagonal,
							columnsPosibilities, rowsPosibilities);
					//Switch current Player 
					currentPlayer = !currentPlayer;
					//If the programm got here the input is valid and the loop can be exited
					valid = true;
				} catch (java.lang.ArrayIndexOutOfBoundsException e) {
					System.out.println("Bitte geben sie einen Wert ein der innerhalb des Spielfelds liegt");
				}
			} while (!valid);
		//Repeat until the game ends and playing is false
		} while (playing.value);
		//close scanner
		sc.close();
	}

	//Fill the arrays
	public static void generatePosibilities(int rows, int columns, int[][] rowsPosibilities,
			int[][] columnsPosibilities) {
		//generate all posibilities to win by having three in a vertical row
		for (int i = 0; i < columnsPosibilities.length; i++) {
			for (int j = 0; j < rows; j++) {
				columnsPosibilities[i][j] = j;
			}
		}
		//generate all posibilities to win by having three in a horizontal row
		for (int i = 0; i < columnsPosibilities.length; i++) {
			for (int j = 0; j < columns; j++) {
				rowsPosibilities[i][j] = j;
			}
		}

	}
	
	//Check all posibilities and determine if someone won or there's a draw
	public static void checkPosibilities(int[][] field, myBoolean playing, myBoolean checkHorizontal,
			myBoolean checkVertical, myBoolean checkDiagonal, int[][] columnsPosibilities, int[][] rowsPosibilities) {
		//Only check columnPosibilities if there are any left
		if (checkVertical.value) {
			checkVertical.value = false;
			for (int column = 0; column < columnsPosibilities.length; column++) {
				//Skip deleted posibilities
				if (columnsPosibilities[column] == null)
					continue;
				//Generate a String for every column 
				String columnString = "";
				for (int row : columnsPosibilities[column]) {
					columnString += field[row][column];
				}
				//Check if winning in this column is possible *delete* the entry if not
				if (columnString.contains("1") && columnString.contains("2")) {
					columnsPosibilities[column] = null;
					continue;
				}
				// If the programm gets here there is atleast one posibility left
				checkVertical.value = true;
				//Check if either player won
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
		//Only check rowPosibilities if there are any left
		if (checkHorizontal.value) {
			checkHorizontal.value = false;
			for (int row = 0; row < rowsPosibilities.length; row++) {
				//Skip *deleted* posibilites
				if (rowsPosibilities[row] == null)
					continue;
				//Generate a String for every row
				String rowString = "";
				for (int column : rowsPosibilities[row]) {
					rowString += field[row][column];
				}
				//Check if winning in this row is possible *delete* the entry if not
				if (rowString.contains("1") && rowString.contains("2")) {
					columnsPosibilities[row] = null;
					continue;
				}
				//If the programm gets here there is atleast one posibility left
				checkHorizontal.value = true;
				//Check if either player won
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
		
		//Check diagonal possibilities if there are any left
		if (checkDiagonal.value)
			checkDiagonalPosibilities(field, checkDiagonal, playing);
		//Check whether there are any possibilities left if not print draw and stop the game
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