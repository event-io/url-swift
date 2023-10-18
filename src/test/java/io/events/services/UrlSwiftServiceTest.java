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
import org.junit.jupiter.api.Test;

import io.events.dto.LinkShorteningResponseDTO;
import io.events.dto.TokenRequestDTO;
import io.events.exceptions.OriginalUrlMatchException;
import io.events.exceptions.RedirectUrlMatchException;
import io.events.mocks.MockSupabaseClient;
import io.events.mocks.MockSupabaseUrlSwiftClient;
import io.events.restclients.SupabaseClient;
import io.events.restclients.SupabaseUrlSwiftClient;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
@QuarkusTest
public class UrlSwiftServiceTest {

    @InjectMock
    @RestClient
    private SupabaseClient mockSupabaseClient;

    @InjectMock
    @RestClient
    private SupabaseUrlSwiftClient mockSupabaseUrlSwiftClient;

    @Inject
    private UrlSwiftService urlSwiftService;

    @BeforeEach
    public void setUp() {
        //Mock
        when(mockSupabaseClient.token(
            any(),
            any(String.class),
            any(TokenRequestDTO.class))
        ).thenReturn(MockSupabaseClient.mockTokenResponseHappyPath);
    }

    @Test
    public void testGetRedirectURL_shouldReceiveURL() {
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
            originalUrl = urlSwiftService.getRedirectURL(happyPathShortenedUrl)
                .subscribeAsCompletionStage().get();
        } catch (InterruptedException|ExecutionException e) {
            fail("Should not fail");
        }

        //Assert
        assertEquals(happyPathCorrectOriginalUrl, originalUrl, "Should be equal");
    }

    @Test
    public void testGetRedirectURL_shouldNotFound() {
        //Mock        
        when(mockSupabaseUrlSwiftClient.getByShortened(
            anyString(),
            anyString()
        )).thenReturn(MockSupabaseUrlSwiftClient.mockGetByShortenedResponseSadPath());

        //Assign
        String shortenedUrl = "ABCDE01";
        
        //Act
        try {
            urlSwiftService.getRedirectURL(shortenedUrl)
                .subscribeAsCompletionStage().get();
            fail("Should be thrown an exception");
        } catch (InterruptedException|ExecutionException e) {
            RuntimeException exception = (RuntimeException) e.getCause();

            //Assert
            assertEquals(RedirectUrlMatchException.class, exception.getCause().getClass(), "Should be equal");
        }
    }

    @Test
    public void testGetInfo_WithSabouter_shouldHandleError() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetInfo_shouldReceiveInfo() {
        //Mock
        when(mockSupabaseUrlSwiftClient.getInfoByOriginal(
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
    public void testGetInfo_shouldNotFound() {
        //Mock        
        when(mockSupabaseUrlSwiftClient.getInfoByOriginal(
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
            assertEquals(OriginalUrlMatchException.class, exception.getCause().getClass(), "Should be equal");
        }
    }

    @Test
    public void testGetRedirectURL_WithSabouter_shouldHandleError() {
        fail("Not yet implemented");
    }

    @Test
    public void testCreateShortenedURL_shouldReceiveURL() {
        fail("Not yet implemented");
    }

    @Test
    public void testCreateShortenedURL_shouldBeInputIncorrect() {
        fail("Not yet implemented");
    }

    @Test
    public void testCreateShortenedURL_shouldBeAConflict() {
        fail("Not yet implemented");
    }

    @Test
    public void testCreateShortenedURL_withB62KGSabouter_shouldHandlerError() {
        fail("Not yet implemented");
    }

    @Test
    public void testCreateShortenedURL_withSabouter_shouldHandlerError() {
        fail("Not yet implemented");
    }
    
}
