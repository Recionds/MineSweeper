package makeTen.makeTen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Board extends JPanel implements ActionListener {
	static final int rowSize = 4;
	static final int colSize = 4;
	Random rand = new Random();
	MyButton[][] cellPanel = new MyButton[rowSize][colSize];

	void init() {
		setLayout(new GridLayout(rowSize, colSize));

		for (int i = 0; i < rowSize; i++) {
			for (int j = 0; j < colSize; j++) {
				cellPanel[i][j] = new MyButton(i, j);
				cellPanel[i][j].addActionListener(this);
				cellPanel[i][j].setFont(cellPanel[i][j].getFont().deriveFont(28.0f));
				add(cellPanel[i][j]);
			}
		}
		setPreferredSize(new Dimension(500, 300));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (selected != null && selected.getBackground() == Color.blue) {
			selected.setBackground((new JButton()).getBackground());
		}
		MyButton my = (MyButton) e.getSource();
		TenFrame.setPosLabel(my.x, my.y);
		my.setBackground(Color.blue);
		selected = my;
		my.setBackground(Color.blue);
		if (pressed == null) {
			pressed = my;
			return;
		}
		int sum = 0;
		int minx = Math.min(pressed.x, my.x);
		int maxx = Math.max(pressed.x, my.x);
		int miny = Math.min(pressed.y, my.y);
		int maxy = Math.max(pressed.y, my.y);
		for (int i = minx; i <= maxx; i++)
			for (int j = miny; j <= maxy; j++) {
				sum += cellPanel[i][j].num;
			}
		if (sum == 10) {
			for (int i = minx; i <= maxx; i++)
				for (int j = miny; j <= maxy; j++) {
					if (cellPanel[i][j].num == 0) {
						continue;
					}
					cellPanel[i][j].setBackground(Color.yellow);
					cellPanel[i][j].num = 0;
					cellPanel[i][j].setText("");
					TenFrame.addScore();
				}
		}
		pressed = null;
	}

	MyButton selected = null;

	class MyButton extends JButton {
		int x;
		int y;
		int num;

		MyButton(int x, int y) {
			this.x = x;
			this.y = y;
			num = rand.nextInt(1, 10);
			setText("<html>" + num + "</html>");
		}
	}

	static MyButton pressed = null;
}
