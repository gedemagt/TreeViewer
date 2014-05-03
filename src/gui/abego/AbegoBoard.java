package gui.abego;

import gui.Board;
import gui.painter.DefaultPainter;
import gui.painter.Painter;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

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
import main.tree.MyTree;
import main.undo.TreeActions;
import main.undo.UndoRedoManager;

@SuppressWarnings("serial")
public class AbegoBoard extends JLayeredPane implements Board{

	private TreeLayout<Entry> treeLayout;
	private MyTree tree;
	private JPanel glassPane;

	public MyTree getTree() {
		return tree;
	}

	private Iterable<Entry> getChildren(Entry parent) {
		return tree.getChildren(parent);
	}

	/**
	 * Specifies the tree to be displayed by passing in a {@link TreeLayout} for
	 * that tree.
	 * 
	 * @param treeLayout
	 */
	public AbegoBoard(MyTree tree, TreeLayout<Entry> treeLayout) {
		this.tree = tree;
		setLayout(new OverlayLayout(this));
		setOpaque(false);
		glassPane = new JPanel();
		glassPane.add(new JLabel("erik"));
		add(glassPane,Integer.valueOf(1));

		((EntryNodeExtentProvider) treeLayout.getNodeExtentProvider()).setFontMetric(getFontMetrics(glassPane.getFont()));
		this.treeLayout = treeLayout;
		p = new DefaultPainter(this);

		Dimension size = treeLayout.getBounds().getBounds().getSize();
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
			p.paintEdges(g, parent);
			for (Entry child : getTree().getChildren(parent)) {
				paintEdges(g, child);
			}
		}
	}

	private void paintBox(Graphics g, Entry e) {
		Rectangle2D.Double r = getTreeLayout().getNodeBounds().get(e);
		p.paintBox(g, e, (int) r.x, (int) r.y);
		if(!e.iscollapsed()) {
			for (Entry child : getChildren(e)){
				paintBox(g, child);
			}
		}
	}

	private Painter p;
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		paintEdges(g, getTree().getRoot());
		paintBox(g, getTree().getRoot());

	}

	public void setTree(MyTree tree) {
		this.tree = tree;
		treeLayout = new TreeLayout<Entry>(tree, treeLayout.getNodeExtentProvider(), treeLayout.getConfiguration());
		setPreferredSize(new Dimension((int) treeLayout.getBounds().getWidth(), (int) treeLayout.getBounds().getHeight()));
	}

	private void registerPopupMenu() {
		final JPopupMenu popup = new JPopupMenu();
		JMenuItem add = new JMenuItem("Add");
		popup.add(add);
		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				Entry new_e = null;
				Entry e = getEntry(popup_x, popup_y);
				if(e == null) return;
				new_e = new Entry();
				int i = 100;
				if(new_e != null) i = showEditPopup(new_e, 0);
				if(i == JOptionPane.OK_OPTION) {
					//					((DefaultTreeForTreeLayout<Entry>) getTree()).addChild(e, new_e);
					UndoRedoManager.addEdit(TreeActions.add(AbegoBoard.this, e, new_e));
					treeLayout = new TreeLayout<Entry>(getTree(), treeLayout.getNodeExtentProvider(), treeLayout.getConfiguration());


					setPreferredSize(new Dimension((int) treeLayout.getBounds().getWidth(), (int) treeLayout.getBounds().getHeight()));
					getParent().revalidate();
					repaint();
				}
			}
		});

		JMenuItem edit = new JMenuItem("Edit");
		edit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Entry e = getEntry(popup_x, popup_y);
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
				Entry e = getEntry(popup_x, popup_y);
				if(e == null) return;
				UndoRedoManager.addEdit(TreeActions.delete(AbegoBoard.this, e));
				
				treeLayout = new TreeLayout<Entry>(getTree(), treeLayout.getNodeExtentProvider(), treeLayout.getConfiguration());

				repaint();
			}
		});
		popup.add(delete);

		JMenuItem expand = new JMenuItem("Expand");
		expand.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				Entry entry = getEntry(popup_x, popup_y);
				if(entry != null) entry.collapsed(false);
				repaint();

			}
		});
		popup.add(expand);

		JMenuItem collapse = new JMenuItem("Collapse");
		collapse.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Entry entry = getEntry(popup_x, popup_y);
				if(entry != null && !treeLayout.getTree().isLeaf(entry)) entry.collapsed(true);
				repaint();
			}
		});
		popup.add(collapse);

//		JMenuItem undo = new JMenuItem("Undo");
//		undo.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				UndoRedoManager.undo();
//				updateTree();
//			}
//		});
//		popup.add(undo);
//
//		JMenuItem redo = new JMenuItem("Redo");
//		redo.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				UndoRedoManager.redo();
//				updateTree();
//			}
//		});
//		popup.add(redo);
		setComponentPopupMenu(popup);
		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
				popup_x = e.getX();
				popup_y = e.getY();
				if(e.getClickCount() == 2) {
					Entry entry = getEntry(e.getX(), e.getY());
					if(entry == null) return;
					entry.setShowDetails(!entry.isShowDetails());
					treeLayout = new TreeLayout<Entry>(getTree(), treeLayout.getNodeExtentProvider(), treeLayout.getConfiguration());
					repaint();
				}

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseClicked(MouseEvent e) {

			}
		});
	}

	private int popup_x;
	private int popup_y;

	private Entry getEntry(int x, int y) {
		for(Entry e : treeLayout.getNodeBounds().keySet()) {

			Rectangle2D.Double r = treeLayout.getNodeBounds().get(e);

			if(r.contains((double) x, (double) y)) {
				return e;
			}
		}
		return null;
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
		return getEntry(x, y);
	}

	@Override
	public void updateTree() {
		treeLayout = new TreeLayout<Entry>(getTree(), treeLayout.getNodeExtentProvider(), treeLayout.getConfiguration());
		setPreferredSize(new Dimension((int) treeLayout.getBounds().getWidth(), (int) treeLayout.getBounds().getHeight()));
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
	public TreeLayout<Entry> getTreeLayout() {
		return treeLayout;
	}

	@Override
	public void setGlassPane(JPanel jp) {
		remove(glassPane);
		glassPane = jp;
		add(jp,1);

	}


}
