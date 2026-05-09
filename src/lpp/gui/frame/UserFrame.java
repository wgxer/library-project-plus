package lpp.gui.frame;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import lpp.Library;
import lpp.LinkedList;
import lpp.account.Author;
import lpp.account.User;
import lpp.gui.GUIUtils;
import lpp.gui.IUpdateable;
import lpp.gui.ViewFactory;
import lpp.gui.handler.ShowParentCloseHandler;
import lpp.item.LibraryItem;

public class UserFrame extends JFrame implements IUpdateable, ActionListener {
	private Library library;
	private User user;

	public UserFrame(Frame parent, Library library, User user) {
		this.library = library;
		this.user = user;
		
		setTitle("Library Management System - User Operations");
		
		if (parent != null) {
			addWindowListener(new ShowParentCloseHandler(parent, library));
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		} else {
			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		}

		setSize(800, 600);
		update();
	}

	public void update() {
		Container contentPane = getContentPane();
		
		LinkedList<LibraryItem> borrowedItems = user.getBorrowedItems();
		int borrowedItemsCount = user.getItemsCount();

		JPanel[] borrowedItemPanels = new JPanel[borrowedItemsCount];
		
		for (int i = 0; i < borrowedItemsCount; i++) {
			borrowedItemPanels[i] = GUIUtils.libraryItem(borrowedItems.get(i), user, this, this);
		}
		
		Font welcomeFont = new Font(Font.SANS_SERIF, 0, 24);
		JLabel welcomeLabel = new JLabel("Welcome, " + user.getUsername() + " !");
		welcomeLabel.setFont(welcomeFont);
		
		JButton browseLibraryButton = new JButton("Browse Library");
		browseLibraryButton.addActionListener(this);
		
		JLabel manuscriptsShowcasedLabel = null;
		
		if (user instanceof Author) {
			manuscriptsShowcasedLabel = new JLabel(String.format("Manuscript showcased: %d manuscript(s)", ((Author) user).getManuscriptsShowcased()));
		}

		contentPane.removeAll();
		contentPane.add(ViewFactory.vertical().padding(20).gap(10).build(welcomeLabel,
				ViewFactory.horizontal().alignY(Component.BOTTOM_ALIGNMENT).build(
					ViewFactory.vertical().fillWidth().padding(10, 0).build(
						new JLabel(String.format("Balance: %.2f $", user.getBalance())),
						new JLabel(String.format("Fees incurred: %.2f $", user.getFees())),
						new JLabel(String.format("Items borrowed in the past: %d items", user.getBorrows())),
						new JLabel(String.format("Items returned in the past: %d items", user.getReturns())),
						manuscriptsShowcasedLabel
						
					),
					ViewFactory.vertical().fillWidth().alignX(Component.RIGHT_ALIGNMENT).build(
						browseLibraryButton
					)
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
	
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		
		if (action.equals("Browse Library")) {
			setVisible(false);
			
			BrowseLibraryFrame browseLibraryFrame = new BrowseLibraryFrame(this, library, user);
			browseLibraryFrame.setLocation(getLocation());
			browseLibraryFrame.setVisible(true);
		}
	}
}
