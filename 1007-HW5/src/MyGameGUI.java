import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The GUI for the Menu and ordering system
 */

public class MyGameGUI extends JFrame implements GameGUI	{


	private static final long serialVersionUID = 1L;
	int size = 30;
	private MyGameRunner game;
	private int[][] grid;
	private int totalMoves;


	/**
	 * Constructs a Screen
	 */

	public MyGameGUI(Maze maze) throws FileNotFoundException{
		//will take in maze
		grid = maze.getWholeGrid();
		game = new MyGameRunner(maze);
		run();
		totalMoves = 0;
		
	}
	
	/**
	 * Runs the graphics
	 */
	public void run(){
		init();
		setSize(800,550);
		setTitle("MAZE");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	/**
	 * Sets up the two main panels-- one with the maze and buttons, one with the timer
	 */

	public void init(){

		JPanel mainPanel = (JPanel) getContentPane();
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, getOutputPanel(), getTimerPanel());
		splitPane.setDividerLocation(500);
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(splitPane, BorderLayout.CENTER);
		
	}

	/**
	 * Creates a panel that includes a stopwatch
	 * @return a panel with a stopwatch
	 */

	public JPanel getTimerPanel() {
		//needs to return a pane w/ the button
		JPanel output = new JPanel();
		return output;

	}

	/**
	 * Creates the panel that displays the maze and the player's position
	 * as well as the buttons for moving.
	 */
	public JPanel getButtonPanel(){
		JPanel buttonArea = new JPanel();
		buttonArea.setLayout(new GridLayout(1,2));
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(3,3));
		
		JLabel x6 = new JLabel("Press the buttons to move your player");
		buttonArea.add(x6);
		buttonArea.add(buttonPanel);
		
		
		JButton up = new JButton("Up");
		JButton down = new JButton("Down");
		JButton left = new JButton("Left");
		JButton right = new JButton("Right");
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

		//tryMove(int direction) For input, 0 is right, 1 is up,2 is left, 3 is down
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
		
		
		return buttonArea;

	}

	/**
	 * The panel that has the maze in it
	 * @return the maze graphics
	 */
	public JPanel getMazePanel(){
		
		JPanel mazePanel = new JPanel();
		mazePanel.setLayout(new GridLayout(size,size));
		mazePanel.setBackground(Color.WHITE);
		for(int i = 0; i<size; i++){
			for (int j = 0; j<size; j++){
				if (grid[i][j] == 0){
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
					JLabel guy = new JLabel(new ImageIcon("guy.gif"));
					mazePanel.add(guy);
				}
			}
		}
		return mazePanel;
	}
	
	/**
	 * Checks the status of the move and changes the position and total move count
	 * @param direction
	 */
	void act(int direction){
		int i = game.tryMove(direction);
		System.out.println(i);
		for(int a = 0; a<size; a++){
			for (int j = 0; j<size; j++){
				System.out.print(grid[a][j]);
			}
			System.out.println();
		}
			
		if (i == 0){
			totalMoves++;
		}
		else if (i == 1){
			totalMoves++;
			grid = game.getWholeGameGrid();
			//what do i do here?????
			run();
			validate();
			repaint();
		}
		else{
			totalMoves++;
		}
		
	}
	/**
	 * The pane on the left side that contains the buttons and maze
	 * @return the output panel
	 */
	public JSplitPane getOutputPanel() {		

		JSplitPane outputPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, getMazePanel(), getButtonPanel());
		outputPane.setDividerLocation(400);
		return outputPane;
	}

	@Override
	/**
	 * Returns total number of moves
	 */
	public int getTime() {
		// TODO Auto-generated method stub
		return totalMoves;
	}

}
