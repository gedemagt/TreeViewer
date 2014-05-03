package main.undo;

import gui.Board;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import main.common.Entry;
import main.tree.TreeStructure;

public class DeleteRedoAction extends AbstractUndoableEdit {
	
	private Entry parent;
	private TreeStructure<Entry> subtree;
	private Board b;
	
	public DeleteRedoAction(Board b, TreeStructure<Entry> subtree, Entry parent){
		this.b = b;
		this.parent = parent;
		this.subtree = subtree;
	}
	
	@Override
	public void undo() throws CannotUndoException {
		b.getTree().addSubTree(parent, subtree);
	}
	
	@Override
	public void redo() throws CannotRedoException {
		TreeActions.delete(b, subtree.root());
	}

}
