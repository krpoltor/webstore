package pl.spring.demo.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.security.Principal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import pl.spring.demo.controller.LoginController;

public class LoginControllerTest {

	private MockMvc mockMvc;

	@Before
	public void setup() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");

		LoginController loginController = new LoginController();
		mockMvc = MockMvcBuilders.standaloneSetup(loginController).setViewResolvers(viewResolver).build();
	}
	
	@Test
	public void shouldTestAccessDenied() throws Exception {
		// given
		// attribute
		//ResultActions resultActions = mockMvc.perform(get("/403").flashAttr("user", value)accept(MediaType.APPLICATION_JSON)
			//	.contentType(MediaType.APPLICATION_JSON).content("1"));
		// then
		//resultActions.andExpect(view().name("login"));
	}

}
