package io.events.mocks;

import java.util.concurrent.CompletionStage;

import io.events.dto.TokenResponseDTO;
import io.smallrye.mutiny.Uni;

public class MockSupabaseClient {

    public static CompletionStage<TokenResponseDTO> mockTokenResponseHappyPath = Uni.createFrom().item(
        new TokenResponseDTO(
            "MOCK_ACCESS_TOKEN",
            "MOCK_TOKEN_TYPE",
            Integer.MAX_VALUE,
            Long.MAX_VALUE,
            "MOCK_REFRESH_TOKEN"            
        )
    ).subscribeAsCompletionStage();
    
}
