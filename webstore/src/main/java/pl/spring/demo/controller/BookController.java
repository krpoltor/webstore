package pl.spring.demo.controller;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
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
		// TODO: implement default method (?)
		model.addAttribute("bookList", bookService.findAllBooks());
		return ViewNames.BOOKS;
	}

	/**
	 * Method collects info about all books
	 */
	@RequestMapping("/all")
	public ModelAndView allBooks() {
		ModelAndView modelAndView = new ModelAndView();
		// TODO: implement method gathering and displaying all books
		
		return modelAndView;
	}

	// TODO: here implement methods which displays book info based on query
	// arguments
	@RequestMapping("/book")
	public String findBookById(Model model, @RequestParam("id") int id){
		BookTo bookToAddToModel = bookService.findAllBooks().get(id-1);
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
