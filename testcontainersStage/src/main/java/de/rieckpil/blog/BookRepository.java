package de.rieckpil.blog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
	//List<Book> updateBook(Book primo, Book secondo);
}
