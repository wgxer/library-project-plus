package lpp.gui.handler;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import lpp.account.InsufficientBalanceException;
import lpp.account.User;
import lpp.gui.IUpdateable;
import lpp.item.LibraryItem;
import lpp.item.UnavailableItemException;

public class BorrowButtonHandler implements ActionListener {
	private User user;
	private LibraryItem libraryItem;
	private Container contentPane;
	private IUpdateable updateable;
	
	public BorrowButtonHandler(User user, LibraryItem libraryItem, Container contentPane, IUpdateable updateable) {
		this.user = user;
		this.libraryItem = libraryItem;
		this.contentPane = contentPane;
		this.updateable = updateable;
	}
	
	public void actionPerformed(ActionEvent e) {
		try {
			user.borrowItem(libraryItem);

			if (updateable != null) {
				updateable.update();
			}
			
		} catch(IllegalStateException exception) {
			JOptionPane.showMessageDialog(
				contentPane, 
				"No more than 5 items can be borrowed at the same time.", 
				"Borrow failed !", 
				JOptionPane.ERROR_MESSAGE
			);
			
		} catch(IllegalArgumentException exception) {
			JOptionPane.showMessageDialog(
				contentPane, 
				"Manuscripts can't be borrowed by users.", 
				"Borrow failed !", 
				JOptionPane.ERROR_MESSAGE
			);
		} catch(UnavailableItemException exception) {
			JOptionPane.showMessageDialog(
				contentPane, 
				"Item is not avaliable right now, please try later.", 
				"Borrow failed !", 
				JOptionPane.ERROR_MESSAGE
			);
			
		} catch(InsufficientBalanceException exception) {
			JOptionPane.showMessageDialog(
				contentPane, 
				"Insufficient balance.", 
				"Borrow failed !", 
				JOptionPane.ERROR_MESSAGE
			);
		}
	}
}
