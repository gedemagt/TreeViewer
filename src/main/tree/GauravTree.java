package main.tree;


import java.util.List;

import main.common.Entry;

import com.gaurav.tree.ArrayTree;
import com.gaurav.tree.NodeNotFoundException;

public class GauravTree implements TreeStructure<Entry>{

	private ArrayTree<Entry> tree;
	
	
	public GauravTree(Entry root) {
		tree = new ArrayTree<Entry>(9000);
		tree.add(root);
	}
	
	public TreeStructure<Entry> getSubTree(Entry element) {

		if(!tree.contains(element)) return null;
		GauravTree t = new GauravTree(element);
		try {
			addChildren(t, element);
		} catch (NodeNotFoundException e) {
			e.printStackTrace();
		}
		return t;
	}
	
	private void addChildren(GauravTree t, Entry parent) throws NodeNotFoundException {
		for(Entry child : tree.children(parent)) {
			t.addChild(parent, child);
			if(!isLeaf(child)) addChildren(t, child);
		}
	}

	public void addSubTree(Entry parent, TreeStructure<Entry> subTree) {
		try {
			tree.remove(subTree.root());
			tree.add(parent, subTree.root());
			copyChildren(subTree, subTree.root(), subTree.root());
		} catch (NodeNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void copyChildren(TreeStructure<Entry> subTree, Entry subParent, Entry parent) throws NodeNotFoundException {
		for(Entry child : subTree.getChildren(subParent)) {

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

	@Override
	public List<Entry> getChildren(Entry arg0) {
		try {
			return tree.children(arg0);
		} catch (NodeNotFoundException e) {
			System.out.println(arg0);
			System.out.println("Getchildren");
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public Entry getParent(Entry arg0) {
		try {
			return tree.parent(arg0);
		} catch (NodeNotFoundException e) {
			System.out.println("Getparent");
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public void remove(Entry parent) {
		tree.remove(parent);
	}

	@Override
	public Entry root() {
		return tree.root();
	}

	@Override
	public boolean isInSubTree(Entry parent, Entry element) {
		try {
			return tree.isDescendant(parent, element);
		} catch (NodeNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean isLeaf(Entry element) {
		try {
			return tree.children(element).size() == 0;
		} catch (NodeNotFoundException e) {
			e.printStackTrace();
			return true;
		}
	}
	
	@Override
	public String toString() {
		String prefix = "";
		StringBuilder b = new StringBuilder();
		b.append(tree.root());
		b.append("\n");
		appendChildren(b, tree.root(), prefix);
		return b.toString();
		
	}

	private void appendChildren(StringBuilder b, Entry root, String prefix) {
		try {
			for(Entry e : tree.children(root)) {
				b.append(prefix);
				b.append("-");
				b.append(e);
				b.append("\n");
				if(!isLeaf(e)) appendChildren(b, e, prefix+"-");
			}
		} catch (NodeNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
