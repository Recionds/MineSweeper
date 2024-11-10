package makeTen.makeTen;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TenFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	static JLabel scoreLabel;

	public void setupMainPanel() {
		JPanel top = new JPanel(new GridLayout(1, 3));
		Board board = null;
		setTitle("Make Ten Game");
		Container pane = getContentPane(); // 여기서만 쓰이는 것은 지역변수로
		top.setPreferredSize(new Dimension(500, 50));
		board = new Board();

		JLabel topLabel = new JLabel("Make Ten");
		topLabel.setFont(topLabel.getFont().deriveFont(24.0f));
		scoreLabel = new JLabel("점수: 0");
		scoreLabel.setFont(scoreLabel.getFont().deriveFont(24.0f));
		top.add(scoreLabel, BorderLayout.CENTER);
		top.add(topLabel);

		JPanel posPane = new JPanel(new BorderLayout());
		posLabel = new JLabel("<html>현재위치 (0, 0)&nbsp</html>");
		posLabel.setFont(posLabel.getFont().deriveFont(28.0f));
		posPane.add(posLabel, BorderLayout.LINE_END);
		top.add(posPane, BorderLayout.LINE_END);

		pane.add(top, BorderLayout.PAGE_START);
		board.init();
		pane.add(board, BorderLayout.CENTER);
	}

	static void addScore() {
		String scoreText = scoreLabel.getText().substring(4);
		scoreLabel.setText("점수: " + (Integer.parseInt(scoreText) + 10));
	}

	static JLabel posLabel;

	static void setPosLabel(int x, int y) {
		posLabel.setText("<html>현재위치 (" + x + ", " + y + ")&nbsp</html>");
	}

	private void createAndShowGUI() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setupMainPanel();
		setPreferredSize(new Dimension(800, 600));
		pack();
		setVisible(true);
	}

	public static void main(String[] args) {
		TenFrame frame = new TenFrame();
		frame.createAndShowGUI();
	}
}
