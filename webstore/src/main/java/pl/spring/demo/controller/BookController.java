package pl.spring.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.spring.demo.constants.ModelConstants;
import pl.spring.demo.constants.TextConstants;
import pl.spring.demo.constants.ViewNames;
import pl.spring.demo.service.BookService;
import pl.spring.demo.to.BookTo;

/**
 * Book controller
 * 
 * @author mmotowid
 *
 */
@Controller
@RequestMapping("/books")
public class BookController {

	@Autowired
	private BookService bookService;

	/**
	 * Mapping for displaying all books.
	 * 
	 * @param model
	 *            for view.
	 * @return view name.
	 */
	@RequestMapping
	public String list(Model model) {
		model.addAttribute("bookList", bookService.findAllBooks());
		return ViewNames.BOOKS;
	}

	/**
	 * Method collects info about all books
	 */
	// TODO: czy ta metoda jest potrzebna skoro wszystkie informacje mamy w
	// /books ?
	@RequestMapping("/all")
	public ModelAndView allBooks() {
		ModelAndView modelAndView = new ModelAndView();
		// TODO: implement method gathering and displaying all books

		return modelAndView;
	}

	/**
	 * Mapping for searching books.
	 * 
	 * @return model for search.jsp view.
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView getBooksToSearchFor() {
		ModelAndView model = new ModelAndView(ViewNames.SEARCH);
		return model;
	}

	/**
	 * Mapping for displaying found books.
	 * 
	 * @param bookTitle
	 *            - Book title.
	 * @param bookAuthor
	 *            - Book author.
	 * @return model for found.jsp view.
	 */
	@RequestMapping(value = "/found", method = RequestMethod.POST)
	public ModelAndView displayFoundBooks(@RequestParam("bookTitle") String bookTitle,
			@RequestParam("bookAuthor") String bookAuthor) {

		ModelAndView model = new ModelAndView(ViewNames.FOUND);

		model.addObject("msg", "You searched for: " + bookTitle + " written by: " + bookAuthor);

		List<BookTo> foundBooksByTitleAndAuthor = bookService.findBooksByTitleAndAuthor(bookTitle, bookAuthor);

		model.addObject("foundBooksList", foundBooksByTitleAndAuthor);
		model.addObject("bookTitle", bookTitle);
		model.addObject("bookAuthor", bookAuthor);
		return model;
	}

	/**
	 * Generating view "book" containing info about specified book.
	 * 
	 * @param model
	 *            - Data model for view.
	 * @param id
	 *            - Book ID from URL.
	 * @return - view book.jsp with data from book.
	 */
	@RequestMapping("/book")
	public String findBookById(Model model, @RequestParam("id") Long id) {

		BookTo bookToAddToModel = bookService.findBookById(id);
		model.addAttribute("book", bookToAddToModel);
		return ViewNames.BOOK;
	}

	/**
	 * Mapping for deleting book with {id}.
	 * 
	 * @param id
	 *            - ID of a book.
	 * @return model for delete.jsp view.
	 */
	@RequestMapping("/delete/book")
	public ModelAndView deleteBook(@RequestParam("id") Long id) {
		ModelAndView model = new ModelAndView(ViewNames.DELETE);
		BookTo deletedBook = bookService.findBookById(id);
		String bookTitle = deletedBook.getTitle();
		String bookAuthors = deletedBook.getAuthors();

		model.addObject("book", deletedBook);
		model.addObject("book.title", bookTitle);
		model.addObject("book.authors", bookAuthors);

		bookService.deleteBook(id);
		return model;
	}

	/**
	 * Mapping for getting new book info from view.
	 * 
	 * @return - model for addBook.jsp view.
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView addBook() {
		ModelAndView model = new ModelAndView(ViewNames.ADD_BOOK);
		BookTo newBook = new BookTo();
		model.addObject("newBook", newBook);
		return model;
	}

	/**
	 * Mapping for adding new book.
	 * 
	 * @param newBook
	 *            - BookTo from addBook().
	 * @return - model for welcome.jsp view.
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ModelAndView saveBook(@ModelAttribute("newBook") BookTo newBook) {
		ModelAndView model = new ModelAndView();
		model.addObject(ModelConstants.GREETING, TextConstants.WELCOME);
		model.addObject(ModelConstants.INFO, TextConstants.INFO_TEXT);
		bookService.saveBook(newBook);
		model.addObject("bookCount", bookService.findAllBooks().size());
		model.setViewName(ViewNames.WELCOME);
		return model;
	}

	/**
	 * Binder initialization
	 */
	@InitBinder
	public void initialiseBinder(WebDataBinder binder) {
		binder.setAllowedFields("id", "title", "authors", "status");
	}

}
