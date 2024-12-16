package MineSweeper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
  /* 메인 프레임 및 상태 클래스 필드 */
  private MainFrame mf;
  private Level level = Level.NORMAL;
  private Mode mode = Mode.NORMAL;

  /* 화면 구성요소 클래스 필드 */
  private JPanel loginBox = null;
  private JLabel titleLabel = null;
  private JLabel nameLabel = null;
  private JLabel difficultylabel = null;
  private JLabel modeLabel = null;
  private JTextField nameTextField = null;
  private JButton howToButton = null;
  private JButton startButton = null;

  /* 게임 설정 메뉴 클래스 필드 선언 */
  private JMenuBar diffMenuBar = null;
  private JMenu difficultyMenu = null;
  private JMenuItem lowLvItem = null;
  private JMenuItem midLvItem = null;
  private JMenuItem highLvItem = null;
  private JMenuBar modeMenuBar = null;
  private JMenu modeMenu = null;
  private JMenuItem normalItem = null;
  private JMenuItem timeAttackItem = null;

  /* 타이틀 이미지 아이콘 선언 */
  private ImageIcon titleImage = null;

  /* 그리드백 레이아웃에 사용할 객체 선언  */
  GridBagConstraints gbc = new GridBagConstraints();

  /* 폰트 설정 */
  Font font1 = new Font("Britannic 굵게", Font.BOLD, 17);

  /* 로그인 패널 메인 로직 */
  LoginPanel(MainFrame mf) {
    this.mf = mf;
    init();
    initMenu();
    setLoginBox();
  }

  /* 기본 환경 설정 */
  public void init() {
    this.setBackground(Color.black);
    this.setPreferredSize(new Dimension(1200, 730));
    this.setLayout(new GridBagLayout());

    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;

    titleImage = new ImageIcon(getClass().getResource("/title.png"));
    titleLabel = new JLabel(titleImage, SwingConstants.CENTER);
    titleLabel.setBorder(new EmptyBorder(70, 0, 0, 0));
    gbc.gridx = 0;
    gbc.gridy = 0;
    this.add(titleLabel, gbc);

    Dimension lblSize = new Dimension(80, 50);
    Dimension btnSize = new Dimension(100, 25);
    int tfSize = 8;

    nameLabel = new JLabel("Name", SwingConstants.CENTER);
    nameLabel.setFont(font1);
    nameLabel.setPreferredSize(lblSize);
    difficultylabel = new JLabel("Difficulty", SwingConstants.CENTER);
    difficultylabel.setFont(font1);
    difficultylabel.setPreferredSize(lblSize);
    modeLabel = new JLabel("Mode", SwingConstants.CENTER);
    modeLabel.setFont(font1);
    modeLabel.setPreferredSize(lblSize);

    nameTextField = new JTextField(tfSize);

    howToButton = new JButton("How To");
    howToButton.setPreferredSize(btnSize);
    startButton = new JButton("START");
    startButton.setPreferredSize(btnSize);
  }

  /* 게임 설정 메뉴 내용 */
  public void initMenu() {
    diffMenuBar = new JMenuBar();
    modeMenuBar = new JMenuBar();
    difficultyMenu = new JMenu("중급(16x16)");
    difficultyMenu.setPreferredSize(new Dimension(80, 20));
    lowLvItem = new JMenuItem("  초급(9x9)");
    midLvItem = new JMenuItem("중급(16x16)");
    highLvItem = new JMenuItem("상급(16x30)");

    lowLvItem.addActionListener(new levelActionListener());
    midLvItem.addActionListener(new levelActionListener());
    highLvItem.addActionListener(new levelActionListener());

    difficultyMenu.add(lowLvItem);
    difficultyMenu.add(midLvItem);
    difficultyMenu.add(highLvItem);
    diffMenuBar.add(difficultyMenu);

    modeMenu = new JMenu("     노말 모드");
    modeMenu.setPreferredSize(new Dimension(100, 20));
    normalItem = new JMenuItem("     노말 모드");
    timeAttackItem = new JMenuItem(" 타임어택 모드");

    normalItem.addActionListener(new modeActionListener());
    timeAttackItem.addActionListener(new modeActionListener());

    modeMenu.add(normalItem);
    modeMenu.add(timeAttackItem);
    modeMenuBar.add(modeMenu);

    howToButton.addActionListener(new buttonActionListener());
    startButton.addActionListener(new buttonActionListener());
  }

  /* 로그인 박스 설정 */
  public void setLoginBox() {
    loginBox = new JPanel();
    loginBox.setLayout(new GridLayout(4, 1));
    loginBox.setPreferredSize(new Dimension(380, 200));
    loginBox.setOpaque(true);
    loginBox.setBackground(Color.WHITE);

    JPanel firstPanel = new JPanel();
    firstPanel.add(nameTextField, SwingConstants.CENTER);
    firstPanel.add(nameLabel, SwingConstants.CENTER);

    JPanel secondPanel = new JPanel();
    secondPanel.add(difficultylabel, SwingConstants.CENTER);
    secondPanel.add(diffMenuBar);

    JPanel thirdPanel = new JPanel();
    thirdPanel.add(modeLabel, SwingConstants.CENTER);
    thirdPanel.add(modeMenuBar);

    JPanel fourthPanel = new JPanel();
    fourthPanel.add(startButton, SwingConstants.CENTER);
    fourthPanel.add(howToButton, SwingConstants.CENTER);

    loginBox.add(firstPanel);
    loginBox.add(secondPanel);
    loginBox.add(thirdPanel);
    loginBox.add(fourthPanel);

    firstPanel.setOpaque(true);
    secondPanel.setOpaque(true);
    thirdPanel.setOpaque(true);
    fourthPanel.setOpaque(true);

    firstPanel.setBackground(Color.WHITE);
    secondPanel.setBackground(Color.WHITE);
    thirdPanel.setBackground(Color.WHITE);
    fourthPanel.setBackground(Color.WHITE);
    gbc.gridy = 1;
    gbc.insets = new Insets(50, 0, 0, 0);
    gbc.anchor = GridBagConstraints.NORTH;
    gbc.fill = GridBagConstraints.NONE;
    this.add(loginBox, gbc);
  }

  /* 난이도 변경 메뉴 액션 리스너 */
  class levelActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == lowLvItem) {
        level = Level.EASY;
        difficultyMenu.setText("  초급(9x9)");
      } else if (e.getSource() == midLvItem) {
        level = Level.NORMAL;
        difficultyMenu.setText("중급(16x16)");
      } else {
        level = Level.HARD;
        difficultyMenu.setText("상급(16x30)");
      }
    }
  }

  /* 모드 변경 메뉴 액션 리스너 */
  class modeActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == normalItem) {
        mode = Mode.NORMAL;
        modeMenu.setText("     노말 모드");
      } else {
        mode = Mode.TIMEATTACK;
        modeMenu.setText(" 타임어택 모드");
      }
    }
  }

  /* How To, Start 버튼 액션 리스너 */
  class buttonActionListener extends Component implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == howToButton) {
        mf.showHowToCard();
      } else if (e.getSource() == startButton && nameTextField.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "게임을 시작하려면 이름을 입력해야 합니다.", "오류!", JOptionPane.WARNING_MESSAGE);
      } else {
        Player p = new Player(nameTextField.getText());
        nameTextField.setText("");
        MainFrame.isLogin = true;
        mf.changeGame(level, mode);
      }
    }
  }
}
