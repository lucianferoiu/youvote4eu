package app.util;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.javalite.activejdbc.LazyList;
import org.javalite.common.Convert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonHelper {

	static final Logger log = LoggerFactory.getLogger(JsonHelper.class);

	public static Map toMap(String json) {

		ObjectMapper mapper = new ObjectMapper();
		try {
			CollectionType mapCollectionType = mapper.getTypeFactory().constructCollectionType(List.class, Map.class);
			Map<String, Object> result = mapper.readValue(json, Map.class);
			//manually parse the dates
			//deserializeDates(result);
			return result;
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Deprecated
	protected static void deserializeDates(Map<String, Object> result) {

		for (String key : result.keySet()) {
			Object val = result.get(key);
			if (val != null) {
				if (key.endsWith("_at") || key.endsWith("_on")) {
					ZonedDateTime date = ZonedDateTime.parse(val.toString(), DateTimeFormatter.RFC_1123_DATE_TIME);
					result.put(key, Convert.toTimestamp(new Timestamp(date.toEpochSecond())));
					log.debug("Parsed date " + val);
				} else if (val instanceof Map) {//deep parse
					deserializeDates((Map) val);
				}
			}
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

	public static String toListJson(List results) {
		if (results == null) return "";
		StringWriter sw = new StringWriter();
		ObjectMapper om = new ObjectMapper();
		try {
			om.writeValue(sw, results);
		}
		catch (IOException e) {
			log.warn("Cannot generate JSON of list: " + results.toString(), e);
		}
		return sw.toString();
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