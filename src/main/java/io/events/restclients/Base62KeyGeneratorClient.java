package io.events.restclients;

import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.events.dto.Base62KeyDTO;
import io.events.factory.RequestUUIDHeaderFactory;
import io.quarkus.rest.client.reactive.NotBody;
import io.vertx.core.Closeable;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/rest/v1")
@RegisterRestClient(configKey = "base62_key_generator")
@RegisterClientHeaders(RequestUUIDHeaderFactory.class)
public interface Base62KeyGeneratorClient extends Closeable {
    
    @POST
    @Path("/rpc/get_key")
    @ClientHeaderParam(name = "apiKey", value = "${supabase.base62_key_generator.api_key}")
    @ClientHeaderParam(name = "Authorization", value = "Bearer {token}")
    @ClientHeaderParam(name = "Content-Profile", value = "${supabase.base62_key_generator.authority.schema}")
    CompletionStage<Base62KeyDTO> generateKey(@NotBody String token);

}
