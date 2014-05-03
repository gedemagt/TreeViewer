package main.undo;

import gui.Board;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import main.common.Entry;

public class EditUndo extends AbstractUndoableEdit {
	
	private Entry entry;
	private String oldTitle;
	private String newTitle;
	private String oldDetails;
	private String newDetails;
	private Board b;
	
	public EditUndo(Board b, Entry entry, String oldTitle, String oldDetails){
		this.b = b;
		this.entry = entry;
		this.oldDetails = oldDetails;
		this.oldTitle = oldTitle;
		this.newDetails = entry.getDetails();
		this.newTitle = entry.getTitle();
	}
	
	@Override
	public void undo() throws CannotUndoException {
		TreeActions.edit(b, oldTitle, oldDetails, entry);
	}
	
	@Override
	public void redo() throws CannotRedoException {
		TreeActions.edit(b, newTitle, newDetails, entry);
	}

}
