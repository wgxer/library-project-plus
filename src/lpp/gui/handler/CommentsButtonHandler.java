package lpp.gui.handler;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import lpp.account.User;
import lpp.gui.frame.CommentsFrame;
import lpp.item.Manuscript;

public class CommentsButtonHandler implements ActionListener {
	private Manuscript manuscript;
	private User user;
	private Frame parent;

	public CommentsButtonHandler(Frame parent, User user, Manuscript manuscript) {
		this.parent = parent;
		this.user = user;
		this.manuscript = manuscript;
	}

	public void actionPerformed(ActionEvent e) {
		parent.setVisible(false);
		
		CommentsFrame commentsFrame = new CommentsFrame(parent, user, manuscript);
		commentsFrame.setLocation(parent.getLocation());
		commentsFrame.setVisible(true);
	}
}
