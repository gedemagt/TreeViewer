package main.undo;

import gui.Board;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import com.gaurav.tree.Tree;

import main.common.Entry;

public class DeleteRedoAction extends AbstractUndoableEdit {
	
	private Entry parent;
	private Tree<Entry> subtree;
	private Board b;
	
	public DeleteRedoAction(Board b, Tree<Entry> subtree, Entry parent){
		this.b = b;
		this.parent = parent;
		this.subtree = subtree;
	}
	
	@Override
	public void undo() throws CannotUndoException {
		b.getTree().addSubTree(subtree, parent);
	}
	
	@Override
	public void redo() throws CannotRedoException {
		TreeActions.delete(b, subtree.root());
	}

}
