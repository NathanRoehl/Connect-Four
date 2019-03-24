package ConnectFourFiles;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * 
 * @author Nathan Roehl
 * 
 * GameBoard does most of the logic for the game.
 * It is also responsible for maintaining all the pieces.
 * Timers used to simulate a game piece falling from the top of the board.
 *
 */
public class GameBoard extends JPanel {

	private ControlPanel cp;
	private static final long serialVersionUID = 1L;
	private boolean red;
	private MyJButton[][] backingGrid;
	private List <MyJButton> placedCells;
	private List <MyJButton> animateCells;
	private boolean running;
	private Timer timer1;
	private MyJButton animateButton;
	private int animateCount = 0;
	public static final int DELAY = 30;
	public static final int SIZE = 8;

	public GameBoard() {
		
		placedCells = new LinkedList<>();
		animateCells = new ArrayList<>(SIZE);
		red = true;
		running = true;
		
		/**
		 * Timer is set on repeat to mimic a piece falling.
		 * When final piece is found in animateCells the timer is stopped, the piece is placed,
		 * and the game checks for a winner.
		 */
		timer1 = new Timer(DELAY, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(animateCount < animateCells.size()-1 && animateButton != null) {
					animateButton.animate(red);
					animateCount += 1;
					animateButton = animateCells.get(animateCount);
				} else {
					timer1.stop();
					animateButton.setCell(red ? Turn.RED : Turn.BLACK);
					animateButton.setCanPlace(false);
					if(animateButton.row != 0) {
						backingGrid[animateButton.row-1][animateButton.col].setCanPlace(true);
					}
					animateCount = 0;
					animateButton = null;
					animateCells.clear();
					if(winner()) {
						running = false;
						JOptionPane.showMessageDialog(null, "Winner! Congratulations " + (red ? "Red Player" : "Black Player"));
						cp.setWinner(red);
					} else {
						red = red ? false:true;
						cp.updateTurn(red);
					}
				}
			}
		});
		timer1.setRepeats(true);
		fillBoard();
	}

	public void setControlPanel(ControlPanel cp) {
		if(cp != null && this.cp == null)
			this.cp = cp;
	}

	public void fillBoard() {
		
		this.setLayout(new GridLayout(SIZE,SIZE));
		backingGrid = new MyJButton[SIZE][SIZE];

		MyJButton mjb;
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {

				if(i == SIZE-1) {
					mjb = new MyJButton(true,i,j);
				} else {
					mjb = new MyJButton(false,i,j);
				}
				mjb.setCell(null);
				mjb.addActionListener(new placePiece());
				add(mjb);
				backingGrid[i][j] = mjb;
				
			}
		}
	}

	/**
	 * @author Nathan Roehl
	 * 
	 * Nested class that is run anytime a button is clicked.
	 *
	 */
	class placePiece implements ActionListener {

		public void actionPerformed(ActionEvent event) {

			if(running) {
				MyJButton mjb = (MyJButton) event.getSource();
				
				//While loop makes it so if any empty cell is clicked, the bottom most available
				//cell is selected as the location for the piece.
				while(!mjb.canPlace() && mjb.getPiece() == null) {
					mjb = backingGrid[mjb.row + 1][mjb.col];
				}

				if(mjb.canPlace()) {
					//Sends top most button of column that was clicked and bottom most available
					//cell to waterfall to animate and place the piece.
					waterfall(backingGrid[0][mjb.col],mjb);
				}
			} else {
				JOptionPane.showMessageDialog(null, "Please reset the game to play again.");
			}
			
		}
	}

	/**
	 * When a cell is clicked, waterfall is called to simulate a piece falling from top to bottom.
	 * This method uses timers in the GameBoard class and the MyJButton class.
	 * 
	 * @param start - The top cell in the column.
	 * @param finalLocation - The bottom cell that will be where the piece ends.
	 */
	private void waterfall(MyJButton start, MyJButton finalLocation) {
		
		animateButton = start;
		finalLocation.setTurn(red ? Turn.RED : Turn.BLACK);
		placedCells.add(finalLocation);
		animateCount = 0;
		
		while(start != finalLocation) {
			animateCells.add(start);
			start = backingGrid[start.row+1][start.col];
		}
		
		animateCells.add(finalLocation);
		timer1.start();
		
	}
	
	public void resetBoard() {
		
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {

				if(i == SIZE-1) {
					backingGrid[i][j].setCanPlace(true);
				} else {
					backingGrid[i][j].setCanPlace(false);
				}
				backingGrid[i][j].clear();
			}
		}
		
		placedCells.clear();
		running = true;
		red = true;
		cp.resetBoard(red);
	}

	/**
	 * Uses the "placedCells" arraylist to loop around only clicked cells.
	 * Method will check each clicked cell by looping around all adjacent cells.
	 * If an adjacent cell matches, it will continue down this path until 4 in a row,
	 * no match is found, or out of bounds.
	 * 
	 * @return
	 */
	public boolean winner() {
		MyJButton tmp;
		int count = 1, row, col;
		
		for(MyJButton button: placedCells) {
			for(int i = -1; i < 2; i++) {
				for(int j = -1; j < 2; j++) {
					row = button.row + i;
					col = button.col + j;
					if(row == button.row && col == button.col)
						continue;
					if(inBounds(row,col)) {
						tmp = backingGrid[row][col];
						while(tmp.samePiece(button.getPiece())) {
							count+=1;
							if(count == 4) {
								return true;
							}
							row += i;
							col += j;
							if(inBounds(row,col)) {
								tmp = backingGrid[row][col];
							} else {
								break;
							}
						}
						count = 1;
					}
				}
			}
		}
		return false;
	}
	
	public boolean inBounds(int x, int y) {
		if(x < SIZE && x >= 0 && y < SIZE && y >= 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * Helper method used for debugging.
	 * Was having some issues with winner(), where certain scenarios won the game when it shouldn't have.
	 * This method was very useful in finding the errors in winner().
	 */
	private void printOut() {
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				if(backingGrid[i][j].colorPiece == null) {
					System.out.print("null ");
				} else {
					System.out.print(backingGrid[i][j].colorPiece.toString() + " ");
				}
			}
			System.out.println();
		}
	}

}
