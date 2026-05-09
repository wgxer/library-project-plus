package lpp.gui.frame;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import lpp.Library;
import lpp.LinkedList;
import lpp.account.Author;
import lpp.account.User;
import lpp.gui.GUIUtils;
import lpp.gui.IUpdateable;
import lpp.gui.LibraryItemListEntry;
import lpp.gui.ViewFactory;
import lpp.gui.handler.RequestShowcaseButtonHandler;
import lpp.gui.handler.ShowParentCloseHandler;
import lpp.item.LibraryItem;

public class BrowseLibraryFrame extends JFrame implements IUpdateable, ActionListener, ListSelectionListener {
	private Library library;
	private User user;
	
	private JLabel balanceLabel;
	private JTextField searchField;
	
	private Container libraryItemContainer;
	private JScrollPane libraryItemsScrollPane;
	private JList<LibraryItemListEntry> libraryItemsList;
	
	private LibraryItem selectedLibraryItem;
	private LibraryItemListEntry[] libraryItemsListEntries;

	public BrowseLibraryFrame(Frame parent, Library library, User user) {
		this.library = library;
		this.user = user;
		
		setTitle("Library Management System - Browse Library");
		
		if (parent != null) {
			addWindowListener(new ShowParentCloseHandler(parent));
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		} else {
			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		}
		
		LinkedList<LibraryItem> libraryItems = library.getItems();
		libraryItemsListEntries = new LibraryItemListEntry[libraryItems.size()];
		
		for (int i = 0; i < libraryItemsListEntries.length; i++) {
			libraryItemsListEntries[i] = new LibraryItemListEntry(libraryItems.get(i));
		}
		
		Container contentPane = getContentPane();
		
		balanceLabel = new JLabel();
		balanceLabel.setFont(new Font(Font.SANS_SERIF, 0, 16));

		libraryItemsScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		libraryItemsScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		
		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(this);
	
		searchField = GUIUtils.textField();
		searchField.setMaximumSize(new Dimension(searchField.getMaximumSize().width, searchButton.getMaximumSize().height));
		
		libraryItemContainer = ViewFactory.vertical().build();
		
		JButton requestManuscriptShowcaseButton = new JButton("Request Manuscript Showcase");
		
		if (user instanceof Author) {			
			requestManuscriptShowcaseButton.addActionListener(new RequestShowcaseButtonHandler((Author) user, library, contentPane, this));
		} else {
			requestManuscriptShowcaseButton.setVisible(false);
		}
		
		contentPane.add(
			ViewFactory.vertical().gap(10).padding(10).fillWidth().fillHeight().build(
				balanceLabel,
				libraryItemContainer,
				ViewFactory.horizontal().alignY(Component.CENTER_ALIGNMENT).gap(10).build(
					new JLabel("Search: "),
					searchField,
					searchButton,
					requestManuscriptShowcaseButton
				),
				libraryItemsScrollPane
			)
		);

		setSize(800, 600);
		update();
	}

	public void update() {
		int previousSelectedIndex = -1;
		int previousScrollY = 0;
		
		if (libraryItemsList != null) {
			previousSelectedIndex = libraryItemsList.getSelectedIndex();
			previousScrollY = libraryItemsScrollPane.getVerticalScrollBar().getValue();
		}
		
		balanceLabel.setText(String.format("Balance: %.2f $", user.getBalance()));
		
		libraryItemContainer.removeAll();
		libraryItemContainer.add(GUIUtils.libraryItem(selectedLibraryItem, user, this, this));
		
		libraryItemsList = new JList<LibraryItemListEntry>(libraryItemsListEntries);
		libraryItemsList.setSelectedIndex(previousSelectedIndex);
		libraryItemsList.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		libraryItemsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		libraryItemsList.addListSelectionListener(this);
		
		libraryItemsScrollPane.setViewportView(libraryItemsList);
		libraryItemsScrollPane.getVerticalScrollBar().setValue(previousScrollY);
		
		revalidate();
		repaint();
	}
	
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		
		if (action.equals("Search")) {
			String searchEntry = searchField.getText();
			
			LinkedList<LibraryItem> libraryItems = library.searchItem(searchEntry);
			libraryItemsListEntries = new LibraryItemListEntry[libraryItems.size()];
			
			for (int i = 0; i < libraryItems.size(); i++) {
				libraryItemsListEntries[i] = new LibraryItemListEntry(libraryItems.get(i));
			}
			
			update();
		}
	}
	
	public void valueChanged(ListSelectionEvent e) {
		int index = libraryItemsList.getSelectedIndex();
		
		if (index != -1) {
			selectedLibraryItem = libraryItemsListEntries[index].getLibraryItem();
		} else {
			selectedLibraryItem = null;
		}
		
		update();
	}
}
