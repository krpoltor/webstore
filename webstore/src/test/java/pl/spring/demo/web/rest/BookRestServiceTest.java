package pl.spring.demo.web.rest;

import static java.lang.Math.toIntExact;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import pl.spring.demo.enumerations.BookStatus;
import pl.spring.demo.service.BookService;
import pl.spring.demo.to.BookTo;
import pl.spring.demo.web.utils.FileUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class BookRestServiceTest {

	@Autowired
	private BookService bookService;
	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		Mockito.reset(bookService);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	/**
	 * Test for getting all books.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testShouldGetAllBooks() throws Exception {
		// given:
		final BookTo bookTo1 = new BookTo(1L, "title", "Author1", BookStatus.FREE);

		// register response for bookService.findAllBooks() mock
		Mockito.when(bookService.findAllBooks()).thenReturn(Arrays.asList(bookTo1));
		// when
		ResultActions response = this.mockMvc//
				.perform(get("/rest/books")//
						.accept(MediaType.APPLICATION_JSON)//
						.contentType(MediaType.APPLICATION_JSON)//
						.content("1"));

		response.andExpect(status().isOk())//
				.andExpect(jsonPath("[0].id").value(bookTo1.getId().intValue()))
				.andExpect(jsonPath("[0].title").value(bookTo1.getTitle()))
				.andExpect(jsonPath("[0].authors").value(bookTo1.getAuthors()));
	}

	/**
	 * Test for adding new book to database.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testShouldSaveBook() throws Exception {
		// given
		File file = FileUtils.getFileFromClasspath("classpath:pl/spring/demo/web/json/bookToSave.json");
		String json = FileUtils.readFileToString(file);
		// when
		ResultActions response = this.mockMvc//
				.perform(post("/rest/books")//
						.accept(MediaType.APPLICATION_JSON)//
						.contentType(MediaType.APPLICATION_JSON)//
						.content(json.getBytes()));
		// then
		response.andExpect(status().isCreated());
	}

	/**
	 * Test for getting book by ID.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testShouldGetBookById() throws Exception {
		// given:
		final BookTo bookTo1 = new BookTo(5L, "title2", "Author2", BookStatus.FREE);
		// register response for bookService.findAllBooks() mock
		Mockito.when(bookService.findBookById(Mockito.anyLong())).thenReturn(bookTo1);
		// when
		ResultActions response = this.mockMvc//
				.perform(get("/rest/books/5")//
						.accept(MediaType.APPLICATION_JSON)//
						.contentType(MediaType.APPLICATION_JSON)//
						.content("1"));

		response.andExpect(status().isOk())//
				.andExpect(jsonPath("$.id").value(toIntExact(bookTo1.getId())))
				.andExpect(jsonPath("$.title").value(bookTo1.getTitle()))
				.andExpect(jsonPath("$.authors").value(bookTo1.getAuthors()));
	}

	/**
	 * Test for deleting book by ID.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testShouldDeleteBook() throws Exception {
		BookTo testBook = new BookTo(1L, "title", "authors", BookStatus.FREE);
		// given
		Mockito.when(bookService.findBookById(Mockito.anyLong())).thenReturn(testBook);
		Mockito.doNothing().when(bookService).deleteBook(Mockito.anyLong());
		// when
		ResultActions response = this.mockMvc//
				.perform(delete("/rest/books/1")//
						.accept(MediaType.APPLICATION_JSON)//
						.contentType(MediaType.APPLICATION_JSON));
		// then
		response.andExpect(status().isNoContent());
		Mockito.verify(bookService, Mockito.times(1)).deleteBook(Mockito.anyLong());
	}

	/**
	 * Test for updating book in database.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testShouldUpdateBook() throws Exception {
		// given
		File file = FileUtils.getFileFromClasspath("classpath:pl/spring/demo/web/json/bookToSave.json");
		String json = FileUtils.readFileToString(file);
		BookTo testBook = new BookTo(1L, "title", "author", BookStatus.MISSING);
		Mockito.when(bookService.findBookById(Mockito.anyLong())).thenReturn(testBook);
		// when
		ResultActions response = this.mockMvc//
				.perform(put("/rest/books/1")//
						.accept(MediaType.APPLICATION_JSON)//
						.contentType(MediaType.APPLICATION_JSON)//
						.content(json.getBytes()));
		// then
		response.andExpect(status().isOk())//
				.andExpect(jsonPath("$.id").value(toIntExact(testBook.getId())))
				.andExpect(jsonPath("$.title").value(testBook.getTitle()))
				.andExpect(jsonPath("$.authors").value(testBook.getAuthors()));
	}

	/**
	 * Test for deleting every book in database.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testShouldDeleteBooks() throws Exception {
		// given
		Mockito.doNothing().when(bookService).deleteAllBooks();
		// when
		ResultActions response = this.mockMvc//
				.perform(delete("/delete/")//
						.accept(MediaType.APPLICATION_JSON)//
						.contentType(MediaType.APPLICATION_JSON));
		// then
		response.andExpect(status().isNoContent());
	}

	/**
	 * Test for searching by title.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testShouldSearchByTitle() throws Exception {
		// given
		List<BookTo> testList = new LinkedList<BookTo>();
		testList.add(new BookTo(1L, "title", "authors", BookStatus.FREE));
		Mockito.when(bookService.findBooksByTitle(Mockito.anyString())).thenReturn(testList);
		// when
		ResultActions response = this.mockMvc//
				.perform(get("/rest/search/book")//
						.accept(MediaType.APPLICATION_JSON)//
						.contentType(MediaType.APPLICATION_JSON));
		// then
		response.andExpect(status().isFound())//
				.andExpect(jsonPath("[0].id").value(toIntExact(testList.get(0).getId())))
				.andExpect(jsonPath("[0].title").value(testList.get(0).getTitle()))
				.andExpect(jsonPath("[0].authors").value(testList.get(0).getAuthors()));
	}

	/**
	 * Test for searching by title and author.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testShouldSearchByTitleAndAuthor() throws Exception {
		// given
		List<BookTo> testList = new LinkedList<BookTo>();
		testList.add(new BookTo(1L, "title", "authors", BookStatus.FREE));
		Mockito.when(bookService.findBooksByTitleAndAuthor(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(testList);
		// when
		ResultActions response = this.mockMvc//
				.perform(get("/rest/search/book/nowak")//
						.accept(MediaType.APPLICATION_JSON)//
						.contentType(MediaType.APPLICATION_JSON));
		// then
		response.andExpect(status().isFound())//
				.andExpect(jsonPath("[0].id").value(toIntExact(testList.get(0).getId())))
				.andExpect(jsonPath("[0].title").value(testList.get(0).getTitle()))
				.andExpect(jsonPath("[0].authors").value(testList.get(0).getAuthors()));
	}

	/**
	 * Test for searching by multiple titles and authors.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testShouldSearchByMultipleTitlesAndAuthors() throws Exception {
		// given
		List<BookTo> testList = new LinkedList<BookTo>();
		testList.add(new BookTo(1L, "title", "authors", BookStatus.FREE));
		Mockito.when(bookService.findBooksByTitleAndAuthor(Mockito.anyString(), Mockito.anyString())).thenReturn(testList);
		// when
		ResultActions response = this.mockMvc//
				.perform(get("/rest/search?title=book&author=authors&title=title")//
						.accept(MediaType.APPLICATION_JSON)//
						.contentType(MediaType.APPLICATION_JSON));
		// then
		response.andExpect(status().isFound())//
				.andExpect(jsonPath("[0].id").value(toIntExact(testList.get(0).getId())))
				.andExpect(jsonPath("[0].title").value(testList.get(0).getTitle()))
				.andExpect(jsonPath("[0].authors").value(testList.get(0).getAuthors()));
	}

}
