package lpp.item;

public class Book extends LibraryItem {
	
	private int publcationYear;

	public Book(int pages,String name,String aoutherName, int publcationYear) {
		super(pages,name,aoutherName);
		this.publcationYear = publcationYear;
	}
	
	public Book(Book b) {
		super(b);
		this.publcationYear= b.publcationYear;
	}
	
	public LibraryItem copyItem() {
		return new Book(this);
	}

	
	public void display() {
		System.out.println("╭──────────────────────────────────────────────────╮");
		System.out.println("│                 Book Information                 │");
		System.out.println("├──────────────────────────────────────────────────┤");
		System.out.printf ("│ %-14s : %-30s  │\n", "Name", name);
		System.out.printf ("│ %-14s : %-30s  │\n", "Author", authorName);
		System.out.printf ("│ %-14s : %-30d  │\n", "Pages", pages);
		System.out.printf ("│ %-14s : %-30d  │\n", "Year", publcationYear);
		System.out.printf ("│ %-14s : %-30.2f  │\n", "Borrowing fee", calculatePrice());
		System.out.printf ("│ %-14s : %-30.2f  │\n", "Rating", reviews);
		System.out.printf ("│ %-14s : %-30d  │\n", "Times borrowed", timesUsed);
		System.out.println("╰──────────────────────────────────────────────────╯");
	}

	
	public double calculatePrice() {
		
		return (getPages() / 15) ;
	}


	public int getPublcationYear() {
		return publcationYear;
	}
}
