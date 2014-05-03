package main;

import java.text.BreakIterator;
import java.util.Locale;

public class Util {

	public static String format(String text, int max) {
		text.replace("\n", "");
		Locale currentLocale = new Locale ("da","DK");
		BreakIterator boundary = BreakIterator.getLineInstance(currentLocale);
		boundary.setText(text);
		int start = boundary.first();
		int end = boundary.next();
		int lineLength = 0;
		String result = "";
		while (end != BreakIterator.DONE) {
			String word = text.substring(start,end);
//			word = word.replace("\n\n", "");
			lineLength = lineLength + word.length();
			if (lineLength >= max) {
				result += "\n";
				lineLength = word.length();
			}
			result += word;
			start = end;
			end = boundary.next();
		}
//		System.out.println(result);
		return result.replace("\n\n", "\n");
	}

	public static String unEscapeString(String s){
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<s.length(); i++)
			switch (s.charAt(i)){
			case '\n': sb.append("\\n"); break;
			case '\t': sb.append("\\t"); break;
			// ... rest of escape characters
			default: sb.append(s.charAt(i));
			}
		return sb.toString();
	}

	

}


