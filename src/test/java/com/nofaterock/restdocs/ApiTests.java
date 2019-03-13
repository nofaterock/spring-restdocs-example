package com.nofaterock.restdocs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nofaterock.restdocs.user.domain.User;
import com.nofaterock.restdocs.user.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit4.SpringRunner;
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
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
public class ApiTests {

	@Rule
	public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;
	private RestDocumentationResultHandler document;

	@Autowired
	private UserService userService;

	@Before
	public void before() {
		this.document = document(
			"{class-name}/{method-name}",
			preprocessResponse(prettyPrint())
		);

		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
			.apply(documentationConfiguration(this.restDocumentation))
			.alwaysDo(document)
			.build();
	}

	@After
	public void after() {

	}

	@Test
	public void users_add() throws Exception {
		Map<String, String> user = new HashMap<>();
		user.put("name", "유저1");
		user.put("nickname", "별명1");
		String json = new ObjectMapper().writeValueAsString(user);

		this.mockMvc.perform(
			post("/api/users")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(json))
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document.document(
				UserSnippetHelper.userRequestFields(),
				UserSnippetHelper.userResponseFields(false)
			))
			.andExpect(jsonPath("$.id", notNullValue()))
			.andExpect(jsonPath("$.name", is(user.get("name"))))
			.andExpect(jsonPath("$.nickname", is(user.get("nickname"))));
	}

	@Test
	public void users_id() throws Exception {
		User user1 = userService.add("유저1", "별명1");

		this.mockMvc.perform(
			get("/api/users/{id}", user1.getId()))
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document.document(
				UserSnippetHelper.userPathParameters(),
				UserSnippetHelper.userResponseFields(false)
			))
			.andExpect(jsonPath("$.id", is(user1.getId())))
			.andExpect(jsonPath("$.name", is(user1.getName())))
			.andExpect(jsonPath("$.nickname", is(user1.getNickname())));
	}

	@Test
	public void users() throws Exception {
		User user1 = userService.add("유저1", "별명1");
		User user2 = userService.add("유저2", null);

		this.mockMvc.perform(
			get("/api/users"))
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document.document(
				UserSnippetHelper.userResponseFields(true)
			))
			.andExpect(jsonPath("$.[0].id", is(user1.getId())))
			.andExpect(jsonPath("$.[0].name", is(user1.getName())))
			.andExpect(jsonPath("$.[0].nickname", is(user1.getNickname())))
			.andExpect(jsonPath("$.[1].id", is(user2.getId())))
			.andExpect(jsonPath("$.[1].name", is(user2.getName())))
			.andExpect(jsonPath("$.[1].nickname", nullValue()));
	}
}
