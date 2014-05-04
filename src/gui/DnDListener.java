package gui;

import gui.painter.Painter;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import main.common.Entry;
import main.tree.TreeStructure;
import main.undo.TreeActions;
import main.undo.UndoRedoManager;

public class DnDListener extends MouseAdapter {

	private Board b;
	private Entry current;
	private Painter p;
	private JPanel glassPanel;
	private boolean startedDragging = true;
	private Cursor defaultCursor;

	public DnDListener(Board b) {
		this.b = b;
		p = b.getPainter();
		glassPanel = new JPanel();
		glassPanel.setOpaque(false);
		b.setGlassPane(glassPanel);
		defaultCursor = glassPanel.getCursor();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		int x = arg0.getX();
		int y = arg0.getY();
		current = b.getEntryAt(x, y);
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if(current != null) {
			startedDragging = false;
			if(!startedDragging) glassPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			int x = arg0.getX();
			int y = arg0.getY();
			Entry e = b.getEntryAt(x, y);
			if(acceptEndEntry(e)) {
				p.highlightAll(null, false);
				p.highlight(e, true);
			}
			else p.highlightAll(null, false);
			b.repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		int x = arg0.getX();
		int y = arg0.getY();
		Entry e = b.getEntryAt(x, y);
		if(acceptEndEntry(e)) {
			UndoRedoManager.addEdit(TreeActions.move(b, current, e));
			b.repaint();
		}
		
		p.highlightAll(null, false);
		startedDragging = true;
		glassPanel.setCursor(defaultCursor);
		b.repaint();
	}
	
	private TreeStructure<Entry> getTree() {
		return b.getTree();
	}
	
	private boolean acceptEndEntry(Entry e) {
		if(e == null) return false;
		if(e == current) return false;
		if(isEntryInSubTree(current, e)) return false;
		if(e.equals(b.getTree().getParent(current))) return false;
		return true;
	}
	
	private boolean isEntryInSubTree(Entry parent, Entry e) {
		for(Entry entry : getTree().getChildren(parent)) {
			if(entry.equals(e)) return true;
			if(!getTree().isLeaf(entry)) {
				boolean b = isEntryInSubTree(entry, e);
				if(b) return true;
			}
		}
		return false;
	}

}
