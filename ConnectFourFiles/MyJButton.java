package ConnectFourFiles;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.Timer;


/**
 * 
 * @author Nathan Roehl
 * Custom button used to represent the pieces.
 * Timers used to simulate a piece falling from the top of the game board.
 *
 */
public class MyJButton extends JButton {

	private static final long serialVersionUID = 1L;
	public final int row;
	public final int col;
	private boolean canPlace;
	public Turn colorPiece;
	private Timer timer1;
	private boolean currentTurn;
	public static final ImageIcon emptyCell = new ImageIcon(".\\Images\\CellEmpty.png");
	public static final ImageIcon redCell = new ImageIcon(".\\Images\\CellRed.png");
	public static final ImageIcon blackCell = new ImageIcon(".\\Images\\CellBlack.png");


	public MyJButton(boolean cp, int x, int y) {
		super();
		this.row = x;
		this.col = y;
		canPlace = cp;
		colorPiece = null;
		
		timer1 = new Timer(GameBoard.DELAY, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear();
			}
		});
		
		timer1.setRepeats(false);

	}

	public boolean canPlace() {
		return canPlace;
	}
	
	public boolean samePiece(Turn t) {
		return this.colorPiece == t;
	}
	
	public Turn getPiece() {
		return colorPiece;
	}

	public void setCanPlace(boolean cp) {
		canPlace = cp;
	}
	
	public void setTurn(Turn t) {
		this.colorPiece = t;
	}

	public void setCell(Turn t) {
		if(t == Turn.RED) {
			this.setIcon(redCell);
			this.colorPiece = Turn.RED;
		} else if (t == Turn.BLACK){
			this.setIcon(blackCell);
			this.colorPiece = Turn.BLACK;
		} else {
			this.setIcon(emptyCell);
			this.colorPiece = null;
		}
	}
	
	public void clear() {
		colorPiece = null;
		setIcon(emptyCell);
	}
	
	public void animate(boolean turn) {
		currentTurn = turn;
		setIcon(currentTurn ? redCell : blackCell);
		timer1.start();
	}
	
}
