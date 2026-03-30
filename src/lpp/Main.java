package lpp;

import java.util.Scanner;

import lpp.account.Account;
import lpp.account.Admin;
import lpp.account.Author;
import lpp.account.User;
import lpp.item.Book;
import lpp.item.LibraryItem;

public class Main {
	public static void main(String[] args) {
		AccountManager accountManager = AccountManager.getInstance();
		Library library = new Library("Test Library", 100);
		
		library.addItem(new Book(56, "abcdef", "Unknown", 2016));
		library.addItem(new Book(105, "Ahmed's ABC book", "Ahmed", 2019));
		
		accountManager.addAccount(new Admin("admin", "admin"));
		accountManager.addAccount(new User("user", "user123", 10.0));
		
		Scanner input = new Scanner(System.in);
		new Book(3, "ABC", "Ahmed", 2024).display();
		
		System.out.println("╭────────────────────────────────────────╮");
		System.out.println("│                                        │");
		System.out.println("│ Welcome to Library Management System ! │");
		System.out.println("│                                        │");
		System.out.println("╰────────────────────────────────────────╯");

		while (true) {
			Account account = AccountManager.getInstance().getCurrentAccount();
			
			if (account == null) {
				showLoginMenu(input);
			} else if (account instanceof Admin) {
				showAdminMenu(input, library, (Admin) account);
			} else if (account instanceof User) {
				showUserMenu(input, library, (User) account);
			}
		}
	}
	
	private static void showLoginMenu(Scanner input) {
		AccountManager accountManager = AccountManager.getInstance();
		
		System.out.println();
		System.out.println("╭────────────────────────────────────────╮");
		System.out.println("│ Login:                                 │");
		System.out.println("│  Username: [░░░░░░░░░░░░]              │");
		System.out.println("│  Password: [            ]              │");
		System.out.println("│                                        │");
		System.out.println("│ Don't have an account? Type '!signup'  │");
		System.out.println("╰────────────────────────────────────────╯");
		System.out.println();
		System.out.print("» Enter username (or '!signup'): ");
		
		String loginUsername = input.next();
		
		if (loginUsername.equals("!signup")) {
			showSignupMenu(input);
			return;
		}
		
		System.out.println();
		System.out.println("╭────────────────────────────────────────╮");
		System.out.println("│ Login:                                 │");
		System.out.println("│  Username: " + loginUsername.substring(0, Math.min(loginUsername.length(), 27)) + " ".repeat(Math.max(0, 27 - loginUsername.length())) + " │");
		System.out.println("│  Password: [░░░░░░░░░░░░]              │");
		System.out.println("╰────────────────────────────────────────╯");
		System.out.println();
		System.out.print("» Enter password: ");
		
		String password = input.next();
		
		if (!accountManager.login(loginUsername, password)) {
			System.out.println("✘ Incorrect username/password, please try again !");
			System.out.println();
			
			return;
		}
		
		System.out.println("✔ Welcome " + accountManager.getCurrentAccount().getUsername() + " to library !");
	}
	
	private static void showSignupMenu(Scanner input) {
		AccountManager accountManager = AccountManager.getInstance();

		boolean tryAgain = false;
		String username;
		
		do {
			System.out.println();
			System.out.println("╭────────────────────────────────────────────╮");
			
			if (tryAgain) {
				System.out.println("│ ✘ A user with that username already        │");
				System.out.println("│   exists, please choose another username   │");
				System.out.println("│                                            │");
				
				tryAgain = false;
			}
			
			System.out.println("│ Signup:                                    │");
			System.out.println("│  Username: [░░░░░░░░░░░░]                  │");
			System.out.println("│                                            │");
			System.out.println("│ Type '!cancel' to go back                  │");
			System.out.println("╰────────────────────────────────────────────╯");
			System.out.println();
			System.out.print("» Enter username (or '!cancel'): ");
			
			username = input.next();
			
			if (username.equals("!cancel")) {
				return;
			} else if (accountManager.findAccount(username) != null) {
				tryAgain = true;
				continue;
			}
		} while(tryAgain);
		
		String password;
		
		do {
			System.out.println();
			System.out.println("╭────────────────────────────────────────────╮");
			
			if (tryAgain) {
				System.out.println("│ ✘ Password doesn't match with confirm      │");
				System.out.println("│   password, please ensure they're equal    │");
				System.out.println("│                                            │");
				
				tryAgain = false;
			}
			
			System.out.println("│ Signup:                                    │");
			System.out.println("│  Password: [░░░░░░░░░░░░]                  │");
			System.out.println("│  Confirm Password: [            ]          │");
			System.out.println("│                                            │");
			System.out.println("│ Type '!cancel' to go to login menu         │");
			System.out.println("╰────────────────────────────────────────────╯");
			System.out.println();
			System.out.print("» Enter password (or '!cancel'): ");
			
			password = input.next();
			
			if (password.equals("!cancel")) return;
			
			System.out.println();
			System.out.println("╭────────────────────────────────────────────╮");
			System.out.println("│ Signup:                                    │");
			System.out.println("│  Password: [************]                  │");
			System.out.println("│  Confirm Password: [░░░░░░░░░░░░]          │");
			System.out.println("╰────────────────────────────────────────────╯");
			System.out.println();
			System.out.print("» Enter your password again: ");
			
			if (!password.equals(input.next())) {
				tryAgain = true;
			}
		} while (tryAgain);
		
		switch (accountManager.addAccount(new User(username, password, 0))) {
		case 0:
			System.out.println("✔ Welcome " + username + " to library for first time!");
			accountManager.login(username, password);

			break;
		case -2:
			System.out.println("! System cannot handle more accounts, please contact program developers !");
			break;
		default:
			System.out.println("! Something went wrong, please contact program developers !");
			break;
		}
	}
	
	private static void showAdminMenu(Scanner input, Library library, Admin admin) {
		AccountManager accountManager = AccountManager.getInstance();

		System.out.println();
		System.out.println("╭────────────────────────────────────────╮");
		System.out.println("│ Admin Operations:                      │");
		System.out.println("│  1. Approve Book                       │");
		System.out.println("│  2. Upgrade a user to author           │");
		System.out.println("│  3. Statistics                         │");
		System.out.println("│  4. Logout                             │");
		System.out.println("╰────────────────────────────────────────╯");
		System.out.println();
		System.out.print("» Enter the number of operation: ");
		
		int operation = input.nextInt();
		
		switch (operation) {
		case 1:
			System.out.println("! Sorry, this service is temporarily unavaliable, please try again later.");
			return;
		case 2:
			boolean tryAgain = false;
			
			do {
				System.out.println("╭────────────────────────────────────────────╮");
				
				if (tryAgain) {
					System.out.println("│ ✘ Incorrect username, please try again !   │");
					System.out.println("│                                            │");
					
					tryAgain = false;
				}
				
				System.out.println("│ Upgrade User to Author:                    │");
				System.out.println("│  Username: [░░░░░░░░░░░░]                  │");
				System.out.println("│                                            │");
				
				
				System.out.println("│ Type '!cancel' to go back                  │");
				System.out.println("╰────────────────────────────────────────────╯");
				System.out.println();
				System.out.print("» Enter username (or '!cancel'): ");
				
				String username = input.next();
				
				if (username.equals("!cancel")) {
					break;
				}
				
				switch(admin.upgradeUser(username)) {
				case 0:
					System.out.println("✔ User '" + username + "' has been upgraded successfully to author !");
					break;
				case -1:
					tryAgain = true;
					break;
				case -2:
					System.out.println("✘ Account '" + username + "' is not a user !");
					break;
				case -3:
					System.out.println("✘ Account '" + username + "' is already an author !");
					break;
				default:
					System.out.println("! Something went wrong, please report to program authors !");
					break;
				}
			} while (tryAgain);
			
			break;
		case 3:
			System.out.println("╭────────────────────────────────────────╮");
			System.out.println("│ Statistics:                            │");
			System.out.printf("│  Total Borrows: %-22d │%n", Admin.getTotalBorrows());
			System.out.printf("│  Total Returns: %-22d │%n", Admin.getTotalReturns());
			System.out.printf("│  Total Revenue: %-22.2f │%n", Admin.getTotalRevenue());
			System.out.println("╰────────────────────────────────────────╯");
			
			break;
		case 4:
			System.out.println("- Goodbye, " + admin.getUsername() + " !");
			accountManager.logout();
			
			break;
		default:
			System.out.println("✘ Invalid operation, please try again !");
			break;
		}
	}
	
	private static void showUserMenu(Scanner input, Library library, User user) {
		AccountManager accountManager = AccountManager.getInstance();
		boolean isAuthor = user instanceof Author;

		System.out.println();
		System.out.println("╭────────────────────────────────────────╮");
		System.out.println("│ User Operations:                       │");
		System.out.println("│  1. Search for library items           │");
		System.out.println("│  2. Return a library item              │");
		System.out.println("│  3. Statistics                         │");
		
		if (isAuthor) {
			System.out.println("│  4. Review manuscripts                 │");
			System.out.println("│  5. Book publishing                    │");
			System.out.println("│  6. Logout                             │");
		} else {
			System.out.println("│  4. Logout                             │");
		}
		
		System.out.println("╰────────────────────────────────────────╯");
		System.out.println();
		System.out.print("» Enter the number of operation: ");
		
		int userOperation = input.nextInt();
		boolean tryAgain = false;
		
		switch (userOperation) {
		case 1:
			LibraryItem[] searchResults;

			do {
				System.out.println("╭────────────────────────────────────────╮");
				
				if (tryAgain) {
					System.out.println("│ ✘ No library items matching search     │");
					System.out.println("│   prompt were found                    │");
					System.out.println("│                                        │");
					tryAgain = false;
				}
				
				System.out.println("│ Search by title:                       │");
				System.out.println("│  [░░░░░░░░░░░░░░░░░░░░░░░░░]           │");
				System.out.println("│                                        │");
				System.out.println("│  Type '!back' to go back.              │");
				System.out.println("╰────────────────────────────────────────╯");
				System.out.println();
				System.out.print("» Enter search prompt (or '!back'): ");
	
				String searchPrompt = input.next();
				
				if (searchPrompt.equals("!back")) {
					return;
				}
				
				searchResults = library.searchItem(searchPrompt);
				
				if (searchResults.length == 0) {
					tryAgain = true;
				}
			} while (tryAgain);
			
			boolean backToSearchResults;
			
			do {
				backToSearchResults = false;
				int itemIndex;
				
				do {
					System.out.println();
					System.out.println("╭────────────────────────────────────────────╮");
					
					if (tryAgain) {
						System.out.println("│ ✘ Incorrect library item number, please    │");
						System.out.println("│   enter a correct one.                     │");
						System.out.println("│                                            │");
						tryAgain = false;
					}
					
					System.out.println("│ Search Results:                            │");
					
					for (int i = 0; i < searchResults.length; i++) {
						LibraryItem item = searchResults[i];
						
						int avaliableSpaceForVariables = 40;
						
						String numberString = String.valueOf(i + 1);
						avaliableSpaceForVariables -= numberString.length();
						
						String itemTitle = item.getName();
						String displayedItemTitle = itemTitle.substring(0, Math.min(itemTitle.length(), avaliableSpaceForVariables));
						avaliableSpaceForVariables -= displayedItemTitle.length();
						
						System.out.println("│ " + numberString + ". " + displayedItemTitle + " ".repeat(avaliableSpaceForVariables) + " │");
					}
					
					System.out.println("│                                            │");
					System.out.println("│  Type '-1' to go to opeartions menu.       │");
					System.out.println("╰────────────────────────────────────────────╯");
					System.out.println();
					System.out.print("» Enter the number of library item you want to open (or '-1'): ");
					
					int itemNumber = input.nextInt();
					
					if (itemNumber == -1) {
						return;
					} 
					
					itemIndex = itemNumber - 1;
					
					if (itemIndex < 0 || itemIndex >= searchResults.length) {
						tryAgain = true;	
					}
				} while (tryAgain);
				
				LibraryItem selectedItem = searchResults[itemIndex];
				
				String errorMessageLine1 = null;
				String errorMessageLine2 = null;
				
				do {
					System.out.println();
					selectedItem.display();
					
					System.out.println("╭────────────────────────────────────────────╮");					
	
					if (errorMessageLine1 != null) {
						System.out.printf("│ ✘ %-40s │%n", errorMessageLine1);
						if (errorMessageLine2 != null) System.out.printf("│   %-40s │%n", errorMessageLine2);
						
						System.out.println("│                                            │");
						
						errorMessageLine1 = null;
						errorMessageLine2 = null;
					}
					
					
					System.out.println("│ Library Item Operations:                   │");
					System.out.println("│  1. Borrow                                 │");
					System.out.println("│  2. Back to Search Results                 │");
					System.out.println("│  3. Back to User Operations                │");
					System.out.println("╰────────────────────────────────────────────╯");
					System.out.println();
					System.out.print("» Enter the operation number: ");
					
					int itemOperation = input.nextInt();
					
					switch (itemOperation) {
					case 1:
						switch (user.borrowItem(selectedItem)) {
						case 1:
							System.out.println("✔ '" + selectedItem.getName() + "' has been borrowed successfully !");
							break;
						case -1:
							errorMessageLine1 = "No more than 5 items can be borrowed";
							errorMessageLine2 = "at the same time.";
							break;
						case -2:
							errorMessageLine1 = "Manuscripts can't be borrowed.";
							break;
						case -3:
							errorMessageLine1 = "Item is not avaliable right now,";
							errorMessageLine2 = "please try later.";
							break;
						case -4:
							errorMessageLine1 = "Insufficient balance.";
							break;
						default:
							errorMessageLine1 = "Something went wrong, please report";
							errorMessageLine2 = "to program developers.";
							break;
						}
						
						break;
					case 2:
						backToSearchResults = true;
						break;
					case 3:
						return;
					}
				} while (errorMessageLine1 != null);
			} while(backToSearchResults);
			
			return;
		case 2:
			int itemIndex;
			LibraryItem[] borrowedItems = user.getBorrowedItems();
			int itemsCount = user.getItemsCount();
			
			if (itemsCount == 0) {
				System.out.println("✘ You don't have any borrowed items to return.");
				return;
			}

			do {
				System.out.println();
				System.out.println("╭────────────────────────────────────────────╮");
				
				if (tryAgain) {
					System.out.println("│ ✘ Incorrect library item number, please    │");
					System.out.println("│   enter a correct one.                     │");
					System.out.println("│                                            │");
					tryAgain = false;
				}
				
				System.out.println("│ Currently Borrowed Items:                  │");
				
				for (int i = 0; i < itemsCount; i++) {
					LibraryItem item = borrowedItems[i];
					int avaliableSpaceForVariables = 40;
					
					String numberString = String.valueOf(i + 1);
					avaliableSpaceForVariables -= numberString.length();
					
					String itemTitle = item.getName();
					String displayedItemTitle = itemTitle.substring(0, Math.min(itemTitle.length(), avaliableSpaceForVariables));
					avaliableSpaceForVariables -= displayedItemTitle.length();
					
					System.out.println("│ " + numberString + ". " + displayedItemTitle + " ".repeat(avaliableSpaceForVariables) + " │");
				}
				
				System.out.println("│                                            │");
				System.out.println("│  Type '-1' to go back.                     │");
				System.out.println("╰────────────────────────────────────────────╯");
				System.out.println();
				System.out.print("» Enter the number of library item you want to return (or '-1'): ");
				
				int itemNumber = input.nextInt();
				
				if (itemNumber == -1) {
					return;
				} 
				
				itemIndex = itemNumber - 1;
				
				if (itemIndex < 0 || itemIndex >= itemsCount) {
					tryAgain = true;
				}
			} while (tryAgain);
				
			LibraryItem selectedItem = borrowedItems[itemIndex];
			
			if (user.returnItem(selectedItem) == 1) {
				System.out.println("✔ '" + selectedItem.getName() + "' has been returned successfully !");
			} else {
				System.out.println("! Item couldn't be returned due to system issues, please contact library adminstrators.");
			}
				
			break;
		case 3:
			user.display();
			break;
		case 4:
			if (!isAuthor) {
				System.out.println("- Goodbye, " + user.getUsername() + " !");
				accountManager.logout();
			}
			
			break;
		case 5:
			if (isAuthor) {
				break;
			}
		case 6:
			if (isAuthor) {
				System.out.println("- Goodbye, " + user.getUsername() + " !");
				accountManager.logout();
				
				break;
			}
		default:
			System.out.println("✘ Invalid operation, please try again !");
			break;
		}
	}
}
