package app.util.dto;

import org.codehaus.jackson.annotate.JsonProperty;

public class HCDataPoint<T> {
	public String key;
	public T value;

	public HCDataPoint(String k, T v) {
		key = k;
		value = v;
	}

	@JsonProperty("hc-key")
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
}
