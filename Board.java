package ProgAssignment3;
// Board class
/* Board numbering scheme:
(0,n) ... (n,n)
 ...  ...  ...
(0,0) ... (n,0)
*/

import java.util.Random;
import java.awt.*;

import javax.swing.*;

public class Board extends JFrame{
	public boolean CONSOLE = false; // False = use GUI, true = use console for input
	//Pebble enum
	public enum Pebble{
		// Declaration of possible values
		EMPTY(0), X(1), O(2);
		
		// Value for each enum constant
		private final int value;		
		
		// Initializes enum constants with their value
		Pebble (int theValue){			
			value = theValue;
		}
		
		// Returns value for an enum constant
		public int getValue(){			
			return value;	
		}
	}
	
	class GUIPebble extends JLabel{
	    int color;
	    public GUIPebble(int color){
	        this.color = color;
	    }
	    
	    // Draw the pebble
	    protected void paintComponent(Graphics g){
	        if(color == 0)
	            g.setColor(Color.BLACK);
	        else
	            g.setColor(Color.RED);
	        g.fillOval(0, 0, getSize().width-1, getSize().height-1);
	    }
	}
	
	// Board private variables
	private Pebble [] [] board;		// Outside array = rows (y), inside array = columns (x)
	public int n;
	private Random randomGen;
	
	// Constructor for board with size parameter
	public Board(int size){
        super("Five in a Row");
        this.setLayout(null);
		this.setSize((size+2)*80,(size+2)*80);
		this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
		n = size;
		board = new Pebble[size][size];
		randomGen = new Random();
		clearBoard();
	}

    public void paint(Graphics g) {
        super.paint(g);
        this.setBackground(Color.WHITE);
        for(int i=80; i<=(n+1)*80; i+= 80){
            g.drawLine(80, i, ((n+1)*80), i);
            g.drawLine(i, 80, i, ((n+1)*80));
        }
    }
	
	// Sets specified position to provided Pebble color
	public void setCell(int xPos, int yPos, Pebble aPebble){
		if (check(xPos, yPos)){
			// Disable verifying that the cell is empty - spec says "discard old value of the cell"
			//if (isEmpty(xPos, yPos)){
				board[yPos-1][xPos-1] = aPebble;
				
				// Update GUI
				int color = 0;
				if (aPebble == Pebble.X){
					color = 0;
				}
				else if (aPebble == Pebble.O){
					color = 1;
				}
			    
				GUIPebble p = new GUIPebble(color);
			    this.add(p);
			    p.setLocation(80*xPos+5, (80*(n+1))-(80*yPos+15));
			    p.setSize(70, 70);
			    p.setEnabled(true);
				
			/*}
			else{
				System.out.println("Specified cell is not empty!");
			}*/
		}
		else{
			System.out.println("Specified cell, (" + xPos + ", " + yPos + "), is invalid!");
		}
	}
	
	// Returns pebble at provided position
	public Pebble getCell(int xPos, int yPos){
		if (check(xPos, yPos)){
			return board[yPos-1][xPos-1];
		}
		else{
			return null;
		}
	}
	
	// Returns if board position is empty;
	public boolean isEmpty(int xPos, int yPos){
		if (check(xPos, yPos)){
			return (getCell(xPos, yPos) == Pebble.EMPTY);
		}
		else{
			return false;
		}
	}
	
	// Returns if the board is empty
	private boolean boardIsEmpty(){
		boolean boardIsEmpty = true;
		for (int i = 0; i < n; i++){
			for (int j = 0; j < n; j++){
				if (!isEmpty(i, j)){
					boardIsEmpty = false;
					break;
				}
			}
		}
		return boardIsEmpty;
	}
	
	// Returns if the board is full
	private boolean boardIsFull(){
		boolean boardIsFull = true;
		for (int i = 0; i < n; i++){
			for (int j = 0; j < n; j++){
				if (isEmpty(i, j)){
					boardIsFull = false;
					break;
				}
			}
		}
		return boardIsFull;
	}
	
	// Clears/initializes board to all empty
	public void clearBoard(){
		for (int i = 0; i < n; i++){
			for (int j = 0; j < n; j++){
				// Following convention with i = y, j = x, even though order doesn't matter here
				board[j][i] = Pebble.EMPTY;
			}
		}
	}
	
	// Returns if the game is over because board is full or one player has won
	public boolean gameEnds(){
		boolean playerWon = false;
		// Check if either player has won
		playerWon = (fiveX() | fiveO());
		
		// Check if board is full, false if any space is empty
		boolean boardFull = true;
		for (int i = 1; i <= n; i++){
			for (int j = 1; j <= n; j++){
				if (getCell(i, j) == Pebble.EMPTY){
					boardFull = false;
					break;
				}
			}
		}
		return (playerWon | boardFull);	
	}
	
	// Returns if there are five Xs in a row
	public boolean fiveX(){
		return linear(5, Pebble.X);
	}
	
	// Returns if there are five Os in a row
	public boolean fiveO(){
		return linear(5, Pebble.O);
	}
	
	// Private method to check for (number)-in-a-row of a specified color
	private boolean linear(int number, Pebble player){
		if (n < number){
			// Not possible if the board is too small
			return false;
		}
		boolean result = false;
		
		// Search each row
		for (int i = 1; i <= n; i++){
			for (int j = 1; j <= (n - (number - 1)); j++){
				// Search each grouping of 5 in the row
				boolean thisRowGroup = true;
				
				// If all 5 match, the group is a match
				for (int k = j; k < j + number; k++){
					if (getCell(i, k) != player){
						thisRowGroup = false;
					}
				}
				
				if (thisRowGroup){
					result = true;
				}
			}
		}
		
		// Search each column
		for (int j = 1; j <= n; j++){
			for (int i = 1; i <= (n - (number - 1)); i++){
				// Search each grouping of 5 in the row
				boolean thisColumnGroup = true;
				
				// If all 5 match, the group is a match
				for (int k = i; k < i + number; k++){
					if (getCell(k, j) != player){
						thisColumnGroup = false;
					}
				}
				
				if (thisColumnGroup){
					result = true;
				}
			}
		}
		
		return result;
	}
	
	// Returns number of 4 Xs in a row, diagonally
	public int fourX(){
		return four(Pebble.X);
	}
	
	// Returns number of 4 Os in a row, diagonally
	public int fourO(){
		return four(Pebble.O);
	}
	
	// Private method to count diagonal 4-in-a-rows of a specified color
	private int four(Pebble player){
		if (n < 4){
			// Not possible if board is too small
			return 0;
		}
		
		// Return -1 is board isn't full, as per spec
		if (!boardIsFull()){
			return -1;
		}
		
		
		int count = 0;
		
		// Check for diagonal 4-in-a-row in \ pattern
		// Outer loop => y
		for (int i = n; i >= 3; i--){
			// Inner loop => x
			for (int j = 0; j <= (n-4); j++){
				boolean thisDiagonal = true;
				
				// Advance through subsequent 3 values
				for (int k = 0; k <= 3; k++){
					if (getCell((i-k)+1, (j+k)+1) != player){
						thisDiagonal = false;
					}
				}
				if (thisDiagonal){
					count++;
				}
			}
		}
		
		// Check for diagonal 4-in-a-row in / pattern
		// Outer loop => y
		for (int i = 0; i <= (n-4); i++){
			// Inner loop => x
			for (int j = 0; j <= (n-4); j++){
				boolean thisDiagonal = true;
				
				// Advance through subsequent 3 values
				for (int k = 0; k <= 3; k++){
					if (getCell((i+k)+1, (j+k)+1) != player){
						thisDiagonal = false;
					}
				}
				if (thisDiagonal){
					count++;
				}
			}
		}
		return count;
	}
	
	// Returns game board 
	public Pebble[] [] getBoard(){
		// Conversion from Pebble[][] to int[][]
		/*int[][] intBoard = new int[n][n];
		for (int i = 0; i < n; i++){
			for (int j = 0; j < n; j++){
				intBoard[j][i] = getCell(i, j).getValue();
			}
		}*/
		return board;
	}
	
	// Method to return whether specified cell is valid (inside board bounds)
	public boolean check(int xPos, int yPos){
		return ((xPos > 0) & (xPos <= n) & (yPos > 0) & (yPos <= n));
	}
	
	// Sammy methods
	// Returns a random empty cell from the board
	public int[] randomEmpty(){
		// Try random cells until an empty one is found
		while(true){
			int x = randomGen.nextInt(n) + 1;
			int y = randomGen.nextInt(n) + 1;
			if (isEmpty(x, y)){
				int[] result = new int[2];
				result[0] = x;
				result[1] = y;
				return result;
			}
		}
	}
	
	// Returns a random cell adjacent to at least one other Pebble
	public int[] randomAdjacent(){
		// Try multiple random cells until one is valid
		return randomRadius(1);
	}
	
	// Returns a random cell with at least one Pebble within a radius of 2 cells from it
	public int[] randomClose(){
		return randomRadius(2);
	}
	
	// Private method to get a random cell within specified radius of a non-empty cell
	private int[] randomRadius(int radius){
		// Check if entire board is empty first

		// Is whole board has been searched and is empty, return null
		if (boardIsEmpty()){
			return null;
		}
		
		// Otherwise continue
		while(true){
			int[] possibleCell = randomEmpty();
			//System.out.println("Candidate cell is (" + possibleCell[0] + ", " + possibleCell[1] + ")");
			boolean hasAdjacent = false;
			
			// Check all adjacent cells
			// Outer loop -> x
			for (int i = possibleCell[0] - radius; i <= possibleCell[0] + radius; i++){
				// Inner loop -> y
				for (int j = possibleCell[1] - radius; j <= possibleCell[1] + radius; j++){
					// See if that cell is within the board limits
					if (check(i, j)){
						if (!isEmpty(i, j)){
							hasAdjacent = true;
							//System.out.println("Validation cell is (" + i + ", " + j + ")");
						}
					}
				}
			}
			if (hasAdjacent){
				// If at least one adjacent Pebble was found, the cell can be used
				return possibleCell;
			}
		}
	}
	
	// Intelligently selects an empty cell
	public int[] clever(Pebble color){
		if (color == Pebble.EMPTY){
			// Parameter is not allowed to be EMPTY by problem description
			return null;
		}
		
		
		double bestCellRating = -1.0;
		int bestCellxPos = -1;
		int bestCellyPos = -1;
		double [] coeffs = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		
		// Check every free space on the board, see which is best
		for (int i = 0; i < n; i++){
			for (int j = 0; j < n; j++){
				// Only empty spaces should be considered
				if (isEmpty(i, j)){
					double cellRating = 0.0;
					
					// Consider all of the player's Pebbles
					for (int factor = 1; factor <= 3; factor++){
						cellRating += (double)numConsecutive((5-factor), color, i, j) * coeffs[(factor-1)*2];
					}
					
					// Determine oponent's color
					Pebble opponent;
					if (color == Pebble.X){
						opponent = Pebble.O;
					}
					else{
						opponent = Pebble.X;
					}
					
					// Consider all of the oponent's Pebbles
					for (int factor = 1; factor <= 3; factor++){
						cellRating += (double)numConsecutive((5-factor), opponent, i, j) * coeffs[(factor*2)-1];
					}
					
					if (cellRating > bestCellRating){
						bestCellRating = cellRating;
						bestCellxPos = i;
						bestCellyPos = j;
					}
				}
			}
		}
		
		int [] bestMove = new int [2];
		bestMove[0] = bestCellxPos;
		bestMove[1] = bestCellyPos;
		
		// Temporary
		return randomAdjacent();
	}
	
	private int numConsecutive(int number, Pebble player, int xPos, int yPos){
		return -1;
	}
}
