package gui;

import gui.abego.AbegoBoard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.common.Constants;
import main.common.Entry;
import main.common.SaveLoad;
import main.tree.TreeStructure;
import main.undo.UndoRedoManager;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {

	private JMenuItem menu_load,menu_save_as, undo, redo;
	private AbegoBoard panel;
	private SaveLoad sl;

	public MenuBar(AbegoBoard panel) {
		this.panel = panel;
		sl = new SaveLoad();
		menu_save_as = new JMenuItem("Save As");
		menu_load = new JMenuItem("Load");
		undo = new JMenuItem("Undo");
		redo = new JMenuItem("Redo");
		add(menu_save_as);
		add(menu_load);
		add(undo);
		add(redo);

		menu_save_as.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveAs();

			}
		});

		menu_load.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				load();

			}
		});

		undo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				UndoRedoManager.undo();

			}
		});

		redo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				UndoRedoManager.redo();

			}
		});




		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK),
				"undo");
		getActionMap().put("undo",
				UndoRedoManager.getUndoAction());
		
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK),
				"redo");
		getActionMap().put("redo",
				UndoRedoManager.getRedoAction());

	}

	private void load() {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Tree files",Constants.suffix.replace(".", ""));
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(filter);
		int returnVal = fc.showOpenDialog(panel);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			try {
				TreeStructure<Entry> tree = sl.load(file);
				panel.setTree(tree);
				panel.repaint();
				panel.revalidate();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	private void saveAs() {

		FileNameExtensionFilter filter = new FileNameExtensionFilter("Tree files", Constants.suffix.replace(".", ""));
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(filter);
		int returnVal = fc.showSaveDialog(panel);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			String path = file.getAbsolutePath();
			if(!file.getAbsolutePath().endsWith(Constants.suffix)) path += Constants.suffix;
			try{
				sl.save(panel.getTree(), new File(path));
			} catch(IOException e) {
				e.printStackTrace();
			}

		}



	}

}
