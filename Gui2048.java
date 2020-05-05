import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;

import java.io.*;
/*Soyeon Kim, cs8bwaha
 *2/22/18
 *
 *This file contains class Gui2048, an inner Key Handler class,
 *and an inner Tile class that constructs a Tile object.
 *Gui2048 class contains a method to start a new game, and update
 *the board based on user input from inner Handler class.
 */


/**
 *This class Gui2048 has a method to start a 2048 game and 
 *updates GUI component of the game based on a user inpur
 */
public class Gui2048 extends Application
{
    private String outputBoard; // The filename for where to save the Board
    private Board board; // The 2048 Game Board

    private GridPane pane;

    private Tile tile;//The tile object to save Rectangle and Text
    private Tile[][] tileArr;//The array of Tile objects
    private Label score;//label that displays the score
    private Rectangle[][] recArr;//The rectangle array to save each Rectangle
    private Label name;//The label for title 2048
    private Color zeroColor = Color.rgb(0,0,0,0);//transparent color for zeros
private Scene scene;
    @Override
        /**
         *This method adds a Gridpane, a Scene to a Stage and display the
         *GUI component of game 2048. It accesses the board object and construct
         *a graphical board based on that.
         */
        public void start(Stage primaryStage)
        {
            // Process Arguments and Initialize the Game Board
            processArgs(getParameters().getRaw().toArray(new String[0]));

            // Create the pane that will hold all of the visual objects
            pane = new GridPane();
            pane.setAlignment(Pos.CENTER);
            pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
            pane.setStyle("-fx-background-color: rgb(187, 173, 160)");

            // Set the spacing between the Tiles
            pane.setHgap(15); 
            pane.setVgap(15);

            /** Add your Code for the GUI Here */
            primaryStage.setTitle("Gui2048");
            name = new Label("2048");
            name.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));
            name.setTextFill(Color.BLACK);

            //make a score board
            score = new Label("Score: " + board.getScore());
            score.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
            score.setTextFill(Color.BLACK);

            pane.add(name,0,0);
            pane.add(score,board.GRID_SIZE-2,0,2,1);

            GridPane.setHalignment(score, HPos.RIGHT);
            GridPane.setHalignment(name, HPos.LEFT);

            //get grid from the actual board
            int[][] grid = board.getGrid();

            //create a rectangle array
            recArr = new Rectangle[board.GRID_SIZE][board.GRID_SIZE];

            //create a tile array
            tileArr = new Tile[board.GRID_SIZE][board.GRID_SIZE];

            //creating rectangle for each tile on the board
            for(int i=0; i < board.GRID_SIZE; i++){
                for(int j =0; j < board.GRID_SIZE; j++){

                    tile = new Tile();

                    //get the number from the actual board grid
                    int x = grid[i][j];
                    String str = Integer.toString(x);

                    //assign that number to a Tile object
                    tile.setText(str);

                    //color object to store color based on tile color
                    Color color = Color.WHITE;

                    if(grid[i][j]== 0){ color = Constants2048.COLOR_EMPTY;
                        tile.getText().setFill(zeroColor); }

                    else if(grid[i][j] == 2){ color = Constants2048.COLOR_2;
                        tile.getText().setFont(Font.font
                                ("Times New Roman",FontWeight.BOLD,Constants2048.TEXT_SIZE_LOW));
                        tile.getText().setFill(Constants2048.COLOR_VALUE_DARK);
                    }
                    else if(grid[i][j] == 4){ color = Constants2048.COLOR_4;
                        tile.getText().setFont(Font.font
                                ("Times New Roman",FontWeight.BOLD,Constants2048.TEXT_SIZE_LOW));
                        tile.getText().setFill(Constants2048.COLOR_VALUE_DARK);
                    }
                    //create new rectangles 
                    recArr[i][j] = new Rectangle
                        (Constants2048.TILE_WIDTH, Constants2048.TILE_WIDTH, color);

//recArr[i][j].heightProperty().bind(pane.prefHeightProperty());
//recArr[i][j].widthProperty().bind(pane.prefWidthProperty());

                    //save rectangle in the tile object
                    tile.setRectangle(recArr[i][j]);

                    //save tile object in a tile array
                    tileArr[i][j] = tile;


//tile.getRectangle().heightProperty().bind(pane.prefHeightProperty());
//tile.getRectangle().widthProperty().bind(pane.prefWidthProperty());


                    //add rectangles on the pane
                    pane.add(tileArr[i][j].getRectangle(),j,i+1);

                    //add numbers on the rectangles
                    pane.add(tileArr[i][j].getText(),j,i+1);

                    GridPane.setHalignment(tile.getText(), HPos.CENTER);


//tile.getRectangle().heightProperty().divide(2).bind(pane.prefHeightProperty());
//tile.getRectangle().widthProperty().divide(2).bind(pane.prefWidthProperty());
                }
            }//end of for loop pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));


 scene = new Scene(pane);

//pane.prefHeightProperty().bind(recArr[i][j].getHeight());
//pane.prefWidthProperty().bind(recArr[i][j].getWidth());

//tile.getRectangle().widthProperty().bind(pane.prefWidthProperty());
//tile.getRectangle().heightProperty().bind(pane.prefWidthProperty());
pane.prefHeightProperty().bind(scene.heightProperty().divide(2));
pane.prefWidthProperty().bind(scene.widthProperty().divide(2));

         //   scene = new Scene(pane);
            primaryStage.setTitle("Gui2048");
            primaryStage.setScene(scene);
            primaryStage.show();

            //instantiate key handler to the scene
            scene.setOnKeyPressed(new myKeyHandler());
        }

    /**
     *This class handle keypresses made by a user.
     */
    private class myKeyHandler implements EventHandler<KeyEvent>{

        /**
         *This method handles a Key Event invoked by KeyEvent e. When a move
         *was nade, the board updates or saves the board.
         *
         *@param e the KeyEvent made by the user
         */
        @Override
            public void handle(KeyEvent e){

                if(e.getCode() == KeyCode.UP){
                    if(board.move(Direction.UP)){

                        board.addRandomTile();
                        System.out.println("Moving Up");
                    }
                    updateBoard();
                    if(board.isGameOver()){updateGameOver();}
                }

                else if(e.getCode() == KeyCode.DOWN){
                    if(board.move(Direction.DOWN)){

                        board.addRandomTile();
                        System.out.println("Moving Down");
                    }
                    updateBoard();
                    if(board.isGameOver()){updateGameOver();}
                }

                else if(e.getCode() == KeyCode.RIGHT){
                    if(board.move(Direction.RIGHT)){

                        board.addRandomTile();
                        System.out.println("Moving Right");
                    }
                    updateBoard();
                    if(board.isGameOver()){updateGameOver();}
                }

                else if(e.getCode() == KeyCode.LEFT){
                    if(board.move(Direction.LEFT)){

                        board.addRandomTile();
                        System.out.println("Moving Left");
                    }
                    updateBoard();
                    if(board.isGameOver()){updateGameOver();}
                }

                else if(e.getCode() == KeyCode.S){
                    try{
                        board.saveBoard(outputBoard);
                    }catch(IOException ex){
                        System.out.println("saveBoard threw an Exception");
                    } System.out.println("Saving Board to "+outputBoard);
                } 

            }
    }

    /**inner class
     *The Tile class has a constructor for a Tile class which
     *stores a Rectangle and a Text. It is used in outer class's start()
     *method to keep rectangles and its texts together
     */
    private class Tile{

        private Text text;
        private Rectangle rectangle;

        /**
         *The no-arg constructor for the Text class.It initializes a Text
         * to 0.
         */
        public Tile(){

            this.setText("0");
        }

        /**
         *The general constructor for the Text class. It sets text and rectangle
         *to the text and rectangle in the parameter.
         *
         * @param text the Text to store
         * @param r tge Rectangle to store
         */
        public Tile(String text, Rectangle r){
            this();
            this.setRectangle(r);
            this.setText(text);
        }

        /**
         *Getter for text.
         *
         *@return a text stored in tile
         */
        public Text getText(){
            return text;
        }

        /**
         *Setter for the text.
         *
         *@param t the string to set the text to
         */
        private void setText(String t){
            text = new Text(t);
        }

        /**
         *Getter for rectangle.
         *
         *@return a rectangle stored in tile
         */
        public Rectangle getRectangle(){
            return rectangle;
        }

        /**
         *Setter for rectangle.
         *
         *@param rectangle the rectangle to store in a tile
         */
        private void setRectangle(Rectangle rectangle){
            this.rectangle = rectangle;
        }
    }//end of the tile class

    /**
     *A method to be called in handle() method when the game is over.
     *It creates a new Rectangle and covers the entire screen with a message.
     */
    private void updateGameOver(){

StackPane stack = new StackPane();

        //create new semi-transparent stackpane
        stack.setPrefHeight(scene.getHeight());
stack.setPrefWidth(scene.getWidth());
// stack.setPadding(new Insets(50,50,50,50));
stack.setStyle("-fx-background-color: rgb(238, 228, 218,0.73)");

        Text txt = new Text();
        txt.setText("Game Over!");
        txt.setFont(Font.font("Times New Roman", FontWeight.BOLD, 50));
        txt.setFill(Constants2048.COLOR_VALUE_DARK);

        stack.getChildren().add(txt);

BorderPane border= new BorderPane();

pane.add(stack,0,0,Integer.MAX_VALUE, Integer.MAX_VALUE);
//stack.getChildren().add(pane);
      GridPane.setHalignment(txt, HPos.CENTER);
       GridPane.setValignment(txt, VPos.CENTER);


    }

    /**
     *Method that updates the GUI component of the boarad when a valid
     *move was made by a user. It reconstructs the board based on new 
     *value from board from the Board class.
     */
    private void updateBoard(){

        //access the updated grid from the board
        int[][] grid = board.getGrid();

        //clear GUI 
        pane.getChildren().clear();

        //update score
        score.setText("Score: " + board.getScore());

        pane.add(name,0,0);
        pane.add(score,board.GRID_SIZE-2,0,2,1);

        GridPane.setHalignment(score, HPos.RIGHT);
        GridPane.setHalignment(name, HPos.LEFT);

        for(int i=0;i<board.GRID_SIZE;i++){
            for(int j=0;j<board.GRID_SIZE;j++){

                int x=grid[i][j];
                String str = Integer.toString(x);

                //store number to a tile
                tile.setText(str);

                //use different color/size for tile and text with different sizes
                Color color = Color.WHITE;
                if(grid[i][j]== 0){ color = Constants2048.COLOR_EMPTY;
                    tile.getText().setFill(zeroColor);
                }
                else if(grid[i][j] == 2){ color = Constants2048.COLOR_2;
                    tile.getText().setFont(Font.font
                            ("Times New Roman", FontWeight.BOLD, Constants2048.TEXT_SIZE_LOW));
                    tile.getText().setFill(Constants2048.COLOR_VALUE_DARK);

                }
                else if(grid[i][j] == 4){ color = Constants2048.COLOR_4;
                    tile.getText().setFont(Font.font
                            ("Times New Roman", FontWeight.BOLD, Constants2048.TEXT_SIZE_LOW));
                    tile.getText().setFill(Constants2048.COLOR_VALUE_DARK);

                }
                else if(grid[i][j] == 8){ color = Constants2048.COLOR_8;
                    tile.getText().setFont(Font.font
                            ("Times New Roman", FontWeight.BOLD, Constants2048.TEXT_SIZE_LOW));
                    tile.getText().setFill(Constants2048.COLOR_VALUE_LIGHT);
                }
                else if(grid[i][j] == 16){ color = Constants2048.COLOR_16;
                    tile.getText().setFont(Font.font
                            ("Times New Roman", FontWeight.BOLD, Constants2048.TEXT_SIZE_LOW));
                    tile.getText().setFill(Constants2048.COLOR_VALUE_LIGHT);
                }
                else if(grid[i][j] ==32){ color = Constants2048.COLOR_32;
                    tile.getText().setFont(Font.font
                            ("Times New Roman", FontWeight.BOLD, Constants2048.TEXT_SIZE_LOW));
                    tile.getText().setFill(Constants2048.COLOR_VALUE_LIGHT);
                }
                else if(grid[i][j] == 64){ color = Constants2048.COLOR_64;
                    tile.getText().setFont(Font.font
                            ("Times New Roman", FontWeight.BOLD, Constants2048.TEXT_SIZE_LOW));
                    tile.getText().setFill(Constants2048.COLOR_VALUE_LIGHT);
                }
                else if(grid[i][j] == 128){ color = Constants2048.COLOR_128;
                    tile.getText().setFont(Font.font
                            ("Times New Roman", FontWeight.BOLD, Constants2048.TEXT_SIZE_MID));
                    tile.getText().setFill(Constants2048.COLOR_VALUE_LIGHT);
                }
                else if(grid[i][j] == 256){ color = Constants2048.COLOR_256;
                    tile.getText().setFont(Font.font
                            ("Times New Roman", FontWeight.BOLD, Constants2048.TEXT_SIZE_MID));
                    tile.getText().setFill(Constants2048.COLOR_VALUE_LIGHT);
                }
                else if(grid[i][j] == 512){ color = Constants2048.COLOR_512;
                    tile.getText().setFont(Font.font
                            ("Times New Roman", FontWeight.BOLD, Constants2048.TEXT_SIZE_MID));
                    tile.getText().setFill(Constants2048.COLOR_VALUE_LIGHT);
                }
                else if(grid[i][j] == 1024){ color = Constants2048.COLOR_1024;
                    tile.getText().setFont(Font.font
                            ("Times New Roman", FontWeight.BOLD, Constants2048.TEXT_SIZE_HIGH));
                    tile.getText().setFill(Constants2048.COLOR_VALUE_LIGHT);
                }
                else if(grid[i][j] == 2048){ color = Constants2048.COLOR_2048;
                    tile.getText().setFont(Font.font
                            ("Times New Roman", FontWeight.BOLD, Constants2048.TEXT_SIZE_HIGH));
                    tile.getText().setFill(Constants2048.COLOR_VALUE_LIGHT);
                }
                else if(grid[i][j] > 2048){ color = Color.BLACK;
                    tile.getText().setFont(Font.font
                            ("Times New Roman", FontWeight.BOLD, Constants2048.TEXT_SIZE_HIGH));
                    tile.getText().setFill(Constants2048.COLOR_VALUE_LIGHT);
                }
                //fill the tile color
                recArr[i][j].setFill(color);





                //store colored rectangle to the tile
                tile.setRectangle(recArr[i][j]);




                //update the tile
                pane.add(tile.getRectangle(), j, i+1);
                pane.add(tile.getText(),j,i+1);
                GridPane.setHalignment(tile.getText(), HPos.CENTER);


//pane.prefHeightProperty().bind(scene.heightProperty().divide(1));
//pane.prefWidthProperty().bind(scene.widthProperty().divide(1));

            }
        }

    }


    /** DO NOT EDIT BELOW */

    // The method used to process the command line arguments
    private void processArgs(String[] args)
    {
        String inputBoard = null;   // The filename for where to load the Board
        int boardSize = 0;          // The Size of the Board

        // Arguments must come in pairs
        if((args.length % 2) != 0)
        {
            printUsage();
            System.exit(-1);
        }

        // Process all the arguments 
        for(int i = 0; i < args.length; i += 2)
        {
            if(args[i].equals("-i"))
            {   // We are processing the argument that specifies
                // the input file to be used to set the board
                inputBoard = args[i + 1];
            }
            else if(args[i].equals("-o"))
            {   // We are processing the argument that specifies
                // the output file to be used to save the board
                outputBoard = args[i + 1];
            }
            else if(args[i].equals("-s"))
            {   // We are processing the argument that specifies
                // the size of the Board
                boardSize = Integer.parseInt(args[i + 1]);
            }
            else
            {   // Incorrect Argument 
                printUsage();
                System.exit(-1);
            }
        }

        // Set the default output file if none specified
        if(outputBoard == null)
            outputBoard = "2048.board";
        // Set the default Board size if none specified or less than 2
        if(boardSize < 2)
            boardSize = 4;

        // Initialize the Game Board
        try{
            if(inputBoard != null)
                board = new Board(new Random(), inputBoard);
            else
                board = new Board(new Random(), boardSize);
        }
        catch (Exception e)
        {
            System.out.println(e.getClass().getName() + 
                    " was thrown while creating a " +
                    "Board from file " + inputBoard);
            System.out.println("Either your Board(String, Random) " +
                    "Constructor is broken or the file isn't " +
                    "formated correctly");
            System.exit(-1);
        }
    }

    // Print the Usage Message 
    private static void printUsage()
    {
        System.out.println("Gui2048");
        System.out.println("Usage:  Gui2048 [-i|o file ...]");
        System.out.println();
        System.out.println("  Command line arguments come in pairs of the "+ 
                "form: <command> <argument>");
        System.out.println();
        System.out.println("  -i [file]  -> Specifies a 2048 board that " + 
                "should be loaded");
        System.out.println();
        System.out.println("  -o [file]  -> Specifies a file that should be " + 
                "used to save the 2048 board");
        System.out.println("                If none specified then the " + 
                "default \"2048.board\" file will be used");  
        System.out.println("  -s [size]  -> Specifies the size of the 2048" + 
                "board if an input file hasn't been"); 
        System.out.println("                specified.  If both -s and -i" + 
                "are used, then the size of the board"); 
        System.out.println("                will be determined by the input" +
                " file. The default size is 4.");
    }
}
