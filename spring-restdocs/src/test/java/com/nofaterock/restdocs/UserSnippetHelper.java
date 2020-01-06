package com.nofaterock.restdocs;

import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.PathParametersSnippet;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;

/**
 * @author 한승룡
 * @since 2019-03-13
 */
public interface UserSnippetHelper {

	static PathParametersSnippet pathParams() {
		return pathParameters(
			parameterWithName("id").description("사용자 ID")
		);
	}

	static RequestFieldsSnippet reqFields() {
		return requestFields(
			fieldWithPath("name").type(JsonFieldType.STRING).description("이름")
				.attributes(key("format").value("20자 이내")),
			fieldWithPath("nickname").type(JsonFieldType.STRING).description("별명").optional()
				.attributes(key("format").value("50자 이내"))
		);
	}

	static ResponseFieldsSnippet resFields(boolean isArray) {
		return responseFields(
			fieldWithPath((isArray ? "[]." : "") + "id").type(JsonFieldType.NUMBER).description("사용자 ID"),
			fieldWithPath((isArray ? "[]." : "") + "name").type(JsonFieldType.STRING).description("이름")
				.attributes(key("format").value("20자 이내")),
			fieldWithPath((isArray ? "[]." : "") + "nickname").type(JsonFieldType.STRING).description("별명").optional()
				.attributes(key("format").value("50자 이내")),
			fieldWithPath((isArray ? "[]." : "") + "created").type(JsonFieldType.STRING).description("생성일자")
				.attributes(key("format").value("LocalDateTime"))
		);
	}

}
