package main.tree;

import java.util.List;

public interface TreeStructure<E> {

	public List<E> getChildren(E parent);
	
	public void remove(E parent);
	
	public E root();
	public void setRoot(E parent);
	
	public TreeStructure<E> getSubTree(E parent);
	public void addSubTree(E parent, TreeStructure<E> subTree);
	
	public void addChild(E parent, E child);
	public E getChild(E parent);
	
	public boolean isInSubTree(E parent, E element);
}
