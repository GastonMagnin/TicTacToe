
import java.util.Scanner;

public class TicTacToe {
	//Create own Boolean class that is passed by reference
	public class myBoolean {
		public boolean value;
		public boolean rows;
		public boolean columns;
		public boolean diagonal;

		myBoolean(boolean val) {
			this.value = val;
		}
		myBoolean(boolean rows, boolean columns, boolean diagonal){
			this.rows = rows;
			this.columns = columns;
			this.diagonal = diagonal;
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
		myBoolean check = new myBoolean(true, true, true);
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
				computerTurn(field, rowsPosibilities, columnsPosibilities, check);
				//print the field again
				printField(field);
				//Check if the computer won or there's a draw
				checkPosibilities(field, playing, check, columnsPosibilities,
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
					checkPosibilities(field, playing, check, columnsPosibilities, rowsPosibilities);
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
	public void generatePosibilities(int rows, int columns, int[][] rowsPosibilities,
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
	public void checkPosibilities(int[][] field, myBoolean playing, myBoolean check, int[][] columnsPosibilities, int[][] rowsPosibilities) {
		//Only check columnPosibilities if there are any left
		if (check.columns) {
			check.columns = false;
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
				check.columns= true;
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
		if (check.rows) {
			check.rows = false;
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
					rowsPosibilities[row] = null;
					continue;
				}
				//If the programm gets here there is atleast one posibility left
				check.rows = true;
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
		if (check.diagonal)
			checkDiagonalPosibilities(field, check, playing);
		//Check whether there are any possibilities left if not print draw and stop the game
		if (!(check.rows || check.columns || check.diagonal)) {
			playing.value = false;
			printField(field);
			System.out.println("Draw, there is no posibility to win left");
		}
	}
	
	public void checkDiagonalPosibilities(int[][] field, myBoolean check, myBoolean playing) {

		//InitializeVariables
		String diaString = "";
		String diaString2 = "";
		check.diagonal = false;
		//Generate diagonal Strings
		for (int i = 0, j = field[0].length - 1; i < field[0].length; i++, j--) {
			diaString += field[i][i];
			diaString2 += field[i][j];

		}
		//Skip if it is not possible to win 
		for (String diagonalString : (new String[] { diaString, diaString2 })) {
			if (diagonalString.contains("1") && diagonalString.contains("2")) {
				continue;
			//Check if either player won
			} else if (!diagonalString.contains("0")) {
				if (diagonalString.contains("1")) {
					System.out.println("Player 1 wins");
					playing.value = false;
				} else {
					System.out.println("Player 2 wins");
					playing.value = false;
				}
			}
			//If this point is reached there is atleast one posibility to win  diagonally left 
			check.diagonal = true;
		}

		

	}
	//print the current playing field
	public void printField(int[][] field) {
		//initialize counter
		int counter = 1;
		System.out.print(" ");
		//Top line with column numbers
		for (int i = 0; i < field[0].length; i++) {
			System.out.printf("__%d_", i+1);
		}
		//rows with separators
		System.out.printf("\n%d", counter++);
		for (int[] row : field) {
			System.out.print("| ");
			for (int entry : row) {
				char output = (entry == 0) ? ' ' : (entry == 1) ? 'x' : 'o';
				System.out.print(output + " | ");
			}
			System.out.print("\n |");
			//Separator between rows
			for (int i = 0; i < row.length; i++) {
				System.out.print("___|");
			}
			//new line after each row
			System.out.printf("\n%s", (counter <= field.length) ? String.valueOf(counter++) : "");
		}

	}

	public void computerTurn(int[][] field, int[][] rowsPosibilities, int[][] columnsPosibilities, myBoolean check) {
		//Initialize variables
		boolean rows = true;
		int row;
		int column;
		int[] entry;
		//Check which posibilites are available and choose the way the computers turn is generated accordingly
		if(check.rows && check.columns) {
			rows = (((int) (Math.random() * 2)) != 0) ? true : false;
		}else if(check.rows || check.columns) {
			rows = (check.rows) ? true : false;
		}else {
			//Choose a random empty cell
			do {
				row = (int) (Math.random() * field.length);
				column = (int) (Math.random() * field[row].length);
			}while(field[row][column] != 0);
			
			field[row][column] = 2;
			return;
		}
		
		
		//Randomly select a cell in one of the rows/columns that still have the potential to lead to a victory
		if (rows) {
			do {
				do {
					//Select a random entry from the array
					row = (int) (Math.random() * rowsPosibilities.length);
					entry = rowsPosibilities[row];
					System.out.println(row);
				//make sure the entry hasn't been deleted
				} while (entry == null);
				//Select a random entry
				column = (int) (Math.random() * rowsPosibilities[row].length);
			//make sure the cell is empty
			} while (field[row][column] != 0);
		} else {
			do {
				do {
					//Select a random entry from the array
					column = (int) (Math.random() * rowsPosibilities.length);
					entry = columnsPosibilities[column];
				//make sure the entry hasn't been deleted
				} while (entry == null);
				//Select a random entry 
				row = (int)( Math.random() * rowsPosibilities[column].length);
			//make sure the cell is empty
			} while (field[row][column] != 0);
		}
		field[row][column] = 2;
	}
	//Get a Integer input with error handling
	public int getNextInt(Scanner sc, String prompt) {
		//Initialize variables
		boolean valid = false;
		int input = 0;
		
		do {
			try {
				//Print the prompt and wait for input
				System.out.println(prompt);
				input = sc.nextInt();
				//If the input is valid and there is no exception the loop can be exited
				valid = true;
			} catch (java.util.InputMismatchException e) {
				// Clear Scanner
				if (sc.hasNext())
					sc.nextLine();
				//Print error message
				System.out.println("Ungueltige Eingabe, bitte geben sie eine ganze Zahl ein");
			}
		} while (!valid);
		//return the int
		return input;
	}
}