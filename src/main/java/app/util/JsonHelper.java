package app.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.javalite.activejdbc.LazyList;

public class JsonHelper {
	public static Map toMap(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			CollectionType mapCollectionType = mapper.getTypeFactory().constructCollectionType(List.class, Map.class);
			return mapper.readValue(json, Map.class);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static Map[] toMaps(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, Map[].class);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String toResultsJson(LazyList results, Long total, Long from, Long to, boolean pretty,
			List<String> attrs) {
		StringWriter sw = new StringWriter();
		sw.write("{" + (pretty ? "\n" : ""));
		if (results != null) {

			sw.write(" \"total\":" + (total != null ? total : -1) + (pretty ? ",\n" : ","));
			if (from != null) sw.write(" \"from\":" + from + (pretty ? ",\n" : ","));
			if (to != null) sw.write(" \"to\":" + to + (pretty ? ",\n" : ","));
			if (attrs != null) {
				if (pretty) {
					Collections.sort(attrs);
				}
				sw.write(" \"results\":" + results.toJson(pretty, attrs.toArray(new String[] {}))
						+ (pretty ? "\n" : ""));
			} else {
				sw.write(" \"results\":" + results.toJson(pretty) + (pretty ? "\n" : ""));
			}
		}
		sw.write((pretty ? "\n" : "") + "}");
		return sw.toString();
	}
}