// 2/5/18

import java.util.*;
import java.io.*;
import java.lang.*;//for thread
//this class creates a method that constructs the overall flow 
//of the game with calling appropiate methods from Board.java
public class GameManager {
	// Instance variables
	private Board board; // The actual 2048 board
	private String outputBoard; // File to save the board to when exiting

	/*ec*/
	private String outputRecord; // file to save the record file, format: [size] wasdwasdwasdawsd
	StringBuilder history = new StringBuilder(); // a string of commands history
	/*ce*/

	//to implement recording
	public GameManager(Random random, int boardSize, String outputRecord)throws IOException{

		this.outputRecord = outputRecord;
		board = new Board(random, boardSize);

		String c = " ";

		File myFile = new File(outputRecord);
		PrintWriter myWriter = new PrintWriter(myFile);

		myWriter.print(boardSize);
		myWriter.print(c);
		myWriter.close();

	}

	//to implement replaying
	public GameManager(Random random, String inputRecord) throws Exception{


		File myFile = new File(inputRecord);
		Scanner myScanner = new Scanner(myFile);
		char z; 

		int boardSize = myScanner.nextInt();
		board = new Board(random, boardSize);

		String string = myScanner.next();
		int i = 0;
		while(i < string.length()){

			z= string.charAt(i);

			if(z=='w'){
				board.move(Direction.UP);}


			else if(z=='s'){

				board.move(Direction.DOWN);}

			else if(z=='a'){

				board.move(Direction.LEFT); }

			else if(z=='d'){

				board.move(Direction.RIGHT); }

			Thread.sleep(500);
			i++;
		}//end of while loop

	}
	// TODO PSA3
	// GameManager Constructor
	// Generate new game
	public GameManager(String outputBoard, int boardSize, Random random) throws IOException {

		this.outputBoard = outputBoard;
		board = new Board(random, boardSize);
		board.saveBoard(outputBoard);
	}

	// TODO PSA3
	// GameManager Constructor
	// Load a saved game
	public GameManager(String inputBoard, String outputBoard, Random random) throws IOException {

		this.outputBoard = outputBoard;
		board = new Board(random, inputBoard);
		board.saveBoard(outputBoard);
	}


	// TODO PSA3
	// Main play loop
	// Takes in input from the user to specify moves to execute
	// valid moves are:
	//      w - Move up
	//      s - Move Down
	//      a - Move Left
	//      d - Move Right
	//      q - Quit and Save Board
	//
	//  If an invalid command is received then print the controls
	//  to remind the user of the valid moves.
	//
	//  Once the player decides to quit or the game is over,
	//  save the game board to a file based on the outputBoard
	//  string that was set in the constructor and then return
	//
	//  If the game is over print "Game Over!" to the terminal
	public void play() throws Exception {

		// File file = new File(outputRecord);
		//	PrintWriter writer = new PrintWriter(file);

		printControls();

		System.out.println(board);

		Scanner scanInput = new Scanner(System.in);
		String str;

		//read user inputs
		while(scanInput.hasNext()){
			str = scanInput.next();

			//print "Game over!" when no more movement can be made  
			if(board.isGameOver()){
				System.out.println(" Game Over! ");
				board.saveBoard(outputBoard);
				return;
			}

			if(str.equals("w")){

				//			writer.append("w");

				if(board.move(Direction.UP) ==true){board.addRandomTile();}
				System.out.println(board);}		

			else if(str.equals("s")){

				//			writer.append("s");

				if(board.move(Direction.DOWN)==true){board.addRandomTile();}
				System.out.println(board);}

			else if(str.equals("a")){
				//			writer.append("a");

				if(board.move(Direction.LEFT)==true){board.addRandomTile();}
				System.out.println(board);
			}

			else if(str.equals("d")){
				//			writer.append("d");

				if(board.move(Direction.RIGHT)==true){board.addRandomTile();}
				System.out.println(board);
			}

			else if(str.equals("q")){

				board.saveBoard(outputBoard);
				return;
			}

			else if (str !="w" && str !="s" &&  str !="a" && str !="d" && str !="q")
			{printControls();}
		}//end of while loop
		//writer.close();
	}

	// Print the Controls for the Game
	private void printControls() {
		System.out.println("  Controls:");
		System.out.println("    w - Move Up");
		System.out.println("    s - Move Down");
		System.out.println("    a - Move Left");
		System.out.println("    d - Move Right");
		System.out.println("    q - Quit and Save Board");
		System.out.println();
	}
}
