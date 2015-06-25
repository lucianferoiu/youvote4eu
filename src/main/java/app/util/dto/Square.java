package app.util.dto;

public class Square {
	public int x;
	public int y;
	public int sz;

	public Square(int x, int y, int sz) {
		this.x = x;
		this.y = y;
		this.sz = sz;
	}

	@Override
	public String toString() {
		return String.format("[(%d,%d) %dx%d]", x, y, sz, sz);
	}

}
