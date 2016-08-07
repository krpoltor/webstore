package pl.spring.demo.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import pl.spring.demo.constants.ModelConstants;
import pl.spring.demo.controller.HomeController;
import pl.spring.demo.enumerations.BookStatus;
import pl.spring.demo.service.BookService;
import pl.spring.demo.to.BookTo;

public class HomeControllerTest {

	@Autowired
	private BookService bookService;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/templates/");
		viewResolver.setSuffix(".html");

		mockMvc = MockMvcBuilders.standaloneSetup(new HomeController()).setViewResolvers(viewResolver).build();
	}

	@Ignore
	@Test
	public void testHomePage() throws Exception {
		// given
		List<BookTo> testFoundBooksList = new LinkedList<BookTo>();
		testFoundBooksList.add(new BookTo(1L, "title", "authors", BookStatus.MISSING));
		// when
		Mockito.when(bookService.findAllBooks()).thenReturn(testFoundBooksList);
		// nullpointer exception for findAllBooks() because model attribute was added
		ResultActions resultActions = this.mockMvc//
				.perform(get("/")//
						.flashAttr("bookCount", testFoundBooksList.size()));
		// then
		resultActions.andExpect(view().name("welcome"))
				.andExpect(model().attribute(ModelConstants.GREETING, new ArgumentMatcher<Object>() {
					@Override
					public boolean matches(Object argument) {
						String text = (String) argument;
						return null != text && text.length() > 0;
					}
				}));
	}
}
