package app.util;

import java.util.BitSet;
import java.util.Iterator;

import app.util.dto.Coord;

public class BitMap implements Iterable<Coord> {
	public int width;
	public int height;
	protected BitSet map;

	public BitMap(int width, int height) {
		this.width = width;
		this.height = height;
		map = new BitSet((height * width) + 1);
	}

	public boolean get(int x, int y) {
		if (x >= width || y >= height) return false;
		return map.get((y * width) + x);
	}

	public boolean get(Coord c) {
		return get(c.x, c.y);
	}

	public void set(int x, int y) {
		if (x >= width || y >= height) return;
		map.set((y * width) + x);
	}

	public void set(Coord c) {
		set(c.x, c.y);
	}

	public void clear(int x, int y) {
		if (x >= width || y >= height) return;
		map.clear((y * width) + x);
	}

	public void clear(Coord c) {
		clear(c.x, c.y);
	}

	class ClearBitsIterator implements Iterator<Coord> {
		int index = 0;

		public ClearBitsIterator() {
			index = 0;
		}

		@Override
		public boolean hasNext() {
			int nextIdx = map.nextClearBit(index + 1);
			if (nextIdx >= width * height) return false;
			return true;
		}

		@Override
		public Coord next() {
			index = map.nextClearBit(index + 1);
			return new Coord(Math.floorMod(index, width), Math.floorDiv(index, width));
		}
	}

	@Override
	public Iterator<Coord> iterator() {
		return new ClearBitsIterator();
	}

}
