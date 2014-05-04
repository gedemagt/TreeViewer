package gui;

import gui.painter.AbegoPainter;
import gui.painter.Painter;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.OverlayLayout;

import org.abego.treelayout.TreeLayout;

import main.common.Entry;
import main.tree.GauravTree;
import main.tree.TreeStructure;
import main.undo.TreeActions;
import main.undo.UndoRedoManager;

@SuppressWarnings("serial")
public class BoardImpl extends JLayeredPane implements Board{

	private TreeStructure<Entry> tree;
	private JPanel glassPane;


	/**
	 * Specifies the tree to be displayed by passing in a {@link TreeLayout} for
	 * that tree.
	 * 
	 * @param treeLayout
	 */
	public BoardImpl(GauravTree tree) {
		this.tree = tree;

		setLayout(new OverlayLayout(this));
		setOpaque(false);
		glassPane = new JPanel();
		add(glassPane,Integer.valueOf(1));

		this.p = new AbegoPainter(this);

		Dimension size = p.getSize();
		setPreferredSize(size);
		registerPopupMenu();

		UndoRedoManager.registerListener(new Runnable() {

			@Override
			public void run() {
				updateTree();

			}
		});
	}

	// -------------------------------------------------------------------
	// painting


	private void paintEdges(Graphics g, Entry parent) {
		if (!parent.iscollapsed()) {
			p.paintChildEdges(g, parent);
			for (Entry child : getTree().getChildren(parent)) {
				paintEdges(g, child);
			}
		}
	}

	private void paintBox(Graphics g, Entry e) {
		p.paintEntry(g, e);
		if(!e.iscollapsed()) {
			for (Entry child : tree.getChildren(e)){
				paintBox(g, child);
			}
		}
	}

	private Painter p;
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		paintEdges(g, getTree().root());
		paintBox(g, getTree().root());

	}

	@Override
	public void setTree(TreeStructure<Entry> tree) {
		this.tree = tree;
		updateTree();
	}

	private void registerPopupMenu() {
		final JPopupMenu popup = new JPopupMenu();
		JMenuItem add = new JMenuItem("Add");
		popup.add(add);
		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				Entry new_e = null;
				Entry e = getEntryAt(popup_x, popup_y);
				if(e == null) return;
				new_e = new Entry();
				int i = 100;
				if(new_e != null) i = showEditPopup(new_e, 0);
				if(i == JOptionPane.OK_OPTION) {
					UndoRedoManager.addEdit(TreeActions.add(BoardImpl.this, e, new_e));
					updateTree();
					repaint();
				}
			}
		});

		JMenuItem edit = new JMenuItem("Edit");
		edit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Entry e = getEntryAt(popup_x, popup_y);
				if(e == null) return;
				showEditPopup(e, 1);


				repaint();
			}
		});
		popup.add(edit);
		JMenuItem delete = new JMenuItem("Delete");
		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Entry e = getEntryAt(popup_x, popup_y);
				if(e == null) return;
				UndoRedoManager.addEdit(TreeActions.delete(BoardImpl.this, e));
				updateTree();
				repaint();
			}
		});
		popup.add(delete);

		JMenuItem expand = new JMenuItem("Expand");
		expand.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				Entry entry = getEntryAt(popup_x, popup_y);
				if(entry != null) entry.collapsed(false);
				repaint();

			}
		});
		popup.add(expand);

		JMenuItem collapse = new JMenuItem("Collapse");
		collapse.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Entry entry = getEntryAt(popup_x, popup_y);
				if(entry != null && !tree.isLeaf(entry)) entry.collapsed(true);
				repaint();
			}
		});
		popup.add(collapse);
		
		setComponentPopupMenu(popup);
		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				popup_x = e.getX();
				popup_y = e.getY();
				if(e.getClickCount() == 2) {
					Entry entry = getEntryAt(e.getX(), e.getY());
					if(entry == null) return;
					entry.setShowDetails(!entry.isShowDetails());
					updateTree();
				}

			}
		});
	}

	private int popup_x;
	private int popup_y;


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
				UndoRedoManager.addEdit(TreeActions.edit(this, title.getText(), details.getText(), e));
			}
			else {
				e.setDetails(details.getText());
				e.setTitle(title.getText());
			}
		}
		return i;
	}

	@Override
	public Entry getEntryAt(int x, int y) {
		return p.getEntryAt(x, y);
	}

	@Override
	public void updateTree() {
		p.revalidate();
		setPreferredSize(p.getSize());
		revalidate();
		repaint();
	}

	@Override
	public void registerListener(MouseAdapter m) {
		addMouseListener(m);
		addMouseMotionListener(m);
	}


	@Override
	public Painter getPainter() {
		return p;
	}

	@Override
	public void setGlassPane(JPanel jp) {
		remove(glassPane);
		glassPane = jp;
		add(jp,1);
	}

	@Override
	public TreeStructure<Entry> getTree() {
		return tree;
	}

	@Override
	public void setPainter(Painter p) {
		this.p = p;
		p.revalidate();
		repaint();
	}
	
}
