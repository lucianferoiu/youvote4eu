package app.util.dto;

public class KV<Tk, Tv> {
	public Tk key;
	public Tv value;

	public KV(Tk k, Tv v) {
		this.key = k;
		this.value = v;
	}

	public Tk getKey() {
		return key;
	}

	public void setKey(Tk key) {
		this.key = key;
	}

	public Tv getValue() {
		return value;
	}

	public void setValue(Tv value) {
		this.value = value;
	}
}
