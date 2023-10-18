package io.events.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutionException;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import io.events.dto.TokenRequestDTO;
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

    
    @Test
    public void testGetRedirectURL_shouldReceiveURL() {
        //Mock
        when(mockSupabaseClient.token(
            any(),
            any(String.class),
            any(TokenRequestDTO.class))
        ).thenReturn(MockSupabaseClient.mockTokenResponseHappyPath);

        when(mockSupabaseUrlSwiftClient.getByShortened(
            anyString(),
            anyString()
        )).thenReturn(MockSupabaseUrlSwiftClient.mockGetByShortenedResponseHappyPath());

        //Assign
        String shortenedUrl = "ABCDE01";
        String correctOriginalUrl = "http://example-mock.com/1";

        //Act
        String originalUrl = "";
        try {
            originalUrl = urlSwiftService.getRedirectURL(shortenedUrl)
            .subscribeAsCompletionStage().get();
        } catch (InterruptedException|ExecutionException e) {
            fail("Should not fail");
        }

        //Assert
        assertEquals(correctOriginalUrl, originalUrl, "Should be equal");
    }

    @Test
    public void testGetRedirectURL_shouldNotFound() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetInfo_WithSabouter_shouldHandleError() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetInfo_shouldReceiveInfo() {
      fail("Not yet implemented");
    }

    @Test
    public void testGetInfo_shouldNotFound() {
        fail("Not yet implemented");
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
