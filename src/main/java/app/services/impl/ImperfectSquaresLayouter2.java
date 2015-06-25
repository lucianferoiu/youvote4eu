package app.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.services.QuestionsLayouter;
import app.util.dto.Square;

@Deprecated
public class ImperfectSquaresLayouter2 implements QuestionsLayouter {

	static final Logger log = LoggerFactory.getLogger(ImperfectSquaresLayouter2.class);

	@Override
	public List<Square> randomLayout() {
		// TODO Auto-generated method stub
		return null;
	}

	//	static final Random random = new Random();//unsecure random, but we don't care..
	//	private int solutionsCount = 0;
	//	private List<List<Element>> solutions;
	//
	//	@Override
	//	public List<Element> randomLayout() {
	//		if (solutions != null && !(solutions.isEmpty())) {
	//			int idx = random.nextInt(solutionsCount);
	//			if (idx >= 0 && idx < solutionsCount) {//should pass all the time because of the above
	//				return solutions.get(idx);
	//			}
	//		}
	//		return Collections.emptyList();
	//	}
	//
	//	protected class Layout implements Cloneable {
	//		public int x = 0, y = 0;
	//		public char[][] map;
	//
	//		@Override
	//		public Layout clone() {
	//			Layout l = new Layout();
	//			l.x = x;
	//			l.y = y;
	//			for (int i = 0; i < 10; i++) {
	//				for (int j = 0; j < 5; j++) {
	//					l.map[i][j] = map[i][j];
	//				}
	//			}
	//			return l;
	//		}
	//
	//		public Layout() {
	//			map = new char[10][5];
	//			for (int i = 0; i < 10; i++) {
	//				for (int j = 0; j < 5; j++) {
	//					map[i][j] = '*';
	//				}
	//			}
	//		}
	//
	//		public boolean canAdd(Element e) {
	//
	//			if (x >= 3) {
	//				boolean same = true;
	//				for (int i = x; i > x - 3; i--) {
	//					if (map[y][i] != '*' && map[y][i] != e.code) {
	//						same = false;
	//						break;
	//					}
	//				}
	//				if (same) return false;
	//			}
	//			if (y >= 4) {
	//				boolean same = true;
	//				for (int i = y; i > y - 4; i--) {
	//					if (map[i][x] != '*' && map[i][x] != e.code) {
	//						same = false;
	//						break;
	//					}
	//				}
	//				if (same) return false;
	//			}
	//
	//			if (Element.BIG.equals(e)) {
	//				if (x > 2 || y > 7) return false;
	//				for (int i = 0; i < 3; i++)
	//					for (int j = 0; j < 3; j++)
	//						if (map[y + j][x + i] != '*') return false;
	//				int cnt = 0;
	//				if (x < 4 && y < 9 && map[y + 1][x + 1] == 'B') cnt++;
	//				if (y < 9 && map[y + 1][x] == 'B') cnt++;
	//				if (x > 0 && y < 9 && map[y + 1][x - 1] == 'B') cnt++;
	//				if (x < 4 && map[y][x + 1] == 'B') cnt++;
	//				if (x > 0 && map[y][x - 1] == 'B') cnt++;
	//				if (x < 4 && y > 0 && map[y - 1][x + 1] == 'B') cnt++;
	//				if (y > 0 && map[y - 1][x] == 'B') cnt++;
	//				if (x > 0 && y > 0 && map[y - 1][x - 1] == 'B') cnt++;
	//				return cnt <= 1;
	//			}
	//			if (Element.MEDIUM.equals(e)) {
	//				if (x > 3 || y > 8) return false;
	//				for (int i = 0; i < 2; i++)
	//					for (int j = 0; j < 2; j++)
	//						if (map[y + j][x + i] != '*') return false;
	//
	//			}
	//			if (Element.SMALL.equals(e)) return x <= 4 && y <= 9;
	//			if (Element.EMPTY.equals(e)) {
	//				if (x > 4 || y > 9) return false;
	//				int cnt = 0;
	//				if (x < 4 && y < 9 && map[y + 1][x + 1] == ' ') cnt++;
	//				if (y < 9 && map[y + 1][x] == ' ') cnt++;
	//				if (x > 0 && y < 9 && map[y + 1][x - 1] == ' ') cnt++;
	//				if (x < 4 && map[y][x + 1] == ' ') cnt++;
	//				if (x > 0 && map[y][x - 1] == ' ') cnt++;
	//				if (x < 4 && y > 0 && map[y - 1][x + 1] == ' ') cnt++;
	//				if (y > 0 && map[y - 1][x] == ' ') cnt++;
	//				if (x > 0 && y > 0 && map[y - 1][x - 1] == ' ') cnt++;
	//
	//				return cnt <= 2;
	//			}
	//			return false;
	//		}
	//
	//		public void add(Element e) {
	//			if (Element.BIG.equals(e)) {
	//				for (int i = 0; i < 3; i++)
	//					for (int j = 0; j < 3; j++)
	//						map[y + j][x + i] = 'B';
	//			} else if (Element.MEDIUM.equals(e)) {
	//				for (int i = 0; i < 2; i++)
	//					for (int j = 0; j < 2; j++)
	//						map[y + j][x + i] = 'M';
	//			} else if (Element.SMALL.equals(e)) {
	//				map[y][x] = 'S';
	//			} else if (Element.EMPTY.equals(e)) {
	//				map[y][x] = ' ';
	//			}
	//			while (map[y][x] != '*') {
	//				x++;
	//				if (x >= 5) {
	//					x = 0;
	//					y++;
	//				}
	//				if (y >= 10) break;
	//			}
	//		}
	//
	//		public boolean isComplete() {
	//			return y >= 10;
	//		}
	//
	//		/*
	//		public void remove(Element e) {
	//			if (y >= 10) {
	//				y = 9;
	//				x = 4;
	//			}
	//			while (map[y][x] != e.code) {
	//				x--;
	//				if (x < 0) {
	//					x = 4;
	//					y--;
	//				}
	//				if (y < 0) break;
	//			}
	//			if (Element.BIG.equals(e)) {
	//				for (int i = 0; i < 3; i++)
	//					for (int j = 0; j < 3; j++)
	//						map[y - j][x - i] = '?';
	//				x -= 2;
	//				y -= 2;
	//			} else if (Element.MEDIUM.equals(e)) {
	//				for (int i = 0; i < 2; i++)
	//					for (int j = 0; j < 2; j++)
	//						map[y - j][x - i] = '?';
	//				x -= 1;
	//				y -= 1;
	//			} else if (Element.SMALL.equals(e)) {
	//				map[y][x] = '?';
	//			} else if (Element.EMPTY.equals(e)) {
	//				map[y][x] = '?';
	//			}
	//		}
	//		*/
	//	}
	//
	//	public ImperfectSquaresLayouter2() {
	//		generateAllPossibleLayouts();
	//	}
	//
	//	private void generateAllPossibleLayouts() {
	//		solutionsCount = 0;
	//		solutions = new ArrayList<List<Element>>();
	//
	//		generateSolution(new ArrayList<Element>(), new Layout());
	//	}
	//
	//	private void generateSolution(ArrayList<Element> sol, Layout layout) {
	//		if (!isSolutionComplete(sol, layout)) {
	//			for (Element e : Element.values()) {
	//				if (canAddToSolution(sol, e, layout)) {
	//					Layout copyOfLayout = layout.clone();
	//					sol.add(e);
	//					copyOfLayout.add(e);
	//					generateSolution(sol, copyOfLayout);
	//					sol.remove(sol.size() - 1);
	//				}
	//			}
	//		} else {
	//			if (isSolutionEsthetic(sol, layout)) {
	//				solutions.add(new ArrayList<Element>(sol));
	//				solutionsCount++;
	//				print(sol, layout);
	//			}
	//		}
	//
	//	}
	//
	//	private boolean isSolutionEsthetic(ArrayList<Element> sol, Layout layout) {
	//		int cntB = 0, cntM = 0, cntS = 0, cntE = 0;
	//		for (int i = 0; i < sol.size(); i++) {
	//			Element e = sol.get(i);
	//			if (e.equals(Element.BIG)) cntB++;
	//			if (e.equals(Element.MEDIUM)) cntM++;
	//			if (e.equals(Element.SMALL)) cntS++;
	//			if (e.equals(Element.EMPTY)) cntE++;
	//		}
	//
	//		if (cntB < 1 || cntB > 4) return false;
	//		if (cntM < 2 || cntM > 6) return false;
	//		//		if (cntM + cntB < 5) return false;
	//		//		if (cntS < 4 || cntS > 11) return false;
	//
	//		return true;
	//	}
	//
	//	private void print(ArrayList<Element> sol, Layout layout) {
	//		System.out.print("Solution " + solutionsCount + ": ");
	//		for (int i = 0; i < sol.size(); i++) {
	//			Element e = sol.get(i);
	//			if (i > 0) System.out.print(", ");
	//			System.out.print(e.code);
	//		}
	//		System.out.println("");
	//		for (int i = 0; i < 10; i++) {
	//			System.out.print("\n");
	//			for (int j = 0; j < 5; j++) {
	//				System.out.print(layout.map[i][j]);
	//			}
	//		}
	//		System.out.println("\n");
	//
	//	}
	//
	//	private boolean canAddToSolution(ArrayList<Element> sol, Element e, Layout layout) {
	//		boolean ret = true;
	//		if (!layout.canAdd(e)) return false;
	//		int cnt = 0;
	//		for (int i = 0; i < sol.size(); i++) {
	//			if (sol.get(i).equals(e)) cnt++;
	//		}
	//		//		if (e.equals(Element.BIG)) return cnt <= 3;
	//		//		if (e.equals(Element.MEDIUM)) return cnt <= 5;
	//		//		if (e.equals(Element.SMALL)) return cnt <= 11;
	//		//		if (e.equals(Element.EMPTY)) return cnt <= 13;
	//
	//		return true;
	//	}
	//
	//	private boolean isSolutionComplete(ArrayList<Element> sol, Layout layout) {
	//		return layout.isComplete();
	//	}

}
