package lpp.account;

public class Account {
	protected String username;
	protected String password;
	
	// TODO: Remove this after all subclasses are implemented
	public Account() {
		this("", "");
	}
	
	public Account(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public boolean isPasswordCorrect(String password) {
		return this.password.equals(password);
	}
}
