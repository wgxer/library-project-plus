package lpp.gui.frame;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
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

import lpp.LinkedList;
import lpp.account.Author;
import lpp.account.User;
import lpp.gui.GUIUtils;
import lpp.gui.IUpdateable;
import lpp.gui.ViewFactory;
import lpp.gui.handler.ShowParentCloseHandler;
import lpp.item.Comment;
import lpp.item.Manuscript;

public class CommentsFrame extends JFrame implements IUpdateable, ActionListener {
	private User user;
	private Manuscript manuscript;
	
	private JTextField newCommentField;
	private Container commentsContainer;
	private JScrollPane commentsScrollPane;

	public CommentsFrame(Frame parent, User user, Manuscript manuscript) {
		this.user = user;
		this.manuscript = manuscript;
		
		setTitle("Library Management System - Manuscript Comments");
		
		if (parent != null) {
			addWindowListener(new ShowParentCloseHandler(parent));
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		} else {
			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		}
		
		Container contentPane = getContentPane();
		
		commentsScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		commentsScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		commentsScrollPane.getVerticalScrollBar().setValue(Integer.MAX_VALUE);
		
		JButton addCommentButton = new JButton("Add Comment");
		addCommentButton.addActionListener(this);
	
		newCommentField = new JTextField();
		newCommentField.setMaximumSize(new Dimension(newCommentField.getMaximumSize().width, addCommentButton.getMaximumSize().height));
		
		JPanel addCommentContainer = null;
		
		if (manuscript.getUsedBy() != null && manuscript.getUsedBy().getUsername().equals(user.getUsername()) && user instanceof Author) {
			addCommentContainer = ViewFactory.vertical().gap(10).build(
				new JLabel("Add Comment: "),
				newCommentField,
				addCommentButton
			);
		}
		
		contentPane.add(
			ViewFactory.vertical().gap(10).padding(10).fillWidth().fillHeight().build(
				new JLabel("Comments of manuscript: " + manuscript.getName() + " - by: " + manuscript.getAuthorName()),
				commentsScrollPane,
				addCommentContainer
			)
		);

		setSize(800, 600);
		update();
	}

	public void update() {
		int previousScrollY = commentsScrollPane.getVerticalScrollBar().getValue();
		
		if (commentsContainer == null) {
			previousScrollY = Integer.MAX_VALUE;
		}
		
		LinkedList<Comment> comments = manuscript.getComments();
		JPanel[] commentPanels = new JPanel[comments.size()];
		
		Font nameFont = new Font(Font.SANS_SERIF, Font.BOLD, 16);
		Font textFont = new Font(Font.SANS_SERIF, 0, 14);
		
		for (int i = 0; i < commentPanels.length; i++) {
			Comment comment = comments.get(i);
			
			commentPanels[i] = ViewFactory.vertical().gap(10).padding(10).roundedBorder(3).fillWidth().build(
				GUIUtils.labelWithFont(comment.getCommenter(), nameFont),
				GUIUtils.labelWithFont(comment.getBody(), textFont)	
			);
		}
		
		commentsContainer = ViewFactory.vertical().gap(10).build(commentPanels);
		
		commentsScrollPane.setViewportView(commentsContainer);
		
		if (previousScrollY == Integer.MAX_VALUE) {
			commentsScrollPane.getVerticalScrollBar().setValue(commentsScrollPane.getVerticalScrollBar().getMaximum());
		} else {
			commentsScrollPane.getVerticalScrollBar().setValue(previousScrollY);
		}
		
		revalidate();
		repaint();
	}
	
	public void actionPerformed(ActionEvent e) {
		Container contentPane = getContentPane();
		String action = e.getActionCommand();
		
		if (action.equals("Add Comment")) {
			String commentBody = newCommentField.getText();
			User manuscriptUsedBy = manuscript.getUsedBy();
			
			if (!(manuscriptUsedBy != null && manuscriptUsedBy.getUsername().equals(user.getUsername()) && user instanceof Author)) {
				JOptionPane.showMessageDialog(contentPane, "Sorry, this item need to be borrowed to add comments to it.", "Adding Comment failed !", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			switch(manuscript.addComment(commentBody)) {
			case 1:
				commentsContainer = null; // Force scroll pane to go down
				update();
				break;
			case -1:
				JOptionPane.showMessageDialog(contentPane, "Sorry, there are too many comments on this item.", "Adding Comment failed !", JOptionPane.ERROR_MESSAGE);
				break;
			case -2:
				JOptionPane.showMessageDialog(contentPane, "Sorry, your comment exceeds character limit.", "Adding Comment failed !", JOptionPane.ERROR_MESSAGE);
				break;
			default:
				break;
			}
		}
	}
}
