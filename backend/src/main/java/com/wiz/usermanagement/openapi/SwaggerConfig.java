package com.wiz.usermanagement.openapi;

import com.wiz.usermanagement.exception.dto.ApiErrorResponse;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

@Configuration
public class SwaggerConfig {

        @Bean
        public OpenAPI customOpenAPI() {

                // Register ApiErrorResponse schema
                Schema<?> errorSchema =
                        new Schema<>().$ref("#/components/schemas/ApiErrorResponse");

                ApiResponse badRequest = new ApiResponse()
                        .description("Bad Request")
                        .content(new Content().addMediaType(
                                MediaType.APPLICATION_JSON_VALUE,
                                new io.swagger.v3.oas.models.media.MediaType().schema(errorSchema)));

                ApiResponse unauthorized = new ApiResponse()
                        .description("Unauthorized")
                        .content(new Content().addMediaType(
                                MediaType.APPLICATION_JSON_VALUE,
                                new io.swagger.v3.oas.models.media.MediaType().schema(errorSchema)));

                ApiResponse forbidden = new ApiResponse()
                        .description("Forbidden")
                        .content(new Content().addMediaType(
                                MediaType.APPLICATION_JSON_VALUE,
                                new io.swagger.v3.oas.models.media.MediaType().schema(errorSchema)));

                ApiResponse notFound = new ApiResponse()
                        .description("Not Found")
                        .content(new Content().addMediaType(
                                MediaType.APPLICATION_JSON_VALUE,
                                new io.swagger.v3.oas.models.media.MediaType().schema(errorSchema)));

                ApiResponse serverError = new ApiResponse()
                        .description("Internal Server Error")
                        .content(new Content().addMediaType(
                                MediaType.APPLICATION_JSON_VALUE,
                                new io.swagger.v3.oas.models.media.MediaType().schema(errorSchema)));

                return new OpenAPI()
                        .info(new Info()
                                .title("User Management API")
                                .version("1.0"))
                        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                        .components(new Components()
                                // JWT security
                                .addSecuritySchemes("bearerAuth",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT"))

                                // Error schema
                                .addSchemas("ApiErrorResponse",
                                        ModelConverters.getInstance()
                                                .read(ApiErrorResponse.class)
                                                .get("ApiErrorResponse"))

                                // Reusable responses
                                .addResponses("BadRequest", badRequest)
                                .addResponses("Unauthorized", unauthorized)
                                .addResponses("Forbidden", forbidden)
                                .addResponses("NotFound", notFound)
                                .addResponses("ServerError", serverError)
                        );
        }

        @Bean
        public GroupedOpenApi authGroup() {
                return GroupedOpenApi.builder()
                        .group("Authentication")
                        .pathsToMatch("/auth/**")
                        .build();
        }

        @Bean
        public GroupedOpenApi userGroup() {
                return GroupedOpenApi.builder()
                        .group("User-Management")
                        .pathsToMatch("/user/**")
                        .build();
        }

        @Bean
        public GroupedOpenApi userApi() {
                return GroupedOpenApi.builder()
                        .group("1-Standard-User")
                        .pathsToMatch("/user/getallusers", "/user/getuserbyid/**", "/user/updateuser/**", "/user/adduser")
                        .build();
        }

        @Bean
        public GroupedOpenApi adminApi() {
                return GroupedOpenApi.builder()
                        .group("2-Administrator")
                        .pathsToMatch("/user/getdeletedusers", "/user/restoreuser/**", "/user/deleteuser/**")
                        .build();
        }
}
