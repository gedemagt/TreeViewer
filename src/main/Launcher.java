package main;

import java.awt.Dimension;
import java.io.IOException;

import gui.BoardImpl;
import gui.DnDListener;
import gui.MenuBar;
import gui.abego.TreeFactory;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import main.tree.GauravTree;
import org.xml.sax.SAXException;

public class Launcher {

	public static void main(String[] args) throws ParserConfigurationException, TransformerException, SAXException, IOException {
		
		JFrame main_frame = new JFrame();

		GauravTree tree = TreeFactory.get();
		BoardImpl panel = new BoardImpl(tree);
		panel.registerListener(new DnDListener(panel));
		
		JScrollPane p = new JScrollPane(panel);
		
		main_frame.add(new JScrollPane(p));
		main_frame.setPreferredSize(new Dimension(860,600));
		main_frame.setJMenuBar(new MenuBar(panel));
		
		main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main_frame.setVisible(true);
		main_frame.pack();
	}
	
}
