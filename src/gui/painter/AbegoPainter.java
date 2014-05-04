package gui.painter;

import gui.Board;
import gui.abego.AbegoTree;
import gui.abego.EntryNodeExtentProvider;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;

import main.common.Entry;

public class AbegoPainter implements Painter{

	private final static int ARC_SIZE = 10;
	private final static Color BOX_COLOR = Color.WHITE;
	private final static Color BORDER_COLOR = Color.darkGray;
	private final static Color HIGHLIGHTED_BORDER_COLOR = Color.GREEN;
	private final static Color TEXT_COLOR = Color.black;

	private Board b;
	private TreeLayout<Entry> treeLayout;
	private List<Entry> highlights;
	private AbegoTree abegoTree;
	
	public AbegoPainter(Board b) {
		this.b = b;
		abegoTree = new AbegoTree(b.getTree());
		highlights = new ArrayList<Entry>();
		DefaultConfiguration<Entry> config = new DefaultConfiguration<Entry>(100,30);
		EntryNodeExtentProvider nodeExtentProvider = new EntryNodeExtentProvider();
		JPanel p = new JPanel();
		nodeExtentProvider.setFontMetric(p.getFontMetrics(p.getFont()));
		treeLayout = new TreeLayout<Entry>(abegoTree, nodeExtentProvider, config);
	}

	@Override
	public void paintEntry(Graphics g, Entry e) {
		Graphics2D g2 = (Graphics2D) g;
		// draw the box in the background
		g.setColor(BOX_COLOR);
		Rectangle2D.Double box = getBoundsOfNode(e);

		g.fillRoundRect((int) box.x, (int) box.y, (int) box.width - 1,
				(int) box.height - 1, ARC_SIZE, ARC_SIZE);
		
		if(e.iscollapsed()) g2.setStroke(new BasicStroke(3));
		else g2.setStroke(new BasicStroke(1));
		
		if(highlights.contains(e)) g.setColor(HIGHLIGHTED_BORDER_COLOR);
		else g.setColor(BORDER_COLOR);
		
		g.drawRoundRect((int) box.x, (int) box.y, (int) box.width - 1,
				(int) box.height - 1, ARC_SIZE, ARC_SIZE);

		// draw the text on top of the box (possibly multiple lines)
		g.setColor(TEXT_COLOR);
		String[] lines = e.getDetails().split("\n");
		FontMetrics m = g.getFontMetrics(g.getFont());
		int x = (int) box.x + ARC_SIZE / 2;
		int y = (int) box.y + m.getAscent() + m.getLeading() + 1;

		// Draw title
		g.drawString(e.getTitle(), x, y);
		if(e.isShowDetails()) {
			y += 4;
			g.fillRect((int) box.x, y, (int) box.width - 1, 2);
			y += m.getHeight() + 4;

			// draw line

			for (int i = 0; i < lines.length; i++) {
				g.drawString(lines[i], x, y);
				y += m.getHeight();
			}
		}


	}

	@Override
	public void paintChildEdges(Graphics g, Entry parent) {
			Rectangle2D.Double b1 = getBoundsOfNode(parent);
			double x1 = b1.getCenterX();
			double y1 = b1.getCenterY();
			for (Entry child : b.getTree().getChildren(parent)) {

				Rectangle2D.Double b2 = getBoundsOfNode(child);
				g.drawLine((int) x1, (int) y1, (int) b2.getCenterX(),
						(int) b2.getCenterY());

			}
		

	}

	private Rectangle2D.Double getBoundsOfNode(Entry node) {
		return treeLayout.getNodeBounds().get(node);
	}

	@Override
	public void highlight(Entry e, boolean shouldHighligh) {
		if(shouldHighligh) if(!highlights.contains(e)) highlights.add(e);
		else highlights.remove(e);
	}

	@Override
	public void highlightAll(List<Entry> e, boolean shouldHighligh) {
		if(shouldHighligh) {
			if(!highlights.contains(e)) highlights.addAll(e);
		}
		else {
			if(e == null) highlights.clear();
			else highlights.remove(e);
		}
	}

	@Override
	public void revalidate() {
		abegoTree.setTree(b.getTree());
		treeLayout = new TreeLayout<Entry>(abegoTree, treeLayout.getNodeExtentProvider(), treeLayout.getConfiguration());
	}

	@Override
	public Dimension getSize() {
		return new Dimension((int) treeLayout.getBounds().getWidth(), (int) treeLayout.getBounds().getHeight());
	}

	@Override
	public Entry getEntryAt(int x, int y) {
		for(Entry e : treeLayout.getNodeBounds().keySet()) {

			Rectangle2D.Double r = treeLayout.getNodeBounds().get(e);

			if(r.contains((double) x, (double) y)) {
				return e;
			}
		}
		return null;
	}

}
