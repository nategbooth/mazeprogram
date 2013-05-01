import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The GUI for the Maze
 */

public class MyGameGUISmall extends JFrame implements GameGUI, Runnable, KeyListener	{


	private static final long serialVersionUID = 1L;
	private MyGameRunner game;
	private int[][] grid;
	private int totalMoves;
	private JPanel mazePanel;
	private JSplitPane outputPane;
	private boolean complete = false;



	/**
	 * Constructs a Screen
	 */

	public MyGameGUISmall() throws FileNotFoundException{
		//will take in maze


	}

	/**
	 * Runs the graphics
	 */
	public void run(){
		init();
		setSize(500,550);		

		setTitle("MAZE");

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	/**
	 * Sets up the two main panels-- one with the maze and buttons, one with the timer
	 */

	public void init(){

		JPanel mainPanel = (JPanel) getContentPane();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(getOutputPanel(), BorderLayout.CENTER);
		mainPanel.setFocusable(true);
		mainPanel.addKeyListener(this);


	}
	/**
	 * The panel with the maze
	 */


	public void getMazePanel(){
		int window = 5;
		mazePanel.setLayout(new GridLayout(window,window));
		mazePanel.setBackground(Color.WHITE);



		for(int i = game.getLocY()- (window-1)/2; i<= game.getLocY() + (window-1)/2; i++){
			for (int j = game.getLocX()-(window-1)/2; j<=game.getLocX() +(window-1)/2; j++){
				if (i<0 || i>=grid.length || j<0||j>= grid.length) {
					JLabel border = new JLabel(new ImageIcon("corn.gif"));
					mazePanel.add(border);
				}
				else if (grid[i][j] == 0 ){
					JLabel x = new JLabel("");
					mazePanel.add(x);
				}
				else if(grid[i][j] == 1){
					JLabel border = new JLabel(new ImageIcon("corn.gif"));
					mazePanel.add(border);
				}
				else if(grid[i][j] == 3){
					JLabel star = new JLabel(new ImageIcon("star.gif"));
					mazePanel.add(star);
				}
				else if (grid[i][j] == 2){
					JLabel guy = new JLabel(new ImageIcon("andrew.gif"));
					mazePanel.add(guy);
				}
			}
		}

	}


	/**
	 * Creates the panel that displays the maze and the player's position
	 * as well as the buttons for moving.
	 */
	public JSplitPane getOutputPanel(){

		JPanel buttonArea = new JPanel();
		buttonArea.setLayout(new GridLayout(1,2));
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(3,3));

		JLabel x6 = new JLabel("Press the buttons to move your player");
		buttonArea.add(x6);
		buttonArea.add(buttonPanel);

		
		JButton up = new JButton("Up");
		up.setFocusable(false);
		JButton down = new JButton("Down");
		down.setFocusable(false);
		JButton left = new JButton("Left");
		left.setFocusable(false);
		JButton right = new JButton("Right");
		right.setFocusable(false);
		 
		JLabel x1 = new JLabel("");
		JLabel x2 = new JLabel("");
		JLabel x3 = new JLabel("");
		JLabel x4 = new JLabel("");
		JLabel x5 = new JLabel("");

		buttonPanel.add(x1);
		buttonPanel.add(up);
		buttonPanel.add(x2);
		buttonPanel.add(left);
		buttonPanel.add(x3);
		buttonPanel.add(right);
		buttonPanel.add(x4);
		buttonPanel.add(down);
		buttonPanel.add(x5);

		
		right.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				act(0);					
			}
		});
		up.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				act(1);				
			}
		});
		left.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				act(2);				
			}
		});
		down.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				act(3);				
			}
		});

		getMazePanel();
		JSplitPane outputPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mazePanel, buttonArea);
		outputPane.setDividerLocation(400);
		return outputPane;

	}


	/**
	 * Checks the status of the move and changes the position and total move count
	 * @param direction
	 */
	void act(int direction){
		int i = game.tryMove(direction);
		

		if (i == 0){
			totalMoves++;
		}
		else if (i == 1){
			totalMoves++;
			grid = game.getWholeGameGrid();

			mazePanel.removeAll();
			getMazePanel();

			pack();
			validate();
			setSize(500,550);
			repaint();

		}
		else{
			totalMoves++;
			JOptionPane.showMessageDialog(getContentPane(), "You have completed the maze in " + totalMoves + 
					" moves", "COMPLETE", JOptionPane.INFORMATION_MESSAGE);
			complete=true;
		}

	}

	public void keyPressed(KeyEvent e) {
		
		if (e.isActionKey()) {
			
			
			if (e.getKeyCode() == 37) {
				
				act(2);
			}
			if (e.getKeyCode() == 40) {
				act(3);
			}
			if (e.getKeyCode() == 39) {
				act(0);
			}
			if (e.getKeyCode() == 38) {
				act(1);
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		
	}
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	/**
	 * Returns total number of moves
	 */
	public int getTime() {
		// TODO Auto-generated method stub
		return totalMoves;
	}

	public boolean getComplete() {
		// TODO Auto-generated method stub
		return complete;
	}

	public void run(MazeCreator maze1) {
		// TODO Auto-generated method stub
		game = new MyGameRunner(maze1);
		grid = game.getWholeGameGrid();

		mazePanel = new JPanel();
		run();
		totalMoves = 0;


	}

}
