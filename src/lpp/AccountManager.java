package lpp;

import lpp.account.Account;

public class AccountManager {
	private static AccountManager INSTANCE;
	
	private Account[] accounts;
	private int accountsCount;
	
	private Account currentAccount;
	
	private AccountManager(int capacity) {
		accounts = new Account[capacity];
		accountsCount = 0;
	}
	
	public static boolean init(int capacity) {
		if (INSTANCE != null) {
			return false;
		}
		
		INSTANCE = new AccountManager(capacity);
		return true;
	}
	
	public static AccountManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AccountManager(100);
		}
		
		return INSTANCE;
	}
	
	public Account findAccount(String username) {
		int accountIndex = findAccountIndex(username);
		
		if (accountIndex != -1) {
			return accounts[accountIndex];
		}
		
		return null;
	}
	
	/**
	 * Adds a new account if possible
	 * 
	 * @param account Account to be added
	 * @return 0 if account was added successfully, 
	 *        -1 if an account with same username exists
	 *        -2 if no account can be added due to capacity constraints
	 */
	public int addAccount(Account account) {
		if (accountsCount >= accounts.length) {
			return -2;
		}
		
		if (findAccount(account.getUsername()) != null) {
			return -1;
		}
		
		accounts[accountsCount++] = account;
		return 0;
	}
	
	public boolean updateAccount(Account account) {
		int accountIndex = findAccountIndex(account.getUsername());
		
		if (accountIndex == -1) {
			return false;
		}
		
		accounts[accountIndex] = account;
		return true;
	}
	
	public boolean deleteAccount(String username) {
		int accountIndex = findAccountIndex(username);
		
		if (accountIndex == -1) {
			return false;
		}
		
		accounts[accountIndex] = accounts[--accountsCount];
		accounts[accountsCount] = null;
		
		return true;
	}
	
	public boolean login(String username, String password) {
		Account account = findAccount(username);
		
		if (account != null && account.isPasswordCorrect(password)) {
			currentAccount = account;
			return true;
		}
		
		return false;
	}
	
	public boolean logout() {
		if (currentAccount == null) {
			return false;
		}
		
		currentAccount = null;
		return true;
	}
	
	public Account getCurrentAccount() {
		return currentAccount;
	}
	
	private int findAccountIndex(String username) {
		for (int accountIndex = 0; accountIndex < accountsCount; accountIndex++) {
			if (accounts[accountIndex].getUsername().equals(username)) {
				return accountIndex;
			}
		}
		
		return -1;
	}
}
