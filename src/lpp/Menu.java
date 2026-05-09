package lpp;

import java.util.NoSuchElementException;
import java.util.Scanner;

import lpp.account.Account;
import lpp.account.Admin;
import lpp.account.Author;
import lpp.account.InsufficientBalanceException;
import lpp.account.User;
import lpp.item.Book;
import lpp.item.Comment;
import lpp.item.LibraryItem;
import lpp.item.Manuscript;
import lpp.item.UnavailableItemException;

public class Menu {


	public static boolean showLoginMenu(Scanner input) {
		AccountManager accountManager = AccountManager.getInstance();

		System.out.println();
		System.out.println("╭────────────────────────────────────────╮");
		System.out.println("│ Login:                                 │");
		System.out.println("│  Username: [░░░░░░░░░░░░]              │");
		System.out.println("│  Password: [            ]              │");
		System.out.println("│                                        │");
		System.out.println("│ Don't have an account? Type '!signup'  │");
		System.out.println("│ Type '!exit' to exit from program      │");
		System.out.println("╰────────────────────────────────────────╯");
		System.out.println();
		System.out.print("» Enter username (or '!signup'): ");

		String loginUsername = input.next();

		if (loginUsername.equals("!signup")) {
			showSignupMenu(input);
			return false;
		} else if (loginUsername.equals("!exit")) {
			return true;
		}

		System.out.println();
		System.out.println("╭────────────────────────────────────────╮");
		System.out.println("│ Login:                                 │");
		System.out.printf("│  Username: %-27s │%n", loginUsername);
		System.out.println("│  Password: [░░░░░░░░░░░░]              │");
		System.out.println("╰────────────────────────────────────────╯");
		System.out.println();
		System.out.print("» Enter password: ");

		String password = input.next();

		if (!accountManager.login(loginUsername, password)) {
			System.out.println("✘ Incorrect username/password, please try again !");
			System.out.println();

			return false;
		}

		System.out.println("✔ Welcome " + accountManager.getCurrentAccount().getUsername() + " to library !");
		return false;
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
		} while (tryAgain);

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

			if (password.equals("!cancel"))
				return;

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

		accountManager.addAccount(new User(username, password, 0));
		
		System.out.println("✔ Welcome " + username + " to library for first time!");
		accountManager.login(username, password);
	}

	public static void showAdminMenu(Scanner input, Library library, Admin admin) {
		AccountManager accountManager = AccountManager.getInstance();

		System.out.println();
		System.out.println("╭─────────────────────────────────────────╮");
		System.out.println("│ Admin Operations:                       │");
		System.out.println("│  1. Manage manuscript showcase requests │");
		System.out.println("│  2. Add/remove balance to user          │");
		System.out.println("│  3. Upgrade a user to author            │");
		System.out.println("│  4. View user statistics                │");
		System.out.println("│  5. Change user password                │");
		System.out.println("│  6. Delete user                         │");
		System.out.println("│  7. Add book                            │");
		System.out.println("│  8. Delete a library item               │");
		System.out.println("│  9. Statistics                          │");
		System.out.println("│  10. Logout                             │");
		System.out.println("╰─────────────────────────────────────────╯");
		System.out.println();
		System.out.print("» Enter the number of operation: ");

		int adminOperation = input.nextInt();
		input.nextLine();

		boolean tryAgain = false;

		switch (adminOperation) {
		case 1:
			LinkedList<LibraryItem> requests = library.getRequests();
			int requestsCount = requests.size();

			if (requestsCount == 0) {
				System.out.println("✘ No manuscript showcase requests, please recheck later !");
				return;
			}

			int requestIndex = selectLibraryItemMenu(input, requests, requestsCount, "Manuscripts Showcase Requests");

			if (requestIndex == -1) {
				return;
			}

			LibraryItem request = requests.get(requestIndex);

			do {
				request.display();

				System.out.println();
				System.out.println("╭─────────────────────────────────────────╮");

				if (tryAgain) {
					System.out.println("│ ✘ Invalid operation. please try again ! │");
					System.out.println("│                                         │");

					tryAgain = false;
				}
				System.out.println("│ Request Operations:                     │");
				System.out.println("│  1. Approve                             │");
				System.out.println("│  2. Decline                             │");
				System.out.println("│  3. Go back                             │");
				System.out.println("╰─────────────────────────────────────────╯");
				System.out.println();
				System.out.print("» Enter the number of operation: ");

				int requestOperation = input.nextInt();

				switch (requestOperation) {
				case 1:
					try {
						library.approveRequest(requestIndex);
						System.out.println("✔ Request has been approved sucessfully !");
					} catch(NoSuchElementException e) {
						System.out.println("✘ No requests to approve !");
					} catch(IllegalArgumentException e) {
						System.out.println("✘ Invalid request !");
					} catch(IndexOutOfBoundsException e) {
						System.out.println("✘ Library is full !");
					}

					break;
				case 2:
					try {
						library.denyRequest(requestIndex);
						System.out.println("✔ Request has been declined sucessfully !");
					} catch(NoSuchElementException e) {
						System.out.println("✘ No requests to decline !");
					} catch(IllegalArgumentException e) {
						System.out.println("✘ Invalid request !");
					}

					break;
				case 3:
					return;
				default:
					tryAgain = true;
					break;
				}
			} while (tryAgain);

			break;
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
			do {
				System.out.println("╭────────────────────────────────────────────╮");

				if (tryAgain) {
					System.out.println("│ ✘ Incorrect username, please try again !   │");
					System.out.println("│                                            │");

					tryAgain = false;
				}

				if (adminOperation == 2) {
					System.out.println("│ Add/Remove Balance:                        │");
				} else if (adminOperation == 3) {
					System.out.println("│ Upgrade User to Author:                    │");
				} else if (adminOperation == 4) {
					System.out.println("│ View User Statistics:                      │");
				} else if (adminOperation == 5) {
					System.out.println("│ Change User Password:                      │");
				} else {
					System.out.println("│ Delete User:                               │");
				}

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

				if (adminOperation == 2) {
					System.out.print("» Enter amount to add/remove (negative to remove, '0' to cancel): ");
					double balanceToAdd = input.nextDouble();

					if (balanceToAdd == 0) {
						break;
					}

					Account userAccount = accountManager.findAccount(username);

					if (userAccount instanceof User) {
						((User) userAccount).modifyBalance(balanceToAdd);

						System.out.printf("✔ '%s' balance has been modified by %.2f succesfully !%n", username,
								balanceToAdd);
					} else {
						System.out.println("✘ Account '" + username + "' is not a user !");
					}

					break;
				} else if (adminOperation == 3) {
					try {
						admin.upgradeUser(username);
						System.out.println("✔ User '" + username + "' has been upgraded successfully to author !");
					} catch (NoSuchElementException e) {
						tryAgain = true;
					} catch (IllegalArgumentException e) {
						System.out.println("✘ Account '" + username + "' is not a user !");
					}
				} else if (adminOperation == 4) {
					Account userAccount = accountManager.findAccount(username);

					if (userAccount instanceof User) {
						((User) userAccount).display();
					} else {
						System.out.println("✘ Account '" + username + "' is not a user !");
					}

					break;
				} else if (adminOperation == 5) {
					System.out.print("» Enter new password ('!cancel' to cancel): ");
					String newPassword = input.next();

					if (newPassword.equals("!cancel")) {
						break;
					}

					accountManager.findAccount(username).setPassword(newPassword);
					System.out.println("✔ '" + username + "' password has been updated sucessfully !");

					break;
				} else {
					Account accountToDelete = accountManager.findAccount(username);
					
					if (accountToDelete == null) {
						System.out.println("✘ Account '" + username + "' doesn't exist !");
					} else if (accountToDelete instanceof User) {
						if (accountManager.deleteAccount(username)) {
							System.out.println("✔ '" + username + "' account has been deleted sucessfully !");
						} else {
							System.out.println("✘ Account '" + username + "' is not a user !");
						}
					} else {
						System.out.println("✘ Account '" + username + "' is not a user !");
					}
				}
			} while (tryAgain);

			break;
		case 7:
			System.out.print("» Enter new book name (or '!cancel' to cancel): ");
			String newBookName = input.nextLine();

			if (newBookName.equals("!cancel")) {
				break;
			}

			System.out.print("» Enter new book author name (or '!cancel' to cancel): ");
			String newBookAuthorName = input.nextLine();

			if (newBookAuthorName.equals("!cancel")) {
				break;
			}

			System.out.print("» Enter new book pages (or '-1' to cancel): ");
			int newBookPages = input.nextInt();

			if (newBookPages == -1) {
				break;
			} else if (newBookPages <= 0) {
				System.out.println("✘ Invalid pages.");
				break;
			}

			System.out.print("» Enter new book publication year (or '-1' to cancel): ");
			int newBookPublicationYear = input.nextInt();

			if (newBookPublicationYear == -1) {
				break;
			} else if (newBookPublicationYear < 0) {
				System.out.println("✘ Invalid publication year.");
				break;
			}

			if (library.addItem(new Book(newBookPages, newBookName, newBookAuthorName, newBookPublicationYear))) {
				System.out.println("✔ Book has been added sucessfully to the library !");
			} else {
				System.out.println("✘ Library is full.");
			}

			break;
		case 8:
			if (library.getItems().isEmpty()) {
				System.out.println("✘ No items to remove.");
				return;
			}

			LinkedList<LibraryItem> libraryItems = searchItems(input, library);

			if (libraryItems == null) {
				return;
			}

			int deleteItemIndex = selectLibraryItemMenu(input, libraryItems, libraryItems.size(),
					"Select an item to delete");

			if (deleteItemIndex == -1) {
				return;
			}

			LibraryItem deleteItem = libraryItems.get(deleteItemIndex);
			int actualDeleteItemIndex = library.findIndex(deleteItem, 0);

			try {
				library.removeItem(actualDeleteItemIndex);
				System.out.println("✔ Item has been removed sucessfully from the library !");
			} catch (NoSuchElementException e) {
				System.out.println("✘ No items to remove.");
			} catch (IllegalArgumentException e) {
				System.out.println("✘ Invalid input.");
			} catch(UnavailableItemException e) {
				System.out.println("✘ This item is currently borrowed, please try again later.");
			}

			break;
		case 9:
			System.out.println("╭────────────────────────────────────────╮");
			System.out.println("│ Statistics:                            │");
			System.out.printf("│  Total Borrows: %-22d │%n", Admin.getTotalBorrows());
			System.out.printf("│  Total Returns: %-22d │%n", Admin.getTotalReturns());
			System.out.printf("│  Total Revenue: %-22.2f │%n", Admin.getTotalRevenue());
			System.out.println("╰────────────────────────────────────────╯");

			break;
		case 10:
			System.out.println("- Goodbye, " + admin.getUsername() + " !");
			accountManager.logout();

			break;
		default:
			System.out.println("✘ Invalid operation, please try again !");
			break;
		}
	}

	public static void showUserMenu(Scanner input, Library library, User user) {
		AccountManager accountManager = AccountManager.getInstance();

		boolean isAuthor = user instanceof Author;
		Author author = null;

		if (user instanceof Author) {
			author = (Author) user;
		}

		System.out.println();
		System.out.println("╭────────────────────────────────────────╮");
		System.out.println("│ User Operations:                       │");
		System.out.println("│  1. View all library items             │");
		System.out.println("│  2. Search for library items           │");
		System.out.println("│  3. View currently borrowed items      │");
		System.out.println("│  4. Statistics                         │");

		if (isAuthor) {
			System.out.println("│  5. Request manuscript showcase        │");
			System.out.println("│  6. Logout                             │");
		} else {
			System.out.println("│  5. Logout                             │");
		}

		System.out.println("╰────────────────────────────────────────╯");
		System.out.println();
		System.out.print("» Enter the number of operation: ");

		int userOperation = input.nextInt();
		input.nextLine(); // Used to allow next nextLine calls, this should return immediately

		boolean tryAgain = false;

		switch (userOperation) {
		case 1: // View all library books
		case 2: // Search for library items
			LinkedList<LibraryItem> libraryItems;
			int libraryItemsCount;

			if (userOperation == 1) {
				libraryItems = library.getItems();
				libraryItemsCount = libraryItems.size();
			} else {
				libraryItems = searchItems(input, library);

				if (libraryItems == null) {
					return;

				}

				libraryItemsCount = libraryItems.size();
			}

			boolean backToLibraryItems;

			do {
				backToLibraryItems = false;
				int itemIndex;

				String listName = "Library Items";

				if (userOperation == 2)
					listName = "Search Results";

				itemIndex = selectLibraryItemMenu(input, libraryItems, libraryItemsCount, listName);

				if (itemIndex == -1) {
					return;
				}

				LibraryItem selectedItem = libraryItems.get(itemIndex);

				if (!userItemOperationsMenu(input, selectedItem, user, listName)) {
					backToLibraryItems = true;
				}
			} while (backToLibraryItems);

			return;
		case 3: // View currently borrowed books
			do {
				tryAgain = false;

				LinkedList<LibraryItem> borrowedItems = user.getBorrowedItems();
				int itemsCount = borrowedItems.size();

				if (itemsCount == 0) {
					System.out.println("✘ You don't have any borrowed items to browse.");
					return;
				}

				int itemIndex = selectLibraryItemMenu(input, borrowedItems, itemsCount, "Currently Borrowed Items");

				if (itemIndex == -1) {
					return;
				}

				LibraryItem selectedItem = borrowedItems.get(itemIndex);

				if (!userItemOperationsMenu(input, selectedItem, user, "currently borrowed items")) {
					tryAgain = true;
				}
			} while (tryAgain);

			break;
		case 4: // Statistics
			user.display();
			break;
		case 5: // Request manuscript showcase (for authors) / logout (for users)
			if (!isAuthor) {
				System.out.println("- Goodbye, " + user.getUsername() + " !");
				accountManager.logout();

				break;
			}

			System.out.println();
			System.out.println("╭────────────────────────────────────────╮");
			System.out.println("│ Manuscript Showcase Request:           │");
			System.out.println("│  Name: [░░░░░░░░░░░░]                  │");
			System.out.println("│  Pages: [     ]                        │");
			System.out.println("│                                        │");
			System.out.println("│ Type '!back' to go back                │");
			System.out.println("╰────────────────────────────────────────╯");
			System.out.println();
			System.out.print("» Enter manuscript's name (or '!back'): ");

			String manuscriptName = input.nextLine();

			if (manuscriptName.equals("!back")) {
				return;
			}

			int manuscriptPages;

			do {
				System.out.println();
				System.out.println("╭────────────────────────────────────────╮");

				if (tryAgain) {
					System.out.println("│ ✘ Incorrect number of pages, please    │");
					System.out.println("│   try again.                           │");
					System.out.println("│                                        │");
				}

				System.out.println("│ Manuscript Showcase Request:           │");
				System.out.printf("│  Name: %-31s │%n", manuscriptName);
				System.out.println("│  Pages: [░░░░░]                        │");
				System.out.println("│                                        │");
				System.out.println("│ Type '-1' to go to operations menu     │");
				System.out.println("╰────────────────────────────────────────╯");
				System.out.println();
				System.out.print("» Enter manuscript's pages (or '-1'): ");

				manuscriptPages = input.nextInt();

				if (manuscriptPages == -1) {
					return;
				} else if (manuscriptPages <= 0) {
					tryAgain = true;
				}
			} while (tryAgain);

			if (author.submitManuscript(manuscriptPages, manuscriptName, library)) {
				System.out.println("✔ Your manuscript showcase request has been sent successfully !");
			} else {
				System.out.println("✘ Sorry, the library has currently too many manuscript showcase ");
				System.out.println("  requests, please try again later !");
			}

			break;
		case 6: // Logout (author only)
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

	private static int selectLibraryItemMenu(Scanner input, LinkedList<LibraryItem> libraryItems, int libraryItemsCount,
			String menuTitle) {

		boolean tryAgain = false;
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

			System.out.printf("│ %-42s │%n", menuTitle + ":");

			for (int i = 0; i < libraryItemsCount; i++) {
				LibraryItem item = libraryItems.get(i);
				int itemNumber = i + 1;

				System.out.printf("│ %-42s │%n", itemNumber + ". " + item.getName());
			}

			System.out.println("│                                            │");
			System.out.println("│  Type '-1' to go to opeartions menu.       │");
			System.out.println("╰────────────────────────────────────────────╯");
			System.out.println();
			System.out.print("» Enter the number of library item you want to open (or '-1'): ");

			int itemNumber = input.nextInt();

			if (itemNumber == -1) {
				return -1;
			}

			itemIndex = itemNumber - 1;

			if (itemIndex < 0 || itemIndex >= libraryItemsCount) {
				tryAgain = true;
			}
		} while (tryAgain);

		return itemIndex;
	}

	/**
	 * 
	 * 
	 * @param input
	 * @param item
	 * @param user
	 * @param listName optional
	 * @return true if user wants to go to user operations, otherwise false
	 */
	private static boolean userItemOperationsMenu(Scanner input, LibraryItem item, User user, String listName) {
		String errorMessageLine1 = null;
		String errorMessageLine2 = null;

		boolean askForOperations = false;

		User itemUsedBy = item.getUsedBy();
		boolean userHasItem = itemUsedBy != null && itemUsedBy.getUsername().equals(user.getUsername());

		do {
			askForOperations = false;

			System.out.println();
			item.display();

			System.out.println("╭────────────────────────────────────────────╮");

			if (errorMessageLine1 != null) {
				System.out.printf("│ ✘ %-40s │%n", errorMessageLine1);
				if (errorMessageLine2 != null)
					System.out.printf("│   %-40s │%n", errorMessageLine2);

				System.out.println("│                                            │");

				errorMessageLine1 = null;
				errorMessageLine2 = null;
			}

			System.out.println("│ Library Item Operations:                   │");

			if (userHasItem) {
				System.out.println("│  1. Return                                 │");
				System.out.println("│  2. Give a rating                          │");
			} else {
				System.out.println("│  1. Borrow                                 │");
			}

			if (item instanceof Manuscript)
				System.out.println("│  3. Show Comments                          │");

			if (listName != null) {
				System.out.printf("│  4. Back to %-30s │%n", listName);
			}

			System.out.println("│  5. Back to user operations                │");
			System.out.println("╰────────────────────────────────────────────╯");
			System.out.println();
			System.out.print("» Enter the operation number: ");

			int itemOperation = input.nextInt();

			switch (itemOperation) {
			case 1:
				if (userHasItem) {
					try {
						user.returnItem(item);
						System.out.println("✔ '" + item.getName() + "' has been returned successfully !");
					} catch(IllegalArgumentException e) {
						errorMessageLine1 = "Item is already avaliable.";
					} catch(NoSuchElementException e) {
						errorMessageLine1 = "Item doesn't belong to the library.";
						break;
					}
				} else {
					try {
						user.borrowItem(item);
						System.out.println("✔ '" + item.getName() + "' has been borrowed successfully !");
					} catch(IndexOutOfBoundsException e) {
						errorMessageLine1 = "No more than 5 items can be borrowed";
						errorMessageLine2 = "at the same time.";
					} catch(IllegalArgumentException e) {
						errorMessageLine1 = "Manuscripts can't be borrowed by users.";
					} catch(UnavailableItemException e) {
						errorMessageLine1 = "Item is not avaliable right now,";
						errorMessageLine2 = "please try later.";
					} catch(InsufficientBalanceException e) {
						errorMessageLine1 = "Insufficient balance.";
						break;
					}
				}

				break;
			case 2:
				if (!userHasItem) {
					errorMessageLine1 = "Invalid operation. please try again !";
					break;
				}

				while (true) {
					System.out.print("» Enter your rating from 1 to 5 (or '-1' to go back): ");
					int newRating = input.nextInt();

					if (newRating == -1) {
						break;
					}

					if (item.reviewItem(newRating)) {
						System.out.println("» This item has been rated at " + newRating + "/5 successfully !");
						break;
					}

					System.out.println("✘ Incorrect rating, please try again !");
				}

				break;
			case 3:
				if (!(item instanceof Manuscript)) {
					errorMessageLine1 = "Invalid operation. please try again !";
					break;
				}

				boolean isAuthor = user instanceof Author;
				boolean isUsedByAuthor = isAuthor && item.getUsedBy() != null
						&& item.getUsedBy().getUsername().equals(user.getUsername());

				boolean tryAgain = false;
				Manuscript manuscript = (Manuscript) item;

				int commentIndex;

				while (true) {
					LinkedList<Comment> comments = manuscript.getComments();
					int commentsCount = comments.size();

					System.out.println();
					System.out.println("╭────────────────────────────────────────────╮");

					if (tryAgain) {
						System.out.println("│ ✘ Incorrect comment number, please enter   │");
						System.out.println("│   a correct one.                           │");
						System.out.println("│                                            │");
						tryAgain = false;
					}

					System.out.printf("│ %-42s │%n", "Comments:");

					if (commentsCount == 0) {
						System.out.println("│  No comments so far...                     │");
					}

					for (int i = 0; i < commentsCount; i++) {
						Comment comment = comments.get(i);
						int commentNumber = i + 1;

						System.out.printf("│ %-42s │%n", commentNumber + ". A comment by " + comment.getCommenter());
					}

					System.out.println("│                                            │");
					System.out.println("│  Type '-1' to go to back.                  │");

					if (isUsedByAuthor) {
						System.out.println("│  Type '-2' to add comments.                │");
					}

					System.out.println("╰────────────────────────────────────────────╯");
					System.out.println();

					if (isUsedByAuthor) {
						System.out.print("» Enter the number of comment you want to see (or '-1' / '-2'): ");
					} else {
						System.out.print("» Enter the number of comment you want to see (or '-1'): ");
					}

					int itemNumber = input.nextInt();
					input.nextLine();

					if (isUsedByAuthor && itemNumber == -2) {
						System.out.print("» Enter your new comment (or '!back'): ");

						String newComment = input.nextLine();

						if (newComment.equals("!back")) {
							askForOperations = true;
							break;
						}

						switch (manuscript.addComment(newComment)) {
						case 1:
							System.out.println("✔ Your comment has been added sucessfully !");
							break;
						case -1:
							System.out.println("✘ Sorry, there are too many comments on this item !");
							break;
						case -2:
							System.out.println("✘ Sorry, your comment exceeds character limit !");
							break;
						default:
							System.out.println("! Something went wrong, please contact library adminstrators.");
							break;
						}

						continue;
					}

					if (itemNumber == -1) {
						askForOperations = true;
						break;
					}

					commentIndex = itemNumber - 1;

					if (commentIndex < 0 || commentIndex >= commentsCount) {
						tryAgain = true;
					} else {
						comments.get(commentIndex).display();
					}
				}

				if (askForOperations) {
					break;
				}

				break;
			case 4:
				if (listName != null)
					return false;
			case 5:
				break;
			default:
				errorMessageLine1 = "Invalid operation. please try again !";
				break;
			}
		} while (errorMessageLine1 != null || askForOperations);

		return true;
	}

	private static LinkedList<LibraryItem> searchItems(Scanner input, Library library) {
		boolean tryAgain = false;
		LinkedList<LibraryItem> libraryItems = null;

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

			String searchPrompt = input.nextLine();

			if (searchPrompt.equals("!back")) {
				return null;
			}

			libraryItems = library.searchItem(searchPrompt);

			if (libraryItems.isEmpty()) {
				tryAgain = true;
			}
		} while (tryAgain);

		return libraryItems;
	}
}
