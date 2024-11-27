package MineSweeper;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class GamePanel extends JPanel implements ActionListener {
  /* 메인 프레임 객체 */
  private MainFrame mf;


  /* 상태 필드 */
  private Level level = Level.NORMAL;
  private Mode mode = Mode.NORMAL;
  private Game game = Game.READY;


  /* 화면 구성요소 필드 */
  private int ROWS = 16;
  private int COLS = 16;
  private int CELL_SIZE = 35;
  private int MINE = 40;
  private int checkMine = 40;
  private int flagCount = 0;
  private int life = 1;
  private int score = 0;
  private int remainFlashItem = 1;
  private int remainLifeItem = 1;
  private int lifeItem = 1;
  private int flashItem = 1;


  /* 화면 소스 클래스 필드 */
  private JLabel[] timeLabel = null;
  private Timer timer = new Timer(1000, this);
  private int time;
  private int[] timeTable = new int[6];
  private Tile[][] tile = null;
  private JLabel mineCount = null;
  private JLabel lifeCount = null;
  private JButton newGameBtn = null;
  private JButton easyLvBtn = null;
  private JButton normalLvBtn = null;
  private JButton hardLvBtn = null;
  private JButton timeEasyLvBtn = null;
  private JButton timeNormalLvBtn = null;
  private JButton timeHardLvBtn = null;
  private JLabel timeAttackLabel = null;
  private JButton myScoreBtn = null;
  private JButton rankingBtn = null;
  private JButton howToPlayBtn = null;
  private JButton endGameBtn = null;
  private JButton backBtn = null;
  private JLabel lifeText = null;
  private JLabel flashText = null;


  /* 화면 필드 */
  private JPanel progressPanel = null;
  private JPanel gamePanel = null;
  private JPanel centerInnerPanel = null;
  private JPanel centerPanel = null;
  private JPanel subPanel = null;


  /* 이미지 아이콘 필드 */
  private ImageIcon[] numImgList = null;
  private ImageIcon[] mineImgList = null;
  private ImageIcon[] tileNumImgList = null;
  private ImageIcon[] lifeImgList = null;
  private ImageIcon[] flashImgList = null;
  private ImageIcon defaultImage = null;
  private ImageIcon closeImage = null;
  private ImageIcon pressedImage = null;
  private ImageIcon wildcardImage = null;
  private ImageIcon flagImage = null;
  private ImageIcon timedivImage = null;


  /* 리스너 내부 클래스 */
  private TileActionListener[][] tileActionListener = null;
  private TileMouseListener[][] tileMouseListener = null;


  /* 폰트 설정 */
  Font font1 = new Font("Britannic 굵게", Font.BOLD, 17);
  Font font2 = new Font("Britannic 굵게", Font.BOLD, 15);


  /* 색깔 설정 */
  Color colorEasy = new Color(118, 152, 152);
  Color colorNormal = new Color(115, 152, 120);
  Color colorHard = new Color(160, 97, 97);
  Color colorMenu = new Color(122, 132, 78);
  Color colorSubPanel = new Color(48, 48, 60);
  Color colorInfoLabel = new Color(217, 217, 217);


  /* 게임 정보 출력용 한글 저장 맵 */
  HashMap<Level, String> levelMap = new HashMap<Level, String>();
  HashMap<Mode, String> modeMap = new HashMap<Mode, String>();


  /* 	난이도 입력받아 게임 패널 생성하는 생성자 */
  public GamePanel(Mode mode, Level lv, MainFrame mf) {
    gameSetting(lv);
    this.mf = mf;
    this.mode = mode;
    this.setLayout(new BorderLayout());
    this.setPreferredSize(new Dimension(1200, 730));
    setCenterPanel();
    setSubPanel();
    if (mode == Mode.TIMEATTACK) {
      setTimer();
      setFirstTimePanel();
    }
    //타이머 1초 늦게 시작되는 문제 해결
    timer.setInitialDelay(0);
  }

  /* 게임 변수 설정 */
  public void gameSetting(Level lv) {
    if (lv == Level.EASY) {
      ROWS = 9;
      COLS = 9;
      MINE = 9;
      checkMine = 9;
      lifeItem = 1;
      flashItem = 1;
      remainLifeItem = 1;
      remainFlashItem = 1;
    } else if (lv == Level.NORMAL) {
      ROWS = 16;
      COLS = 16;
      MINE = 40;
      checkMine = 40;
      lifeItem = 2;
      flashItem = 5;
      remainLifeItem = 2;
      remainFlashItem = 5;
    } else {
      ROWS = 16;
      COLS = 30;
      MINE = 1;
      checkMine = 1;
      lifeItem = 3;
      flashItem = 8;
      remainLifeItem = 3;
      remainFlashItem = 8;
    }
    level = lv;
  }

  public void setCenterPanel() {
    int hmargin = 25;
    int wmargin = 300;
    if (level == Level.EASY) {
      hmargin = 135;
      wmargin = 600;
    }
    centerInnerPanel = new JPanel(new BorderLayout());
    centerInnerPanel.setBorder(new LineBorder(Color.WHITE, 3));
    centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, wmargin, hmargin));
    centerPanel.setBackground(Color.BLACK);
    setImageLoading();
    setProgressPane();
    setGamePane();
    setName();
    centerPanel.add(centerInnerPanel);
    setBackBtn();
    this.add(centerPanel, BorderLayout.CENTER);
  }

  /* 게임 정보 출력용 한글 맵 데이터 입력 */
  public void setName() {
    levelMap.put(Level.EASY, "초급(9x9)");
    levelMap.put(Level.NORMAL, "중급(16x16)");
    levelMap.put(Level.HARD, "상급(16x30)");
    modeMap.put(Mode.NORMAL, "노말모드");
    modeMap.put(Mode.TIMEATTACK, "타임어택모드");
  }

  /* 이미지 불러오기 */
  public void setImageLoading() {
    numImgList = new ImageIcon[11];
    for (int i = 0; i < 10; i++)
      numImgList[i] = new ImageIcon("img/" + i + "n.png");

    mineImgList = new ImageIcon[4];
    for (int i = 0; i < 4; i++)
      mineImgList[i] = new ImageIcon("img/mine" + (i + 1) + ".png");

    tileNumImgList = new ImageIcon[7];
    for (int i = 0; i < 7; i++)
      tileNumImgList[i] = new ImageIcon("img/" + (i + 1) + "s.png");

    lifeImgList = new ImageIcon[2];
    for (int i = 0; i < 2; i++)
      lifeImgList[i] = new ImageIcon("img/life" + i + ".png");
    flashImgList = new ImageIcon[2];
    for (int i = 0; i < 2; i++)
      flashImgList[i] = new ImageIcon("img/flash" + i + ".png");

    defaultImage = new ImageIcon("img/default.png");
    closeImage = new ImageIcon("img/close.png");
    pressedImage = new ImageIcon("img/pressed.png");
    wildcardImage = new ImageIcon("img/wildcard.png");
    flagImage = new ImageIcon("img/flag.png");
    timedivImage = new ImageIcon("img/timediv.png");
  }

  /* 진행 정보 패널 설정 */
  public void setProgressPane() {
    progressPanel = new JPanel();
    progressPanel.setLayout(new GridLayout(1, 3));
    JPanel lPanel = new JPanel();
    JPanel cPanel = new JPanel();
    JPanel rPanel = new JPanel();
    JPanel[] pPanel = {lPanel, cPanel, rPanel};
    JPanel minePanel = new JPanel();
    JPanel timePanel = new JPanel();
    JPanel lifePanel = new JPanel();

    //남은 지뢰 개수 표시
    mineCount = new JLabel(MINE + "", SwingConstants.CENTER);
    minePanel = makeProgressPanel(minePanel, "Mine", mineCount);
    lPanel.add(minePanel);

    //타이머 이미지 설정
    timePanel.setLayout(new GridLayout(1, 8));
    timeLabel = new JLabel[6];
    if (level != Level.EASY) {
      for (int i = 0; i < 6; i++) {
        timeLabel[i] = new JLabel(numImgList[0]);
        timePanel.add(timeLabel[i]);
        if (i == 1 || i == 3) {
          timePanel.add(new JLabel(timedivImage));
        }
      }
    } else {
      for (int i = 0; i < 3; i++) {
        if (i == 1) {
          timePanel.add(new JLabel(timedivImage));
        }
        timeLabel[i] = new JLabel(numImgList[0]);
        timePanel.add(timeLabel[i]);
      }
    }
    timePanel.setBackground(Color.BLACK);
    timePanel.setBorder(new EmptyBorder(15, 0, 0, 0));
    cPanel.add(timePanel);

    //남은 목숨 표시
    lifeCount = new JLabel(life + "", SwingConstants.CENTER);
    lifePanel = makeProgressPanel(lifePanel, "Life", lifeCount);
    rPanel.add(lifePanel);

    for (JPanel p : pPanel) {
      p.setOpaque(true);
      p.setBackground(Color.BLACK);
      progressPanel.add(p);
    }

    this.add(progressPanel, BorderLayout.NORTH);
    centerInnerPanel.add(progressPanel, BorderLayout.NORTH);
  }

  /* 타임어택 모드 첫 타이머 세팅 */
  public void setFirstTimePanel() {
    if (level == Level.EASY) { //초급
      timeLabel[0].setIcon(numImgList[1]);
    } else if (level == Level.NORMAL) { //중급
      timeLabel[3].setIcon(numImgList[5]);
    } else { //상급
      timeLabel[2].setIcon(numImgList[1]);
    }
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

    panel.setBackground(colorInfoLabel);
    panel.setPreferredSize(new Dimension((int) (CELL_SIZE * 1.5), (int) (CELL_SIZE * 1.5)));
    return panel;
  }

  /* 게임 패널 설정 메서드 */
  public void setGamePane() {
    gamePanel = new JPanel();
    gamePanel.setLayout(new GridLayout(ROWS, COLS));
    gamePanel.setPreferredSize(new Dimension(CELL_SIZE * COLS, CELL_SIZE * ROWS));
    gamePanel.setBackground(Color.BLACK);
    gamePanel.setOpaque(true);
    setButtonTile();
    centerInnerPanel.add(gamePanel, BorderLayout.CENTER);
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
    //life, flash 아이템 배치
    configureItem(map, -2, lifeItem);
    configureItem(map, -3, flashItem);

    //지뢰 주변 8방향의 셀에 해당 셀 근처의 지뢰 개수를 부여
    for (int r = 0; r < ROWS; r++) {
      for (int c = 0; c < COLS; c++) {
        if (map[r][c] == -1) {
          for (int i = 0; i < 9; i++) {
            int nx = r + dx[i];
            int ny = c + dy[i];
            // 경계를 벗어나지 않는지 확인
            if (nx >= 0 && nx < ROWS && ny >= 0 && ny < COLS) {
              // 주변 칸이 빈칸(0) 또는 숫자 칸(양수)인 경우에만 업데이트
              if (map[nx][ny] >= 0) {
                map[nx][ny]++;
              }
            }
          }
        }
      }
    }
    return map;
  }

  /* 사이드 패널 설정 */
  public void setSubPanel() {
    GridBagConstraints gbc = new GridBagConstraints();
    JPanel firstPanel = new JPanel(new GridBagLayout());
    JPanel secondPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 12));
    JPanel thirdPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 13));
    JPanel fourthPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 13));
    JPanel fifthPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 7));
    JPanel[] panels = {firstPanel, secondPanel, thirdPanel, fourthPanel, fifthPanel};
    for (JPanel p : panels) {
      p.setPreferredSize(new Dimension(140, 30));
      p.setBackground(colorSubPanel);
    }
    //사이드바 구성 요소 제작
    initBtn();
    initItemPanel(fifthPanel);

    subPanel = new JPanel(new GridBagLayout());
    subPanel.setPreferredSize(new Dimension(140, 630));
    subPanel.setBackground(Color.BLACK);

    gbc.gridx = 0;
    gbc.weightx = 1.0;
    gbc.anchor = GridBagConstraints.CENTER;

    //사이드바 내부 패널
    firstPanel.add(newGameBtn, gbc);
    gbc.weighty = 0.05;
    gbc.fill = GridBagConstraints.BOTH;
    subPanel.add(firstPanel, gbc);
    gbc.gridy = 1;
    gbc.weighty = 0.20;
    gbc.insets = new Insets(5, 0, 0, 0);
    subPanel.add(secondPanel, gbc);
    gbc.gridy = 2;
    gbc.weighty = 0.27;
    subPanel.add(thirdPanel, gbc);
    gbc.gridy = 3;
    gbc.weighty = 0.28;
    subPanel.add(fourthPanel, gbc);
    gbc.gridy = 4;
    gbc.weighty = 0.1;
    subPanel.add(fifthPanel, gbc);

    secondPanel.add(easyLvBtn);
    secondPanel.add(normalLvBtn);
    secondPanel.add(hardLvBtn);

    thirdPanel.add(timeAttackLabel);
    thirdPanel.add(timeEasyLvBtn);
    thirdPanel.add(timeNormalLvBtn);
    thirdPanel.add(timeHardLvBtn);

    fourthPanel.add(myScoreBtn);
    fourthPanel.add(rankingBtn);
    fourthPanel.add(howToPlayBtn);
    fourthPanel.add(endGameBtn);

    this.add(subPanel, BorderLayout.WEST);
  }

  public void initBtn() {
    newGameBtn = new JButton("New Game");
    easyLvBtn = new JButton("Easy");
    normalLvBtn = new JButton("Normal");
    hardLvBtn = new JButton("Hard");
    timeEasyLvBtn = new JButton("Easy");
    timeNormalLvBtn = new JButton("Normal");
    timeHardLvBtn = new JButton("Hard");
    timeAttackLabel = new JLabel("Time-Attack");
    myScoreBtn = new JButton("My score");
    rankingBtn = new JButton("Ranking");
    howToPlayBtn = new JButton("How to play");
    endGameBtn = new JButton("End Game");
    JButton[] gameLevelList = {easyLvBtn, normalLvBtn, hardLvBtn, timeEasyLvBtn, timeNormalLvBtn, timeHardLvBtn};
    JButton[] menuBtn = {myScoreBtn, rankingBtn, howToPlayBtn, endGameBtn};
    for (JButton jb : gameLevelList) {
      jb.setFont(font1);
      jb.setPreferredSize(new Dimension(100, 35));
    }
    newGameBtn.setFont(font1);
    newGameBtn.setPreferredSize(new Dimension(100, 35));
    timeAttackLabel.setFont(font2);
    timeAttackLabel.setHorizontalAlignment(SwingConstants.CENTER);
    timeAttackLabel.setPreferredSize(new Dimension(105, 30));
    timeAttackLabel.setOpaque(true);
    newGameBtn.setBackground(colorInfoLabel);
    timeAttackLabel.setBackground(colorInfoLabel);
    newGameBtn.setBorder(new LineBorder(colorInfoLabel, 4));
    easyLvBtn.setBackground(colorEasy);
    timeEasyLvBtn.setBackground(colorEasy);
    normalLvBtn.setBackground(colorNormal);
    timeNormalLvBtn.setBackground(colorNormal);
    hardLvBtn.setBackground(colorHard);
    timeHardLvBtn.setBackground(colorHard);

    for (JButton jb : menuBtn) {
      jb.setFont(font2);
      jb.setBackground(colorMenu);
      jb.setPreferredSize(new Dimension(120, 35));
      jb.addActionListener(new menuActionListener());
    }

    newGameBtn.addActionListener(new newActionListener());
    for (JButton i : gameLevelList) {
      i.addActionListener(new levelActionListener());
    }
  }

  /* 아이템 패널 구현 */
  public void initItemPanel(JPanel fifthPanel) {
    JPanel lifeItemPanel = new JPanel(new GridLayout(1, 3));
    JPanel flashItemPanel = new JPanel(new GridLayout(1, 3));
    JPanel[] itemPanel = {lifeItemPanel, flashItemPanel};
    lifeText = new JLabel("0" + remainLifeItem, SwingConstants.CENTER);
    flashText = new JLabel("0" + remainFlashItem, SwingConstants.CENTER);
    JLabel XText = new JLabel("X", SwingConstants.CENTER);
    JLabel XText1 = new JLabel("X", SwingConstants.CENTER);
    JLabel[] itemText = {lifeText, flashText, XText, XText1};

    for (JPanel p : itemPanel) {
      p.setPreferredSize(new Dimension(120, 35));
      p.setBackground(colorInfoLabel);
      fifthPanel.add(p);
    }
    for (JLabel jl : itemText) {
      jl.setFont(font1);
    }

    lifeItemPanel.add(new JLabel(lifeImgList[1]));
    flashItemPanel.add(new JLabel(flashImgList[1]));
    lifeItemPanel.add(XText);
    flashItemPanel.add(XText1);
    lifeItemPanel.add(lifeText);
    flashItemPanel.add(flashText);
  }

  //뒤로가기 버튼
  public void setBackBtn() {
    backBtn = new JButton("Back");
    backBtn.setFont(font1);
    backBtn.setPreferredSize(new Dimension(100, 35));
    backBtn.setBackground(colorInfoLabel);
    backBtn.setBorder(new LineBorder(colorInfoLabel, 4));
    backBtn.addActionListener(new backActionListener());
    centerPanel.add(backBtn);
  }

  //뒤로가기 액션 리스너
  class backActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      mf.showLoginCard();
    }
  }

  //새 게임 액션 리스너
  class newActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      mf.newGame();
    }
  }

  //난이도 변경 액션 리스너
  class levelActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == easyLvBtn) {
        mf.changeGame(Level.EASY, Mode.NORMAL);
      } else if (e.getSource() == normalLvBtn) {
        mf.changeGame(Level.NORMAL, Mode.NORMAL);
      } else if (e.getSource() == hardLvBtn) {
        mf.changeGame(Level.HARD, Mode.NORMAL);
      } else if (e.getSource() == timeEasyLvBtn) {
        mf.changeGame(Level.EASY, Mode.TIMEATTACK);
      } else if (e.getSource() == timeNormalLvBtn) {
        mf.changeGame(Level.NORMAL, Mode.TIMEATTACK);
      } else if (e.getSource() == timeHardLvBtn) {
        mf.changeGame(Level.HARD, Mode.TIMEATTACK);
      }
    }
  }

  //메뉴 선택 액션 리스너
  class menuActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == myScoreBtn) {
        mf.showMyScorePanel();
      } else if (e.getSource() == rankingBtn) {
        mf.showRankingCard();
      } else if (e.getSource() == howToPlayBtn) {
        mf.showHowToPanel();
      } else if (e.getSource() == endGameBtn) {
        int result;
        result = JOptionPane.showConfirmDialog(mf, "게임을 종료하시겠습니까?", "Game End",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) System.exit(0);
      }
    }
  }

  /* 맵에 아이템을 배치 */
  public void configureItem(int[][] map, int itemValue, int itemCount) {
    int count = 0;
    while (true) {
      int x = (int) (Math.random() * ROWS);
      int y = (int) (Math.random() * COLS);
      if (map[x][y] != 0) continue;
      else {
        map[x][y] = itemValue;
        count++;
      }
      if (count == itemCount) break;
    }
  }

  public boolean isMineTile(Tile tile) {
    return tile.getFace() == -1;
  }

  public boolean isNumTile(Tile tile) {
    return tile.getFace() >= 1 && tile.getFace() <= 9;
  }

  public boolean isLifeItemTile(Tile tile) {
    return tile.getFace() == -2;
  }

  public boolean isFlashItemTile(Tile tile) {
    return tile.getFace() == -3;
  }

  //0인 칸을 열었을 경우, 주변의 0인 칸을 재귀적으로 열어주는 메서드
  public void spaceTileOpen(int r, int c) {
    if (!validRange(r, c)) return;
    if (tile[r][c].getState() == State.OPEN) return;
    if (isMineTile(tile[r][c])) return;
    if (tile[r][c].getState() != State.FLAG) {
      tile[r][c].setState(State.OPEN);
    } else return;
    if (isLifeItemTile(tile[r][c])) {
      lifeTileAction(tile[r][c]);
      return;
    } else if (isFlashItemTile(tile[r][c])) {
      flashTileAction(tile[r][c]);
      return;
    }
    if (isNumTile(tile[r][c])) {
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
   *  숫자 타일일 때,
   *  해당 타일에 맞는 숫자 이미지로 대체하고 클릭을 비활성화 하는 메서드
   */
  public void replaceTile() {
    for (int r = 0; r < ROWS; r++) {
      for (int c = 0; c < COLS; c++) {
        if (tile[r][c].getState() == State.OPEN) {
          if (isNumTile(tile[r][c])) {
            tile[r][c].setDisabledIcon(tileNumImgList[tile[r][c].getFace() - 1]);
          } else if (isLifeItemTile(tile[r][c])) {
            tile[r][c].setDisabledIcon(lifeImgList[0]);
          } else if (isFlashItemTile(tile[r][c])) {
            tile[r][c].setDisabledIcon(flashImgList[0]);
          }
          tile[r][c].setEnabled(false);
        }
      }
    }
  }

  public void diplayMineCount() {
    mineCount.setText(checkMine + "");
  }

  public void displayLifeCount() {
    lifeCount.setText(life + "");
  }

  public void displayRemainItem() {
    lifeText.setText("0" + remainLifeItem);
    flashText.setText("0" + remainFlashItem);
  }

  //모든 지뢰에 깃발을 세웠는지 확인
  public boolean isAllCheckFlagMine() {
    if (game != Game.READY) {
      for (int r = 0; r < ROWS; r++) {
        for (int c = 0; c < COLS; c++) {
          if (!isMineTile(tile[r][c]) && tile[r][c].getState() == State.FLAG) {
            return false;
          }
          if (!isMineTile(tile[r][c]) && tile[r][c].getState() != State.OPEN) {
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
    int div = time;
    if (div == 0) div = 1;
    if (level == Level.EASY) defaultScore = 5000;
    else if (level == Level.NORMAL) defaultScore = 10000;
    else if (level == Level.HARD) defaultScore = 15000;
    if (mode == Mode.NORMAL)
      score = (defaultScore / div) + defaultScore;
    else
      score = (defaultScore + time * 10);
  }

  public void endGame() {
    timer.stop();
    game = Game.END;
  }

  /* 게임 승리
   *  게임 기록 레코드에 저장
   */
  public void removeTileListener() {
    for (int r = 0; r < ROWS; r++) {
      for (int c = 0; c < COLS; c++) {
        tile[r][c].removeActionListener(tileActionListener[r][c]);
        tile[r][c].removeMouseListener(tileMouseListener[r][c]);
      }
    }
  }

  //게임 종료 프로세스(리스너 제거, 미확인 지뢰 보여주기)
  public void gameEndProcess() {
    removeTileListener();
    for (int r = 0; r < ROWS; r++) {
      for (int c = 0; c < COLS; c++) {
        if (isMineTile(tile[r][c])) {
          tile[r][c].setDisabledIcon(mineImgList[0]);
          tile[r][c].setEnabled(false);
          if (tile[r][c].getState() == State.FLAG)
            tile[r][c].setDisabledIcon(mineImgList[2]);
        }
      }
    }
  }

  /*  게임 승리  */
  public void gameWin() {
    endGame();
    removeTileListener();
    calculateScore();
    if (mode == Mode.TIMEATTACK) {
      JOptionPane.showMessageDialog(this, "모든 지뢰를 찾았습니다!\n플레이어 : " + Player.name + "\n플레이 난이도 : " + levelMap.get(level) +
              "\n남은 시간 : " + calculateTimeToString() + "\n플레이 모드 : " + modeMap.get(mode) + "\n점수: " + score, "Game Clear!", JOptionPane.OK_OPTION);

    } else {
      JOptionPane.showMessageDialog(this, "모든 지뢰를 찾았습니다!\n플레이어 : " + Player.name + "\n플레이 난이도 : " + levelMap.get(level) +
              "\n플레이 시간 : " + calculateTimeToString() + "\n플레이 모드 : " + modeMap.get(mode) + "\n점수: " + score, "Game Clear!", JOptionPane.OK_OPTION);
    }
    /* 타임어택 모드에서 time = 남은 시간이었는데
     *  기록에 저장할 때는 플레이 타임으로 저장해야 하므로 변경 */
    if (mode == Mode.TIMEATTACK) {
      time = switch (level) {
        case EASY -> 60 - time;
        case NORMAL -> 300 - time;
        case HARD -> 900 - time;
      };
      time--;
    }
    try {
      FileWriter fileWriter = new FileWriter("ranking.txt", true);
      String st = "\n" + new Record(Player.name, level.toString(), mode.toString(), time, score);
      fileWriter.write(st);
      fileWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /*  게임 종료  */
  public void gameOver() {
    gameEndProcess();
    if (mode == Mode.TIMEATTACK) {
      time = switch (level) {
        case EASY -> 60 - time; // 1분 타이머
        case NORMAL -> 300 - time; // 5분 타이머
        case HARD -> 900 - time; // 15분 타이머
      };
      JOptionPane.showMessageDialog(this, "지뢰를 밟았습니다...\n플레이어 : " + Player.name + "\n남은 시간 : " + calculateTimeToString() +
              "\n플레이 난이도 : " + levelMap.get(level) + "\n플레이 모드 : " + modeMap.get(mode), "Game Over...", JOptionPane.OK_OPTION);
    } else {
      JOptionPane.showMessageDialog(this, "지뢰를 밟았습니다...\n플레이어 : " + Player.name + "\n플레이 타임 : " + calculateTimeToString() +
              "\n플레이 난이도 : " + levelMap.get(level) + "\n플레이 모드 : " + modeMap.get(mode), "Game Over...", JOptionPane.OK_OPTION);
    }
    endGame();
  }

  /* 타임어택 모드 제한시간 초과 시, 게임 종료 */
  public void timeOver() {
    gameEndProcess();
    JOptionPane.showMessageDialog(this, "제한시간 안에 모든 지뢰를 찾지 못했습니다...\n플레이어 : " + Player.name +
            "\n플레이 난이도 : " + levelMap.get(level) + "\n플레이 모드 : " + modeMap.get(mode), "Time Over...", JOptionPane.OK_OPTION);
    endGame();
  }

  /* 난이도별 제한시간 설정 메서드*/
  public void setTimer() {
    time = switch (level) {
      case EASY -> 60; // 1분 타이머
      case NORMAL -> 300; // 5분 타이머
      case HARD -> 900; // 15분 타이머
    };
  }

  /* 표시할 시간을 자리별로 계산 */
  public void calculateTime() {
    int[] timeDivide = {1, 36000, 3600, 600, 60, 10};
    for (int i = 1; i < 5; i++) {
      timeTable[i] = time % timeDivide[i] / timeDivide[i + 1];
    }
    timeTable[0] = time / 36000; //100000의 자리 xx시간
    timeTable[5] = (time % 10); //1의 자리 x초
  }

  /* 결과 화면에 출력할 시간 문자열 제작 */
  public String calculateTimeToString() {
    StringBuilder result = new StringBuilder();
    String[] units = {"시간", "분", "초"};
    // 각 단위(시간, 분, 초)에 대해 반복
    for (int i = 0; i < 3; i++) {
      // 두 자리 숫자를 하나로 합산
      int value = timeTable[2 * i] * 10 + timeTable[2 * i + 1];
      if (value > 0) {
        result.append(value).append(units[i]).append(" ");
      }
    }
    // 결과 문자열 반환 (앞뒤 공백 제거)
    if (result.toString().isEmpty()) return "0초";
    return result.toString().trim();
  }

  /*
   * 타이머
   * 타임어택, 노말모드를 구분해서 남은(경과)시간 표시
   */
  public void actionPerformed(ActionEvent e) {
    /* 각 자리에 표시할 시간 계산 */
    calculateTime();
    if (mode == Mode.TIMEATTACK) {
      if (time > 0) {
        time--;
        if (level != Level.EASY) {
          for (int i = 5; i >= 0; i--) {
            timeLabel[i].setIcon(numImgList[timeTable[i]]);
          }
        } else {
          for (int i = 2; i >= 0; i--) {
            timeLabel[i].setIcon(numImgList[timeTable[i + 3]]);
          }
        }
      } else {
        if (level != Level.EASY) timeLabel[5].setIcon(numImgList[timeTable[0]]);
        timeLabel[2].setIcon(numImgList[timeTable[0]]);
        timeOver();
      }
    } else {
      time++;
      if (level != Level.EASY) {
        for (int i = 5; i >= 0; i--) {
          timeLabel[i].setIcon(numImgList[timeTable[i]]);
        }
      } else {
        for (int i = 2; i >= 0; i--) {
          timeLabel[i].setIcon(numImgList[timeTable[i + 3]]);
        }
      }
    }
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
      int nx = r + dx[i];
      int ny = c + dy[i];
      // 경계 검사
      if (nx >= 0 && nx < tile.length && ny >= 0 && ny < tile[0].length) {
        if (tile[nx][ny].getState() == State.FLAG) {
          flagCnt++;
        }
      }
    }

    /*
     * 타일의 숫자와 주변 플래그 개수가 같다면
     * 즉, 플레이어가 합리적인 예측을 했다면 주변 타일을 확인하면서 하나씩 열어본다.
     */
    if (tile[r][c].getFace() == flagCnt) {
      for (int i = 0; i < 8; i++) {
        int nx = r + dx[i];
        int ny = c + dy[i];
        // 경계 검사
        if (nx >= 0 && nx < tile.length && ny >= 0 && ny < tile[0].length) {
          if (tile[nx][ny].getState() == State.CLOSE || tile[nx][ny].getState() == State.WILDCARD) {
            Tile t = tile[nx][ny];
            tileAction(t);
          }
        }
      }
    }

    /*
     * 타일의 숫자와 플래그 개수가 같지 않다면,
     * 즉, 플레이어의 예측이 비합리적이라면 주변 타일의 닫힌 상태를 유지한다.
     */
    else {
      for (int i = 0; i < 8; i++) {
        int nx = r + dx[i];
        int ny = c + dy[i];
        // 경계 검사
        // 클릭 타일이 플래쉬 아이템일 경우 1칸 이내의 지뢰를 탐지 못하는 버그가 생겨서
        // 플래쉬 아이템을 클릭했을 경우는 제외.
        if (nx >= 0 && nx < tile.length && ny >= 0 && ny < tile[0].length) {
          if (tile[nx][ny].getState() != State.FLAG && tile[nx][ny].getState() != State.OPEN && !isFlashItemTile(tile[r][c])) {
            tile[nx][ny].setIcon(closeImage);
          }
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
      if (game == Game.READY) {
        timer.start();
        game = Game.START;
      }
      Tile t = (Tile) e.getSource();
      if (t.getState() == State.CLOSE | t.getState() == State.WILDCARD) {
        tileAction(t);
      }
    }
  }

  public void tileAction(Tile t) {
    if (isMineTile(t)) {
      mineTileAction(t);
    } else if (isNumTile(t)) {
      numTileAction(t);
    } else if (isLifeItemTile(t)) {
      lifeTileAction(t);
    } else if (isFlashItemTile(t)) {
      flashTileAction(t);
    } else {
      spaceTileOpen(t.getRow(), t.getCol());
      replaceTile();
    }
  }

  /* 지뢰 타일이라면
   *  목숨이 2이상이면, 한 번 봐줌.
   *  아니라면 게임 종료
   */
  public void mineTileAction(Tile t) {
    life--;
    displayLifeCount();
    if (life >= 1) {
      oneMoreTime();
      t.setIcon(closeImage);
      return;
    }
    t.setDisabledIcon(mineImgList[1]);
    timer.stop();
    game = Game.END;
    gameOver();
  }

  public void numTileAction(Tile t) {
    t.setState(State.OPEN);
    t.setDisabledIcon(tileNumImgList[t.getFace() - 1]);
    t.setEnabled(false);
  }

  public void lifeTileAction(Tile t) {
    t.setState(State.OPEN);
    t.setDisabledIcon(lifeImgList[0]);
    t.setEnabled(false);
    life++;
    remainLifeItem--;
    displayRemainItem();
    displayLifeCount();
  }

  /* flash타일이라면,
   *  해당 타일의 상하좌우 2칸에 지뢰가 있는 칸을 2초동안 보여준다.*/
  public void flashTileAction(Tile t) {
    remainFlashItem--;
    displayRemainItem();
    t.setState(State.OPEN);
    t.setDisabledIcon(flashImgList[0]);
    t.setEnabled(false);
    //범위의 타일에 대해
    //if(닫혀있는 타일이라면) 3초동안 열었다가 다시 닫는다.
    int[] dx = {-2, -1, 1, 2, 0, 0, 0, 0};
    int[] dy = {0, 0, 0, 0, -2, -1, 1, 2};
    ArrayList<Tile> tilesToReset = new ArrayList<>();
    for (int i = 0; i < dx.length; i++) {
      int nx = t.getRow() + dx[i];
      int ny = t.getCol() + dy[i];
      if (nx >= 0 && nx < tile.length && ny >= 0 && ny < tile[0].length) {
        if (tile[nx][ny].getState() == State.CLOSE && isMineTile(tile[nx][ny])) {
          tile[nx][ny].setIcon(mineImgList[3]);
          tilesToReset.add(tile[nx][ny]);
        }
      }
    }
    new Thread(() -> {
      try {
        Thread.sleep(2000);
        for (Tile tile : tilesToReset) {
          tile.setIcon(closeImage);
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }).start();
  }

  //목숨이 2이상일 경우 목숨 감소
  public void oneMoreTime() {
    timer.stop();
    JOptionPane.showMessageDialog(this, "지뢰를 밟았다!!\n목숨이 1 감소합니다..\n남은 목숨 : " + life,
            "목숨 감소...", JOptionPane.OK_OPTION);
    timer.start();
  }

  //타일 마우스 클릭 이벤스 리스너
  class TileMouseListener extends MouseAdapter {
    private boolean bothLeft = false;
    private boolean bothRight = false;

    public void mousePressed(MouseEvent e) {
      Tile t = (Tile) e.getSource();
      if (SwingUtilities.isLeftMouseButton(e)) {
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
            if (isMineTile(t)) flagCount++;
            diplayMineCount();
          } else if (t.getState() == State.FLAG) {
            t.setIcon(wildcardImage);
            t.setState(State.WILDCARD);
            checkMine += 1;
            if (isMineTile(t)) flagCount--;
            diplayMineCount();
          } else {
            t.setIcon(closeImage);
            t.setState(State.CLOSE);
          }
        }
        // 모든 지뢰를 탐색했다면 게임 승리
        if (isAllCheckFlagMine()) {
          gameWin();
        }
        // both mouse check
        if (t.getState() == State.OPEN) {
          bothRight = true;
        }
      }
      if (t.getState() == State.OPEN) {
        if (bothLeft && bothRight) {
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
        gameWin();
      }
    }
  }
}
