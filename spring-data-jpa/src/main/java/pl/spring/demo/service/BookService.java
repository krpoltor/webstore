package pl.spring.demo.service;

import pl.spring.demo.to.BookTo;

import java.util.List;

public interface BookService {

    List<BookTo> findAllBooks();
    List<BookTo> findBooksByTitle(String title);
    List<BookTo> findBooksByAuthor(String author);

    BookTo saveBook(BookTo book);
    void deleteBook(Long id);
    //Added functionalities
	BookTo findBookById(Long id);
	List<BookTo> findBooksByTitleAndAuthor(String title, String author);
	void deleteAllBooks();
}
