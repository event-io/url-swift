package io.events.mocks;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import io.events.dto.LinkShorteningCreationDTO;
import io.events.dto.LinkShorteningResponseDTO;
import io.smallrye.mutiny.Uni;

public class MockUrlSwiftService {
    
    public static final Uni<LinkShorteningResponseDTO> LINK_SHORTENING_RESPONSE_DTO_HAPPY_PATH = Uni.createFrom().item(
        new LinkShorteningResponseDTO("ABCEDFG", "https://urlswift.io/shortened", LocalDateTime.of(2023, 11, 10, 0, 0, 0).toInstant(ZoneOffset.UTC))
    );
    
    public static final Uni<LinkShorteningCreationDTO> LINK_SHORTENING_CREATION_DTO_HAPPY_PATH = Uni.createFrom().item(
        new LinkShorteningCreationDTO("https://urlswift.io/shortened", "ABCEDFG")
    );
}
