package main.undo;

import gui.Board;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import main.common.Entry;

public class MoveUndoAction extends AbstractUndoableEdit{

	private Board b;
	private Entry from;
	private Entry to;
	private Entry fromParent;
	
	public MoveUndoAction(Board b, Entry from, Entry to, Entry fromParent){
		this.b = b;
		this.from = from;
		this.to = to;
		this.fromParent = fromParent;
	}
	
	@Override
	public void undo() throws CannotUndoException {
		TreeActions.move(b, from, fromParent);
	}
	
	@Override
	public void redo() throws CannotRedoException {
		TreeActions.move(b, from, to);
	}
	
}
