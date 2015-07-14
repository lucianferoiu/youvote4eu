package app.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import org.javalite.activeweb.AbstractDBConfig;
import org.javalite.activeweb.AppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.util.StringUtils;

public class DbConfig extends AbstractDBConfig {

	private static final String HEROKU_DATABASE_URL = "DATABASE_URL";
	static final Logger log = LoggerFactory.getLogger(DbConfig.class);

	@Override
	public void init(AppContext context) {

		String herokuDB = System.getenv(HEROKU_DATABASE_URL);

		if (StringUtils.nullOrEmpty(herokuDB)) {//local dev
			environment("development").jdbc("org.postgresql.Driver", "jdbc:postgresql://localhost/youvote4eu_d", "lucian", "");
			environment("development").testing().jdbc("org.postgresql.Driver", "jdbc:postgresql://localhost/youvote4eu_t", "lucian", "");
			environment("production").jndi("jdbc/youvote4eu_p");
			log.warn("Setting up the DB for local machine - URL={}", "jdbc:postgresql://localhost/youvote4eu_d");
		} else {
			Properties jdbcProps = parseHerokuDBUrl(herokuDB);
			if (jdbcProps != null) {
				environment("development").jdbc(jdbcProps.getProperty("driver"), jdbcProps.getProperty("url"),
						jdbcProps.getProperty("user"), jdbcProps.getProperty("password"));
				environment("development").testing().jdbc(jdbcProps.getProperty("driver"), jdbcProps.getProperty("url"),
						jdbcProps.getProperty("user"), jdbcProps.getProperty("password"));
				environment("production").jdbc(jdbcProps.getProperty("driver"), jdbcProps.getProperty("url"),
						jdbcProps.getProperty("user"), jdbcProps.getProperty("password"));
				log.warn("Setting up the DB for Heroku - URL={}", jdbcProps.getProperty("url"));
			} else {
				throw new RuntimeException("Cannot configure application DB");
			}
		}
	}

	protected static Properties parseHerokuDBUrl(String database_url) {
		try {
			URI dbUri = new URI(database_url);
			String driver = inferDriverClass(database_url);
			String user = dbUri.getUserInfo().split(":")[0];
			String password = dbUri.getUserInfo().split(":")[1];
			//String authPart = user + ":" + password + "@"; //
			//String url = "jdbc:" + database_url.replace(authPart, ""); // <-- this doesn't work because the schema is different in the Herok env than the one required by the driver - postgresql
			String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

			Properties p = new Properties();
			p.put("driver", driver);
			p.put("url", dbUrl);
			p.put("user", user);
			p.put("password", password);
			return p;

		}
		catch (URISyntaxException e) {
			log.error("Failed to parse Heroku DB Url", e);
			throw new RuntimeException("Failed to parse Heroku DATABASE_URL=" + database_url, e);
		}
	}

	private static String inferDriverClass(String database_url) {
		String driver;
		if (database_url.contains("postgres")) {
			driver = "org.postgresql.Driver";
		} else if (database_url.contains("mysql")) {
			driver = "com.mysql.jdbc.Driver";
		} else {
			throw new RuntimeException("Cannot infer the JDBC driver from DATABASE_URL=" + database_url);
		}
		return driver;
	}
}
