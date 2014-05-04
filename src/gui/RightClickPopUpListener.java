package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import main.common.Entry;
import main.undo.TreeActions;
import main.undo.UndoRedoManager;

public class RightClickPopUpListener implements MouseListener {

	private Board b;
	private int popup_x;
	private int popup_y;
	private JPopupMenu popup;
	private JPopupMenu popup2;
	private Entry entry;
	
	public RightClickPopUpListener(Board b) {
		this.b = b;
		setupPopup();
		setupSecondPopup();
	}

	private void setupSecondPopup() {
		popup2 = new JPopupMenu();
		JMenuItem add = new JMenuItem("Add root");
		popup2.add(add);
		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				Entry new_e = new Entry();
				int i = 100;
				if(new_e != null) i = showEditPopup(new_e, 0);
				if(i == JOptionPane.OK_OPTION) {
					UndoRedoManager.addEdit(TreeActions.add(b, b.getTree().root(), new_e));
					b.revalidate();
				}
			}
		});
		
	}

	private void setupPopup() {
		popup = new JPopupMenu();
		JMenuItem add = new JMenuItem("Add");
		popup.add(add);
		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				Entry new_e = null;
				Entry e = b.getEntryAt(popup_x, popup_y);
				if(e == null) return;
				new_e = new Entry();
				int i = 100;
				if(new_e != null) i = showEditPopup(new_e, 0);
				if(i == JOptionPane.OK_OPTION) {
					UndoRedoManager.addEdit(TreeActions.add(b, e, new_e));
					b.revalidate();
				}
			}
		});

		JMenuItem edit = new JMenuItem("Edit");
		edit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Entry e = b.getEntryAt(popup_x, popup_y);
				if(e == null) return;
				showEditPopup(e, 1);

				b.repaint();
			}
		});
		popup.add(edit);
		JMenuItem delete = new JMenuItem("Delete");
		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Entry e = b.getEntryAt(popup_x, popup_y);
				if(e == null) return;
				UndoRedoManager.addEdit(TreeActions.delete(b, e));
				b.revalidate();
			}
		});
		popup.add(delete);

		JMenuItem expand = new JMenuItem("Expand");
		expand.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				Entry entry = b.getEntryAt(popup_x, popup_y);
				if(entry != null) entry.collapsed(false);
				b.revalidate();

			}
		});
		popup.add(expand);

		JMenuItem collapse = new JMenuItem("Collapse");
		collapse.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Entry entry = b.getEntryAt(popup_x, popup_y);
				if(entry != null && !b.getTree().isLeaf(entry)) entry.collapsed(true);
				b.revalidate();
			}
		});
		popup.add(collapse);
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		if(SwingUtilities.isRightMouseButton(arg0)) {
			popup_x = arg0.getX();
			popup_y = arg0.getY();
			entry = b.getEntryAt(popup_x, popup_y);
			if(entry != null) popup.show(arg0.getComponent(), popup_x, popup_y);
			else popup2.show(arg0.getComponent(), popup_x, popup_y);
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private int showEditPopup(Entry e, int add_edit) {
		JTextArea details = new JTextArea(e.getDetails());
		details.setLineWrap(true);
		details.setWrapStyleWord(true);
		details.setRows(20);
		JTextField title = new JTextField(e.getTitle());
		JPanel p = new JPanel();
		p.add(new JLabel("Title"));
		p.add(title);
		p.add(new JLabel("Details"));
		p.add(new JScrollPane(details));
		JComponent[] inputs = new JComponent[] {
				new JLabel("Title"),
				title,
				new JLabel("Details"),
				new JScrollPane(details)
		};
		int i = JOptionPane.showConfirmDialog(null, inputs, "asds", JOptionPane.OK_CANCEL_OPTION);
		if(i == JOptionPane.OK_OPTION) {
			if(add_edit == 1) {
				UndoRedoManager.addEdit(TreeActions.edit(b, title.getText(), details.getText(), e));
			}
			else {
				e.setDetails(details.getText());
				e.setTitle(title.getText());
			}
		}
		return i;
	}
	
}
