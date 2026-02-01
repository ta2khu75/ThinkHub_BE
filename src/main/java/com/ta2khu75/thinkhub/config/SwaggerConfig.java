//package com.ta2khu75.thinkhub.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.method.HandlerMethod;
//
//import com.ta2khu75.thinkhub.shared.util.StringUtil;
//
//import io.swagger.v3.oas.models.Operation;
//import lombok.RequiredArgsConstructor;
//
//import org.springdoc.core.customizers.OperationCustomizer;
//
//@Configuration
//@RequiredArgsConstructor
//public class SwaggerConfig {
//	@Bean
//	OperationCustomizer operationCustomizer() {
//		return (Operation operation, HandlerMethod handlerMethod) -> {
////			// Lấy tag đầu tiên (nếu có)
//			if (operation.getTags() != null && !operation.getTags().isEmpty()) {
////				String tag = operation.getTags() != null && !operation.getTags().isEmpty()
////						? operation.getTags().get(0)
////						: handlerMethod.getBeanType().getSimpleName();
//				String tag = operation.getTags().get(0).toLowerCase();
//				String tagOpId = operation.getTags().get(0).toUpperCase().replace(" ", "_");
//				this.customOperationId(tagOpId, operation, handlerMethod);
//				this.customSummary(tag, operation);
//				this.customDescription(tag, operation);
//			}
//			return operation;
//		};
//	}
//
//	private void customDescription(String tag, Operation operation) {
//		String description = operation.getDescription();
//		// Nếu chưa có description, thì bỏ qua
//		if (description == null || description.isBlank() || !description.contains("%s")) {
//			return;
//		}
//		// Prefix tag name vào operationId (nếu chưa có)
//		operation.setDescription(String.format(description, tag.toLowerCase()));
//	}
//
//	private void customOperationId(String tag, Operation operation, HandlerMethod handlerMethod) {
//		String opId = operation.getOperationId();
//		// Nếu chưa có operationId, sinh mặc định theo method name
//		if (opId == null || opId.isBlank()) {
//			opId = StringUtil.toUpperSnakeCase(handlerMethod.getMethod().getName().toUpperCase());
//		}
//		// Prefix tag name vào operationId (nếu chưa có)
//		operation.setOperationId(tag + "::" + opId.toUpperCase());
//	}
//
//	private void customSummary(String tag, Operation operation) {
//		String summary = operation.getSummary();
//		// Nếu chưa có summary thì bỏ qua
//		if (summary == null || summary.isBlank() || !summary.contains("%s")) {
//			return;
//		}
//		operation.setSummary(String.format(summary, tag.toLowerCase()));
//	}
//}
package com.ta2khu75.thinkhub.config;


