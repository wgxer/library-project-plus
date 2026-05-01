package lpp.gui.handler;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import lpp.Library;
import lpp.gui.IUpdateable;

public class ApproveRequestButtonHandler implements ActionListener {
	private Library library;
	private int requestIndex;

	private Container contentPane;
	private IUpdateable updateable;
	
	public ApproveRequestButtonHandler(Library library, int requestIndex, Container contentPane, IUpdateable updateable) {
		this.library = library;
		this.requestIndex = requestIndex;
		this.contentPane = contentPane;
		this.updateable = updateable;
	}
	
	public void actionPerformed(ActionEvent e) {
		switch(library.approveRequest(requestIndex)) {
		case 1:
			if (updateable != null) {
				updateable.update();
			}
			
			break;
		case -1:
			JOptionPane.showMessageDialog(
				contentPane, 
				"No requests to approve.", 
				"Request Approval failed !", 
				JOptionPane.ERROR_MESSAGE
			);
			
			break;
		case -2:
			JOptionPane.showMessageDialog(
				contentPane, 
				"Invalid request.", 
				"Request Approval failed !", 
				JOptionPane.ERROR_MESSAGE
			);
			
			break;
		case -3:
			JOptionPane.showMessageDialog(
				contentPane, 
				"Library is full.", 
				"Request Approval failed !", 
				JOptionPane.ERROR_MESSAGE
			);
			
			break;
		default:
			break;
		}
	}
}
