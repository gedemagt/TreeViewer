package main.undo;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

public class UndoRedoManager {

	private static UndoManager manager = new UndoManager();
	private static List<Runnable> listeners = new ArrayList<Runnable>();


	public static void registerListener(Runnable r) {
		listeners.add(r);
	}

	public static void addEdit(UndoableEdit edit) {
		manager.addEdit(edit);
	}

	public static void undo() {
		try{
			manager.undo();
		} catch(CannotUndoException e) {
			e.printStackTrace();
		}
		for(Runnable r : listeners) {
			r.run();
		}
	}

	public static void redo() {
		try{
			manager.redo();
		} catch(CannotRedoException e) {
			e.printStackTrace();
		}
		for(Runnable r : listeners) {
			r.run();
		}
	}

	public static Action getUndoAction() {

		return new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				undo();

			}
		};
	}

	public static Action getRedoAction() {

		return new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				redo();

			}
		};
	}

}
