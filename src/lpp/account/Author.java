package lpp.account;

public class Author extends User {
	
	private int publishedBooks;
	private int feedbacksGiven;
	
	
	public Author(String username, String password, double balance) {
		super(username, password, balance);
		publishedBooks= 0;
		feedbacksGiven= 0;
	}
	
	// publish a book, and then send request to administator to process
	public void publishBook(int pages, String name) {
     
	}
}
