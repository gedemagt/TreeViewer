package gui.abego;

import main.common.Entry;
import main.tree.MyTree;

public class AbegoTree {

	
	public static MyTree get() {
		Entry root = new Entry("Root","Root node!");
		MyTree tree = new MyTree(root);
		return tree;
	}
	
	public static MyTree getDefault() {
		Entry root = new Entry("Root","AMAGAD");
		Entry child1 = new Entry("Child1","MORE AMAGAD, MORE AMAGAD, MORE AMAGAD,MORE AMAGAD");
		Entry child2 = new Entry("Child2","MOST AMAGAD");
		Entry child21 = new Entry("Child21","MOST AMAGAD");
		Entry child22 = new Entry("Child22","MOST AMAGAD");
		MyTree tree = new MyTree(root);
		tree.addChild(root, child1);
		tree.addChild(root, child2);
		tree.addChild(child2, child21);
		tree.addChild(child2, child22);
		return tree;
	}
}
