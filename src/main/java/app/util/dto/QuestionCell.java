package app.util.dto;

public class QuestionCell {
	public int x;
	public int y;
	public int sz;
	public long q;

	public QuestionCell(int x, int y, int sz, long q) {
		this.x = x;
		this.y = y;
		this.sz = sz;
		this.q = q;
	}

	@Override
	public String toString() {
		return String.format("[#%d (%d,%d) %dx%d]", q, x, y, sz, sz);
	}

}
