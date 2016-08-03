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

	// TODO: Inject properly book service
	@Autowired
	private BookService bookService;

	@RequestMapping(value = "/rest/books", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<BookTo>> getBook() {
		List<BookTo> allBooks = bookService.findAllBooks();
		if (allBooks.isEmpty()) {
			return new ResponseEntity<List<BookTo>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<BookTo>>(allBooks, HttpStatus.OK);
	}

	// TODO: implement all necessary CRUD operations as a rest service

	// -------------------Retrieve Single
	// Book--------------------------------------------------------

	@RequestMapping(value = "/rest/books/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BookTo> getUser(@PathVariable("id") Long id) {
		BookTo book = bookService.findBookById(id);
		if (book.equals(null)) {
			LOGGER.info("Book with id " + id + " not found");
			return new ResponseEntity<BookTo>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<BookTo>(book, HttpStatus.OK);
	}

	// ------------------- Create a book
	// --------------------------------------------------------
	@RequestMapping(value = "/rest/books", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> addBook(@RequestBody BookTo book, UriComponentsBuilder ucBuilder) {

		LOGGER.info("Creating book: " + book.toString());
		bookService.saveBook(book);
		LOGGER.info("Book created");

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/rest/books/{id}").buildAndExpand(book.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a book
	// --------------------------------------------------------

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

	// ------------------- Delete a book
	// --------------------------------------------------------

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

	// TODO: implement some search methods considering single request parameters
	// / multiple request parameters / array request parameters

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
	public ResponseEntity<Set<BookTo>> searchByMultipleTitles(@RequestParam(value = "title") String[] titlesArray) {
		Set<BookTo> foundBooks = new HashSet<BookTo>();
		for (String title : titlesArray) {
			foundBooks.addAll(bookService.findBooksByTitle(title));
		}

		return new ResponseEntity<Set<BookTo>>(foundBooks, HttpStatus.FOUND);
	}
}
