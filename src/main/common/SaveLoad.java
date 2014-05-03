package main.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import main.tree.GauravTree;
import main.tree.TreeStructure;

public class SaveLoad {

	public TreeStructure<Entry> load(File f) throws IOException {

		FileReader r = new FileReader(f);
		BufferedReader reader = new BufferedReader(r);
		String line = reader.readLine();

		Entry current = formatLine(line);
		GauravTree tree = new GauravTree(current);

		int indents = 0;
		line = reader.readLine();

		while(line != null) {
			int new_indents = line.indexOf(Constants.left_border, 0);
			if(new_indents < indents) {
				Entry e = formatLine(line);
				Entry grandparent = tree.getParent(tree.getParent(current));
				tree.addChild(grandparent, e);
				current = e;
			}
			else if(new_indents > indents) {
				Entry e = formatLine(line);
				tree.addChild(current, e);
				current = e;
			}
			else{
				Entry e = formatLine(line);
				tree.addChild(tree.getParent(current), e);
				current = e;
			}
			indents = new_indents;

			line = reader.readLine();
		}
		reader.close();
		return tree;
	}



	private Entry formatLine(String line) {
		int first_index = line.indexOf(Constants.left_border, 0)+1;
		int last_index = line.lastIndexOf(Constants.right_border, line.length());
		line = line.substring(first_index, last_index);
		String[] rootText = line.split(Constants.splitter);

		return new Entry(rootText[0], rootText[1].replace(Constants.newLine, "\n"));
	}

	public void save(TreeStructure<Entry> tree, File f) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(f));
		writeChildren(tree, writer, tree.root(),"");
		writer.close();

	}

	private void writeChildren(TreeStructure<Entry> tree, BufferedWriter writer, Entry parent, String suffix) throws IOException {
		writer.write(suffix + formatEntry(parent) + "\n");
		if(!tree.isLeaf(parent)) {
			for(Entry e : tree.getChildren(parent)) {
				writeChildren(tree, writer, e, suffix + Constants.indent);
			}
		}

	}

	private String formatEntry(Entry e) {
		return Constants.left_border + e.getTitle() + Constants.splitter + e.getDetails().replace("\n", Constants.newLine) + Constants.right_border;
	}

}
