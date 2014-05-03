package main.common;

import main.Util;

public class Entry {

	private String title;
	private String details;
	private boolean collapsed;
	private boolean showDetails;
	
	public boolean isShowDetails() {
		return showDetails;
	}

	public void setShowDetails(boolean showDetails) {
		this.showDetails = showDetails;
	}

	public Entry(String title, String details) {
		this.title = title;
		this.details = Util.format(details, 30);
		collapsed = false;
		showDetails = false;
	}
	
	public Entry() {
		title = "";
		details = "";
	}

	
	public String getTitle() {
		return title;
	}

	public String getDetails() {
		return details;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDetails(String details) {
		this.details = Util.format(details, 30);
	}
	
	@Override
	public String toString() {
		return title;
	}
	
	public void collapsed(boolean b) {
		this.collapsed  = b;
	}
	
	public boolean iscollapsed() {
		return collapsed;
	}
	
	public Entry copy() {
		Entry r  = new Entry();
		r.collapsed = collapsed;
		r.details = details;
		r.showDetails = showDetails;
		r.title = title;
		
		return r;
		
	}
	
}
