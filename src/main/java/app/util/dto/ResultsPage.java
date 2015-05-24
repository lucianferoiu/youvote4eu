package app.util.dto;

import java.io.IOException;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.ObjectMapper;

public class ResultsPage {

	public Long total;
	public Integer page;
	public Integer from;
	public Integer to;
	public String results;

	public String toJson() {
		ObjectMapper om = new ObjectMapper();
		om.setVisibilityChecker(om.getSerializationConfig().getDefaultVisibilityChecker()
				.withGetterVisibility(JsonAutoDetect.Visibility.NONE)
				.withSetterVisibility(JsonAutoDetect.Visibility.NONE));
		try {
			return om.writeValueAsString(this);
		}
		catch (IOException e) {
			return null;
		}
	}

}
