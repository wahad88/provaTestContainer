package de.rieckpil.blog;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.booleanThat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@Testcontainers
@SpringBootTest
class ApplicationTests {

  @Container 
  public static OracleContainer container = new OracleContainer("gvenzl/oracle-xe:18.4.0-slim");
		  	//.withDatabaseName("testDB")
		    //.withUsername("testUser")
		    //.withPassword("testPassword")
  			//.withInitScript("database/INIT.sql");
  

  
  @Autowired
  private BookRepository bookRepository;

  // requires Spring Boot >= 2.2.6
  
  @DynamicPropertySource
  static void properties(DynamicPropertyRegistry registry) {
    Supplier<Object> jdbcurl = container::getJdbcUrl;
	registry.add("spring.datasource.url", jdbcurl);
    Supplier<Object> getpassword = container::getPassword;
	registry.add("spring.datasource.password", getpassword);
    Supplier<Object> getuser = container::getUsername;
	registry.add("spring.datasource.username", getuser);
  }
  
  
  @BeforeEach
  void popolamentoDatabase() throws SQLException {
	  System.out.println("Popolamento database in corso...");
    try (Connection connection = DriverManager.getConnection(container.getJdbcUrl(), container.getPassword(),container.getUsername());
         PreparedStatement preparedStatement = connection.prepareStatement(
        		  "INSERT ALL "+
        		  "INTO books (name) VALUES ('Libro numero 1') "+
        		  "INTO books (name) VALUES ('Libro numero 2') "+
        		  "INTO books (name) VALUES ('Libro numero 3') "+
        		  "INTO books (name) VALUES ('Libro numero 4') "+
        		  "INTO books (name) VALUES ('Libro numero 5') "+
        		  "SELECT * FROM dual"
        		  );
         ResultSet resultSet = preparedStatement.executeQuery(); 
         PreparedStatement preparedStatement_select = connection.prepareStatement("SELECT * FROM books")) {
    		  try (ResultSet resultSet_select = preparedStatement_select.executeQuery()) {
    			  while (resultSet_select.next()) {
    		          System.out.println(resultSet_select.getString("name"));
    	}
       } 
     }
   }
  
  @AfterEach
  void resetDatabase() throws SQLException {
	  System.out.println("Reset del database in corso...");
    try (Connection connection = DriverManager.getConnection(container.getJdbcUrl(), container.getPassword(),container.getUsername());
         PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM books");
         ResultSet resultSet = preparedStatement.executeQuery(); 
         PreparedStatement preparedStatement_select = connection.prepareStatement("SELECT * FROM books")) {
    		  try (ResultSet resultSet_select = preparedStatement_select.executeQuery()) {
    			  while (resultSet_select.next()) {
    		          System.out.println(resultSet_select.getString("name"));
    	}
       } catch (SQLException e) {
    	   System.out.println(e.getMessage());
		}
     }
   }


  @Test
  void testOracleSQL_Show_Table() throws SQLException {
	  System.out.println("Stampa tabella in corso...");
    try (Connection connection = DriverManager.getConnection(container.getJdbcUrl(), container.getPassword(),container.getUsername());
         PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM books")) {
    		try (ResultSet resultSet = preparedStatement.executeQuery()) {
    			while (resultSet.next()) {
    				System.out.println(resultSet.getString("name"));
        }
      }
    }
  }
  
  @Test
  void testOracleSQ_Update() throws SQLException {
	  System.out.println("Aggiornamento tabella in corso...");
    try (Connection connection = DriverManager.getConnection(container.getJdbcUrl(), container.getPassword(),container.getUsername());
         PreparedStatement preparedStatement = connection.prepareStatement("UPDATE books SET name = 'Ho modificato Libro numero 1' WHERE name = 'Libro numero 1'");
         ResultSet resultSet = preparedStatement.executeQuery(); 
         PreparedStatement preparedStatement_select = connection.prepareStatement("SELECT * FROM books")) {
    		  try (ResultSet resultSet_select = preparedStatement_select.executeQuery()) {
    			  while (resultSet_select.next()) {
    		          System.out.println(resultSet_select.getString("name"));
    	}
       } 
     }
   }
  
  
  @Test
  void testOracleSQL_insert() throws SQLException {
	  System.out.println("Inserimento nella tabella in corso...");
    try (Connection connection = DriverManager.getConnection(container.getJdbcUrl(), container.getPassword(),container.getUsername());
         PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO books (name) VALUES ('Libro numero 6')");
         ResultSet resultSet = preparedStatement.executeQuery(); 
         PreparedStatement preparedStatement_select = connection.prepareStatement("SELECT * FROM books")) {
    		  try (ResultSet resultSet_select = preparedStatement_select.executeQuery()) {
    			  while (resultSet_select.next()) {
    		          System.out.println(resultSet_select.getString("name"));
    	}
       } 
     }
   }
  
  @Test
  void testOracleSQL_delete() throws SQLException {
	  System.out.println("Eliminazione di una riga nella tabella in corso...");
    try (Connection connection = DriverManager.getConnection(container.getJdbcUrl(), container.getPassword(),container.getUsername());
         PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM books\r\n"
         		+ "WHERE name = 'Libro numero 3'");
         ResultSet resultSet = preparedStatement.executeQuery(); 
         PreparedStatement preparedStatement_select = connection.prepareStatement("SELECT * FROM books")) {
    		  try (ResultSet resultSet_select = preparedStatement_select.executeQuery()) {
    			  while (resultSet_select.next()) {
    		          System.out.println(resultSet_select.getString("name"));
    	}
       } 
     }
   }
  
  /*
  @Test
  void contextLoads() {

    Book book1 = new Book();
    book1.setName("Testcontainers");
    Book book2 = new Book();
    book2.setName("Testcontainers2");
    Book book3 = new Book();
    book3.setName("Testcontainers3");

    bookRepository.save(book1);
    
    bookRepository.save(book2);
    
    bookRepository.save(book3);
    
    List<Book> actuaList=new ArrayList<Book>(bookRepository.findAll());
	assertThat(actuaList).hasSize(3);

    System.out.println("Context loads!");
  }
  
  @Test
  void deleteBooks() {
	  
	  try {
		  
		  bookRepository.deleteAll();

	    	 List<Book> actuaList=bookRepository.findAll();
			  assertThat(actuaList).hasSize(0);
	    	
	    	} catch(Exception e) {
	    }
  }
  
  @Test
  void getBooks() {
	  
	  try {
		  
		  	Book book1 = new Book();
		    book1.setName("Testcontainers");
		    Book book2 = new Book();
		    book2.setName("Testcontainers2");
		    Book book3 = new Book();
		    book3.setName("Testcontainers3");

		    bookRepository.save(book1); 
		    
		    bookRepository.save(book2);
		    
		    bookRepository.save(book3);
		    List<Book> actuaList=new ArrayList<Book>(bookRepository.findAll());
		  
		    assertThat(actuaList).hasSize(3);
		    System.out.print(actuaList);
		  
	  } catch(Exception e) {
	    }
  
  }
  
  */
  

}
