package com.example.todoappmultidb.webcontroller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.todoappmultidb.dto.UserDTO;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import javassist.NotFoundException;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ErrorWebController.class)
public class ErrorWebControllerHtmlUnitTest {
	@Autowired
	private WebClient webClient;

	@Test
	public void testErrorPage_emptyMessage() throws Exception {
		HtmlPage page = this.webClient.getPage("/error/");
		assertThat(page.getBody().getTextContent()).contains("Generic Error");
	}

	@Test
	public void testErrorPage_fullMessage() throws Exception {
		HtmlPage page = this.webClient.getPage("/error/Error");
		assertThat(page.getBody().getTextContent()).contains("Error");
	}

	@Test
	public void test_ErrorPage_redirectToHome() throws Exception {
		HtmlPage page = this.webClient.getPage("/error/");
		assertThat(page.getAnchorByText("Turn to Home").getHrefAttribute()).isEqualTo("/");
	}
}
