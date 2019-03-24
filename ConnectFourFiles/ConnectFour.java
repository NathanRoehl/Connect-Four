package ConnectFourFiles;
import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 * 
 * @author Nathan Roehl
 * 
 * Main method.
 * Constructor allows gameboard to reference control panel and vice versa.
 * 
 */
public class ConnectFour extends JFrame {
	
	private static final long serialVersionUID = 1L;

	public ConnectFour() {
		setSize(580,580);
		setTitle("Connect Four");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		createContents();
		setVisible(true);
		setResizable(false);	
	}
	
	public void createContents() {

		GameBoard gb = new GameBoard();
		add(gb,BorderLayout.CENTER);
		ControlPanel cp = new ControlPanel();
		add(cp,BorderLayout.NORTH);
		gb.setControlPanel(cp);
		cp.setGameBoard(gb);
		
	}
	
	public static void main (String[] args) {
		new ConnectFour();
	}
	
}
