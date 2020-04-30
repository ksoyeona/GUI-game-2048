//1/27/18

/**
 * Sample Board
 * <p/>
 * 0   1   2   3
 * 0   -   -   -   -
 * 1   -   -   -   -
 * 2   -   -   -   -
 * 3   -   -   -   -
 * <p/>
 * The sample board shows the index values for the columns and rows
 * Remember that you access a 2D array by first specifying the row
 * and then the column: grid[row][column]
 */

import java.util.*;
import java.io.*;

//this Board class creates a board object and contains methods to save, load,
//and modify such objects to obtain intended outcome
public class Board {
	public final int NUM_START_TILES = 2; 
	public final int TWO_PROBABILITY = 90;
	public final int GRID_SIZE;

	private final Random random; // a reference to the Random object, passed in 
	// as a parameter in Boards constructors
	private int[][] grid;  // a 2D int array, its size being boardSize*boardSize
	private int score;     // the current score, incremented as tiles merge 

	// Constructs a fresh board with random tiles
	public Board(Random random, int boardSize) {
		this.random = random; 
		GRID_SIZE = boardSize;
		grid = new int[GRID_SIZE][GRID_SIZE];
		addRandomTile();
		addRandomTile();              
	}

	// Construct a board based off of an input file
	// assume board is valid
	public Board(Random random, String inputBoard) throws IOException {
		this.random = random; 

		File myFile = new File(inputBoard);
		Scanner myScanner = new Scanner(myFile);

		//set the first number as grid size
		GRID_SIZE = myScanner.nextInt();

		//set the second number as score
		score = myScanner.nextInt();

		//create grid to fill in with tiles
		grid = new int[GRID_SIZE][GRID_SIZE];        

		while(myScanner.hasNextInt()){

			//loop through the array to fill the grid
			for(int i = 0; i < grid.length; i++){
				for(int j = 0; j < grid[i].length; j++){
					int numb = myScanner.nextInt();
					grid[i][j] = numb;
				}
			}

		}
	}	

	// Saves the current board to a file
	public void saveBoard(String outputBoard) throws IOException {

		PrintWriter myWriter = new PrintWriter(outputBoard);

		myWriter.print(GRID_SIZE);
		myWriter.println();
		myWriter.print(score);
		myWriter.println();

		//to append after each number
		String c = " ";
		//to start a new line according to grid dimension
		int incCheck = 0;

		for(int i = 0; i < grid.length; i++){
			for(int j = 0; j < grid[i].length; j++){

				myWriter.print(grid[i][j]);
				myWriter.print(c);
				incCheck++;
				//start new line after each row
				if(incCheck % GRID_SIZE == 0)
				{myWriter.println();}

			}
		}
		myWriter.close();
	}


	// Adds a random tile (of value 2 or 4) to a
	// random empty space on the board
	public void addRandomTile() {

		int count = 0;
		//counting number of empty tiles
		for(int i = 0; i < grid.length; i++){
			for(int j = 0; j < grid[i].length; j++){

				if(grid[i][j] == 0){
					count++;
				}
			}
		}

		if(count == 0){
			return;}


		int location = random.nextInt(count);
		int value = random.nextInt(100);

		//index to find location-th tile. it starts at -1 because
		//when there's an empty spot then it should increase to 0,
		//which equals to one tile.
		int index  = -1;

		for(int i = 0; i < grid.length; i++){
			for(int j =0; j < grid.length;j++){
				//	for(int i = 0; i < grid.length; i++){
				//increase index everytime there's no tile
				if(grid[i][j] == 0){
					index++;

					//insert tile when index matches location
					if(index == location){

						if(value < TWO_PROBABILITY)
						{grid[i][j] =  NUM_START_TILES;}

						else grid[i][j] = 4;
					}
				}
			}
			}
		}

		//helper method to determine whether tiles can move left, and it 
		//will be used in canMove() method
		private boolean canMoveLeft(){

			for(int i = 0; i < grid.length; i++){
				for(int j=1; j < grid[i].length; j++){
					if(grid[i][j] != 0){
						if(grid[i][j] == grid[i][j-1] || grid[i][j-1] ==0)
						{return true;}
					}
				}
			}
			return false;
		}
		//helper method to determine whether tiles can move right, and it 
		//will be used in canMove() method
		private boolean canMoveRight(){

			for(int i =0; i < grid.length; i++){
				for(int j = 0; j < grid[i].length-1;j++){
					if(grid[i][j] != 0){
						if(grid[i][j] == grid[i][j+1] || grid[i][j+1]==0)
						{ return true;}
					}
				}
			}
			return false;
		}
		//helper method to determine whether tiles can move up, and it 
		//will be used in canMove() method
		private boolean canMoveUp(){

			for(int i=1; i < grid.length; i++){
				for(int j =0; j < grid[i].length;j++){
					if(grid[i][j] != 0){
						if(grid[i][j] == grid[i-1][j] || grid[i-1][j] == 0)
						{ return true;}
					}
				}
			}
			return false;
		}
		//helper method to determine whether tiles can move left, and it 
		//will be used in canMove() method
		private boolean canMoveDown(){

			for(int i=0; i < grid.length-1; i++){
				for(int j=0; j < grid[i].length;j++){
					if(grid[i][j] != 0){
						if(grid[i][j] == grid[i+1][j] || grid[i+1][j] == 0)
						{ return true;}
					}
				}
			}
			return false;
		}
		// determins whether the board can move in a certain direction
		// return true if such a move is possible
		public boolean canMove(Direction direction){

			for(int i = 0; i < grid.length; i++){
				for(int j = 0; j < grid[i].length; j++){

					if(direction == Direction.RIGHT && canMoveRight() == true)
					{return true; }
					else if(direction == Direction.LEFT && canMoveLeft() == true)
					{return true; }
					else if(direction == Direction.UP && canMoveUp() == true)
					{return true; }
					else if(direction == Direction.DOWN && canMoveDown() == true)
					{return true; }
				}
			}
			return false;
		}

		//helper method for move
		private void moveLeft(){
			int x = 0;
			while( x < grid.length-1 ){
				for(int i = 0; i < grid.length; i++){
					for(int j =0; j < grid.length-1; j++){

						//if the tile is not zero and has zero right to it
						if(grid[i][j+1] != 0 && grid[i][j] ==0){

							//replace the the left spot with tile at location i,j
							grid[i][j] = grid[i][j+1];

							grid[i][j+1] = 0;
						}
					}
				}x++;//repeats until or the zeros in between are gone
			}//end of while loop

			for(int i = 0; i < grid.length; i++){
				for(int j = 1; j < grid.length; j++){

					//if the left tile has same number, combine
					if(grid[i][j] != 0 && grid[i][j-1] == grid[i][j]){

						grid[i][j-1] = 2*grid[i][j];
						grid[i][j] = 0;
						//update score
						score = score + grid[i][j-1];
					}
				}
			}//end of for loop
			//get rid of zeros in between just like the first method
			int b= 0;
			while(b<grid.length-1){
				for(int i = 0; i < grid.length; i++){
					for(int j = 0 ; j < grid.length-1 ; j++){
						if(grid[i][j+1] != 0 && grid[i][j] ==0){
							grid[i][j] = grid[i][j+1];

							grid[i][j+1] = 0;

						}
					}
				}b++;
			}
		}
		//helper method for move
		private void moveRight(){
			int k = 0;
			while( k < grid.length){
				for(int i = 0; i < grid.length; i++){
					for(int j =0; j< grid.length-1; j++){

						//if the tile on the right is zero
						if(grid[i][j] != 0 && grid[i][j+1] == 0){

							//set that tile as as the number on
							//ijth location
							grid[i][j+1] = grid[i][j];
							grid[i][j] = 0;
						}
					}
				}k++;
			}//end of while loop		

			//this for loop is for combining
			for(int i = 0; i < grid.length; i++){
				for(int j=grid.length-1; j >= 1; j--){

					if(grid[i][j] != 0 && grid[i][j-1] ==grid[i][j]){
						grid[i][j] = grid[i][j-1] + grid[i][j];
						grid[i][j-1] = 0;
						score = score + grid[i][j];
					}
				}
			}//end of for loop

			int h = 0;
			while(h<grid.length-1){ 	
				for(int i = 0; i < grid.length; i++){
					for(int j =0; j<grid.length-1; j++){

						if(grid[i][j] != 0 && grid[i][j+1] == 0){
							grid[i][j+1] = grid[i][j];
							grid[i][j] = 0;
						}

					}
				}h++;
			}//end of while loop
		}


		//helper method
		private void moveUp(){
			int p=0;
			while( p < grid.length-1 ){

				for(int j = 0; j < grid.length; j++){
					for(int i = 1; i < grid.length; i++){

						//if ij tile is non zero and 
						//tile above is zero set the tile above as 
						//tile at ij 
						if(grid[i][j] != 0 && grid[i-1][j] == 0){
							grid[i-1][j] = grid[i][j];
							grid[i][j] = 0;
						}
					}
				}
				p++;
			}//end of while loop

			for(int j = 0; j < grid.length; j++){
				for(int i = 1; i < grid.length; i++){

					//if tile is non zero and tile above has 
					//same number, combine
					if(grid[i][j]!= 0 && grid[i-1][j] ==grid[i][j]){
						grid[i-1][j] = grid[i][j] + grid[i][j];
						grid[i][j] = 0;
						score = score + grid[i-1][j];
					}
				}
			}//end of for loop
			int u = 0;
			while(u < grid.length-1){
				for(int j = 0; j < grid.length; j++){

					for(int i = 1; i < grid.length; i++){

						if(grid[i][j] != 0 && grid[i-1][j] == 0){
							grid[i-1][j] = grid[i][j];
							grid[i][j] =0;

						}
					}
				}u++;

			}//end of while loop
		}

		//helper method
		private void moveDown(){
			int z=0;

			while( z< grid.length-1 ){

				for(int j = 0; j < grid.length; j++){
					for(int i = grid.length-2; i >= 0; i--){

						//if tile at i,j is non zero and tile below is zero, set the bottom 
						//tile as the value at i,j 
						if(grid[i][j] != 0 && grid[i+1][j] == 0){

							grid[i+1][j] = grid[i][j];
							grid[i][j] = 0;
						}
					}
				}
				z++;
			}//end of while loop - tiles aligned with no zeros in between

			for(int i = grid.length-1; i > 0; i--){
				for(int j = 0; j < grid.length; j++){

					//combine tiles if the tile above has same number
					if(grid[i][j]!= 0 && grid[i][j] ==grid[i-1][j]){

						grid[i][j] = grid[i-1][j] + grid[i][j];

						grid[i-1][j] = 0;
						//update score
						score = score + grid[i][j];
					}
				}
			}
			int s=0;
			while(s<grid.length-1){
				for(int j = 0; j < grid.length; j++){
					for(int i = grid.length-2; i >= 0 ; i--){

						if(grid[i][j] != 0 && grid[i+1][j] == 0){

							grid[i+1][j] = grid[i][j];

							grid[i][j] = 0;

						}
					}
				} s++;
			}//end of while loop
		}

		// move the board in a certain direction
		// return true if such a move is successful
		public boolean move(Direction direction) {


			if(canMove(direction) == false){return false;}

			if(Direction.LEFT==direction){moveLeft(); }
			if(Direction.RIGHT == direction){moveRight();}
			if(Direction.UP == direction){moveUp();}
			if(Direction.DOWN == direction){moveDown();}

			return true; 
		}

		// No need to change this for PSA3
		// Check to see if we have a game over
		public boolean isGameOver() {

			if(canMove(Direction.RIGHT) == false &&
					canMove(Direction.LEFT) == false &&
					canMove(Direction.UP) == false &&
					canMove(Direction.DOWN) == false)
			{return true;}	

			return false;
		}

		// Return the reference to the 2048 Grid
		public int[][] getGrid() {
			return grid;
		}

		// Return the score
		public int getScore() {
			return score;
		}

		@Override
			public String toString() {
				StringBuilder outputString = new StringBuilder();
				outputString.append(String.format("Score: %d\n", score));
				for (int row = 0; row < GRID_SIZE; row++) {
					for (int column = 0; column < GRID_SIZE; column++)
						outputString.append(grid[row][column] == 0 ? "    -" :
								String.format("%5d", grid[row][column]));
					outputString.append("\n");
				}
				return outputString.toString();

			}
	}
