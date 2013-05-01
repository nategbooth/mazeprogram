import java.io.Serializable;
import java.util.ArrayList;

/**
 * This is the ClientData class which keeps track of all the data associated with a single user
 * @author abc
 *
 */
public class ClientData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;
	private String hostName;
	private String ipAddress;
	private int gameNo;
	private int firstGameScore = -1;
	private int secondGameScore = -1;
	private int lastGameScore = -1;
	private int firstOppGameScore = -1;
	private int secondOppGameScore = -1;
	private int lastOppGameScore = -1;
	private Maze testMaze;
	private MazeCreator testMazeCreator;
	public ArrayList<MazeCreator> mazeList = new ArrayList<MazeCreator>();
	public ArrayList<MazeCreator> oppMazeList = new ArrayList<MazeCreator>();
	private ArrayList<Result> result;
	private String opponent = "-";
	public ArrayList<Integer> myScores = new ArrayList<Integer>();
	public ArrayList<Integer> oppScores = new ArrayList<Integer>();
	public String winner;
	public String loser;
	public boolean haswinner=false;
	private int myTotalScore;
	private int oppTotalScore;

	public ClientData(String n,String h){
		userName = n;
		hostName = h;
		//ipAddress = i;
		//testMaze = maze;
		//testMazeCreator = maze;
		

	}
	/**
	 * This adds mazes to the arraylist of mazes
	 * @param a
	 */
	public void addMaze(MazeCreator a){
		mazeList.add(a);
	}
	
	public ArrayList<MazeCreator> getMazeList(){
		return mazeList;
	}
	
	public void addOppMaze(MazeCreator a){
		oppMazeList.add(a);
	}
	
	public ArrayList<MazeCreator> getOppMazeList(){
		return oppMazeList;
	}
	
	public Maze getMaze(){
		return testMaze;
	}
	
	public MazeCreator getMazeCreator(){
		return testMazeCreator;
	}

	public String getName(){
		return userName;
	}

	public void setScore(int i, int moves) {
		// TODO Auto-generated method stub
		if(i==0){
			firstGameScore = moves;
			myScores.add(moves);
		}else if(i==1){
			secondGameScore = moves;
			myScores.add(moves);
		}else if(i==2){
			lastGameScore = moves;
			myScores.add(moves);
		}
	}
	
	public int getScore(int i){
		if(i==1)
			return firstGameScore;
		else if(i==2)
			return secondGameScore;
		else if(i==3)
			return lastGameScore;
		
		return i;
	}

	public void storeOpponent(String a) {
		// TODO Auto-generated method stub
		opponent = a;
	}

	public String getOpponent(){
		return opponent;
	}

	public boolean hasOpponent(){
		if(!opponent.equals("-"))
			return true;
		else 
			return false;
	}
	public void setOppScore(int i, int moves) {
		// TODO Auto-generated method stub
		
		
		if(i==1)
			firstOppGameScore = moves;
		else if(i==2)
			secondOppGameScore = moves;
		else if(i==3)
			lastOppGameScore = moves;
		
	}
	
	public int getOppScore(int i){
		if(i==1)
			return firstOppGameScore;
		else if(i==2)
			return secondOppGameScore;
		else if(i==3)
			return lastOppGameScore;
		
		return i;
	}

	public ArrayList<Integer> getScoreAR() {
		// TODO Auto-generated method stub
		return myScores;
	}

	public void setOppScoreAR(ArrayList<Integer> scoreAR) {
		// TODO Auto-generated method stub
		oppScores = scoreAR;
	}

	public void setWinner(String opponent2, String name) {
		// TODO Auto-generated method stub
		winner = opponent2;
		loser = name;
		haswinner = true;
	}
	
	public String getWinner(){
		return winner;
	}
	
	public boolean hasWinner(){
		return haswinner;
	}

	public int getTotalScore() {
		// TODO Auto-generated method stub
		return myTotalScore;
	}
	
	public int getOppTotalScore(){
		return oppTotalScore;
	}

	public void setTotalScore(int myTotalScore1) {
		// TODO Auto-generated method stub
		myTotalScore = myTotalScore1;
	}

	public void setOppTotalScore(int oppTotalScore1) {
		// TODO Auto-generated method stub
		oppTotalScore = oppTotalScore1;
	}
	public void clearOpponent() {
		// TODO Auto-generated method stub
		haswinner = false;
		myScores.clear();
		oppScores.clear();
	}
	public String getLoser() {
		// TODO Auto-generated method stub
		return loser;
	}

}
