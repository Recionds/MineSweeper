package source;/*
 * Author: �̹���
 * Created: 2008�� 1�� 26�� ����� ���� 1:09:47
 * Modified: 2008�� 1�� 26�� ����� ���� 1:09:47
 * E-Mail : vamalboro@nate.com
 * URL    : http://abum.tistory.com/
 */
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainFrame extends JFrame {
	private Level			level			= Level.MIDDLE;
	private MineGUI			mineGUI			= null;	
	/* menu */
	private JMenuBar		menuBar			= null;
	private JMenu			fileMenu		= null;
	private JMenuItem		newItem			= null;
	private JMenuItem		rowLvItem		= null;
	private JMenuItem		midlLvItem		= null;
	private JMenuItem		highLvItem		= null;
	private JMenuItem		bestTimeItem	= null;
	private JMenuItem		exitItem		= null;
	
	private JMenu			helpMenu		= null;
	private JMenuItem		helpItem		= null;
	private JMenuItem		inforItem		= null;

	private String			helpString		= "�������� ����ã��� ���ӹ���� �����մϴ�.";
	private String			inforString		= "������ : �̹���\n���۳�¥ : 2008. 01. 25";
	private String			endString		= "���α׷��� ���� �Ͻðڽ��ϱ�?";
	
	public MainFrame( String title ) {
		super( title );
		initMenu();
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		this.mineGUI = new MineGUI(this.level);
		this.setContentPane(this.mineGUI);
		this.pack();
		this.setCenterPosition();
		// frame icon 
		MediaTracker tracker = new MediaTracker( this );
		Image img = Toolkit.getDefaultToolkit().getImage( "IMG/reset.gif" );
		tracker.addImage( img, 0 );
		setIconImage( img );
		this.setResizable(false);
		this.setVisible(true);
	}
	
	/* Inner class [ActionListener]*/
	class newActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			changeGame();
		}
	}
	class levelActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if ( e.getSource() == rowLvItem ) {
				changeLevel(Level.ROW);
			} else if (e.getSource() == midlLvItem ) {
				changeLevel(Level.MIDDLE);
			} else {
				changeLevel(Level.HIGH);
			}
		}
	}
	class bestTimeActionListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			bestTimeShow();
		}
		public void bestTimeShow() {
			String score = null;
			File file[] 	= new File[3];
			file[0]	= new File("rowscore.dat");
			file[1]	= new File("middlescore.dat");
			file[2]	= new File("highscore.dat");
			String title[]	= {"�ʱ�", "�߱�", "���"};
			score = bestTimeRead( file[0], title[0] ) + bestTimeRead( file[1], title[1] ) + bestTimeRead( file[2], title[2] );
			JOptionPane.showMessageDialog( MainFrame.this, score, "����ã�� �ְ���", JOptionPane.INFORMATION_MESSAGE );
		}
		public String bestTimeRead(File file, String level) {
			String text		= null;
			String score	= null;
			if ( file.exists() ) {
				try {
					BufferedReader reader = new BufferedReader( new FileReader(file));
					text = reader.readLine();
					reader.close();
					if ( text == null ) score = level + " " + "999��" + " �͸�";
					else {
						String[] temp = text.split("/");
						score = level + " " + temp[0] + "�� " + temp[1];
					}
				} catch( IOException ex ) { System.err.println("������ �д� �� ���� �߻�"); }
			} else {
				try {
					FileWriter writer = new FileWriter( file );
					writer.close();
					score = level + " " + "999��" + " �͸�";
				} catch(IOException ex) { System.err.println("������ ���� �� ���� �߻�"); }
			}
			return score + "\n";
		}
	}
	class exitActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int result;
			result = JOptionPane.showConfirmDialog( MainFrame.this, endString, "Game End", 
														JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE );
			if ( result == JOptionPane.YES_OPTION ) System.exit(0);
		}
	}
	class dialogActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if ( e.getSource() == helpItem ) {
				JOptionPane.showMessageDialog( MainFrame.this, helpString, "Help", JOptionPane.INFORMATION_MESSAGE );
			} else if ( e.getSource() == inforItem ) {
				JOptionPane.showMessageDialog( MainFrame.this, inforString, "Information", JOptionPane.INFORMATION_MESSAGE );
			}
		}
	} // Inner class end


	/* Member Method */
	public void initMenu() {
		menuBar			= new JMenuBar();
		fileMenu		= new JMenu( "����" );
		newItem			= new JMenuItem( "�� ����" );
		rowLvItem		= new JMenuItem( "�ʱ�" );
		midlLvItem		= new JMenuItem( "�߱�" );
		highLvItem		= new JMenuItem( "���" );
		bestTimeItem	= new JMenuItem( "�ְ� ���" );
		exitItem		= new JMenuItem( "������" );
		
		newItem.addActionListener( new newActionListener() );
		rowLvItem.addActionListener( new levelActionListener() );
		midlLvItem.addActionListener( new levelActionListener() );
		highLvItem.addActionListener( new levelActionListener() );
		bestTimeItem.addActionListener( new bestTimeActionListener() );
		exitItem.addActionListener( new exitActionListener() );
		
		fileMenu.add( newItem );
		fileMenu.addSeparator();
		fileMenu.add( rowLvItem );
		fileMenu.add( midlLvItem );
		fileMenu.add( highLvItem );
		fileMenu.addSeparator();
		fileMenu.add( bestTimeItem );
		fileMenu.add( exitItem );
		
		menuBar.add( fileMenu );
		
		helpMenu		= new JMenu( "����" );
		helpItem		= new JMenuItem( "HELP" );
		inforItem		= new JMenuItem( "���� ����" );
		
		helpItem.addActionListener( new dialogActionListener() );
		inforItem.addActionListener( new dialogActionListener() );
		
		helpMenu.add( helpItem );
		helpMenu.addSeparator();
		helpMenu.add( inforItem );
		
		menuBar.add( helpMenu );
		
		this.setJMenuBar( menuBar );
	}
	public void setCenterPosition() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frm = this.getSize();
		int xpos = (int)( screen.getWidth() / 2 - frm.getWidth() / 2 );
		int ypos = (int)( screen.getHeight() / 2 - frm.getHeight() / 2 );
		this.setLocation( xpos, ypos );
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
	public static void main ( String args[] )
	{
		new MainFrame( "���� ã��" );
	}
}