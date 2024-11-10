/*
package MineSearch;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

public class Cell extends JButton {
	private boolean isMine;
	private boolean isFlagged;
	private int surroundingMines;

	public Cell() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					open();
				} else if (SwingUtilities.isRightMouseButton(e)) {
					toggleFlag();
				}
			}
		});
	}

	public void setMine() {
		isMine = true;
	}

	public boolean isMine() {
		return isMine;
	}

	public void setSurroundingMines(int count) {
		surroundingMines = count;
	}

	public int getSurroundingMines() {
		return surroundingMines;
	}

	void open() {
		if (isMine) {
			setText("X"); // Display mine (game over condition)
			// TODO: Trigger game over logic
		} else {
			setText(String.valueOf(surroundingMines));
			setEnabled(false); // Disable after opening
		}
	}

	private void toggleFlag() {
		isFlagged = !isFlagged;
		setText(isFlagged ? "F" : ""); // "F" for flag symbol
	}
}

 */
