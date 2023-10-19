package io.events.mocks;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.CompletionStage;

import io.events.dto.Base62KeyDTO;
import io.smallrye.mutiny.Uni;

public class MockBase62KeyGeneratorClient {

    public static final String KEY_HAPPY_PATH = "LCZZZ99";
    public static final Instant CREATED_AT_HAPPY_PATH = LocalDateTime.of(2023, 01, 01, 0, 0, 0).toInstant(ZoneOffset.UTC);
    
    public static CompletionStage<Base62KeyDTO> mockKeyGenerationResponseHappyPath() {
        return Uni.createFrom().item(
            new Base62KeyDTO(KEY_HAPPY_PATH, CREATED_AT_HAPPY_PATH)
        ).subscribeAsCompletionStage();
    }
}
