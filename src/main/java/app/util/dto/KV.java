package app.util.dto;

public class KV<Tk, Tv> {
	public Tk key;
	public Tv value;

	public KV(Tk k, Tv v) {
		this.key = k;
		this.value = v;
	}
}
