package MineSweeper;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

public class MineGUI extends JPanel implements ActionListener {
  /* 상태 클래스 필드 */
  private Level level = Level.MIDDLE;
  private Mode mode = Mode.NORMAL;
  private Game game = Game.READY;

  /* 화면 구성요소 클래스 필드 */
  private int ROWS = 16;
  private int COLS = 16;
  private int CELL_SIZE = 40;
  private int MINE = 40;
  private int checkMine = 40;
  private int flagCount = 0;
  private int life = 1;
  private int score = 0;

  /* 화면 소스 클래스 필드 */
  private JLabel[] timeLabel = null;
  private Timer timer = new Timer(1000, this);
  private int time = 0;
  private Tile[][] tile = null;
  private JLabel mineCount = null;
  private JLabel lifeCount = null;

  /* 화면 필드 */
  private JPanel progressPanel = null;
  private JPanel gamePanel = null;


  /* 이미지 아이콘 필드 */
  private ImageIcon[] numImgList = null;
  private ImageIcon[] mineImgList = null;
  private ImageIcon[] tileNumImgList = null;
  private ImageIcon defaultImage = null;
  private ImageIcon closeImage = null;
  private ImageIcon pressedImage = null;
  private ImageIcon wildcardImage = null;
  private ImageIcon flagImage = null;

  /* Listener inner class */
  private TileActionListener[][] tileActionListener = null;
  private TileMouseListener[][] tileMouseListener = null;

  /* Font Field */
  Font font1 = new Font("Britannic 굵게", Font.BOLD, 17);
  Font font2 = new Font("Britannic 굵게", Font.BOLD, 16);


  /* 	난이도 입력받아 게임 패널 생성하는 생성자 */
  public MineGUI(Level lv) {
    if (lv == Level.LOW) {
      ROWS = 9;
      COLS = 9;
      MINE = 9;
      checkMine = 9;
    } else if (lv == Level.MIDDLE) {
      ROWS = 16;
      COLS = 16;
      MINE = 40;
      checkMine = 40;
    } else if (lv == Level.HIGH) {
      ROWS = 16;
      COLS = 30;
      MINE = 99;
      checkMine = 99;
    } else {
      ROWS = 20;
      COLS = 30;
      MINE = 120;
      checkMine = 120;
    }
    level = lv;
    this.setLayout(new BorderLayout());
    setImageLoading();
    setProgressPane();
    setGamePane();
  }

  /* 이미지 불러오기 */
  public void setImageLoading() {
    numImgList = new ImageIcon[10];
    for (int i = 0; i < 10; i++)
      numImgList[i] = new ImageIcon("img/" + i + "n.gif");

    mineImgList = new ImageIcon[3];
    for (int i = 0; i < 3; i++)
      mineImgList[i] = new ImageIcon("img/mine" + (i + 1) + ".png");

    tileNumImgList = new ImageIcon[7];
    for (int i = 0; i < 7; i++)
      tileNumImgList[i] = new ImageIcon("img/" + (i + 1) + "s.png");

    defaultImage = new ImageIcon("img/default.png");
    closeImage = new ImageIcon("img/close.png");
    pressedImage = new ImageIcon("img/pressed.png");
    wildcardImage = new ImageIcon("img/wildcard.png");
    flagImage = new ImageIcon("img/flag.png");
  }

  /* 진행 정보 패널 설정 - mine, time, life 정의 메서드 */
  public void setProgressPane() {
    progressPanel = new JPanel();
    progressPanel.setLayout(new GridLayout(1, 3));
    JPanel lPanel = new JPanel();
    JPanel cPanel = new JPanel();
    JPanel rPanel = new JPanel();
    JPanel minePanel = new JPanel();
    JPanel timePanel = new JPanel();
    JPanel lifePanel = new JPanel();

    mineCount = new JLabel(MINE + "", SwingConstants.CENTER);
    minePanel = makeProgressPanel(minePanel, "Mine", mineCount);
    lPanel.add(minePanel);

    timePanel.setLayout(new GridLayout(1, 8));
    timeLabel = new JLabel[3];
    for (int i = 0; i < 3; i++) {
      timeLabel[i] = new JLabel(numImgList[0]);
      timePanel.add(timeLabel[i]);
    }
    cPanel.add(timePanel);

    lifeCount = new JLabel(life + "", SwingConstants.CENTER);
    lifePanel = makeProgressPanel(lifePanel, "Life", lifeCount);
    rPanel.add(lifePanel);

    progressPanel.add(lPanel);
    progressPanel.add(cPanel);
    progressPanel.add(rPanel);
    this.add(progressPanel, BorderLayout.NORTH);

    lPanel.setOpaque(true);
    cPanel.setOpaque(true);
    rPanel.setOpaque(true);

    lPanel.setBackground(Color.BLACK);
    cPanel.setBackground(Color.BLACK);
    rPanel.setBackground(Color.BLACK);
  }

  /* setProgressPane의 세부 패널 제작 */
  public JPanel makeProgressPanel(JPanel panel, String name, JLabel label2) {
    panel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;

    JLabel label1 = new JLabel(name, SwingConstants.CENTER);
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(-20, 0, 0, 0);
    panel.add(label1, gbc);
    label1.setFont(font1);

    gbc.gridy = 1; // 두 번째 행에 위치
    gbc.insets = new Insets(-20, 0, -10, 0);
    panel.add(label2, gbc);
    label2.setFont(font2);

    panel.setBackground(new Color(217, 217, 217));
    panel.setPreferredSize(new Dimension((int) (CELL_SIZE * 1.5), (int) (CELL_SIZE * 1.5)));
    return panel;
  }

  /* 게임 패널 설정 메서드 */
  public void setGamePane() {
    gamePanel = new JPanel();
    gamePanel.setLayout(new GridLayout(ROWS, COLS));
    gamePanel.setPreferredSize(new Dimension(CELL_SIZE * COLS, CELL_SIZE * ROWS));
    setButtonTile();
    this.add(gamePanel, BorderLayout.CENTER);
    game = Game.READY;
  }

  /*  타일에 리스너를 배정하고 상태를 정의해주는 메서드  */
  public void setButtonTile() {
    int[][] map = configureMine();
    tile = new Tile[ROWS][COLS];
    tileActionListener = new TileActionListener[ROWS][COLS];
    tileMouseListener = new TileMouseListener[ROWS][COLS];
    for (int r = 0; r < ROWS; r++) {
      for (int c = 0; c < COLS; c++) {
        tile[r][c] = new Tile(map[r][c], r, c, closeImage);

        tileActionListener[r][c] = new TileActionListener();
        tile[r][c].addActionListener(tileActionListener[r][c]);
        tileMouseListener[r][c] = new TileMouseListener();
        tile[r][c].addMouseListener(tileMouseListener[r][c]);

        tile[r][c].setPressedIcon(pressedImage);
        tile[r][c].setDisabledIcon(defaultImage);
        tile[r][c].setRolloverEnabled(false);
        gamePanel.add(tile[r][c]);
      }
    }
  }

  /*	맵에 임의로 지뢰를 배치하고,
      지뢰 주변의 셀에 해당 셀 근처에 몇 개의 지뢰가 있는지 설정한 맵을 반환하는 메서드
  *   0  = 지뢰가 없는 상태
  *   -1 = 지뢰가 있는 상태
  */
  public int[][] configureMine() {
    int count = 0;
    //맵을 모두 빈칸으로 초기화
    int[][] map = new int[ROWS][COLS];
    for (int r = 0; r < ROWS; r++)
      for (int c = 0; c < COLS; c++)
        map[r][c] = 0;

    int[] dx = {-1, -1, -1, 0, 0, 0, 1, 1, 1};
    int[] dy = {-1, 0, 1, -1, 0, 1, -1, 0, 1};
    /*
     *   무작위 위치에 지뢰 배치
     *   이미 지뢰가 있는 경우( != 0이면) 배치하지 않음
     *   설치한 지뢰의 개수가 총 지뢰 개수와 같으면 종료
     */
    while (true) {
      int x = (int) (Math.random() * ROWS);
      int y = (int) (Math.random() * COLS);
      if (map[x][y] != 0) continue;
      else {
        map[x][y] = -1;
        count++;
      }
      if (count == MINE) break;
    }

    //지뢰 주변 8방향의 셀에 해당 셀 근처의 지뢰 개수를 부여
    for (int r = 0; r < ROWS; r++) {
      for (int c = 0; c < COLS; c++) {
        if (map[r][c] == -1) {
          for (int i = 0; i < 9; i++) {
            try {
              if (!(dx[i] == 0 && dy[i] == 0) && map[r + dx[i]][c + dy[i]] != -1)
                map[r + dx[i]][c + dy[i]]++;
            } catch (IndexOutOfBoundsException e) {
            }
          }
        }
      }
    }
    return map;
  }

  //0인 칸을 열었을 경우, 주변의 0인 칸을 재귀적으로 열어주는 메서드
  public void spaceTileOpen(int r, int c) {
    if (!validRange(r, c)) return;
    if (tile[r][c].getState() == State.OPEN) return;
    if (tile[r][c].getFace() == -1) return;
    if (tile[r][c].getState() != State.FLAG) {
      tile[r][c].setState(State.OPEN);
    } else return;
    if (tile[r][c].getFace() != 0 && tile[r][c].getFace() != -1) {
      tile[r][c].setState(State.OPEN);
      return;
    }

    spaceTileOpen(r - 1, c);
    spaceTileOpen(r + 1, c);
    spaceTileOpen(r, c - 1);
    spaceTileOpen(r, c + 1);
    spaceTileOpen(r - 1, c - 1);
    spaceTileOpen(r - 1, c + 1);
    spaceTileOpen(r + 1, c - 1);
    spaceTileOpen(r + 1, c + 1);
  }

  // 맵을 벗어나지 않는지 확인하는 메서드
  public boolean validRange(int r, int c) {
    return ((r >= 0 && r < ROWS) && (c >= 0 && c < COLS));
  }

  /*
   *  타일이 0이 아닌 숫자이면서 지뢰 타일이 아닐 때,
   *  해당 타일에 맞는 숫자 이미지로 대체하고 클릭을 비활성화 하는 메서드
   */
  public void replaceTile() {
    for (int r = 0; r < ROWS; r++) {
      for (int c = 0; c < COLS; c++) {
        if (tile[r][c].getState() == State.OPEN) {
          if (tile[r][c].getFace() != 0 && tile[r][c].getFace() != -1) {
            tile[r][c].setDisabledIcon(tileNumImgList[tile[r][c].getFace() - 1]);
          }
          tile[r][c].setEnabled(false);
        }
      }
    }
  }

  /*  게임 종료 메서드  */
  public void GameOver() {
    for (int r = 0; r < ROWS; r++) {
      for (int c = 0; c < COLS; c++) {
        tile[r][c].removeActionListener(tileActionListener[r][c]);
        tile[r][c].removeMouseListener(tileMouseListener[r][c]);
        if (tile[r][c].getFace() == -1) {
          tile[r][c].setDisabledIcon(mineImgList[0]);
          tile[r][c].setEnabled(false);
          if (tile[r][c].getState() == State.FLAG)
            tile[r][c].setDisabledIcon(mineImgList[2]);
        }
      }
    }
    calculateScore();
    JOptionPane.showMessageDialog(this, "지뢰를 밟았습니다...\n플레이어 : " + Player.name + "\n플레이 타임 : " + time +
            "\n플레이 난이도 : " + level, "Game Over...", JOptionPane.OK_OPTION);
    game = Game.END;
  }

//  /*  게임 재시작 메서드  */
//  public void reStartGame() {
//    this.remove(progressPanel);
//    this.remove(gamePanel);
//    this.setVisible(false);
//    setProgressPane();
//    setGamePane();
//    this.setVisible(true);
//  }

  //남은 지뢰 개수 표시
  public void diplayMineCount() {
    mineCount.setText(checkMine + "");
  }

  //모든 지뢰에 깃발을 세웠는지 확인
  public boolean isAllCheckFlagMine() {
    if (game != Game.READY) {
      for (int r = 0; r < ROWS; r++) {
        for (int c = 0; c < COLS; c++) {
          if (tile[r][c].getFace() != -1 && tile[r][c].getState() == State.FLAG) {
            return false;
          }
          if (tile[r][c].getFace() != -1 && tile[r][c].getState() != State.OPEN) {
            return false;
          }
        }
      }
      return (flagCount == MINE);
    } else return false;
  }

  //게임 스코어 계산
  public void calculateScore() {
    int defaultScore = 0;
    int div = (int) (time * 0.1);
    if (level == Level.LOW) defaultScore = 5000;
    else if (level == Level.MIDDLE) defaultScore = 10000;
    else if (level == Level.HIGH) defaultScore = 15000;
    if (div == 0) div = defaultScore;
    score = (defaultScore / div) + defaultScore;
  }

  /* 게임 승리
  *  게임 기록 레코드에 저장
  */
  public void GameWin() {
    for (int r = 0; r < ROWS; r++) {
      for (int c = 0; c < COLS; c++) {
        tile[r][c].removeActionListener(tileActionListener[r][c]);
        tile[r][c].removeMouseListener(tileMouseListener[r][c]);
      }
    }
    timer.stop();
    calculateScore();
    try {
      FileWriter fileWriter = new FileWriter("ranking.txt", true);
      String st = "\n" + new Record(Player.name, level.toString(), mode.toString(), time, score);
      fileWriter.write(st);
      fileWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    JOptionPane.showMessageDialog(this, "모든 지뢰를 찾았습니다!\n플레이어 : " + Player.name + "\n플레이 난이도 : " + level +
            "\n플레이 시간 : " + time + "\n점수: " + score, "Game Clear!", JOptionPane.OK_OPTION);
    game = Game.END;
  }

  /* 타이머 */
  public void actionPerformed(ActionEvent e) {
    if (time == 1000) {
      timer.stop();
      return;
    }
    time = time + 1;
    int one = time % 10;
    int ten = time / 10;
    int hund = 0;
    if (ten / 10 != 0) {
      hund = ten / 10;
      ten = ten % 10;
    }
    timeLabel[0].setIcon(numImgList[hund]);
    timeLabel[1].setIcon(numImgList[ten]);
    timeLabel[2].setIcon(numImgList[one]);
  }

  //근처 타일이 눌러져있을 때, 눌러진 상태 이미지로 변경
  public void aroundTilePressed(int r, int c) {
    int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
    int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

    for (int i = 0; i < 8; i++) {
      try {
        if (tile[r + dx[i]][c + dy[i]].getState() != State.FLAG && tile[r + dx[i]][c + dy[i]].getState() != State.OPEN)
          tile[r + dx[i]][c + dy[i]].setIcon(pressedImage);
      } catch (IndexOutOfBoundsException e) {
      }
    }
  }

  // 주변 타일 열기
  public void aroundTileReleased(int r, int c) {
    int flagCnt = 0;
    int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
    int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

    // 주변 8방향의 타일을 확인하고 깃발 개수를 저장한다.
    for (int i = 0; i < 8; i++) {
      try {
        if (tile[r + dx[i]][c + dy[i]].getState() == State.FLAG) flagCnt++;
      } catch (IndexOutOfBoundsException e) {
      }
    }
    /*
     *   타일의 숫자와 주변 플래그 개수가 같다면
     *   즉, 플레이어가 합리적인 예측을 했다면
     *   주변 타일을 확인하면서 하나씩 열어본다.
     * */
    if (tile[r][c].getFace() == flagCnt) {
      for (int i = 0; i < 8; i++) {
        try {
          if (tile[r + dx[i]][c + dy[i]].getState() == State.CLOSE | tile[r + dx[i]][c + dy[i]].getState() == State.WILDCARD) {
            Tile t = tile[r + dx[i]][c + dy[i]];
            //지뢰가 있는 경우 게임을 종료하고 지뢰 이미지를 표시한다.
            if (t.getFace() == -1) {
              game = Game.END;
              t.setDisabledIcon(mineImgList[1]);
              timer.stop();
              game = Game.END;
              GameOver();
              return;
            }
            //숫자 타일이면 타일을 열고, 해당 숫자 이미지를 표시한다.
            else if (t.getFace() != 0 && t.getFace() != -1) {
              t.setState(State.OPEN);
              t.setDisabledIcon(tileNumImgList[t.getFace() - 1]);
              t.setEnabled(false);
            }
            //빈 타일이면, 주변의 빈 타일을 재귀적으로 열고, 열린 타일들의 상태를 업데이트 한다.
            else {
              spaceTileOpen(t.getRow(), t.getCol());
              replaceTile();
            }
          }
        } catch (IndexOutOfBoundsException e) {
        }
      }
    }
    /*
     * 타일의 숫자와 플래그 개수가 같지 않다면,
     * 즉, 플레이어의 예측이 비합리적이라면
     * 주변 타일의 닫힌 상태를 유지한다.
     */
    else {
      for (int i = 0; i < 8; i++) {
        try {
          if (tile[r + dx[i]][c + dy[i]].getState() != State.FLAG && tile[r + dx[i]][c + dy[i]].getState() != State.OPEN)
            tile[r + dx[i]][c + dy[i]].setIcon(closeImage);
        } catch (IndexOutOfBoundsException e) {
        }
      }
    }
  }

  /*
   *  타일 클릭 액션리스너
   *  타일이 클릭되었을 때, 게임이 레디 상태라면 게임을 시작한다.
   *  지뢰 타일이라면 게임 종료, 숫자 타일이라면 타일을 열고, 빈 타일이라면 주변의 빈 타일을 재귀적으로 연다.
   * */
  class TileActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if (game == Game.READY) {        // timer start
        timer.start();
        game = Game.START;
      }
      Tile t = (Tile) e.getSource();
      if (t.getState() == State.CLOSE | t.getState() == State.WILDCARD) {
        if (t.getFace() == -1) {
          game = Game.END;
          t.setDisabledIcon(mineImgList[1]);
          timer.stop();
          game = Game.END;
          GameOver();
          return;
        } else if (t.getFace() != 0 && t.getFace() != -1) {
          t.setState(State.OPEN);
          t.setDisabledIcon(tileNumImgList[t.getFace() - 1]);
          t.setEnabled(false);
        } else {
          spaceTileOpen(t.getRow(), t.getCol());
          replaceTile();
        }
      }
      if (isAllCheckFlagMine()) {
        GameWin();
      }
    }
  }

  /*
   *  타일 마우스 클릭 이벤스 리스너
   * */
  class TileMouseListener extends MouseAdapter {
    private boolean bothLeft = false;
    private boolean bothRight = false;

    public void mousePressed(MouseEvent e) {
      Tile t = (Tile) e.getSource();
      if (SwingUtilities.isLeftMouseButton(e)) {
        //newButton.setIcon(newImgList[1]);
        if (t.getState() == State.OPEN) {
          bothLeft = true;
        }
      }
      /*
       * 우클릭을 했을 때,
       * 1. 닫혀있는 타일이라면 -> 플래그 설정, 남은 지뢰 개수 -1 표시,
       *   1-1. 진짜 지뢰 타일이라면 checkMine -1 (예측 성공)
       * 2. 플래그 타일이라면 -> 와일드카드 설정, 남은 지뢰 개수 +1 표시
       *   2-1. 진짜 지뢰 타일이라면 checkMine +1 (예측 실패)
       * 3. 와일드카드 타일이라면 -> 닫힌 타일로 설정
       * */
      if (SwingUtilities.isRightMouseButton(e)) {
        if (t.getState() != State.OPEN) {
          if (t.getState() == State.CLOSE) {
            t.setIcon(flagImage);
            t.setState(State.FLAG);
            checkMine -= 1;
            if (t.getFace() == -1) flagCount++;
            diplayMineCount();
          } else if (t.getState() == State.FLAG) {
            t.setIcon(wildcardImage);
            t.setState(State.WILDCARD);
            checkMine += 1;
            if (t.getFace() == -1) flagCount--;
            diplayMineCount();
          } else {
            t.setIcon(closeImage);
            t.setState(State.CLOSE);
          }
        }
        // 모든 지뢰를 탐색했다면 게임 승리
        if (isAllCheckFlagMine()) {
          GameWin();
        }
        // both mouse check
        if (t.getState() == State.OPEN) {
          bothRight = true;
        }
      }
      if (t.getState() == State.OPEN) {
        if (bothLeft == true && bothRight == true) {
          aroundTilePressed(t.getRow(), t.getCol());
        }
      }
    }

    public void mouseReleased(MouseEvent e) {
      Tile t = (Tile) e.getSource();
      if (SwingUtilities.isLeftMouseButton(e)) {
        if (t.getState() == State.OPEN) {
          bothLeft = false;
        }
      }
      if (SwingUtilities.isRightMouseButton(e)) {
        if (t.getState() == State.OPEN) {
          bothRight = false;
        }
      }
      if (t.getState() == State.OPEN) {
        if (!bothLeft && !bothRight) {
          aroundTileReleased(t.getRow(), t.getCol());
        }
      }
      if (isAllCheckFlagMine()) {
        GameWin();
      }
    }
  }
}
