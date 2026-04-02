package lpp.account;

public class Account {
	private String username;
	private String password;
	
	public Account(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public Account(Account other) {
		this.username = other.username;
		this.password = other.password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public boolean isPasswordCorrect(String password) {
		return this.password.equals(password);
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
