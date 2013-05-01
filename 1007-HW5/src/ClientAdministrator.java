import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * This class handles all the operations of the client
 * @author abc
 *
 */
public class ClientAdministrator implements Client, ActionListener{

	private Socket socket;
	private ObjectOutputStream out;
	private Scanner in = new Scanner(System.in);
	private String userName = "User1";
	private String passWord;
	private int noOfGames;
	private String hostname;
	private int port;
	private int length;
	private ClientData cd;
	boolean check = false;
	private ClientData to;
	private int games = 0;
	private ArrayList<String> listOfUsers = new ArrayList<String>();
	private Authentication a;
	private Authentication n;
	private boolean newUser = true;
	private boolean wrongPW = false;
	private boolean nG = false;

	JLabel myFirst = new JLabel("");
	JLabel mySecond = new JLabel("");
	JLabel myThird = new JLabel("");
	JLabel oppFirst = new JLabel("");
	JLabel oppSecond = new JLabel("");
	JLabel oppThird = new JLabel("");
	JFrame frame1;
	JFrame frame;
	JTextField un;
	JPasswordField pw;
	JFrame frame3;
	JFrame frame4;

	JLabel unLabel;
	JLabel pwLabel;
	JLabel firstLabel;
	JLabel extraLabel;
	JButton submitButton;
	ObjectInputStream ois;
	private MazeCreator maze1;
	private int moves;
	private int moves1;
	
	/**
	 * This method is the constructor which records the hostname, port, and opens the welcome screen for users to login
	 * @param h
	 * @param p
	 */
	public ClientAdministrator(String h, int p) {
		hostname = h;
		port = p;
		createWelcomeScreen();	
	}
/**
 * This method creates a welcome screen which takes user's credentials
 */
	private void createWelcomeScreen() {
		// TODO Auto-generated method stub
		frame = new JFrame ("Welcome!");

		frame.setMinimumSize(new Dimension(640,240));
		un = new JTextField (10);
		pw = new JPasswordField (10);
		pw.setActionCommand("ok");

		firstLabel = new JLabel("To create a new user, enter a unique username and password. Logging in once will register you as a user.");
		JLabel secondLabel = new JLabel("  You must be registered for a " +
				"friend to play a game with you. Good luck!");
		JLabel blank = new JLabel("");
		unLabel = new JLabel("Enter New/Existing Username:");
		pwLabel = new JLabel("Enter New/Existing Password (1-10 Characters)");
		extraLabel = new JLabel();

		submitButton = new JButton("Submit!");
		
		frame.setLayout(new GridLayout(5,2));
		//frame.setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(firstLabel);
		//frame.add(blank);
		frame.add(secondLabel);
		frame.add(unLabel);
		frame.add(un);
		frame.add(pwLabel);
		frame.add(pw);
		frame.add(extraLabel);
		frame.add(submitButton);

		//listeners go here
		submitButton.setActionCommand("ok");
		submitButton.addActionListener(this);

		frame.pack();
		frame.setVisible(true);
	}
	/**
	 * This method will close all connections gracefully
	 */
	private void closeConnections() {
		try {
			ois.close();
			out.close();
			in.close();
			socket.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getGameNo() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean timeOut() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean win() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

		int moves = 0;
		String cmd = arg0.getActionCommand();
		if(cmd.equals("ok")){

			userName = un.getText();
			passWord = new String(pw.getPassword());
			to = new ClientData (userName, passWord);
			try {
				checkUser(userName, passWord);
				checkAndDisplayCorrectScreen();
				//getObject();
				closeConnections();
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//System.out.println(userName + passWord);
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
/**
 * This method checks the authentication object returned by the server and displays the appropriate screen.
 * @throws InterruptedException
 * @throws IOException
 * @throws ClassNotFoundException
 */
	private void checkAndDisplayCorrectScreen() throws InterruptedException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		System.out.println("checking and displaying correct screen...");
		if(wrongPW){
			pwLabel.setText("Enter Password AGAIN!");
			extraLabel.setText("YOU HAVE ENTERED THE WRONG PASSWORD");
			pwLabel.revalidate();
			pwLabel.repaint();
			frame.repaint();
			wrongPW = false;
			newUser = false;
		}else{
			frame.dispose();
			if(!newUser){
				Authentication a = new Authentication(userName, passWord);
				checkPreviousWins(a);
			}
			chooseOpponent(listOfUsers);
			//createMainGui("Welcome!");

		}

	}
/**
 * This method sends the user credentials to the server to verify
 * @param userName2
 * @param passWord2
 * @throws UnknownHostException
 * @throws IOException
 * @throws ClassNotFoundException
 */
	private void checkUser(String userName2, String passWord2) throws UnknownHostException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		System.out.println("checking user...");
		socket = new Socket(hostname, port);		
		out = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());

		n = new Authentication(userName2, passWord2); //create authentication object with user info
		
		out.writeObject(0); //send signal to signify user info object
		out.flush();
		out.writeObject(n);
		out.flush();

		a = (Authentication) ois.readObject(); 
		//ois.close();
		newUser = a.getAuth();
		wrongPW = a.getWrongPW();
		listOfUsers = new ArrayList<String>();
		listOfUsers = a.getUserNames();

		if(!wrongPW){
			to = (ClientData) ois.readObject();
		}
		out.close();
		ois.close();
	}
/**
 * This method checks is a user has won/lost previous games when he logs in
 * @param a
 * @throws IOException
 * @throws ClassNotFoundException
 */
	private void checkPreviousWins(Authentication a) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		System.out.println("checking previous wins...");
		JLabel testLabel11 = null;
		JLabel testLabel12 = null;
		
		socket = new Socket(hostname, port);		
		out = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());

		out.writeObject(2);
		out.flush();
		out.writeObject(a);
		out.flush();

		to  = (ClientData) ois.readObject(); 

		if(to.hasWinner()&&(userName.equals(to.getWinner())||userName.equals(to.getLoser()))){
			frame3 = new JFrame ("Previously...");
			frame3.setMinimumSize(new Dimension(320,240));

			JLabel testLabel = new JLabel("You played a Game with " + to.getOpponent());
			//testLabel11 = new JLabel("Your Score: " + to.getTotalScore());
			//testLabel12 = new JLabel(to.getOpponent() + "'s score: " + to.getOppTotalScore());
			testLabel11 = new JLabel("Winner: " + to.getWinner());
			testLabel12 = new JLabel("Loser: " + to.getLoser());
			//to.clearOpponent();
			
			JButton close = new JButton("Close");
			close.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e)
				{
					frame3.dispose();
				}
			});    
			frame3.setLayout(new GridLayout(5,1));
			frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame3.add(testLabel);
			frame3.add(testLabel11);
			frame3.add(testLabel12);
			frame3.add(close);
			frame3.setAlwaysOnTop(true);
			frame3.pack();
			frame3.setVisible(true);

		}else if(to.hasOpponent()){
			frame3 = new JFrame ("Previously...");
			frame3.setMinimumSize(new Dimension(320,240));

			JLabel testLabel = new JLabel(to.getOpponent() + " has started a game with you!");
			JButton play = new JButton("Play!");
			play.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e)
				{
					try {
						createMainGui();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					nG = false;
					frame3.dispose();
				}
			});
			JButton close = new JButton("Close");
			close.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e)
				{
					frame3.dispose();
				}
			});    
			frame3.setLayout(new GridLayout(3,1));
			frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame3.add(testLabel);
			frame3.add(close);
			frame3.add(play);
			frame3.setAlwaysOnTop(true);
			frame3.pack();
			frame3.setVisible(true);

		}

		closeConnections();

	}
/**
 * This method creates the screen for the user to choose an opponent
 * @param listOfUsers
 */
	private void chooseOpponent(ArrayList<String> listOfUsers) {
		// TODO Auto-generated method stub
		nG = false;
		boolean ngSelected = false;
		System.out.println("choosing opponent...");
		final JFrame frame2 = new JFrame ("Choose Opponent!");
		final JLabel label1 = new JLabel ("To Start a new game, clicked Create New Game, AND THEN click on opponent name. Oppo" +
		"nent MUST be registered in System.");
		frame2.add(label1);
		frame2.setMinimumSize(new Dimension(320,240));

		final JButton newGame = new JButton("CREATE NEW GAME");
		newGame.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e)
			{
				nG = true;   
			}
		});    
		frame2.add(newGame);
		for(String x : listOfUsers){
			if(!x.equals(userName)){
			final JButton a = new JButton(x);
			a.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e){
					if(to.getOpponent().equals(a.getText())||nG){
					to.storeOpponent(a.getText());
					System.out.println("storing opponent...");
					if(nG){
						//if(true){
							try {
								createMainGui();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ClassNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						frame2.dispose();
					}else
						label1.setText("You do not have an existing game with this person. Click New Game!");
				}

			});    
			frame2.add(a);
		}
		}
		frame2.setMinimumSize(new Dimension(640,480));
		frame2.setLayout(new GridLayout(10,1));
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame2.pack();
		frame2.setAlwaysOnTop(false);
		frame2.setVisible(true);
	}
/**
 * This method runs the game
 * Contains a invokeLater method
 * Uses a schedule task to see if the game is complete
 * @throws FileNotFoundException
 * @throws InterruptedException
 */
	private void playGame() throws FileNotFoundException, InterruptedException {

		System.out.println("playing game...");
		final MyGameGUISmall g1 = new MyGameGUISmall();
		Timer timer = new Timer();
		check = false;
		
		Runnable doHelloWorld = new Runnable() {
			public void run() {
				//System.out.println("PICKING MAZE: " + games);
				g1.run(to.mazeList.get(games));
				//System.out.println(to.mazeList.get(games).toString());
			}
		};
		TimerTask runner = new TimerTask(){
			public void run() {
				//Task here ...
				check = g1.getComplete();
				//System.out.println("checking...");
				if(check){
					this.cancel();
					moves = g1.getTime();
					g1.dispose();
					//System.out.println("NUMBER OF MOVES: " +g1.getTime());
					try {
						System.out.println("updating moves...");
						update(moves);
						if(games==3){
							if(to.hasOpponent()&&!to.oppScores.isEmpty())
								checkWinner();
						}
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//System.out.println("Game Over!");
					catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		timer.scheduleAtFixedRate(runner, 5000, 1000);

		
		if(games<=3){
		SwingUtilities.invokeLater(doHelloWorld);
		}
		if(check){
			System.out.println("Game Over!");
			//moves = g1.getTime();
		}


	}
/**
 * This method creates the mainGUI which keeps track of scores and lets the user start the game
 * @throws IOException
 * @throws ClassNotFoundException
 */
	private void createMainGui() throws IOException, ClassNotFoundException{
		System.out.println("creating main GUI...");
		//getObject();
		if(nG){ //Create 10 mazes
			//System.out.println("CREATING NEW MAZES");
			to.mazeList.clear();
			for(int i = 0;i<10;i++){
				MazeCreator b = new MazeCreator();
				to.mazeList.add(b);
				//System.out.println(b.toString());
			}
		}else if(!nG){ //Get 3 mazes
			//to.mazeList.clear();
			to.mazeList.clear();
			//System.out.println("LOADING OPP MAZES");
			for(MazeCreator a : to.getOppMazeList()){
				to.mazeList.add(a);
			}
			
		}

		frame4 = new JFrame ("This is the Main GUI, Your Scores will be reflected here");
		frame4.setMaximumSize(new Dimension(640,480));

		JLabel testLabel = new JLabel("This is the Main GUI");
		JLabel testLabel11 = new JLabel("Your First Score: ");
		JLabel testLabel12 = new JLabel("Your Second Score: ");
		JLabel testLabel13 = new JLabel("Your Third Score: ");
		JButton play = new JButton("Play");
		play.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e)
			{
				try {
					playGame();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});    
		JLabel testLabel21;
		JLabel testLabel22;
		JLabel testLabel23;
		if(to.hasOpponent()){
			testLabel21 = new JLabel(to.getOpponent() + " 's First Score: ");
			testLabel22 = new JLabel(to.getOpponent() + " 's Second Score: ");
			testLabel23 = new JLabel(to.getOpponent() + " 's Third Score: ");
		}else{
			testLabel21 = new JLabel("Opponent's First Score: ");
			testLabel22 = new JLabel("Opponent's Second Score: ");
			testLabel23 = new JLabel("Opponent's Third Score: ");	
		}

		frame4.setLayout(new GridLayout(10,2));
		frame4.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame4.add(testLabel11);
		frame4.add(myFirst);
		frame4.add(testLabel12);
		frame4.add(mySecond);
		frame4.add(testLabel13);
		frame4.add(myThird);
		frame4.add(testLabel21);
		frame4.add(oppFirst);
		frame4.add(testLabel22);
		frame4.add(oppSecond);
		frame4.add(testLabel23);
		frame4.add(oppThird);
		frame4.add(play);
		frame4.pack();
		frame4.setVisible(true);


	}
/**
 * This method updates scores on the server and on the mainGUI
 * @param moves2
 * @throws UnknownHostException
 * @throws IOException
 * @throws InterruptedException
 */
	protected void update(int moves2) throws UnknownHostException, IOException, InterruptedException {
		// TODO Auto-generated method stub
		System.out.println("updating...");
		to.setScore(games,moves);
		if(games == 0){
			myFirst.setText(Integer.toString(moves));
			//frame1.repaint();
		}else if(games == 1){
			mySecond.setText(Integer.toString(moves));
			//frame1.repaint();
		}else if(games == 2){
			myThird.setText(Integer.toString(moves));
			frame4.dispose();
			//frame1.repaint();
		}
		
		socket = new Socket(hostname, port);
		out = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());
		//System.out.println(to.getName());
		//System.out.println(moves);
		try {
			out.writeObject(1);
			out.flush();
			out.writeObject(to);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			to = (ClientData) ois.readObject();
			if(to.hasOpponent())
				//System.out.println(to.getOpponent());
			if(to.hasOpponent()&&!to.oppScores.isEmpty()){
				for(int i=0;i<games; i++){
					if(i==0)
						oppFirst.setText(Integer.toString(to.oppScores.get(i)));
					else if(i==1)
						oppSecond.setText(Integer.toString(to.oppScores.get(i)));
					else if(i==2)
						oppThird.setText(Integer.toString(to.oppScores.get(i)));
				}
				frame4.repaint();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			out.close();
			ois.close();
		}
		if(games<2){
			playGame();
			
		}
		games++;
		closeConnections();
	}
/**
 * This method creates a window which displays who the winner of the match is
 * @throws IOException
 * @throws ClassNotFoundException
 */
	private void checkWinner() throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		System.out.println("checking winner...");
		JFrame frame3 = new JFrame("And the winner is...");
		frame3.setLayout(new FlowLayout());
		frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel label = new JLabel("");
		int myTotalScore = 0;
		for(int i : to.myScores){
			myTotalScore = myTotalScore + i;
		}
		int oppTotalScore = 0; 
		for(int i : to.oppScores){
			oppTotalScore = oppTotalScore + i;
		}
		if(myTotalScore<oppTotalScore){
			label.setText("You Win! Your score: " + myTotalScore + " vs. " + oppTotalScore);
			to.setWinner(to.getName(),to.getOpponent());
		}else if(oppTotalScore<myTotalScore){
			label.setText(to.getOpponent() + " Wins! Your score: " + myTotalScore + " vs. " + oppTotalScore);
			to.setWinner(to.getOpponent(),to.getName());
		}else if(oppTotalScore==myTotalScore)
			label.setText("ITS A TIE!" + myTotalScore + " vs. " + oppTotalScore);
		to.setTotalScore(myTotalScore);
		to.setOppTotalScore(oppTotalScore);
		updateFinal();
		frame3.add(label);
		frame3.pack();
		frame3.setVisible(true);
	}
/**
 * This method updates the server after all the games are complete
 * @throws IOException
 * @throws ClassNotFoundException
 */
	private void updateFinal() throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		System.out.println("final update...");
		socket = new Socket(hostname, port);

		out = new ObjectOutputStream(socket.getOutputStream());

		//in = new Scanner(socket.getInputStream());
		ois = new ObjectInputStream(socket.getInputStream());
		//System.out.println(to.getName());
		//System.out.println(moves);
		
		try {
			out.writeObject(1);
			out.flush();
			out.writeObject(to);
			out.flush();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			to = (ClientData) ois.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	private void getObject() throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		to = (ClientData) ois.readObject();  
		//maze1 = to.getMazeCreator();		
	}
}
