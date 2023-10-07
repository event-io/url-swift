package io.events.restclients;

import java.util.Set;
import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestForm;

import io.events.dto.LinkShorteningCreationDTO;
import io.events.dto.LinkShorteningResponseDTO;
import io.events.factory.RequestUUIDHeaderFactory;
import io.quarkus.rest.client.reactive.NotBody;
import io.vertx.core.Closeable;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import lombok.NonNull;

@Path("/rest/v1/link_shortening_registry")
@RegisterRestClient(configKey = "url_swift")
@RegisterClientHeaders(RequestUUIDHeaderFactory.class)
@ClientHeaderParam(name = "apiKey", value = "${supabase.url_swift.api_key}")
@ClientHeaderParam(name = "Authorization", value = "Bearer {token}") 
public interface SupabaseUrlSwiftClient extends Closeable {
    
    @GET
    @ClientHeaderParam(name = "Accept-Profile", value = "${supabase.url_swift.authority.schema}")
    CompletionStage<Set<LinkShorteningResponseDTO>> getById(
        @NotBody String token,
        @NonNull @QueryParam("shortened_link") String shortenedLink
    );


    @POST
    @ClientHeaderParam(name = "Content-Profile", value = "${supabase.url_swift.authority.schema}")
    @ClientHeaderParam(name = "Prefer", value = "missing=default")
    CompletionStage<Void> create(
        @NotBody String token,
        @RestForm LinkShorteningCreationDTO linkShorteningCreationDTO
    );

}
