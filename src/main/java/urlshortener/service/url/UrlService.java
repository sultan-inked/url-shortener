package urlshortener.service.url;

import urlshortener.entity.Url;
import urlshortener.exception.ApiException;
import urlshortener.exception.ResourceNotFoundException;
import urlshortener.repository.cache.UrlCacheRepository;
import urlshortener.repository.url.UrlRepository;
import urlshortener.service.hash.util.HashCache;
import urlshortener.util.uri.UriBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RequiredArgsConstructor
@Service
public class UrlService {
    private final UrlCacheRepository urlCacheRepository;
    private final UrlRepository urlRepository;
    private final HashCache hashCache;
    private final UriBuilder uriBuilder;

    @Value("${spring.date.ttl.hour.url}")
    private long urlTtlInCache;

    @Transactional
    public String createHashUrl(String uri) {
        String hash = hashCache.getHash()
                .orElseThrow(() -> new ApiException("Hashes is empty, try later", INTERNAL_SERVER_ERROR));

        Url url = Url.builder()
                .hash(hash)
                .url(uri)
                .build();

        urlRepository.save(url);
        urlCacheRepository.saveByTtlInHour(url, urlTtlInCache);

        return uriBuilder.response(hash);
    }

    @Transactional(readOnly = true)
    public String getPrimalUri(String hash) {
        Optional<Url> urlOpt = urlCacheRepository.findByHash(hash);
        if (urlOpt.isPresent()) {
            return urlOpt.get().getUrl();
        }
        Url url = urlRepository.findById(hash).orElseThrow(() ->
                new ResourceNotFoundException("Url by hash: %s not found", hash));

        urlCacheRepository.saveByTtlInHour(url, urlTtlInCache);

        return url.getUrl();
    }
}
