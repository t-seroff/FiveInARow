package ProgAssignment3;
public class Sammy extends Player {
	
	public Sammy(Board.Pebble playersColor, Board theBoard){
		super(playersColor, theBoard);
	}
	
	public int[] nextMove(){
		return theBoard.clever(getColor());
	}
}
