package ProgAssignment3;

public abstract class Player {
	Board.Pebble color;
	protected Board theBoard;
	
	Player(Board.Pebble playersColor, Board gameBoard){
		color = playersColor;
		theBoard = gameBoard;
	}
	
	Board.Pebble getColor(){
		return color;
	}
	
	abstract public int[] nextMove();
}
