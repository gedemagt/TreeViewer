package gui.painter;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;

import main.common.Entry;

public interface Painter {

	public void paintTree(Graphics g);
	
	public void highlight(Entry e, boolean shouldHighligh);
	
	public void highlightAll(List<Entry> e, boolean shouldHighligh);
	
	public void revalidate();
	
	public Dimension getSize();
	
	public Entry getEntryAt(int x, int y);
	
}
