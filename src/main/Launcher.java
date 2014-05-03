package main;

import java.io.IOException;

import gui.DnDListener;
import gui.MenuBar;
import gui.abego.AbegoBoard;
import gui.abego.AbegoTree;
import gui.abego.EntryNodeExtentProvider;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import main.common.Entry;
import main.tree.MyTree;

import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;
import org.xml.sax.SAXException;

public class Launcher {

	public static void main(String[] args) throws ParserConfigurationException, TransformerException, SAXException, IOException {
		
		JFrame main_frame = new JFrame();
		
		DefaultConfiguration<Entry> config = new DefaultConfiguration<Entry>(100,30);
		EntryNodeExtentProvider nodeExtentProvider = new EntryNodeExtentProvider();
		MyTree tree = AbegoTree.get();
		TreeLayout<Entry> treeLayout = new TreeLayout<Entry>(tree, nodeExtentProvider, config);
		AbegoBoard panel = new AbegoBoard(tree, treeLayout);
		panel.registerListener(new DnDListener(panel));
		JScrollPane p = new JScrollPane(panel);
		main_frame.add(new JScrollPane(p));
		main_frame.setJMenuBar(new MenuBar(panel));
		
		main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main_frame.setVisible(true);
		main_frame.pack();
	}
	
}
