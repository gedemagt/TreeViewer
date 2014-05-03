package gui.abego;

import main.common.Entry;
import main.tree.GauravTree;

public class TreeFactory {

	
	public static GauravTree get() {
		Entry root = new Entry("Root","Root node!");
		GauravTree tree = new GauravTree(root);
		return tree;
	}
	
	public static GauravTree getDefault() {
		Entry root = new Entry("Root","AMAGAD");
		Entry child1 = new Entry("Child1","MORE AMAGAD, MORE AMAGAD, MORE AMAGAD,MORE AMAGAD");
		Entry child2 = new Entry("Child2","MOST AMAGAD");
		Entry child21 = new Entry("Child21","MOST AMAGAD");
		Entry child22 = new Entry("Child22","MOST AMAGAD");
		GauravTree tree = new GauravTree(root);
		tree.addChild(root, child1);
		tree.addChild(root, child2);
		tree.addChild(child2, child21);
		tree.addChild(child2, child22);
		return tree;
	}
}
