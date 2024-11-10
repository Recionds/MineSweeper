package MineSweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private Level level = Level.MIDDLE;
    private MineGUI mineGUI = null;

    private JMenuBar menuBar = null;
    private JMenu gameMenu = null;
    private JMenuItem newGameItem = null;
    private JMenuItem rowLvItem = null;
    private JMenuItem midLvItem = null;
    private JMenuItem highLvItem = null;
    private JMenuItem challLvItem = null;
    private JMenuItem exitItem = null;

    private JMenu rankingMenu = null;
    private JMenuItem rankingItem = null;

    private JMenu helpMenu = null;
    private JMenuItem helpItem = null;


    private String helpString = "";
    private String endString = "게임을 종료하시겠습니까?";

    MainFrame() {
        setTitle("지뢰찾기");
        initMenu();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.mineGUI = new MineGUI(this.level);
        this.setContentPane(this.mineGUI);
        this.pack();
        this.setCenterPosition();
        MediaTracker tracker = new MediaTracker(this);
        Image img = Toolkit.getDefaultToolkit().getImage("IMG/reset.gif");
        tracker.addImage(img, 0);
        setIconImage(img);

        this.setResizable(false);
        setVisible(true);
    }

    class newActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            changeGame();
        }
    }

    class levelActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == rowLvItem) {
                changeLevel(Level.ROW);
            } else if (e.getSource() == midLvItem) {
                changeLevel(Level.MIDDLE);
            } else {
                changeLevel(Level.HIGH);
            }
        }
    }

    class exitActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int result;
            result = JOptionPane.showConfirmDialog(MainFrame.this, endString, "Game End",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.YES_OPTION) System.exit(0);
        }
    }

    class dialogActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == helpItem) {
                JOptionPane.showMessageDialog(MainFrame.this, helpString, "Help", JOptionPane.INFORMATION_MESSAGE);
            }
            else if (e.getSource() == rankingItem) {
                JOptionPane.showMessageDialog(MainFrame.this, helpString, "랭킹 순위", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public void initMenu() {
        menuBar = new JMenuBar();
        gameMenu = new JMenu("게임");
        newGameItem = new JMenuItem("새 게임");
        rowLvItem = new JMenuItem("초급(9x9)");
        midLvItem = new JMenuItem("중급(16x16)");
        highLvItem = new JMenuItem("상급(16x30)");
        challLvItem = new JMenuItem("도전(타임어택)");
        exitItem = new JMenuItem("게임 종료");

        newGameItem.addActionListener(new newActionListener());
        rowLvItem.addActionListener(new levelActionListener());
        midLvItem.addActionListener(new levelActionListener());
        highLvItem.addActionListener(new levelActionListener());
        challLvItem.addActionListener(new levelActionListener());
        exitItem.addActionListener(new exitActionListener());

        gameMenu.add(newGameItem);
        gameMenu.addSeparator();
        gameMenu.add(rowLvItem);
        gameMenu.add(midLvItem);
        gameMenu.add(highLvItem);
        gameMenu.add(challLvItem);
        gameMenu.addSeparator();
        gameMenu.add(exitItem);

        menuBar.add(gameMenu);

        rankingMenu = new JMenu("랭킹");
        rankingItem = new JMenuItem("랭킹 보기");

        rankingItem.addActionListener(new dialogActionListener());

        rankingMenu.add(rankingItem);
        menuBar.add(rankingMenu);

        helpMenu = new JMenu("도움말");
        helpItem = new JMenuItem("HELP");

        helpItem.addActionListener(new dialogActionListener());


        helpMenu.add(helpItem);
        helpMenu.addSeparator();

        menuBar.add(helpMenu);
        this.setJMenuBar(menuBar);
    }

    public void setCenterPosition() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frm = this.getSize();
        int xpos = (int) (screen.getWidth() / 2 - frm.getWidth() / 2);
        int ypos = (int) (screen.getHeight() / 2 - frm.getHeight() / 2);
        this.setLocation(xpos, ypos);
        MediaTracker tracker = new MediaTracker(this);

    }

    public void changeLevel(Level lv) {
        this.level = lv;
        changeGame();
    }

    public void changeGame() {
        this.remove(this.mineGUI);
        this.mineGUI = new MineGUI(this.level);
        this.setContentPane(this.mineGUI);
        this.pack();
        this.setCenterPosition();
        this.setResizable(false);
        this.setVisible(true);
    }


    public static void main(String[] args) {
        MainFrame mf = new MainFrame();
    }
}
