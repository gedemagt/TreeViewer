package gui.abego;

import java.awt.FontMetrics;

import main.common.Entry;

import org.abego.treelayout.NodeExtentProvider;

public class EntryNodeExtentProvider implements NodeExtentProvider<Entry> {
	
	private FontMetrics m;
	
	public void setFontMetric(FontMetrics m) {
		this.m = m;
	}
	
	@Override
	public double getHeight(Entry arg0) {
		if(m == null) return 100;
		String[] detalis = arg0.getDetails().split("\n");
		int details = detalis.length;
		
		String[] titles = arg0.getTitle().split("\n");
		int title = titles.length;
		int h = title;
		if(arg0.isShowDetails()) return m.getHeight()*(h+details+1);
		else return m.getHeight()*h;
	}

	@Override
	public double getWidth(Entry arg0) {
		return 200;
	}

}
