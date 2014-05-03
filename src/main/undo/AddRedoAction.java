package main.undo;

import gui.Board;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import main.common.Entry;

public class AddRedoAction extends AbstractUndoableEdit {
	
	private Board b;
	private Entry parent;
	private Entry add;
	
	public AddRedoAction(Board b, Entry parent, Entry add){
		this.b = b;
		this.parent = parent;
		this.add = add;
	}
	
	@Override
	public void undo() throws CannotUndoException {
		TreeActions.delete(b, add);
	}
	
	@Override
	public void redo() throws CannotRedoException {
		TreeActions.add(b, parent, add);
	}

}
