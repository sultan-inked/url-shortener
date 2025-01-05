package urlshortener.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import urlshortener.service.url.UrlService;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "URL controller", description = "Main URL controller for short URL creation and redirection")
@RequiredArgsConstructor
@RestController
@Validated
public class UrlController {
    private final UrlService urlService;

    @Operation(summary = "Short URL creation", description = "Send long URL to get long")
    @PostMapping("/url")
    public String createHashUrl(@RequestParam(name = "url") @NotEmpty @URL
                                @Parameter(description = "Primal URL", required = true) String url) {
        return urlService.createHashUrl(url);
    }

    @Operation(summary = "Redirect by short URL", description = "Redirect to prime URL by short URL")
    @GetMapping("/{hash}")
    public ResponseEntity<Void> redirect(@PathVariable @Parameter(description = "Unique hash") String hash) {
        String primalUri = urlService.getPrimalUri(hash);
        return ResponseEntity
                .status(302)
                .header("Location", primalUri)
                .build();
    }
}
