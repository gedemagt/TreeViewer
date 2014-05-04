package gui;

import gui.painter.Painter;

import java.awt.event.MouseAdapter;

import javax.swing.JPanel;

import main.common.Entry;
import main.tree.TreeStructure;

public interface Board {

	public Entry getEntryAt(int x, int y);
	
	public void setTree(TreeStructure<Entry> tree);
	
	public TreeStructure<Entry> getTree();
	
	public void updateTree();
	
	public void registerListener(MouseAdapter m);
	
	public void setGlassPane(JPanel jp);
	
	public Painter getPainter();
	
	public void setPainter(Painter p);
	
	public void repaint();
	
}
