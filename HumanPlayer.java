package ProgAssignment3;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Scanner;

public class HumanPlayer extends Player {
	
	Scanner scan;
	boolean turnDone = false;
	int [] move;
	
	public HumanPlayer(Board.Pebble playersColor, Board theBoard){
		super(playersColor, theBoard);
		scan = new Scanner(System.in);
	}
	
	public int[] nextMove(){
		turnDone = false;
		move = new int[2];
		
		if (!theBoard.CONSOLE){
			// Wait until the user has clicked on a valid move
	        while (!turnDone) {
	            try {
	                 Thread.sleep(500);
	            } 
	            catch (InterruptedException exception) {
	            	;
	            }
	        }
	        return move;
		}
		else{
	        // Console move input method
			while (true){
				// Get the move
				System.out.println("Enter X position of play");
				int x = scan.nextInt();
				System.out.println("Enter Y position of play");
				int y = scan.nextInt();
				
				// If it's valid and empty, return this as the chosen move
				if (theBoard.check(x, y)){
					if (theBoard.isEmpty(x, y)){
						move[0] = x;
						move[1] = y;
						turnDone = true;
						return move;
					}
					else{
						// Otherwise print error and ask again
						System.out.println("That cell is not empty! Choose another.");
					}
				}
				else{
					System.out.println("That cell is not valid! Choose another.");
				}
			}
		}
	}
	
	// Mouse listener for user input of moves
    public class mouse implements MouseListener{
    	// Get location of click and determine corresponding location on grid
    	public void mouseReleased(MouseEvent e) { 
    			int x = e.getX();
    			int y = e.getY();
    			int xPos = (x / 80);
    			int yPos = theBoard.n - (y / 80) + 1;
    			
    			//System.out.println("(" + xPos + ", " + yPos + ")");
    			
    			// Only return the move if it's valid
    			if (theBoard.check(xPos, yPos)){
    				if (theBoard.isEmpty(xPos, yPos)){
    	    			move[0] = xPos;
    	    			move[1] = yPos;
    	    			turnDone = true;
    				}
    			}
    	}
    	
    	// Other required methods
    	public void mouseClicked(MouseEvent e) { }
    	public void mousePressed(MouseEvent e) { }
    	public void mouseEntered(MouseEvent e) { }
    	public void mouseExited(MouseEvent e) { }
    }
}
