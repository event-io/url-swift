package io.events.mocks;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import io.events.dto.LinkShorteningCreationDTO;
import io.events.dto.LinkShorteningResponseDTO;
import io.smallrye.mutiny.Uni;

public class MockSupabaseUrlSwiftClient {

    public static final String ORIGINAL_LINK_SAD_PATH = "http://www.example.com/path/to/resource?param1=value1&param2=value2&param3=value3&param4=value4&param5=value5&param6=value6&param7=value7&param8=value8&param9=value9&param10=value10&param11=value11&param12=value12&param13=value13&param14=value14&param15=value15&param16=value16&param17=value17&param18=value18&param19=value19&param20=value20&param21=value21&param22=value22&param23=value23&param24=value24&param25=value25&param26=value26&param27=value27&param28=value28&param29=value29&param30=value30&param31=value31&param32=value32&param33=value33";

    public static final LinkShorteningCreationDTO LINK_SHORTENING_CREATION_DTO_HAPPY_PATH = new LinkShorteningCreationDTO(
        "http://example-mock.com/1", "LCZZZ99"
    );

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

    public static CompletionStage<Void> mockCreateResponseHappyPath() {
        return Uni.createFrom().voidItem().subscribeAsCompletionStage();
    }
    
}
