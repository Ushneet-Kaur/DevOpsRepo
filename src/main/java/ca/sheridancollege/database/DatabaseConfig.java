package ca.sheridancollege.database;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
public class DatabaseConfig {

	//Creates our connection to our database.  In this case h2
		@Bean
		public DataSource dataSource() {
			DriverManagerDataSource dataSource = new DriverManagerDataSource();
			dataSource.setDriverClassName("org.h2.Driver");
			dataSource.setUrl("jdbc:h2:mem:testdb");
			dataSource.setUsername("sa");
			dataSource.setPassword("");
			return dataSource;
		}

	//Load the schema
		@Bean
		public DataSource loadSchema() {
		    return new EmbeddedDatabaseBuilder()
		      .setType(EmbeddedDatabaseType.H2)
		      .addScript("classpath:schema.sql")
		      .addScript("classpath:newsecuritysetup.sql")
		      //You can include additional .addScript() for multiple sql files.
		      .build();
		}
		
	//Method used to connect to DatabaseAccess class that will create queries
		@Bean
		public NamedParameterJdbcTemplate 
				namedParemterJdbcTemplate(DataSource dataSource) {
			return new NamedParameterJdbcTemplate(dataSource);
		}

}
