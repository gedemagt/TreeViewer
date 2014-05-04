package gui.painter;

import gui.abego.AbegoBoard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import main.common.Entry;

public class AbegoPainter implements Painter{

	private final static int ARC_SIZE = 10;
	private final static Color BOX_COLOR = Color.WHITE;
	private final static Color BORDER_COLOR = Color.darkGray;
//	private final static Color COLLAPSED_BORDER_COLOR = Color.RED;
	private final static Color HIGHLIGHTED_BORDER_COLOR = Color.GREEN;
	private final static Color TEXT_COLOR = Color.black;

	private AbegoBoard b;
	private List<Entry> highlights;
	
	public AbegoPainter(AbegoBoard b) {
		this.b = b;
		highlights = new ArrayList<Entry>();
	}

	@Override
	public void paintBox(Graphics g, Entry e, int x_c, int y_c) {
		Graphics2D g2 = (Graphics2D) g;
		// draw the box in the background
		g.setColor(BOX_COLOR);
		Rectangle2D.Double box = getBoundsOfNode(e);

		g.fillRoundRect(x_c, y_c, (int) box.width - 1,
				(int) box.height - 1, ARC_SIZE, ARC_SIZE);
		
		if(e.iscollapsed()) g2.setStroke(new BasicStroke(3));
		else g2.setStroke(new BasicStroke(1));
		
		if(highlights.contains(e)) g.setColor(HIGHLIGHTED_BORDER_COLOR);
		else g.setColor(BORDER_COLOR);
		
		g.drawRoundRect(x_c, y_c, (int) box.width - 1,
				(int) box.height - 1, ARC_SIZE, ARC_SIZE);

		// draw the text on top of the box (possibly multiple lines)
		g.setColor(TEXT_COLOR);
		String[] lines = e.getDetails().split("\n");
		FontMetrics m = g.getFontMetrics(g.getFont());
		int x = x_c + ARC_SIZE / 2;
		int y = y_c + m.getAscent() + m.getLeading() + 1;

		// Draw title
		g.drawString(e.getTitle(), x, y);
		if(e.isShowDetails()) {
			y += 4;
			g.fillRect(x_c, y, (int) box.width - 1, 2);
			y += m.getHeight() + 4;

			// draw line

			for (int i = 0; i < lines.length; i++) {
				g.drawString(lines[i], x, y);
				y += m.getHeight();
			}
		}


	}

	@Override
	public void paintEdges(Graphics g, Entry parent) {
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
		return b.getTreeLayout().getNodeBounds().get(node);
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

}
