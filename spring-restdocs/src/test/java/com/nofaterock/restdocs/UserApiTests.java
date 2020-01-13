package com.nofaterock.restdocs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nofaterock.restdocs.user.domain.User;
import com.nofaterock.restdocs.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author 한승룡
 * @since 2019-02-14
 */
@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Transactional
public class UserApiTests {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	@Autowired
	private UserService userService;

	@BeforeEach
	public void before(RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
			.apply(documentationConfiguration(restDocumentation))
			.build();
	}

	@Test
	public void postUsers() throws Exception {
		Map<String, String> user = new HashMap<>();
		user.put("name", "유저1");
		user.put("nickname", "별명1");
		String json = new ObjectMapper().writeValueAsString(user);

		this.mockMvc.perform(
			post("/api/users")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(json))
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("{class-name}/{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
				UserSnippetHelper.reqFields(),
				UserSnippetHelper.resFields(false)
			))
			.andExpect(jsonPath("$.id", notNullValue()))
			.andExpect(jsonPath("$.name", is(user.get("name"))))
			.andExpect(jsonPath("$.nickname", is(user.get("nickname"))));
	}

	@Test
	public void getUsersById() throws Exception {
		User user1 = userService.add("유저1", "별명1");

		this.mockMvc.perform(
			get("/api/users/{id}", user1.getId())
				.accept(MediaType.APPLICATION_JSON_UTF8))
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("{class-name}/{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
				UserSnippetHelper.pathParams(),
				UserSnippetHelper.resFields(false)
			))
			.andExpect(jsonPath("$.id", is(user1.getId())))
			.andExpect(jsonPath("$.name", is(user1.getName())))
			.andExpect(jsonPath("$.nickname", is(user1.getNickname())));
	}

	@Test
	public void getUsers() throws Exception {
		User user1 = userService.add("유저1", "별명1");
		User user2 = userService.add("유저2", null);

		this.mockMvc.perform(
			get("/api/users")
				.accept(MediaType.APPLICATION_JSON_UTF8))
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("{class-name}/{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
				UserSnippetHelper.resFields(true)
			))
			.andExpect(jsonPath("$.[0].id", is(user1.getId())))
			.andExpect(jsonPath("$.[0].name", is(user1.getName())))
			.andExpect(jsonPath("$.[0].nickname", is(user1.getNickname())))
			.andExpect(jsonPath("$.[1].id", is(user2.getId())))
			.andExpect(jsonPath("$.[1].name", is(user2.getName())))
			.andExpect(jsonPath("$.[1].nickname", nullValue()));
	}
}
