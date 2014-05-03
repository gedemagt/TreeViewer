package main.tree;

import java.util.List;

public interface TreeStructure<E> {

	public List<E> getChildren(E parent);
	
	public void remove(E parent);
	
	public E root();
	
	public TreeStructure<E> getSubTree(E parent);
	public void addSubTree(E parent, TreeStructure<E> subTree);
	
	public void addChild(E parent, E child);
	
	public boolean isInSubTree(E parent, E element);
	
	public boolean isLeaf(E element);
	
	public E getParent(E element);
}
