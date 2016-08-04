package pl.spring.demo.rest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import pl.spring.demo.service.BookService;
import pl.spring.demo.to.BookTo;

@Controller
@ResponseBody
public class BookRestService {

	// REST dla szachow
	private static Logger LOGGER = Logger.getLogger(BookRestService.class.getName());

	@Autowired
	private BookService bookService;

	/** 
	 * Find all books.
	 * 
	 * @return
	 */
	@RequestMapping(value = "/rest/books", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<BookTo>> getBook() {
		List<BookTo> allBooks = bookService.findAllBooks();
		if (allBooks.isEmpty()) {
			return new ResponseEntity<List<BookTo>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<BookTo>>(allBooks, HttpStatus.OK);
	}

	/**
	 * Get book by ID.
	 * 
	 * @param id
	 *            - book ID.
	 * @return
	 */
	@RequestMapping(value = "/rest/books/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BookTo> getBook(@PathVariable("id") Long id) {
		BookTo book = bookService.findBookById(id);
		if (book.equals(null)) {
			LOGGER.info("Book with id " + id + " not found");
			return new ResponseEntity<BookTo>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<BookTo>(book, HttpStatus.OK);
	}

	/**
	 * Add new book to Database.
	 * 
	 * @param book
	 *            - book to add.
	 * @param ucBuilder
	 * @return
	 */
	@RequestMapping(value = "/rest/books", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> addBook(@RequestBody BookTo book, UriComponentsBuilder ucBuilder) {

		LOGGER.info("Creating book: " + book.toString());
		bookService.saveBook(book);
		LOGGER.info("Book created");

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/rest/books/{id}").buildAndExpand(book.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	/**
	 * Update book from Database.
	 * 
	 * @param id
	 *            - ID of book to update.
	 * @param book
	 * @return
	 */
	@RequestMapping(value = "/rest/books/{id}", method = RequestMethod.PUT)
	public ResponseEntity<BookTo> updateBook(@PathVariable("id") Long id, @RequestBody BookTo book) {
		BookTo currentBook = bookService.findBookById(id);

		if (currentBook.equals(null)) {
			LOGGER.info("Book with id " + id + " not found");
			return new ResponseEntity<BookTo>(HttpStatus.NOT_FOUND);
		}

		currentBook.setTitle(book.getTitle());
		currentBook.setAuthors(book.getAuthors());
		currentBook.setStatus(book.getStatus());

		bookService.saveBook(currentBook);
		return new ResponseEntity<BookTo>(currentBook, HttpStatus.OK);
	}

	/**
	 * Delete book.
	 * 
	 * @param id
	 *            - ID of book to delete.
	 * @return
	 */
	@RequestMapping(value = "/rest/books/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<BookTo> deleteBook(@PathVariable("id") Long id) {
		LOGGER.info("Fetching & Deleting User with id " + id);
		BookTo book = bookService.findBookById(id);
		if (book.equals(null)) {
			LOGGER.info("Unable to delete. Book with id " + id + " not found");
			return new ResponseEntity<BookTo>(HttpStatus.NOT_FOUND);
		}

		bookService.deleteBook(id);
		return new ResponseEntity<BookTo>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/rest/search/{title}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<BookTo>> searchById(@PathVariable("title") String title) {
		List<BookTo> foundBooks = bookService.findBooksByTitle(title);
		return new ResponseEntity<List<BookTo>>(foundBooks, HttpStatus.FOUND);
	}

	@RequestMapping(value = "/rest/search/{title}/{author}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<BookTo>> searchByTitleAndAuthor(@PathVariable("title") String title,
			@PathVariable("author") String author) {
		List<BookTo> foundBooks = bookService.findBooksByTitleAndAuthor(title, author);

		return new ResponseEntity<List<BookTo>>(foundBooks, HttpStatus.FOUND);
	}

	@RequestMapping(value = "/rest/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<BookTo>> searchByMultipleTitlesAndAuthors(
			@RequestParam(value = "title") String[] titlesArray,
			@RequestParam(value = "author") String[] authorsArray) {
		Set<BookTo> foundBooks = new HashSet<BookTo>();
		if (titlesArray.length == 0) {
			for (String author : authorsArray) {
				foundBooks.addAll(bookService.findBooksByAuthor(author));
			}
		} else if (authorsArray.length == 0) {
			for (String title : titlesArray) {
				foundBooks.addAll(bookService.findBooksByTitle(title));
			}
		} else {
			for (String title : titlesArray) {
				for (String author : authorsArray) {
					foundBooks.addAll(bookService.findBooksByTitleAndAuthor(title,author));
				}
			}
		}
		return new ResponseEntity<Set<BookTo>>(foundBooks, HttpStatus.FOUND);
	}
	
	@RequestMapping(value = "/rest/coffee", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> teapot() {
		String message = "I'm a teapot!";
		return new ResponseEntity<String>(message, HttpStatus.I_AM_A_TEAPOT);
	}
}
