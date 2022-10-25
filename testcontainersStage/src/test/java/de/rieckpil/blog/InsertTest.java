package de.rieckpil.blog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.*;
import java.util.function.Supplier;

@Testcontainers
@SpringBootTest
public class InsertTest {

  @Container
  public static OracleContainer container = new OracleContainer("gvenzl/oracle-xe:18.4.0-slim")
    //.withUsername("testcontainers")
    //.withPassword("testcontainers")
    //.withDatabaseName("testcontainers");
  	.withInitScript("database/INIT.sql");
  
  @DynamicPropertySource
  static void properties(DynamicPropertyRegistry registry) {
    Supplier<Object> jdbcurl = container::getJdbcUrl;
	registry.add("spring.datasource.url", jdbcurl);
    Supplier<Object> getpassword = container::getPassword;
	registry.add("spring.datasource.password", getpassword);
    Supplier<Object> getuser = container::getUsername;
	registry.add("spring.datasource.username", getuser);
  }

  @Test
  void testPostgreSQLModule() throws SQLException {
    try (Connection connection = DriverManager
      .getConnection(container.getJdbcUrl(),container.getUsername(),container.getPassword());
         PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM messages")) {
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          System.out.println(resultSet.getString("content"));
        }
      }
    }
  }
}
