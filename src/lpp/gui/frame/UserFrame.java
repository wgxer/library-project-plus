package lpp.gui.frame;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import lpp.AccountManager;
import lpp.account.User;
import lpp.gui.IUpdateable;
import lpp.gui.ViewFactory;
import lpp.gui.handler.RateButtonHandler;
import lpp.gui.handler.ReturnButtonHandler;
import lpp.item.Book;
import lpp.item.LibraryItem;

public class UserFrame extends JFrame implements IUpdateable {
	private User user;

	public UserFrame(User user) {
		this.user = user;
		
		setTitle("Library Management System - User Operations");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		setSize(800, 600);
		update();
	}

	public void update() {
		Container contentPane = getContentPane();
		
		LibraryItem[] borrowedItems = user.getBorrowedItems();
		int borrowedItemsCount = user.getItemsCount();

		JPanel[] borrowedItemPanels = new JPanel[borrowedItemsCount];
		
		for (int i = 0; i < borrowedItemsCount; i++) {
			Font nameFont = new Font(Font.SANS_SERIF, Font.BOLD, 16);
			Font textFont = new Font(Font.SANS_SERIF, 0, 14);

			LibraryItem item = borrowedItems[i];
			
			JLabel nameLabel = new JLabel(item.getName());
			nameLabel.setFont(nameFont);
			
			String yearText = "";
			
			if (item instanceof Book) {
				yearText = "  -  " + ((Book) item).getPublcationYear();
			}
			
			JLabel authorLabel = new JLabel(" by: " + item.getAuthorName() + yearText);
			authorLabel.setFont(textFont);
			
			JLabel pagesLabel = new JLabel(" Pages: " + item.getPages());
			pagesLabel.setFont(textFont);
			
			String ratingText = "not rated";
			
			if (item.getReviewsCount() > 0) {
				ratingText = String.format("%.2f out of 5 (%d)", item.getReviews(), item.getReviewsCount());
			}
			
			JLabel ratingLabel = new JLabel(" Rating: " + ratingText);
			ratingLabel.setFont(textFont);
			
			JButton returnButton = new JButton("Return");
			returnButton.addActionListener(new ReturnButtonHandler(user, item, contentPane, this));
			
			JButton rateButton = new JButton("Rate");
			rateButton.addActionListener(new RateButtonHandler(item, contentPane, this));
			
			borrowedItemPanels[i] = ViewFactory.vertical().fillWidth().gap(10).padding(10).roundedBorder(2).build(
				ViewFactory.vertical().build(
					nameLabel,
					authorLabel
				),
				ViewFactory.vertical().build(
					pagesLabel,
					ratingLabel,
					ViewFactory.vertical().fillWidth().alignX(Component.RIGHT_ALIGNMENT).build(
						ViewFactory.horizontal().gap(12).build(returnButton, rateButton)
					)
				)
			);
		}
		
		Font welcomeFont = new Font(Font.SANS_SERIF, 0, 24);
		JLabel welcomeLabel = new JLabel("Welcome, " + user.getUsername() + " !");
		welcomeLabel.setFont(welcomeFont);

		contentPane.removeAll();
		contentPane.add(ViewFactory.vertical().padding(20).gap(10).build(welcomeLabel,
				ViewFactory.vertical().padding(10, 0).build(
					new JLabel(String.format("Balance: %.2f$", user.getBalance())),
					new JLabel(String.format("Items borrowed in the past: %d items", user.getBorrows())),
					new JLabel(String.format("Items returned in the past: %d items", user.getReturns()))
				),
				ViewFactory.vertical().padding(10, 0).build(
						new JLabel("Borrowed Items (" + user.getItemsCount() + "):"),
						new JScrollPane(ViewFactory.vertical().padding(0, 0, 0, 10).gap(12).build(borrowedItemPanels), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)
					)
				)
		);
		
		contentPane.revalidate();
		contentPane.repaint();
	}
}
