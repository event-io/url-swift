package io.events.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.ExecutionException;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.events.dto.LinkShorteningCreationDTO;
import io.events.dto.LinkShorteningResponseDTO;
import io.events.dto.TokenRequestDTO;
import io.events.exceptions.RedirectUrlMatchException;
import io.events.mocks.MockBase62KeyGeneratorClient;
import io.events.mocks.MockSupabaseClient;
import io.events.mocks.MockSupabaseUrlSwiftClient;
import io.events.restclients.Base62KeyGeneratorClient;
import io.events.restclients.SupabaseClient;
import io.events.restclients.SupabaseUrlSwiftClient;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
@QuarkusTest
class UrlSwiftServiceTest {

    @InjectMock
    @RestClient
    private SupabaseClient mockSupabaseClient;

    @InjectMock
    @RestClient
    private SupabaseUrlSwiftClient mockSupabaseUrlSwiftClient;

    @InjectMock
    @RestClient
    private Base62KeyGeneratorClient mockBase62KeyGeneratorClient; 

    @Inject
    private UrlSwiftService urlSwiftService;

    @BeforeEach
    void setUp() {
        //Mock
        when(mockSupabaseClient.token(
            any(),
            any(String.class),
            any(TokenRequestDTO.class))
        ).thenReturn(MockSupabaseClient.mockTokenResponseHappyPath);
    }

    @Test
    void testGetRedirectURL_shouldReceiveURL() {
        //Mock
        when(mockSupabaseUrlSwiftClient.getByShortened(
            anyString(),
            anyString()
        )).thenReturn(MockSupabaseUrlSwiftClient.mockGetByShortenedResponseHappyPath());

        //Assign
        String happyPathShortenedUrl = "ABCDE01";
        String happyPathCorrectOriginalUrl = "http://example-mock.com/1";

        //Act
        String originalUrl = "";
        try {
            originalUrl = urlSwiftService.getInfo(happyPathShortenedUrl)
                .subscribeAsCompletionStage().get().getOriginalLink();
        } catch (InterruptedException|ExecutionException e) {
            fail("Should not fail");
        }

        //Assert
        assertEquals(happyPathCorrectOriginalUrl, originalUrl, "Should be equal");
    }

    @Test
    void testGetRedirectURL_shouldNotFound() {
        //Mock        
        when(mockSupabaseUrlSwiftClient.getByShortened(
            anyString(),
            anyString()
        )).thenReturn(MockSupabaseUrlSwiftClient.mockGetByShortenedResponseSadPath());

        //Assign
        String shortenedUrl = "ABCDE01";
        
        //Act
        try {
            urlSwiftService.getInfo(shortenedUrl)
                .subscribeAsCompletionStage().get();
            fail("Should be thrown an exception");
        } catch (InterruptedException|ExecutionException e) {
            RuntimeException exception = (RuntimeException) e.getCause();

            //Assert
            assertEquals(RedirectUrlMatchException.class, exception.getClass(), "Should be equal");
        }
    }

    @Disabled("Disabled until RestClientException is implemented")
    @Test
    void testGetInfo_WithSabouter_shouldHandleError() {
        fail("Not yet implemented");
    }

    @Test
    void testGetInfo_shouldReceiveInfo() {
        //Mock
        when(mockSupabaseUrlSwiftClient.getByShortened(
            anyString(),
            anyString()
        )).thenReturn(MockSupabaseUrlSwiftClient.mockGetByShortenedResponseHappyPath());

        //Assign
        String happyPathShortenedUrl = "ABCDE01";
        String happyPathCorrectOriginalUrl = "http://example-mock.com/1";
        Instant happyCreatedAt = LocalDateTime.of(2023, 10, 19, 0, 0, 0).toInstant(ZoneOffset.UTC);

        //Act
        LinkShorteningResponseDTO shorteningResponseDTO = null;
        try {
            shorteningResponseDTO = urlSwiftService.getInfo(happyPathCorrectOriginalUrl)
                .subscribeAsCompletionStage().get();
        } catch (InterruptedException|ExecutionException e) {
            fail("Should not fail");
        }

        //Assert
        assertNotNull(shorteningResponseDTO, "Should not be null");
        assertEquals(shorteningResponseDTO.getOriginalLink(), happyPathCorrectOriginalUrl, "Should originalLink be equal");
        assertEquals(shorteningResponseDTO.getShortnedLink(), happyPathShortenedUrl, "Should shortenedLink be equal");
        assertEquals(shorteningResponseDTO.getCreatedAt(), happyCreatedAt, "Should createdAt be equal");
    }

    @Test
    void testGetInfo_shouldNotFound() {
        //Mock        
        when(mockSupabaseUrlSwiftClient.getByShortened(
            anyString(),
            anyString()
        )).thenReturn(MockSupabaseUrlSwiftClient.mockGetByShortenedResponseSadPath());

        //Assign
        String originalUrl = "http://example-mock.com/1";
        
        //Act
        try {
            urlSwiftService.getInfo(originalUrl)
                .subscribeAsCompletionStage().get();
            fail("Should be thrown an exception");
        } catch (InterruptedException|ExecutionException e) {
            RuntimeException exception = (RuntimeException) e.getCause();

            //Assert
            assertEquals(RedirectUrlMatchException.class, exception.getClass(), "Should be equal");
        }
    }

    @Disabled("Disabled until RestClientException is implemented")
    @Test
    void testGetRedirectURL_WithSabouter_shouldHandleError() {
        fail("Not yet implemented");
    }

    @Test
    void testCreateShortenedURL_shouldReceiveURL() {
        //Mock
        when(mockBase62KeyGeneratorClient.generateKey(""))
            .thenReturn(MockBase62KeyGeneratorClient.mockKeyGenerationResponseHappyPath());

        when(mockSupabaseUrlSwiftClient.create(
            "",
            MockSupabaseUrlSwiftClient.LINK_SHORTENING_CREATION_DTO_HAPPY_PATH
        )).thenReturn(MockSupabaseUrlSwiftClient.mockCreateResponseHappyPath());

        //Assing
        LinkShorteningCreationDTO happyPathLinkShorteningCreationDTO = new LinkShorteningCreationDTO(
            "http://example-mock.com/1", null
        );
        String happyPathShortenedUrl = "LCZZZ99";

        //Act
        LinkShorteningCreationDTO linkShorteningCreationDTO = null;
        try {
            linkShorteningCreationDTO = urlSwiftService.create(happyPathLinkShorteningCreationDTO)
                .subscribeAsCompletionStage().get();
        } catch (InterruptedException|ExecutionException e) {
            fail("Should not fail");
        }

        //Assert
        assertNotNull(linkShorteningCreationDTO, "Should not be null");
        assertEquals(linkShorteningCreationDTO.getOriginalLink(), happyPathLinkShorteningCreationDTO.getOriginalLink(), "Should originalLink be equal");
        assertEquals(linkShorteningCreationDTO.getShortnedLink(), happyPathShortenedUrl, "Should shortenedLink be equal");
    }

    @Test
    void testCreateShortenedURL_shouldBeInputIncorrect() {
        when(mockBase62KeyGeneratorClient.generateKey(""))
            .thenThrow(new RuntimeException("Base62KeyGenerator method should not be called"));

        when(mockSupabaseUrlSwiftClient.create(anyString(), any(LinkShorteningCreationDTO.class)
        )).thenThrow(new RuntimeException("UrlSwift creation method should not be called"));

        //Assing
        LinkShorteningCreationDTO sadPathLinkShorteningCreationDTO = new LinkShorteningCreationDTO(
            MockSupabaseUrlSwiftClient.ORIGINAL_LINK_SAD_PATH, null
        );

        //Act
         try {
            urlSwiftService.create(sadPathLinkShorteningCreationDTO)
                .subscribeAsCompletionStage().get();
            fail("Should fail");
        } catch (InterruptedException|ExecutionException|ConstraintViolationException e) {
            //Assert
            assertEquals(ConstraintViolationException.class, e.getClass(), "Should be thrown ConstraintViolationException");
            
        }

    }

    @Disabled("Disabled until RestClientException is implemented")
    @Test
    void testCreateShortenedURL_shouldBeAConflict() {
        fail("Not yet implemented");
    }

    @Disabled("Disabled until RestClientException is implemented")
    @Test
    void testCreateShortenedURL_withB62KGSabouter_shouldHandlerError() {
        fail("Not yet implemented");
    }

    @Disabled("Disabled until RestClientException is implemented")
    @Test
    void testCreateShortenedURL_withSabouter_shouldHandlerError() {
        fail("Not yet implemented");
    }
    
}
