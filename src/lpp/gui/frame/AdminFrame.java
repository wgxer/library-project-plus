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
import lpp.account.Admin;
import lpp.gui.GUIUtils;
import lpp.gui.IUpdateable;
import lpp.gui.ViewFactory;
import lpp.gui.handler.ApproveRequestButtonHandler;
import lpp.gui.handler.DenyRequestButtonHandler;
import lpp.gui.handler.ShowParentCloseHandler;
import lpp.item.LibraryItem;

public class AdminFrame extends JFrame implements IUpdateable, ActionListener {
	private Library library;
	private Admin admin;

	public AdminFrame(Frame parent, Library library, Admin admin) {
		this.library = library;
		this.admin = admin;
		
		setTitle("Library Management System - Admin Operations");
		
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
		
		LibraryItem[] requests = library.getRequests();
		int requestsCount = library.getRequestsCount();

		JPanel[] requestPanels = new JPanel[requestsCount];
		
		for (int i = 0; i < requestsCount; i++) {
			LibraryItem request = requests[i];
			
			JButton approveButton = new JButton("Approve");
			approveButton.addActionListener(new ApproveRequestButtonHandler(library, i, contentPane, this));
			
			JButton denyButton = new JButton("Deny");
			denyButton.addActionListener(new DenyRequestButtonHandler(library, i, contentPane, this));
			
			Container buttonsContainer = ViewFactory.horizontal().gap(10).build(
				approveButton, denyButton
			);
			
			requestPanels[i] = GUIUtils.libraryItem(request, buttonsContainer, contentPane, this);
		}
		
		Font welcomeFont = new Font(Font.SANS_SERIF, 0, 24);
		JLabel welcomeLabel = new JLabel("Welcome, " + admin.getUsername() + " !");
		welcomeLabel.setFont(welcomeFont);
		

		JButton manageUsersButton = new JButton("Manage Users");
		manageUsersButton.addActionListener(this);
		
		JButton manageLibraryButton = new JButton("Manage Library");
		manageLibraryButton.addActionListener(this);

		contentPane.removeAll();
		contentPane.add(ViewFactory.vertical().padding(20).gap(10).build(
				welcomeLabel,
				ViewFactory.horizontal().alignY(Component.BOTTOM_ALIGNMENT).build(
					ViewFactory.vertical().fillWidth().padding(10, 0).build(
						new JLabel(String.format("Total Borrows: %d borrows", Admin.getTotalBorrows())),
						new JLabel(String.format("Total Returns: %d returns", Admin.getTotalReturns())),
						new JLabel(String.format("Total Revenue: %.2f $", Admin.getTotalRevenue()))
					),
					ViewFactory.vertical().fillWidth().alignX(Component.RIGHT_ALIGNMENT).gap(10).build(
						manageUsersButton,
						manageLibraryButton
					)
				),
				ViewFactory.vertical().padding(10, 0).build(
					new JLabel("Showcase Manuscript Requests (" + library.getRequestsCount() + "):"),
					new JScrollPane(
						ViewFactory.vertical().padding(0, 0, 0, 10).gap(12).build(requestPanels), 
						JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
						JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
					)
				)
		));
		
		contentPane.revalidate();
		contentPane.repaint();
	}
	
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		
		if (action.equals("Manage Library")) {
			setVisible(false);
			
			ManageLibraryFrame manageLibraryFrame = new ManageLibraryFrame(this, library);
			manageLibraryFrame.setLocation(getLocation());
			manageLibraryFrame.setVisible(true);
		} else if (action.equals("Manage Users")) {
			setVisible(false);
			
			ManageUsersFrame manageUsersFrame = new ManageUsersFrame(this, admin);
			manageUsersFrame.setLocation(getLocation());
			manageUsersFrame.setVisible(true);
		}
	}
}
