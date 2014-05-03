package main.tree;


import java.util.List;

import main.common.Entry;

import org.abego.treelayout.util.AbstractTreeForTreeLayout;

import com.gaurav.tree.ArrayTree;
import com.gaurav.tree.NodeNotFoundException;
import com.gaurav.tree.Tree;

public class MyTree extends AbstractTreeForTreeLayout<Entry>{

	private ArrayTree<Entry> tree;
	
	
	public MyTree(Entry root) {
		super(root);
		tree = new ArrayTree<Entry>(9000);
		tree.add(root);
	}
	
	public Tree<Entry> getSubTree(Entry element) {

		if(!tree.contains(element)) return null;
		Tree<Entry> t = new ArrayTree<Entry>(9000);
		t.add(element);
		try {
			addChildren(t, element);
		} catch (NodeNotFoundException e) {
			e.printStackTrace();
		}
		return t;
	}
	
	private void addChildren(Tree<Entry> t, Entry parent) throws NodeNotFoundException {
		for(Entry child : tree.children(parent)) {
			t.add(parent, child);
			if(!isLeaf(child)) addChildren(t, child);
		}
	}

	public void addSubTree(Tree<Entry> subTree, Entry parent) {
		try {
			tree.remove(subTree.root());
			tree.add(parent, subTree.root());
			copyChildren(subTree, subTree.root(), subTree.root());
		} catch (NodeNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void copyChildren(Tree<Entry> subTree, Entry subParent, Entry parent) throws NodeNotFoundException {
		for(Entry child : subTree.children(subParent)) {

			tree.add(parent, child);
			if(!isLeaf(child)) copyChildren(subTree, child, child);
		}
	}
	
	public void addChild(Entry parent, Entry child) {
		try {
			tree.add(parent, child);
		} catch (NodeNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void removeElement(Entry element) {
		tree.remove(element);
	}

	@Override
	public List<Entry> getChildrenList(Entry arg0) {
		try {
			return tree.children(arg0);
		} catch (NodeNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public Entry getParent(Entry arg0) {
		try {
			return tree.parent(arg0);
		} catch (NodeNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
	}

}
