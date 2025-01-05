package urlshortener.config.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Url Shortener Api",
                description = "To reduce your links",
                version = "1.0.0"
        )
)
public class OpenApiConfig {
}
