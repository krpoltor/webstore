package pl.spring.demo.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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

	// TODO: here implement methods which displays book info based on query
	// arguments

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView getBooksToSearchFor() {
		ModelAndView model = new ModelAndView(ViewNames.SEARCH);
		return model;
	}

	@RequestMapping(value = "/found", method = RequestMethod.POST)
	public ModelAndView displayFoundBooks(@RequestParam("bookTitle") String bookTitle,
			@RequestParam("bookAuthor") String bookAuthor) {

		ModelAndView model = new ModelAndView(ViewNames.FOUND);

		model.addObject("msg", "You searched for: " + bookTitle + " written by: " + bookAuthor);

		List<BookTo> foundBooksByTitle = bookService.findBooksByTitle(bookTitle);
		List<BookTo> foundBooksByAuthor = bookService.findBooksByAuthor(bookAuthor);
		
		model.addObject("bookListByTitle", foundBooksByTitle);
		model.addObject("bookListByAuthor", foundBooksByAuthor);
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

	// TODO: Implement GET / POST methods for "add book" functionality

	/**
	 * Binder initialization
	 */
	@InitBinder
	public void initialiseBinder(WebDataBinder binder) {
		binder.setAllowedFields("id", "title", "authors", "status");
	}

}
