package MineSweeper;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MainFrame extends JFrame {
  /* 상태 클래스 필드 */
  private Level level = Level.NORMAL;
  private Mode mode = null;

  /* 화면 클래스 필드 */
  private GamePanel gamePanel = null;
  private LoginPanel loginPanel = null;
  private RankingPanel rankingPanel = null;
  private HowToPanel howToPanel = null;
  private MyScorePanel myScorePanel = null;

  /* 랭킹 파일 경로 */
  public static final String FILE_NAME = "ranking.txt";
  public static final String currentDirectory = System.getProperty("user.dir");
  public static final String filePath = currentDirectory + File.separator + FILE_NAME;

  /* 화면 전환 레이아웃 */
  private CardLayout cards = new CardLayout();
  private Container cardPanel;

  /* 로그인 상태 확인 */
  public static boolean isLogin = false;

  MainFrame() {
    setTitle("지뢰찾기");
    ImageIcon icon = new ImageIcon(getClass().getResource("/icon.png"));
    setIconImage(icon.getImage());
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    /* 화면 생성 후, 기본으로 로그인 화면 설정*/
    loginPanel = new LoginPanel(this);
    gamePanel = new GamePanel(this.mode, this.level, this);
    rankingPanel = new RankingPanel(this);
    howToPanel = new HowToPanel(this);
    myScorePanel = new MyScorePanel(this);
    setupCardLayout();
    this.add(cardPanel);
    this.pack();
    this.setCenterPosition();
    this.setResizable(false);
    this.setVisible(true);
  }

  /*
   *  카드레이아웃 설정 메서드
   *  기본 화면 = 로그인 화면
   */
  public void setupCardLayout() {
    cardPanel = new JPanel(cards);
    cardPanel.add(loginPanel, "login");
    cardPanel.add(gamePanel, "game");
    cardPanel.add(rankingPanel, "ranking");
    cardPanel.add(howToPanel, "howto");
    cardPanel.add(myScorePanel, "myscore");
    cards.show(cardPanel, "login");
  }

  public void showRankingCard() {
    cards.show(cardPanel, "ranking");
    rankingPanel.setRankingBox();
  }

  public void showMyScoreCard() {
    cards.show(cardPanel, "myscore");
  }

  public void showHowToCard() {
    cards.show(cardPanel, "howto");
  }

  public void showGameCard() {
    cards.show(cardPanel, "game");
  }

  public void showLoginCard() {
    cards.show(cardPanel, "login");
  }

  /* 화면 중앙 설정 */
  public void setCenterPosition() {
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frm = this.getSize();
    int xpos = (int) (screen.getWidth() / 2 - frm.getWidth() / 2);
    int ypos = (int) (screen.getHeight() / 2 - frm.getHeight() / 2);
    this.setLocation(xpos, ypos);
  }

  /* 새로운 설정으로 게임 변경 */
  public void newGame() {
    this.remove(this.gamePanel);
    this.gamePanel = new GamePanel(this.mode, this.level, this);
    cardPanel.add(gamePanel, "game");
    cards.show(cardPanel, "game");
  }

  /* 로그인 후 게임 시작 */
  public void changeGame(Level lv, Mode mode) {
    this.level = lv;
    this.mode = mode;
    this.remove(this.gamePanel);
    this.gamePanel = new GamePanel(this.mode, this.level, this);
    cardPanel.add(gamePanel, "game");
    cards.show(cardPanel, "game");
  }

  public static void main(String[] args) {
    MainFrame mf = new MainFrame();
  }
}
