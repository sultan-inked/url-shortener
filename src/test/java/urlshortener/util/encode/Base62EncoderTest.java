package urlshortener.util.encode;

import org.junit.jupiter.api.Test;

import static urlshortener.test.utils.TestData.HASHES;
import static urlshortener.test.utils.TestData.NUMBERS;
import static org.assertj.core.api.Assertions.assertThat;

class Base62EncoderTest {
    private final Base62Encoder base62Encoder = new Base62Encoder();

    @Test
    void testEncode_successful() {
        assertThat(base62Encoder.encode(NUMBERS))
                .isNotNull()
                .isEqualTo(HASHES);
    }
}
