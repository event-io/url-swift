package io.events.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;

import io.events.dto.LinkShorteningCreationDTO;
import io.events.dto.LinkShorteningResponseDTO;
import io.events.exceptions.RedirectUrlMatchException;
import io.events.mocks.MockUrlSwiftService;
import io.events.services.UrlSwiftService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;

@QuarkusTest
class UrlSwiftResourceTest {

    @InjectMock
    private UrlSwiftService mockUrlSwiftService;

    @Inject
    private UrlSwiftResource urlSwiftResource;

    @Test
    void testRedirectURLShortner_shouldRedirect() {
        //Mock
        when(mockUrlSwiftService.getInfo(anyString())
        ).thenReturn(MockUrlSwiftService.LINK_SHORTENING_RESPONSE_DTO_HAPPY_PATH);

        //Assign
        String happyShortnerPath = "ABCEDFG";
        int happyResponseCode = 303;
        String happyRedirectOriginalUrl = "https://urlswift.io/shortened";


        //Act
        Response response = null;
        try {
            response = this.urlSwiftResource.redirect(happyShortnerPath).subscribe().asCompletionStage().get();
        } catch(InterruptedException|ExecutionException e) {
            fail("Should not throw exception");
        }

        //Assert
        assertEquals(happyResponseCode, response.getStatus());
        assertEquals(happyRedirectOriginalUrl, response.getLocation().toString());
    }

    @Test
    void testRedirectURLShortner_shouldShortnerPathBeIncorrect() {
        //Mock
        when(mockUrlSwiftService.getInfo(anyString())
        ).thenReturn(MockUrlSwiftService.LINK_SHORTENING_RESPONSE_DTO_HAPPY_PATH);

        //Assign
        String sadShortnerPath = "ABCEDFG1GASD";

        //Act
        Response response = null;
        try {
            response = this.urlSwiftResource.redirect(sadShortnerPath).subscribe().asCompletionStage().get();
        } catch(InterruptedException|ExecutionException e) {
            fail("Should not throw exception");
        }

        //Assert
        assertEquals(400, response.getStatus());
    }

    @Test
    void testRedirectURLShortner_shouldShortnerPathNotFound() {
        //Mock
        when(mockUrlSwiftService.getInfo(anyString())
        ).thenThrow(new RedirectUrlMatchException("Shortned path not found"));

        //Assign
        String happyShortnerPath = "ABCEDFG";

        //Act
        Response response = null;
        try {
            response = this.urlSwiftResource.redirect(happyShortnerPath).subscribe().asCompletionStage().get();
        } catch(InterruptedException|ExecutionException e) {
            fail("Should not throw exception");
        }

        //Assert
        assertEquals(404, response.getStatus());
    }

    
    @Test
    void testRedirectURLShortner_shouldReceiveServerError() {
        //Mock
        when(mockUrlSwiftService.getInfo(anyString())
        ).thenThrow(new RuntimeException("Unknown Error"));

        //Assign
        String happyShortnerPath = "ABCDEFG";

        //Act
        Response response = null;
        try {
            response = this.urlSwiftResource.redirect(happyShortnerPath).subscribe().asCompletionStage().get();
        } catch(InterruptedException|ExecutionException e) {
            fail("Should not throw exception");
        }

        //Assert
        assertEquals(500, response.getStatus());
    }

    @Test
    void testInfoURLShortner_shouldRedirect() {
        //Mock
        when(mockUrlSwiftService.getInfo(anyString())
        ).thenReturn(MockUrlSwiftService.LINK_SHORTENING_RESPONSE_DTO_HAPPY_PATH);

        //Assign
        int happyResponseCode = 200;
        String happyShortnerPath = "ABCEDFG";
        String happyRedirectOriginalUrl = "https://urlswift.io/shortened";
        Instant happyCreatedAt = LocalDateTime.of(2023, 11, 10, 0, 0, 0).toInstant(ZoneOffset.UTC);


        //Act
        Response response = null;
        try {
            response = this.urlSwiftResource.info(happyShortnerPath).subscribe().asCompletionStage().get();
        } catch(InterruptedException|ExecutionException e) {
            fail("Should not throw exception");
        }

        //Assert
        assertEquals(happyResponseCode, response.getStatus());
        assertEquals(happyRedirectOriginalUrl, response.readEntity(LinkShorteningResponseDTO.class).getOriginalLink());
        assertEquals(happyShortnerPath, response.readEntity(LinkShorteningResponseDTO.class).getShortnedLink());
        assertEquals(happyCreatedAt, response.readEntity(LinkShorteningResponseDTO.class).getCreatedAt());
    }

    @Test
    void testInfoURLShortner_shouldShortnerPathBeIncorrect() {
        //Mock
        when(mockUrlSwiftService.getInfo(anyString())
        ).thenReturn(MockUrlSwiftService.LINK_SHORTENING_RESPONSE_DTO_HAPPY_PATH);

        //Assign
        String sadShortnerPath = "ABCEDFG1GASD";

        //Act
        Response response = null;
        try {
            response = this.urlSwiftResource.info(sadShortnerPath).subscribe().asCompletionStage().get();
        } catch(InterruptedException|ExecutionException e) {
            fail("Should not throw exception");
        }

        //Assert
        assertEquals(400, response.getStatus());
    }

    @Test
    void testInfoURLShortner_shouldShortnerPathNotFound() {
        //Mock
        when(mockUrlSwiftService.getInfo(anyString())
        ).thenThrow(new RedirectUrlMatchException("Shortned path not found"));

        //Assign
        String happyShortnerPath = "ABCEDFG";

        //Act
        Response response = null;
        try {
            response = this.urlSwiftResource.info(happyShortnerPath).subscribe().asCompletionStage().get();
        } catch(InterruptedException|ExecutionException e) {
            fail("Should not throw exception");
        }

        //Assert
        assertEquals(404, response.getStatus());
    }

    
    @Test
    void testInfoURLShortner_shouldReceiveServerError() {
        //Mock
        when(mockUrlSwiftService.getInfo(anyString())
        ).thenThrow(new RuntimeException("Unknown Error"));

        //Assign
        String happyShortnerPath = "ABCDEFG";

        //Act
        Response response = null;
        try {
            response = this.urlSwiftResource.info(happyShortnerPath).subscribe().asCompletionStage().get();
        } catch(InterruptedException|ExecutionException e) {
            fail("Should not throw exception");
        }

        //Assert
        assertEquals(500, response.getStatus());
    }

    @Test
    void testCreateURLShortner_shouldCreate() {
        //Mock
        when(mockUrlSwiftService.create(any(LinkShorteningCreationDTO.class))
        ).thenReturn(MockUrlSwiftService.LINK_SHORTENING_CREATION_DTO_HAPPY_PATH);

        //Assing
        LinkShorteningCreationDTO happyShortnerCreationDTO = new LinkShorteningCreationDTO(
            "https://urlswift.io/shortened", null
        );

        //Act
        Response response = null;
        try {
            response = this.urlSwiftResource.create(happyShortnerCreationDTO).subscribe().asCompletionStage().get();
        } catch(InterruptedException|ExecutionException e) {
            fail("Should not throw exception");
        }

        //Assert
        assertEquals(200, response.getStatus());
        assertEquals(happyShortnerCreationDTO.getOriginalLink(), response.readEntity(LinkShorteningCreationDTO .class).getOriginalLink());
        assertEquals("ABCEDFG", response.readEntity(LinkShorteningCreationDTO .class).getShortnedLink());
    }

    @Test
    void testCreateURLShortner_shouldInputBeIncorrect() {
        //Mock
        when(mockUrlSwiftService.create(any(LinkShorteningCreationDTO.class))
        ).thenReturn(MockUrlSwiftService.LINK_SHORTENING_CREATION_DTO_HAPPY_PATH);

        //Assing
        LinkShorteningCreationDTO sadhortnerCreationDTO = new LinkShorteningCreationDTO(
            "https:/urlswift.io/shorten%d", null
        );

        //Act
        assertThrows(ConstraintViolationException.class, () -> {
            this.urlSwiftResource.create(sadhortnerCreationDTO).subscribe().asCompletionStage().get();
        });
    }

    @Test
    void testCreateURLShortner_shouldReceiveServerError() {
         //Mock
        when(mockUrlSwiftService.create(any(LinkShorteningCreationDTO.class))
        ).thenThrow(new RuntimeException("Unknown Error"));

        //Assing
        LinkShorteningCreationDTO happyShortnerCreationDTO = new LinkShorteningCreationDTO(
            "https://urlswift.io/shortened", null
        );

        //Act
        Response response = null;
        try {
            response = this.urlSwiftResource.create(happyShortnerCreationDTO).subscribe().asCompletionStage().get();
        } catch(InterruptedException|ExecutionException e) {
            fail("Should not throw exception");
        }

        //Assert
        assertEquals(500, response.getStatus());
    }
    
}
