package main.undo;

import gui.Board;

import javax.swing.undo.UndoableEdit;

import main.common.Entry;

import com.gaurav.tree.Tree;


public class TreeActions {

	public static UndoableEdit add(Board b, Entry parent, Entry newEntry) {
		b.getTree().addChild(parent, newEntry);
		return new AddRedoAction(b, parent, newEntry);
	}
	
	public static UndoableEdit edit(Board b, String newTitle, String newDetails, Entry e) {
		String oldDetails = e.getDetails();
		String oldTitle = e.getTitle();
		e.setDetails(newDetails);
		e.setTitle(newTitle);
		
		return new EditUndo(b, e, oldTitle, oldDetails);
	}
	
	public static UndoableEdit delete(Board b, Entry e) {
		Entry parent = b.getTree().getParent(e);
		Tree<Entry> subtree = b.getTree().getSubTree(e);
		b.getTree().removeElement(e);
		
		return new DeleteRedoAction(b, subtree, parent);
	}
	
	public static UndoableEdit move(Board b, Entry from, Entry to) {
		Entry parent = b.getTree().getParent(from);
		Tree<Entry> subtree = b.getTree().getSubTree(from);
		b.getTree().addSubTree(subtree, to);
		return new MoveUndoAction(b, from, to, parent);
	}
	
}
