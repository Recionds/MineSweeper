/*
package MineSearch;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Minesweeper {
	private JFrame frame; //지뢰찾기 게임판 
	private GamePanel gamePanel;
	private ControlPanel controlPanel;

	public Minesweeper() {
		frame = new JFrame("Minesweeper");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	
		controlPanel = new ControlPanel();
		frame.add(controlPanel, BorderLayout.NORTH);

		
		gamePanel = new GamePanel(controlPanel);
		frame.add(gamePanel, BorderLayout.CENTER);

		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(Minesweeper::new);
	}
}

 */
