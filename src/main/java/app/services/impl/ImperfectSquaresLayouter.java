package app.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.services.QuestionsLayouter;
import app.util.LayoutTemplate;
import app.util.dto.Coord;
import app.util.dto.Square;

public class ImperfectSquaresLayouter implements QuestionsLayouter {

	static final Logger log = LoggerFactory.getLogger(ImperfectSquaresLayouter.class);
	static final Random random = new Random();//unsecure random, but we don't care..

	protected List<LayoutTemplate> templates;
	protected int templatesCount = 0;

	public ImperfectSquaresLayouter() {
		templates = new ArrayList<LayoutTemplate>();
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
