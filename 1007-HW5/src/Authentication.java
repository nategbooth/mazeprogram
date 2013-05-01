import java.io.Serializable;
import java.util.ArrayList;


public class Authentication implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1932980956208050248L;
	private String userName;
	private String passWord;
	private boolean access = false;
	private boolean wrongPW = false;
	private ArrayList<String> users = new ArrayList<String>();
	
	public Authentication(boolean b){
		access = b;
	}
	
	public Authentication(String userName2, String passWord2) {
		// TODO Auto-generated constructor stub
		userName = userName2;
		passWord = passWord2;
	}

	public boolean getAuth(){
		return access;
	}

	public void setAuth(boolean s) {
		// TODO Auto-generated method stub
		access = s;
	}
	
	public boolean getWrongPW(){
		return wrongPW;
	}

	public void setWrongPW(boolean b){
		wrongPW = b;
	}
	public String getUser() {
		// TODO Auto-generated method stub
		return userName;
	}
	
	public void addUser(String a){
		users.add(a);
	}
	
	public ArrayList<String> getUserNames() {
		// TODO Auto-generated method stub
		return users;
	}
	public String getPassword() {
		// TODO Auto-generated method stub
		return passWord;
	}
}
