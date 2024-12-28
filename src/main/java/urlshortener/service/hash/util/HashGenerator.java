package urlshortener.service.hash.util;

import urlshortener.entity.Hash;
import urlshortener.service.hash.HashService;
import urlshortener.util.encode.Base62Encoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class HashGenerator {
    private final HashService hashService;
    private final Base62Encoder encoder;

    public HashGenerator(@Lazy HashService hashService, Base62Encoder encoder) {
        this.hashService = hashService;
        this.encoder = encoder;
    }

    @Value("${app.hash_generator.db_hashes_limit}")
    private long dbHashesLimit;

    public void generate() {
        Long dbHashesSize = hashService.getHashesSize();

        if (dbHashesSize < dbHashesLimit) {
            List<Long> numbers = hashService.getUniqueNumbers(dbHashesLimit - dbHashesSize);
            List<String> hashes = encoder.encode(numbers);

            List<Hash> hashesEntity = hashes.stream()
                    .map(Hash::new)
                    .toList();

            hashService.saveAllBatch(hashesEntity);
        }
    }

    public List<String> generateAndGet(int size) {
        List<Long> numbers = hashService.getUniqueNumbers(size);

        return encoder.encode(numbers);
    }
}
