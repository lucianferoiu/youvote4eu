package app.util;

import java.util.ArrayList;
import java.util.List;

import app.util.dto.Square;

public class LayoutTemplate {
	protected int width;
	protected int height;
	public List<Square> squares;
	public BitMap map;

	public LayoutTemplate(int w, int h, Square... sq) {
		width = w;
		height = h;
		squares = new ArrayList<Square>();
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

		//		print();
	}

	private void print() {
		System.out.println("\n----------");
		for (int j = 0; j < height; j++) {
			System.out.print("");
			for (int i = 0; i < width; i++) {
				System.out.print(map.get(i, j) ? "\u2588" : "\u25A1");
			}
			System.out.println("");
		}
		System.out.println("----------\n");
	}
}