package app.config;

import org.javalite.activeweb.AbstractDBConfig;
import org.javalite.activeweb.AppContext;

public class DbConfig extends AbstractDBConfig {

	
    public void init(AppContext context) {

        environment("development").jdbc("org.postgresql.Driver", "jdbc:postgresql://localhost/youvote4eu_d", "lucian", "");
        environment("development").testing().jdbc("org.postgresql.Driver", "jdbc:postgresql://localhost/youvote4eu_t", "lucian", "");
        environment("production").jndi("jdbc/youvote4eu_p");//TODO: retrieve the URL from the variable $DATABSE_URL set by Heroku        
    }
}
