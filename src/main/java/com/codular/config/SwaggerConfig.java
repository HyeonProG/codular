package com.codular.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI codularOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Codular API")
                        .description("Codular API Docs")
                        .version("V1"))
                /*
                 * 보안이 필요한 곳은 Controller Operation에 아래 어노테이션 추가
                 * security = { @SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_NAME) }
                 */
                .components(new Components().addSecuritySchemes(SECURITY_SCHEME_NAME,
                        new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }

}
