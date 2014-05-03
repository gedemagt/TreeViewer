package main.tree;

import java.util.List;

import main.common.Entry;

import org.abego.treelayout.util.AbstractTreeForTreeLayout;

public class AbegoTree extends AbstractTreeForTreeLayout<Entry> {

	TreeStructure<Entry> tree;
	
	public AbegoTree(TreeStructure<Entry> tree) {
		super(tree.root());
		this.tree = tree;
	}
	
	@Override
	public Entry getRoot() {
		return tree.root();
	}

	@Override
	public List<Entry> getChildrenList(Entry arg0) {
		return tree.getChildren(arg0);
	}

	@Override
	public Entry getParent(Entry arg0) {
		return tree.getParent(arg0);
	}
	
	public void setTree(TreeStructure<Entry> tree){
		this.tree = tree;
	}

}
