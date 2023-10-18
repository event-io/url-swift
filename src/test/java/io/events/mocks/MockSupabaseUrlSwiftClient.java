package io.events.mocks;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import io.events.dto.LinkShorteningResponseDTO;
import io.smallrye.mutiny.Uni;

public class MockSupabaseUrlSwiftClient {

    public static final String SHORTNED_LINK_HAPPY_PATH = "ABCDE01"; 
    public static final String ORIGINAL_LINK_HAPPY_PATH = "http://example-mock.com/1";
    public static final Instant CREATED_AT_HAPPY_PATH = LocalDateTime.of(2023, 10, 19, 0, 0, 0).toInstant(ZoneOffset.UTC);

    public static CompletionStage<Set<LinkShorteningResponseDTO>> mockGetByShortenedResponseHappyPath() {
        return Uni.createFrom().item(
            Set.of(new LinkShorteningResponseDTO(
                SHORTNED_LINK_HAPPY_PATH,
                ORIGINAL_LINK_HAPPY_PATH,
                CREATED_AT_HAPPY_PATH
            ))
        ).subscribeAsCompletionStage();
    }

    public static CompletionStage<Set<LinkShorteningResponseDTO>> mockGetByShortenedResponseSadPath() {
        Set<LinkShorteningResponseDTO> emptySet = Collections.emptySet();
        return Uni.createFrom().item(emptySet).subscribeAsCompletionStage();
    }
    
}
