package pl.spring.demo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.spring.demo.enumerations.BookStatus;
import pl.spring.demo.service.BookService;
import pl.spring.demo.to.BookTo;

@Controller
@ResponseBody
public class BookRestService {

	// TODO: Inject properly book service
	@Autowired
	private BookService bookService;

	@RequestMapping(value = "/rest/books", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BookTo> getBook() {
		BookTo currentBook = new BookTo(1L, "title", "author", null);
		currentBook.setStatus(BookStatus.FREE);
		return new ResponseEntity<BookTo>(currentBook, HttpStatus.OK);
	}

	// TODO: implement all necessary CRUD operations as a rest service

	// TODO: implement some search methods considering single request parameters
	// / multiple request parameters / array request parameters

}
