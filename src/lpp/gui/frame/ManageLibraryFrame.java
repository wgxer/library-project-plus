package lpp.gui.frame;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.NoSuchElementException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import lpp.Library;
import lpp.LinkedList;
import lpp.gui.GUIUtils;
import lpp.gui.IUpdateable;
import lpp.gui.LibraryItemListEntry;
import lpp.gui.ViewFactory;
import lpp.gui.handler.ShowParentCloseHandler;
import lpp.item.Book;
import lpp.item.LibraryItem;
import lpp.item.UnavailableItemException;

public class ManageLibraryFrame extends JFrame implements IUpdateable, ActionListener, ListSelectionListener {
	private Library library;
	
	private JTextField searchField;
	private Container libraryItemContainer;
	private JScrollPane libraryItemsScrollPane;
	private JList<LibraryItemListEntry> libraryItemsList;
	
	private LibraryItem selectedLibraryItem;
	private LibraryItemListEntry[] libraryItemsListEntries;

	public ManageLibraryFrame(Frame parent, Library library) {
		this.library = library;
		setTitle("Library Management System - Manage Library");
		
		if (parent != null) {
			addWindowListener(new ShowParentCloseHandler(parent));
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		} else {
			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		}
		
		refreshLibraryItems();
		Container contentPane = getContentPane();

		libraryItemsScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		libraryItemsScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		
		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(this);
	
		searchField = GUIUtils.textField();
		searchField.setMaximumSize(new Dimension(searchField.getMaximumSize().width, searchButton.getMaximumSize().height));
		
		libraryItemContainer = ViewFactory.vertical().build();
		
		JButton addBookButton = new JButton("Add Book");
		addBookButton.addActionListener(this);
		
		contentPane.add(
			ViewFactory.vertical().gap(10).padding(10).fillWidth().fillHeight().build(
				libraryItemContainer,
				ViewFactory.horizontal().alignY(Component.CENTER_ALIGNMENT).gap(10).build(
					new JLabel("Search: "),
					searchField,
					searchButton,
					addBookButton
				),
				libraryItemsScrollPane
			)
		);

		setSize(800, 600);
		update();
	}

	public void update() {
		Container contentPane = getContentPane();
		
		int previousSelectedIndex = -1;
		int previousScrollY = 0;
		
		if (libraryItemsList != null) {
			previousSelectedIndex = libraryItemsList.getSelectedIndex();
			previousScrollY = libraryItemsScrollPane.getVerticalScrollBar().getValue();
		}
		
		JButton deleteItemButton = null;
		
		if (selectedLibraryItem != null && selectedLibraryItem.isAvailable()) {
			deleteItemButton = new JButton("Delete");
			deleteItemButton.addActionListener(this);
		}
		
		libraryItemContainer.removeAll();
		libraryItemContainer.add(GUIUtils.libraryItem(selectedLibraryItem, deleteItemButton, contentPane, this));
		
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
	
	private void refreshLibraryItems() {
		LinkedList<LibraryItem> libraryItems = library.getItems();
		libraryItemsListEntries = new LibraryItemListEntry[libraryItems.size()];
		
		for (int i = 0; i < libraryItemsListEntries.length; i++) {
			libraryItemsListEntries[i] = new LibraryItemListEntry(libraryItems.get(i));
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		Container contentPane = getContentPane();
		String action = e.getActionCommand();
		
		if (action.equals("Search")) {
			String searchEntry = searchField.getText();
			
			LinkedList<LibraryItem> libraryItems = library.searchItem(searchEntry);
			int libraryItemsCount = libraryItems.size();
			
			libraryItemsListEntries = new LibraryItemListEntry[libraryItemsCount];
			
			for (int i = 0; i < libraryItemsCount; i++) {
				libraryItemsListEntries[i] = new LibraryItemListEntry(libraryItems.get(i));
			}
			
			update();
		} else if (action.equals("Add Book")) {
			String title = "Add Book";
			
			String bookName = JOptionPane.showInputDialog(contentPane, "Enter new book name", title, JOptionPane.QUESTION_MESSAGE);
			if (bookName == null) return;
			
			String bookAuthorName = JOptionPane.showInputDialog(contentPane, "Enter new book author name", title, JOptionPane.QUESTION_MESSAGE);
			if (bookAuthorName == null) return;
			
			int bookPages = GUIUtils.showNumberDialog(contentPane, title, "Enter new book pages", "Invalid number of pages, please try again.", true);
			if (bookPages == -1) return;
			
			int bookPublicationYear = GUIUtils.showNumberDialog(contentPane, title, "Enter new book publication year", "Invalid publication year, please try again.", true);
			if (bookPublicationYear == -1) return;
			
			if (library.addItem(new Book(bookPages, bookName, bookAuthorName, bookPublicationYear))) {
				JOptionPane.showMessageDialog(contentPane, "Book has been added sucessfully.", "Add Book", JOptionPane.INFORMATION_MESSAGE);
				
				refreshLibraryItems();
				update();
			} else {
				JOptionPane.showMessageDialog(contentPane, "Library is full.", "Add Book failed !", JOptionPane.ERROR_MESSAGE);
			}
		} else if(action.equals("Delete")) {
			int selectedOption = JOptionPane.showConfirmDialog(
				contentPane, 
				"Are you sure you want to delete this item?\n"
					+ " " + selectedLibraryItem.getName() + "\n"
					+ " by " + selectedLibraryItem.getAuthorName(),
				"Delete Item Confirmation",
				JOptionPane.YES_NO_OPTION
			);
			
			if (selectedOption == JOptionPane.YES_OPTION) {
				try {
					library.removeItem(library.findIndex(selectedLibraryItem, 0));

					selectedLibraryItem = null;
					libraryItemsList.setSelectedIndices(new int[] {});

					refreshLibraryItems();
					update();
				} catch(NoSuchElementException exception) {
					JOptionPane.showMessageDialog(contentPane, "No items to remove.", "Item Deletion failed !", JOptionPane.ERROR_MESSAGE);
				} catch(IllegalArgumentException exception) {
					JOptionPane.showMessageDialog(contentPane, "Invalid item.", "Item Deletion failed !", JOptionPane.ERROR_MESSAGE);
				} catch(UnavailableItemException exception) {
					JOptionPane.showMessageDialog(contentPane, "This item can't be deleted because it's currently borrowed.", "Item Deletion failed !", JOptionPane.ERROR_MESSAGE);
				}
			}
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
