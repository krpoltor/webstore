package pl.spring.demo.web.controller;

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
import pl.spring.demo.dao.BookDao;
import pl.spring.demo.enumerations.BookStatus;
import pl.spring.demo.service.BookService;
import pl.spring.demo.to.BookTo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "controller-test-configuration.xml")
@WebAppConfiguration
public class ValidBookControllerTest {

	@Autowired
	private BookService bookService;
	
	@Autowired 
	private BookDao bookDao;
	 
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

	/**
	 * Test for adding new book to database.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAddBookPage() throws Exception {
		// given
		BookTo testBook = new BookTo(1L, "Test title", "Test Author", BookStatus.FREE);
		Mockito.when(bookService.saveBook(Mockito.any())).thenReturn(testBook);
		// please take a look how we pass @ModelAttribute as a request
		// attribute
		ResultActions resultActions = this.mockMvc.perform(post("/books/add").flashAttr("newBook", testBook));
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

	/**
	 * Testing main page.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMainBookPage() throws Exception {
		// given
		List<BookTo> testList = generateDefaultData();
		Mockito.when(bookService.findAllBooks()).thenReturn(testList);
		// attribute
		ResultActions resultActions = this.mockMvc.perform(post("/books").flashAttr("bookList", testList));
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

	/**
	 * Testing search page.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSearchPage() throws Exception {
		// given
		// attribute
		ResultActions resultActions = this.mockMvc.perform(get("/books/search").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content("1"));
		// then
		resultActions.andExpect(view().name("search"));
	}

	/**
	 * Testing found page.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testFoundBookPage() throws Exception {
		// given
		String testTitle = "book";
		String testAuthor = "kowalski";
		List<BookTo> testList = new LinkedList<BookTo>();
		testList.add(new BookTo(1L, "First book", "Jan Kowalski", BookStatus.FREE));
		Mockito.when(bookService.findBooksByTitleAndAuthor(testTitle, testAuthor)).thenReturn(testList);
		// attribute
		ResultActions resultActions = this.mockMvc.perform(post("/books/found?bookTitle=book&bookAuthor=kowalski"));
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

	/**
	 * Test for getting book by ID.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetBookByIdPage() throws Exception {
		// given
		List<BookTo> testList = generateDefaultData();
		Mockito.when(bookService.findBookById(1L)).thenReturn(testList.get(0));
		// attribute
		ResultActions resultActions = this.mockMvc.perform(get("/books/book?id=1").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content("1"));
		// then
		resultActions.andExpect(view().name("book")).andExpect(model().attribute("book", new ArgumentMatcher<Object>() {
			@Override
			public boolean matches(Object argument) {
				BookTo book = (BookTo) argument;
				return null != book && book.equals(testList.get(0));
			}
		}));
	}

	/**
	 * Test for deleting book by ID.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDeleteBookByIdPage() throws Exception {
		// given
		BookTo tmpBook = new BookTo(1L, "title", "author", BookStatus.MISSING);
		Mockito.when(bookService.findBookById(Mockito.anyLong())).thenReturn(tmpBook);
		Mockito.doNothing().when(bookService).deleteBook(Mockito.anyLong());
		// attribute
		ResultActions resultActions = this.mockMvc.perform(get("/books/delete/book?id=1"));
		// then
		Mockito.verify(bookService, Mockito.times(1)).deleteBook(1L);
	}

	// FIXME: test for deleting all books
	@Test(expected = RuntimeException.class)
	public void testDeleteAllBooks() throws Exception {
		// given
		Mockito.doNothing().when(bookDao).deleteAll();
		// when
		ResultActions resultActions = this.mockMvc.perform(get("/books/delete/all"));
		//then
		Mockito.verify(bookDao, Mockito.times(1)).deleteAll();
	}

	/**
	 * Test for /books/add page. View addBook.jsp with RequestMethod.GET
	 * contains BookTo with all fields null.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAddBookPageGET() throws Exception {
		// given
		BookTo testBook = new BookTo(null, null, null, null);
		// attribute
		ResultActions resultActions = this.mockMvc.perform(get("/books/add").flashAttr("newBook", testBook));
		// then
		resultActions.andExpect(view().name("addBook"))
				.andExpect(model().attribute("newBook", new ArgumentMatcher<Object>() {
					@Override
					public boolean matches(Object argument) {
						BookTo book = (BookTo) argument;
						return null != book && testBook.equals(book);
					}
				}));
	}

	/**
	 * Test for /books/add page with RequestMethod.POST. Checks if view contains
	 * fields.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAddBookPagePOST() throws Exception {
		// given
		String testTitle = "testTitle";
		String testAuthors = "testAuthors";
		BookStatus testStatus = BookStatus.MISSING;
		// attribute
		ResultActions resultActions = this.mockMvc.perform(post("/books/add")//
				.flashAttr("title", testTitle)//
				.flashAttr("authors", testAuthors)//
				.flashAttr("status", testStatus));
		// then
		resultActions.andExpect(view().name("welcome"))
				.andExpect(model().attribute("title", new ArgumentMatcher<Object>() {
					@Override
					public boolean matches(Object argument) {
						String bookTitle = (String) argument;
						return null != bookTitle && testTitle.equals(bookTitle);
					}
				})).andExpect(model().attribute("authors", new ArgumentMatcher<Object>() {
					@Override
					public boolean matches(Object argument) {
						String bookAuthors = (String) argument;
						return null != bookAuthors && testAuthors.equals(bookAuthors);
					}
				})).andExpect(model().attribute("status", new ArgumentMatcher<Object>() {
					@Override
					public boolean matches(Object argument) {
						BookStatus bookStatus = (BookStatus) argument;
						return null != bookStatus && testStatus.equals(bookStatus);
					}
				}));
	}
}
