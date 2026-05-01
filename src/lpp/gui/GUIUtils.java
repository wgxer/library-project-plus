package lpp.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import lpp.account.Author;
import lpp.account.User;
import lpp.gui.handler.BorrowButtonHandler;
import lpp.gui.handler.CommentsButtonHandler;
import lpp.gui.handler.RateButtonHandler;
import lpp.gui.handler.ReturnButtonHandler;
import lpp.item.Book;
import lpp.item.LibraryItem;
import lpp.item.Manuscript;

public class GUIUtils {
	public static JLabel labelWithFont(String text, Font font) {
		JLabel label = new JLabel(text);
		label.setFont(font);
		
		return label;
	}
	
	public static JTextField textField() {
		return textField(null);
	}

	public static JTextField textField(String defaultValue) {
		JTextField textField = new JTextField(defaultValue);
		textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, textField.getPreferredSize().height));
		
		return textField;
	}

	public static JButton buttonAsLink(String text) {
		JButton button = new JButton(text);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setForeground(Color.BLUE);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button.setBorder(BorderFactory.createEmptyBorder());
		
		return button;
	}
	
	public static JPanel fieldWithLabel(String fieldName, String defaultValue) {
		JPanel fieldPanel = new JPanel();

		JLabel fieldLabel = new JLabel(fieldName);
		JTextField textField = new JTextField(defaultValue);

		textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, textField.getPreferredSize().height));

		fieldPanel.add(fieldLabel);
		fieldPanel.add(textField);

		fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.X_AXIS));
		fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		return fieldPanel;
	}
	
	public static JPanel libraryItem(LibraryItem item, User user, JFrame frame, IUpdateable updateable) {
		Container contentPane = frame.getContentPane();
		JButton[] buttons = {};
		
		if (item != null) {
			User itemUsedBy = item.getUsedBy();
			
			if (itemUsedBy != null && itemUsedBy.getUsername().equals(user.getUsername())) {
				JButton returnButton = new JButton("Return");
				returnButton.addActionListener(new ReturnButtonHandler(user, item, contentPane, updateable));
				
				JButton rateButton = new JButton("Rate");
				rateButton.addActionListener(new RateButtonHandler(item, contentPane, updateable));
				
				buttons = new JButton[] {
					returnButton, 
					rateButton
				};
			} else {
				JButton borrowButton = new JButton("Borrow");
				borrowButton.addActionListener(new BorrowButtonHandler(user, item, contentPane, updateable));
				
				buttons = new JButton[] {
					borrowButton
				};
			}
			
			if (item instanceof Manuscript) {
				JButton[] newButtons = new JButton[buttons.length + 1];
				
				for (int i = 0; i < buttons.length; i++) {
					newButtons[i] = buttons[i];
				}
				
				JButton commentsButton = new JButton("Comments");
				commentsButton.addActionListener(new CommentsButtonHandler(frame, user, (Manuscript) item));
				
				newButtons[newButtons.length - 1] = commentsButton;
				buttons = newButtons;
			}
		}
		
		Component buttonsContainer = ViewFactory.horizontal().gap(12).build(buttons);
		return libraryItem(item, buttonsContainer, contentPane, updateable);
	}
	
	public static JPanel libraryItem(LibraryItem item, Component buttonsContainer, Container contentPane, IUpdateable updateable) {
		Font nameFont = new Font(Font.SANS_SERIF, Font.BOLD, 16);
		Font textFont = new Font(Font.SANS_SERIF, 0, 14);
		
		String itemName = "no selection";
		String itemAuthorName = "none";
		String itemPages = "--";
		
		String avaliablity = "--";
		String borrowingFee = "--";
		String borrowedTimes = "-- time(s)";
		
		if (item != null) {
			itemName = item.getName();
			itemAuthorName = item.getAuthorName();
			itemPages = String.valueOf(item.getPages());

			if (item.isAvailable()) {
				avaliablity = "avaliable";
			} else {
				avaliablity = "not avaliable";
			}
			
			borrowingFee = String.format("%.2f $", item.calculatePrice());
			borrowedTimes = item.getTimesUsed() + " time(s)";
		}
		
		String yearText = "";
		
		if (item != null && item instanceof Book) {
			yearText = "  -  " + ((Book) item).getPublcationYear();
		}
		
		JLabel pagesLabel = new JLabel(" Pages: " + itemPages);
		pagesLabel.setFont(textFont);
		
		String ratingText = "not rated";
		
		if (item != null && item.getReviewsCount() > 0) {
			ratingText = String.format("%.2f out of 5 (%d)", item.getReviews(), item.getReviewsCount());
		}
		
		
		if (buttonsContainer == null) {
			// A button that is used to get the height of it to keep layout from changing when we don't have anything selected
			JButton referenceButton = new JButton("Invisible");
			referenceButton.setVisible(false);
			
			buttonsContainer = Box.createVerticalStrut(referenceButton.getPreferredSize().height);
		}
		
		return ViewFactory.vertical().fillWidth().gap(10).padding(10).roundedBorder(2).build(
			ViewFactory.vertical().build(
				GUIUtils.labelWithFont(itemName, nameFont),
				GUIUtils.labelWithFont(" by: " + itemAuthorName + yearText, textFont)
			),
			ViewFactory.vertical().build(
				GUIUtils.labelWithFont(" Pages: " + itemPages, textFont),
				GUIUtils.labelWithFont(" Rating: " + ratingText, textFont)
			), 
			ViewFactory.horizontal().alignY(Component.BOTTOM_ALIGNMENT).build(
				ViewFactory.vertical().build(
					GUIUtils.labelWithFont(" Avaliablity: " + avaliablity, textFont),
					GUIUtils.labelWithFont(" Borrowing fees: " + borrowingFee, textFont),
					GUIUtils.labelWithFont(" Borrowed: " + borrowedTimes, textFont)
				),
				ViewFactory.vertical().fillWidth().alignX(Component.RIGHT_ALIGNMENT).build(buttonsContainer)
			)
		);
	}
	
	public static JPanel displayUser(User user, Component buttonsContainer, Container contentPane, IUpdateable updateable) {
		Font nameFont = new Font(Font.SANS_SERIF, Font.BOLD, 16);
		Font textFont = new Font(Font.SANS_SERIF, 0, 14);
		
		String username = "no selection";
		String balance = "--.- $";
		String feesIncurred = "--.- $";
		
		String borrows = "-- time(s)";
		String returns = "-- time(s)";
		
		if (user != null) {
			username = user.getUsername();
			balance = String.format("%.2f $", user.getBalance());
			feesIncurred = String.format("%.2f $", user.getFees());
			
			borrows = String.format("%d item(s)", user.getBorrows());
			returns = String.format("%d item(s)", user.getReturns());
		}
		
		if (buttonsContainer == null) {
			// A button that is used to get the height of it to keep layout from changing when we don't have anything selected
			JButton referenceButton = new JButton("Invisible");
			referenceButton.setVisible(false);
			
			buttonsContainer = Box.createVerticalStrut(referenceButton.getPreferredSize().height);
		}
		
		JLabel manuscriptsShowcasedLabel = new JLabel(" ");
		manuscriptsShowcasedLabel.setFont(textFont);
		
		String role = "User";
		
		if (user instanceof Author) {
			manuscriptsShowcasedLabel.setText(String.format("Manuscript showcased: %d manuscript(s)", ((Author) user).getManuscriptsShowcased()));
			role = "Author";
		}
		
		
		return ViewFactory.vertical().fillWidth().gap(10).padding(10).roundedBorder(2).build(
			ViewFactory.vertical().build(
				GUIUtils.labelWithFont(username, nameFont)
			),
			ViewFactory.horizontal().alignY(Component.BOTTOM_ALIGNMENT).build(
				ViewFactory.vertical().build(
					GUIUtils.labelWithFont("Role: " + role, textFont),
					GUIUtils.labelWithFont("Balance: " + balance, textFont),
					GUIUtils.labelWithFont("Fees incurred: " + feesIncurred, textFont),
					GUIUtils.labelWithFont("Items borrowed in the past: " + borrows, textFont),
					GUIUtils.labelWithFont("Items returned in the past: " + returns, textFont),
					manuscriptsShowcasedLabel
				),
				ViewFactory.vertical().fillWidth().alignX(Component.RIGHT_ALIGNMENT).build(buttonsContainer)
			)
		);
	}
	
	public static int showNumberDialog(Container contentPane, String title, String questionMessage, String errorMessage, boolean nonNegativeOnly) {
		int result = -1;
		
		while (result == -1) {
			String manuscriptPagesInput = JOptionPane.showInputDialog(contentPane, questionMessage, title, JOptionPane.QUESTION_MESSAGE);
			if (manuscriptPagesInput == null) return -1;
			
			try {
				int newNumber = Integer.parseInt(manuscriptPagesInput);
				
				if (!nonNegativeOnly || newNumber >= 0) {
					result = newNumber;
					break;
				}
			} catch (NumberFormatException exception) {}
			
			JOptionPane.showMessageDialog(contentPane, errorMessage, title, JOptionPane.ERROR_MESSAGE);
		}
		
		return result;
	}
}
