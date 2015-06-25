package app.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.services.QuestionsLayouter;
import app.util.BitMap;
import app.util.dto.Coord;
import app.util.dto.Square;

public class ImperfectSquaresLayouter implements QuestionsLayouter {

	static final Logger log = LoggerFactory.getLogger(ImperfectSquaresLayouter.class);
	static final Random random = new Random();//unsecure random, but we don't care..

	public static class LayoutTemplate {
		protected int width;
		protected int height;
		public List<Square> squares;
		public BitMap map;

		public LayoutTemplate(int w, int h, Square... sq) {
			width = w;
			height = h;
			map = new BitMap(width, height);
			for (int i = 0; i < sq.length; i++) {
				Square s = sq[i];
				squares.add(s);
				for (int x = s.x; x < s.x + s.sz; x++) {
					for (int y = s.y; y < s.y + s.sz; y++) {
						map.set(x, y);
					}
				}
			}

		}
	}

	protected List<LayoutTemplate> templates;
	protected int templatesCount = 0;

	public ImperfectSquaresLayouter() {
		templates = new ArrayList<ImperfectSquaresLayouter.LayoutTemplate>();
		hydrateTemplates();
	}

	private void hydrateTemplates() {
		templates.add(new LayoutTemplate(5, 10, new Square(0, 0, 3), new Square(3, 1, 2), new Square(2, 3, 2), new Square(3, 1, 2),
				new Square(0, 4, 2), new Square(3, 1, 2), new Square(2, 5, 3), new Square(0, 7, 2), new Square(2, 8, 2)));
		templates.add(new LayoutTemplate(5, 10, new Square(1, 0, 2), new Square(3, 0, 2), new Square(1, 2, 3), new Square(0, 5, 2),
				new Square(2, 5, 3), new Square(1, 8, 2), new Square(3, 8, 2)));
		templates.add(new LayoutTemplate(5, 10, new Square(1, 0, 2), new Square(3, 0, 2), new Square(0, 2, 3), new Square(3, 4, 2),
				new Square(1, 5, 2), new Square(1, 7, 3)));
		templates.add(new LayoutTemplate(5, 10, new Square(0, 0, 2), new Square(2, 0, 3), new Square(0, 2, 2), new Square(2, 3, 2),
				new Square(0, 5, 3), new Square(3, 5, 2), new Square(2, 8, 2)));
		/*
		templates.add(new LayoutTemplate(5, 10, 
				new Square(,,),
				new Square(,,),
				new Square(,,)
		));
		*/
		templatesCount = templates.size();
	}

	@Override
	public List<Square> randomLayout() {
		int which = random.nextInt(templatesCount);
		LayoutTemplate lt = templates.get(which);
		List<Square> layout = new ArrayList<Square>(lt.squares);
		for (Coord c : lt.map) {
			if (random.nextInt(3) == 0) {
				layout.add(new Square(c.x, c.y, 1));
			}
		}

		return layout;
	}
}
