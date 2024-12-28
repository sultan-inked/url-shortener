package urlshortener.service.url;

import urlshortener.entity.Hash;
import urlshortener.repository.cache.UrlCacheRepository;
import urlshortener.repository.url.UrlRepository;
import urlshortener.service.hash.HashService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UrlCleanerService {
    private final UrlCacheRepository urlCacheRepository;
    private final UrlRepository urlRepository;
    private final HashService hashService;

    @Value("${app.scheduler.url_cleaner.url_lifetime_days}")
    private int days;

    @Transactional
    public void removeExpiredUrlsAndResaveHashes() {
        List<String> hashes = urlRepository.getAndDeleteUrlsByDate(LocalDateTime.now().minusDays(days));
        List<Hash> hashesEntity = hashes.stream()
                .map(Hash::new)
                .toList();
        hashService.saveAllBatch(hashesEntity);
        urlCacheRepository.deleteAll(hashes);
    }
}
