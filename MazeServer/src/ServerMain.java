/**
 * This is the Main Server Class which handles interactions
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;


public class ServerMain implements Runnable{

	private ServerSocket socket;
	private Socket client;
	private PrintWriter out;
	private Scanner in;
	private double x;
	private double y;
	private double result;
	private String func;
	private ArrayList<ClientData> clients = new ArrayList<ClientData>();
	private ObjectOutputStream oos;
	private ObjectInputStream oin;
	private HashMap<String, String> hm = new HashMap();
	private HashMap<String, ClientData> hm1 = new HashMap();
	private boolean wrongPW = false;
	
	
	//private ArrayList<Maze> mazes;

	/**
	 * Server constructor which takes port number
	 * @param port
	 * @throws ClassNotFoundException
	 */
	public ServerMain(int port) throws ClassNotFoundException {
		hm.put("testuser", "testpwd");
		hm.put("Andrew", "Andrew");
		hm.put("t", "t");
		MazeCreator maze = new MazeCreator();
		ClientData a1 = new ClientData("Andrew", "Andrew");
		ClientData a2 = new ClientData("testuser", "testpwd");
		ClientData a3 = new ClientData("t", "t");
		hm1.put("Andrew",a1);
		hm1.put("testuser",a2);
		hm1.put("t",a3);
		
		try {
			//create a server socket on the specified port
			socket = new ServerSocket(port);
			System.out.println("The server is started at port " + port + " and is waiting for connections...");
		while(true){
			//wait for incoming connections
			client = socket.accept();
			System.out.println("Client Connected!! Yay!");

			//get the input and output streams to communicate
			oos = new ObjectOutputStream(client.getOutputStream()); 
			oin = new ObjectInputStream(client.getInputStream());
			
			Object i = oin.readObject();
			
			if(i.equals(0)){
				getClientData(); //receive user object	
			}else if(i.equals(2)){
				System.out.println("checking existing user...");
				checkPreviousUser();
			}else if(i.equals(1)){
				ClientData test = (ClientData) oin.readObject();
				hm1.get(test.getOpponent()).storeOpponent(test.getName());
				if(test.hasWinner()){
					hm1.get(test.getOpponent()).setWinner(test.getWinner(), test.getLoser());
				}
				
					hm1.get(test.getOpponent()).setOppScoreAR(test.getScoreAR());
				
				for(MazeCreator a : test.getMazeList()){
					hm1.get(test.getOpponent()).addOppMaze(a);
					
				}//System.out.println("adding maze to opponent");
				
				for(int i1=0;i1<10;i1++){ //add maze to opponent
					hm1.get(test.getOpponent()).oppMazeList.set(i1, test.mazeList.get(i1));
					//System.out.println(test.mazeList.get(i1).toString());
				}
				//System.out.println(test.getName());
				hm1.put(test.getName(),test);
				clients.add(test);
				/*
				if(hm1.get(test.getName()).hasOpponent()){
					System.out.println("adding opponent's scores...");
					hm1.get(test.getName()).setOppScoreAR(hm1.get(hm1.get(test.getName()).getOpponent()).getScoreAR());
					
				}*/
				oos.writeObject(hm1.get(test.getName()));
				oos.flush();
				/*
				System.out.println("Top Scores:");
				for(ClientData x : clients){
					System.out.println(x.getName()+":  "+x.getScore(1));
				}
				*/
			}
			
		}	
			//doSomething();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//closeConnections();
		}
		
	}
/**
 * This method checks whether a previous game has been played with the current user
 * @throws IOException
 * @throws ClassNotFoundException
 */
	private void checkPreviousUser() throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		Authentication n = (Authentication) oin.readObject();
		String user = n.getUser();
		System.out.println("checking username " + user);
		oos.writeObject(hm1.get(user));
		oos.flush();
	}

	@SuppressWarnings("static-access")
	/**
	 * This method returns a ClientDate object to the client
	 */
	private void getClientData() throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		String message = null;
		String str[] = null;
		
		Authentication n = (Authentication) oin.readObject();
		String[] str1 = new String[2];
		str1[0]=n.getUser();
		str1[1]=n.getPassword();
		checkUser(str1);
	}
/**
 * This method checks a user's username and password combination to see if it is a existing or new user
 * @param str
 * @throws IOException
 */
	private void checkUser(String[] str) throws IOException {
		boolean newUser = false;
		ClientData a = null;
		Authentication auth = new Authentication(false);
		
		Set set = hm1.entrySet();
		Iterator i = set.iterator();
		while(i.hasNext()) { 
			Map.Entry me = (Map.Entry)i.next(); 
			auth.addUser((String) me.getKey());	
		}
		
		if(check(str[0],str[1])){
			System.out.println("1");
			auth.setAuth(false);
			auth.setWrongPW(false);
			oos.writeObject(auth);
			newUser = false;	
		}else if(!check(str[0],str[1])&&!wrongPW){
			System.out.println("0");
			newUser = true;
			auth.setAuth(true);
			auth.setWrongPW(false);
			oos.writeObject(auth);
			a = new ClientData(str[0],str[1]); //Creates new User Object
		}else if(!check(str[0],str[1])&&wrongPW){
			System.out.println("Wrong Password!");
			auth.setWrongPW(true);
			oos.writeObject(auth);
			newUser = false;
		}
		oos.flush();	
		
		if(newUser&&!wrongPW){ //this sends the relevant data
			hm1.put(str[0],a);
			oos.writeObject(hm1.get(str[0]));
		}else if(!newUser&&!wrongPW){
			hm.put(str[0],str[1]);
			oos.writeObject(hm1.get(str[0]));
		}else{
		}	//oos.writeObject(hm1.get(str[0]));
		
		oos.flush();
	}
/**
 * This method checks to see if the password entered exists within the database (which is a hashmap)
 * @param string
 * @param string2
 * @return
 */
	private boolean check(String string, String string2) {
		// TODO Auto-generated method stub
		wrongPW = false;
		boolean contains = false;
		boolean nUser = false;
		Set set = hm.entrySet();
		Iterator i = set.iterator();
		while(i.hasNext()) { 
			Map.Entry me = (Map.Entry)i.next(); 
			if(me.getKey().equals(string) && me.getValue().equals(string2)){
				contains = true;
			}else if(me.getKey().equals(string) && !me.getValue().equals(string2)){
				wrongPW = true;
			}else if(!me.getKey().equals(string) && !me.getValue().equals(string2))
				nUser = true;
			} 
		return contains;
	}

	/**
	 * This will close all open connections gracefully
	 */
	
	
	private void closeConnections() {
		try {
			//out.close();
			oos.close();
			in.close();
			client.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}