package pl.spring.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.spring.demo.constants.ModelConstants;
import pl.spring.demo.constants.TextConstants;
import pl.spring.demo.constants.ViewNames;
import pl.spring.demo.service.BookService;

@Controller
public class HomeController {

	@Autowired
	BookService bookService;

	@RequestMapping("/")
	public String welcome(Model model) {
		model.addAttribute(ModelConstants.GREETING, TextConstants.WELCOME);
		model.addAttribute(ModelConstants.INFO, TextConstants.INFO_TEXT);
		//model.addAttribute("bookCount", bookService.findAllBooks().size());
		return ViewNames.WELCOME;
	}
}
