package gui;

import gui.painter.Painter;

import java.awt.event.MouseAdapter;

import javax.swing.JPanel;

import org.abego.treelayout.TreeLayout;

import main.common.Entry;
import main.tree.MyTree;

public interface Board {

	public Entry getEntryAt(int x, int y);
	
	public void setTree(MyTree tree);
	
	public MyTree getTree();
	
	public TreeLayout<Entry> getTreeLayout();
	
	public void updateTree();
	
	public void registerListener(MouseAdapter m);
	
	public void setGlassPane(JPanel jp);
	
	public Painter getPainter();
	
	public void repaint();
	
}
