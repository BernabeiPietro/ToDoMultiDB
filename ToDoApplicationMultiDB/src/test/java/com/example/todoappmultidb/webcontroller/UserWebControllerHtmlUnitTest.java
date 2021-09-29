package com.example.todoappmultidb.webcontroller;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.todoappmultidb.dto.UserDTO;
import com.example.todoappmultidb.service.UserService;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UserWebController.class)
public class UserWebControllerHtmlUnitTest {
	@Autowired
	private WebClient webClient;
	@MockBean
	private UserService userService;

	@Test
	public void testHomePageTitle() throws Exception {
		HtmlPage page = webClient.getPage("/");
		assertThat(page.getTitleText()).isEqualTo("Users");
	}

	@Test
	public void testHomePageWithNoUser() throws Exception {
		when(userService.getAllUser()).thenReturn(emptyList());
		HtmlPage page = this.webClient.getPage("/");
		assertThat(page.getBody().getTextContent()).contains("No user");
	}

	@Test
	public void test_HomePageWithUsers_ShouldShowThemInATable() throws Exception {
		when(userService.getAllUser())
				.thenReturn(asList(new UserDTO(1L, "nome1", "email1"), new UserDTO(2L, "nome2", "email2")));
		HtmlPage page = this.webClient.getPage("/");
		assertThat(page.getBody().getTextContent()).doesNotContain("No user");
		HtmlTable table = page.getHtmlElementById("user_table");
		assertThat(table.asText()).isEqualTo(
				"Users\n" + "ID	Name	Email\n" + "1	nome1	email1	Edit\n" + "2	nome2	email2	Edit");
		page.getAnchorByHref("/user/edit/1");
		page.getAnchorByHref("/user/edit/2");
	}

	@Test
	public void testEditNonExistentUsers() throws Exception {
		when(userService.getUserById(1L)).thenReturn(null);
		HtmlPage page = this.webClient.getPage("/user/edit/1");
		assertThat(page.getBody().getTextContent()).contains("No user found with id: 1");
	}

	@Test
	public void testEditExistentUser() throws Exception {
		when(userService.getUserById(1)).thenReturn(new UserDTO(1L, "original name", "original email"));
		HtmlPage page = this.webClient.getPage("/user/edit/1");
		// Get the form that we are dealing with
		final HtmlForm form = page.getFormByName("user_form");
		// make sure the fields are filled with the correct values
		// and then change their values
		form.getInputByValue("original name").setValueAttribute("modified name");
		form.getInputByValue("original email").setValueAttribute("modified email");
		// Now submit the form by clicking the button and get back the second page.
		form.getButtonByName("btn_submit").click();
		// verify that the modified employee has been updated through the service
		// using the values entered in the form
		verify(userService).updateUserById(1L, new UserDTO(1L, "modified name", "modified email"));
	}

	@Test
	public void testEditNewUser() throws Exception {
		HtmlPage page = this.webClient.getPage("/user/new");
		// Get the form that we are dealing with
		final HtmlForm form = page.getFormByName("user_form");
		// retrieve fields by their names and change their values
		form.getInputByName("name").setValueAttribute("new name");
		form.getInputByName("email").setValueAttribute("new email");
		// Now submit the form by clicking the button and get back the second page.
		form.getButtonByName("btn_submit").click();
		// verify that the employee has been inserted through the service
		// using the values entered in the form (note the id must be null)
		verify(userService).insertNewUser(new UserDTO(null, "new name", "new email"));
	}

	@Test
	public void test_HomePage_ShouldProvideALinkForCreatingANewUser() throws Exception {
		HtmlPage page = this.webClient.getPage("/");
		assertThat(page.getAnchorByText("New user").getHrefAttribute()).isEqualTo("/user/new");
	}
}
