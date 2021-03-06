package app.util;

public class StringUtils {

	public static boolean nullOrEmpty(String s) {
		if (s == null) return true;
		return s.equalsIgnoreCase("") || s.trim().equalsIgnoreCase("");//it seems like a double test, but most often, the || will save the need for a call to trim() 
	}

	public static String nvl(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	public static String nvl(Object obj, Object deflt) {
		return obj == null ? deflt.toString() : obj.toString();
	}

}
