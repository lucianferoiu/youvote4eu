package app.services;

import java.util.List;

public interface QuestionsLayouter {
	public enum Element {
		BIG('B'), MEDIUM('M'), EMPTY('0'), SMALL('S');
		public char code;

		private Element(char c) {
			code = c;
		}
	}

	List<Element> randomLayout();

}
