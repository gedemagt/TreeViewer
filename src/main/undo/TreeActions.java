package main.undo;

import gui.Board;

import javax.swing.undo.UndoableEdit;

import main.common.Entry;
import main.tree.TreeStructure;

public class TreeActions {

	public static UndoableEdit add(Board b, Entry parent, Entry newEntry) {
		b.getTree().addChild(parent, newEntry);
		b.revalidate();
		return new AddRedoAction(b, parent, newEntry);
	}
	
	public static UndoableEdit edit(Board b, String newTitle, String newDetails, Entry e) {
		String oldDetails = e.getDetails();
		String oldTitle = e.getTitle();
		e.setDetails(newDetails);
		e.setTitle(newTitle);
		b.revalidate();
		return new EditUndo(b, e, oldTitle, oldDetails);
	}
	
	public static UndoableEdit delete(Board b, Entry e) {
		Entry parent = b.getTree().getParent(e);
		TreeStructure<Entry> subtree = b.getTree().getSubTree(e);
		b.getTree().remove(e);
		b.revalidate();
		return new DeleteRedoAction(b, subtree, parent);
	}
	
	public static UndoableEdit move(Board b, Entry from, Entry to) {
		Entry parent = b.getTree().getParent(from);
		TreeStructure<Entry> subtree = b.getTree().getSubTree(from);
		b.getTree().addSubTree(to, subtree);
		b.revalidate();
		return new MoveUndoAction(b, from, to, parent);
	}
	
}
