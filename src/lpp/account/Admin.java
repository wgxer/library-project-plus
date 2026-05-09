package lpp.account;

import java.util.NoSuchElementException;

import lpp.AccountManager;

public class Admin extends Account {
	private static int TOTAL_BORROWS = 0;
	private static int TOTAL_RETURNS = 0;
	private static double TOTAL_REVENUE = 0;
	
	public Admin(String username, String password) {
		super(username, password);
	}
	
	/**
	 * Upgrades a user to author
	 * 
	 * @param username User to be upgraded
	 * @return 0 if done successfully, 
	 * 	-1 if no account with that username exists,
	 *  -2 if account is not a user
	 *  -3 if account is already an author
	 */
	public void upgradeUser(String username) {
		AccountManager accountManager = AccountManager.getInstance();
		Account account = accountManager.findAccount(username);
		
		if (account == null) {
			throw new NoSuchElementException("Username `" + username + "` not found.");
		}
		
		if (!(account instanceof User)) {
			throw new IllegalArgumentException("`" + username + "` is not a user.");
		}
		
		if (account instanceof Author) {
			throw new IllegalArgumentException("`" + username + "` is already an author.");
		}
		
		Author newAccount = new Author((User) account);
		accountManager.updateAccount(newAccount);
	}
	
	public static int getTotalBorrows() {
		return TOTAL_BORROWS;
	}
	
	public static int getTotalReturns() {
		return TOTAL_RETURNS;
	}
	
	public static double getTotalRevenue() {
		return TOTAL_REVENUE;
	}
	
	public static void recordBorrow() {
		TOTAL_BORROWS += 1;
	}
	
	public static void recordReturn() {
		TOTAL_RETURNS += 1;
	}
	
	public static boolean recordRevenue(double revenue) {
		if (revenue < 0) {
			return false;
		}
		
		TOTAL_REVENUE += revenue;
		return true;
	}
}
