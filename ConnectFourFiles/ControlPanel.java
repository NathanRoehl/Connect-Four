package ConnectFourFiles;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * 
 * @author Nathan Roehl
 * 
 * Simple control panel that displays turn and allows players to reset the board.
 *
 */
public class ControlPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	private JLabel turn;
	private JLabel displayTurn;
	private GameBoard gb;
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JButton reset;
	
	
	public ControlPanel() {
		
		displayTurn = new JLabel("RED");
		turn = new JLabel("TURN: ");
		this.setLayout(new GridLayout(1,2));
		leftPanel = new JPanel();
		this.add(leftPanel);
		leftPanel.add(turn);
		leftPanel.add(displayTurn);
		
		reset = new JButton("RESET");
		reset.addActionListener(new ResetGame());
		rightPanel = new JPanel();
		rightPanel.add(reset);
		this.add(rightPanel);
		
	}
	
	class ResetGame implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			gb.resetBoard();
		}
	}
	
	public void setGameBoard(GameBoard gb) {
		if(gb != null && this.gb == null) {
			this.gb = gb;
		}
	}
	
	public void setWinner(boolean red) {
		turn.setText("WINNER: ");
		displayTurn.setText(red ? "RED" : "BLACK");
	}
	
	public void resetBoard(boolean red) {
		turn.setText("TURN: ");
		displayTurn.setText(red ? "RED" : "BLACK");
	}
	
	public void updateTurn(boolean red) {
		displayTurn.setText(red ? "RED" : "BLACK");
	}
	
}
