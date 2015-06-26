package app.util.dto;

public class QuestionCell extends Square {
	public long q;

	public QuestionCell(int x, int y, int sz, long q) {
		super(x, y, sz);
		this.q = q;
	}

	@Override
	public String toString() {
		return String.format("[#%d (%d,%d) %dx%d]", q, x, y, sz, sz);
	}

}
