package MineSweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;

public class MainFrame extends JFrame {
  /* 상태 클래스 필드 */
  private Level level = Level.MIDDLE;
  private Mode mode = Mode.NORMAL;

  /* 화면 클래스 필드 */
  private MineGUI mineGUI = null;
  private LoginPanel loginGUI = null;

  /* 메뉴 클래스 필드 */
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

  /* 플레이어 랭킹 정보 배열리스트 */
  private ArrayList<Record> rankingRecord = new ArrayList<>();

  /* 문자열 클래스 필드 */
  private String helpString = "";
  private String endString = "게임을 종료하시겠습니까?";

  /* 화면 전환 레이아웃 */
  CardLayout cards = new CardLayout();
  Container cardPanel;

  MainFrame() {
    setTitle("지뢰찾기");
    initMenu();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    /* 화면 생성 후, 기본으로 로그인 화면 설정*/
    this.loginGUI = new LoginPanel(this);
    this.mineGUI = new MineGUI(this.level);
    setupCardLayout(loginGUI, mineGUI);
    this.setContentPane(cardPanel);
    this.pack();
    this.setCenterPosition();
    MediaTracker tracker = new MediaTracker(this);
    Image img = Toolkit.getDefaultToolkit().getImage("IMG/reset.gif");
    tracker.addImage(img, 0);
    setIconImage(img);
    this.setResizable(false);
    setVisible(true);
  }
  /* 카드레이아웃 설정 메서드 */
  public void setupCardLayout(JPanel loginPanel, JPanel gamePanel) {
    cardPanel = new JPanel(cards);

    cardPanel.add(loginPanel, "login");
    cardPanel.add(gamePanel, "game");

    cards.show(cardPanel, "login");
  }

  /* 새 게임 액션 리스너 */
  class newActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      changeGame();
    }
  }

  /* 게임 난이도 변경 액션 리스너 */
  class levelActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == rowLvItem) {
        changeLevel(Level.LOW);
      } else if (e.getSource() == midLvItem) {
        changeLevel(Level.MIDDLE);
      } else if (e.getSource() == highLvItem){
        changeLevel(Level.HIGH);
      } else {
        changeLevel(Level.CHALLENGE);
      }
    }
  }

  /* 게임 종료 액션 리스너 */
  class exitActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      int result;
      result = JOptionPane.showConfirmDialog(MainFrame.this, endString, "Game End",
              JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
      if (result == JOptionPane.YES_OPTION) System.exit(0);
    }
  }

  /*
  *  다이어로그 출력 액션 리스너
  *  도움말, 랭킹 출력
  */
  class dialogActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == helpItem) {
        JOptionPane.showMessageDialog(MainFrame.this, helpString, "Help", JOptionPane.INFORMATION_MESSAGE);
      } else if (e.getSource() == rankingItem) {
        String str = readRankingRecord();
        JTextArea textArea = new JTextArea(str);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        textArea.setBackground(new Color(217, 217, 217));
        JOptionPane.showMessageDialog(MainFrame.this, scrollPane, "랭킹 순위", JOptionPane.INFORMATION_MESSAGE);
      }
    }
  }

  /* ranking.txt 파일 읽어서 랭킹 출력용 문자열 생성 */
  public String readRankingRecord() {
    StringBuilder rankingText = new StringBuilder();
    Scanner filein = openFile("ranking.txt");
    Record record = null;
    while (filein.hasNext()) {
      record = new Record(filein);
      rankingRecord.add(record);
    }
    filein.close();
    /* 점수순으로 객체배열리스트 정렬 */
    rankingRecord.sort((r1, r2) -> Integer.compare(r2.getScore(), r1.getScore()));
    /* 최대 10위까지 정보 출력 */
    int limit = Math.min(10, rankingRecord.size());
    for (int i = 0; i < limit; i++) {
      Record r = rankingRecord.get(i);
      rankingText.append("\n" + (i + 1) + "위: ")
              .append("이름: ").append(r.getName()).append(", ")
              .append("난이도: ").append(r.getLevel()).append(", ")
              .append("모드: ").append(r.getMode()).append(", ")
              .append("시간: ").append(r.getTime()).append(", ")
              .append("점수: ").append(r.getScore()).append("\n");
    }
    return rankingText.toString();
  }

  /* 파일 입력 메서드 */
  Scanner openFile(String filename) {
    Scanner filein = null;
    try {
      filein = new Scanner(new File(filename));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return filein;
  }

  /* 메뉴 설정 */
  public void initMenu() {
    menuBar = new JMenuBar();
    gameMenu = new JMenu("게임");
    newGameItem = new JMenuItem("새 게임");
    rowLvItem = new JMenuItem("초급(9x9)");
    midLvItem = new JMenuItem("중급(16x16)");
    highLvItem = new JMenuItem("상급(16x30)");
    challLvItem = new JMenuItem("도전(20x30)");
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

  /* 화면 중앙 설정 */
  public void setCenterPosition() {
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frm = this.getSize();
    int xpos = (int) (screen.getWidth() / 2 - frm.getWidth() / 2);
    int ypos = (int) (screen.getHeight() / 2 - frm.getHeight() / 2);
    this.setLocation(xpos, ypos);
    MediaTracker tracker = new MediaTracker(this);

  }

  /* 난이도 변경 */
  public void changeLevel(Level lv) {
    this.level = lv;
    changeGame();
  }

  /* 새로운 설정으로 게임 변경 */
  public void changeGame() {
    this.remove(this.mineGUI);
    this.mineGUI = new MineGUI(this.level);
    this.setContentPane(this.mineGUI);
    this.pack();
    this.setCenterPosition();
    this.setResizable(false);
    this.setVisible(true);
  }

  /* 로그인 후 게임 시작 */
  public void changeGame(Level lv, Mode mode) {
    this.level = lv;
    this.mode = mode;
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
