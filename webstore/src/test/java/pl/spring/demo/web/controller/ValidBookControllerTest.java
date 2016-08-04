package pl.spring.demo.web.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import java.util.LinkedList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

import pl.spring.demo.controller.BookController;
import pl.spring.demo.enumerations.BookStatus;
import pl.spring.demo.service.BookService;
import pl.spring.demo.to.BookTo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "controller-test-configuration.xml")
@WebAppConfiguration
public class ValidBookControllerTest {

	@Autowired
	private BookService bookService;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		Mockito.reset(bookService);

		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");

		BookController bookController = new BookController();
		mockMvc = MockMvcBuilders.standaloneSetup(bookController).setViewResolvers(viewResolver).build();
		// Due to fact, that We are trying to construct real Bean - Book
		// Controller, we have to use reflection to mock existing field book
		// service
		ReflectionTestUtils.setField(bookController, "bookService", bookService);
	}

	private List<BookTo> generateDefaultData() {
		List<BookTo> testList = new LinkedList<BookTo>();
		testList.add(new BookTo(1L, "First book", "Jan Kowalski", BookStatus.FREE));
		testList.add(new BookTo(2L, "Second book", "Zbigniew Nowak", BookStatus.FREE));
		testList.add(new BookTo(3L, "Third book", "Janusz Jankowski", BookStatus.FREE));
		return testList;
	}

	/**
	 * (Example) Sample method which convert's any object from Java to String
	 */
	private static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final String jsonContent = mapper.writeValueAsString(obj);
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testAddBookPage() throws Exception {
		// given
		BookTo testBook = new BookTo(1L, "Test title", "Test Author", BookStatus.FREE);
		Mockito.when(bookService.saveBook(Mockito.any())).thenReturn(testBook);
		// TODO: please take a look how we pass @ModelAttribute as a request
		// attribute
		ResultActions resultActions = mockMvc.perform(post("/books/add").flashAttr("newBook", testBook));
		// then
		resultActions.andExpect(view().name("welcome"))
				.andExpect(model().attribute("newBook", new ArgumentMatcher<Object>() {
					@Override
					public boolean matches(Object argument) {
						BookTo book = (BookTo) argument;
						return null != book && testBook.getTitle().equals(book.getTitle());
					}
				}));
	}

	@Test
	public void testMainBookPage() throws Exception {
		// given
		List<BookTo> testList = generateDefaultData();
		Mockito.when(bookService.findAllBooks()).thenReturn(testList);
		// attribute
		ResultActions resultActions = mockMvc.perform(post("/books").flashAttr("bookList", testList));
		// then
		resultActions.andExpect(view().name("books"))
				.andExpect(model().attribute("bookList", new ArgumentMatcher<Object>() {
					@Override
					public boolean matches(Object argument) {
						List<BookTo> bookList = (List<BookTo>) argument;
						return null != bookList && testList.equals(bookList);
					}
				}));
	}

	@Test
	public void testSearchPage() throws Exception {
		// given
		// attribute
		ResultActions resultActions = mockMvc.perform(get("/books/search").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content("1"));
		// then
		resultActions.andExpect(view().name("search"));
	}

	@Test
	public void testFoundBookPage() throws Exception {
		// given
		String testTitle = "book";
		String testAuthor = "kowalski";
		List<BookTo> testList = new LinkedList<BookTo>();
		testList.add(new BookTo(1L, "First book", "Jan Kowalski", BookStatus.FREE));
		Mockito.when(bookService.findBooksByTitleAndAuthor(testTitle, testAuthor)).thenReturn(testList);
		// attribute
		ResultActions resultActions = mockMvc.perform(post("/books/found?bookTitle=book&bookAuthor=kowalski")
				.flashAttr("bookTitle", testTitle).flashAttr("bookAuthor", testAuthor));
		// then
		resultActions.andExpect(view().name("found"))
				.andExpect(model().attribute("bookTitle", new ArgumentMatcher<Object>() {
					@Override
					public boolean matches(Object argument) {
						String book = (String) argument;
						return null != book && testTitle.equals(book);
					}
				})).andExpect(model().attribute("bookAuthor", new ArgumentMatcher<Object>() {
					@Override
					public boolean matches(Object argument) {
						String book = (String) argument;
						return null != book && testAuthor.equals(book);
					}
				}));
	}

	@Test
	public void testGetBookByIdPage() throws Exception {
		// given
		List<BookTo> testList = generateDefaultData();
		Mockito.when(bookService.findBookById(1L)).thenReturn(testList.get(0));
		// attribute
		ResultActions resultActions = mockMvc.perform(get("/books/book?id=1").flashAttr("id", 1L)
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content("1"));
		// then
		// TODO: Zapytac, no modelandview found
		resultActions.andExpect(view().name("book")).andExpect(model().attribute("id", new ArgumentMatcher<Object>() {
			@Override
			public boolean matches(Object argument) {
				Long book = (Long) argument;
				return null != book && book.equals(testList.get(0).getId());
			}
		}));
	}

	@Test
	public void testDeleteBookByIdPage() throws Exception {
		// given
		List<BookTo> testList = generateDefaultData();
		Mockito.doCallRealMethod().when(bookService.deleteBook(1L));
		// attribute
		ResultActions resultActions = mockMvc.perform(post("/books/delete/book?id=1").flashAttr("id", 1L));
		// then
		resultActions.andExpect(view().name("delete"))
				.andExpect(model().attribute("book", new ArgumentMatcher<Object>() {
					@Override
					public boolean matches(Object argument) {
						List<BookTo> bookList = (List<BookTo>) argument;
						return null != bookList && testList.equals(bookList);
					}
				}));
	}

}
