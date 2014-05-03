package gui.painter;

import java.awt.Graphics;
import java.util.List;

import main.common.Entry;

public interface Painter {

	public void paintBox(Graphics g, Entry e, int x_c, int y_c);
	
	public void paintEdges(Graphics g, Entry e);
	
	public void highlight(Entry e, boolean shouldHighligh);
	
	public void highlightAll(List<Entry> e, boolean shouldHighligh);
	
}
