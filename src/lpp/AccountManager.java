package lpp;

import lpp.account.Account;
import java.util.LinkedList;
import lpp.account.User;

public class AccountManager {
private static AccountManager INSTANCE;
    
    private LinkedList<Account> accounts; 
    private int accountsCount;
    
    private Account currentAccount;
    
    private AccountManager(int capacity) {
        accounts = new LinkedList<Account>(); 
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
            return accounts.get(accountIndex); 
        }
        
        return null;
    }
    
    public User[] searchUsers(String searchEntry) {
        searchEntry = searchEntry.toLowerCase();
        int usersCount = 0;
        
        for (int accountIndex = 0; accountIndex < accounts.size(); accountIndex++) {
            Account account = accounts.get(accountIndex);
            if (account instanceof User && account.getUsername().toLowerCase().contains(searchEntry)) {
                usersCount++;
            }
        }
        
        User[] users = new User[usersCount];
        int userIndex = 0;
        
        for (int accountIndex = 0; accountIndex < accounts.size(); accountIndex++) {
            Account account = accounts.get(accountIndex);
            if (account instanceof User && account.getUsername().toLowerCase().contains(searchEntry)) {
                users[userIndex] = (User) account;
                userIndex++;
            }
        }
        
        return users;
    }
    
    /**
     * Adds a new account if possible
     * * @param account Account to be added
     * @return 0 if account was added successfully, 
     * -1 if an account with same username exists
     * -2 if no account can be added due to capacity constraints
     */
    public int addAccount(Account account) {
        if (findAccount(account.getUsername()) != null) {
            return -1;
        }
        
        accounts.add(account);
        accountsCount++;
        return 0;
    }
    
    public boolean updateAccount(Account account) {
        int accountIndex = findAccountIndex(account.getUsername());
        
        if (accountIndex == -1) {
            return false;
        }
        
        accounts.set(accountIndex, account);
        return true;
    }
    
    public boolean deleteAccount(String username) {
        int accountIndex = findAccountIndex(username);
        
        if (accountIndex == -1) {
            return false;
        }
        
        accounts.remove(accountIndex);
        accountsCount--;
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
        for (int accountIndex = 0; accountIndex < accounts.size(); accountIndex++) {
            if (accounts.get(accountIndex).getUsername().equals(username)) {
                return accountIndex;
            }
        }
        return -1;
    }
    
    public LinkedList<Account> getAccounts() {
        return accounts;
    }
}
