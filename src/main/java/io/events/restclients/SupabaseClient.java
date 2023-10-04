package io.events.restclients;

import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.events.dto.TokenRequestDTO;
import io.events.dto.TokenResponseDTO;
import io.events.factory.RequestUUIDHeaderFactory;
import io.events.models.SupabaseGrantType;
import io.quarkus.rest.client.reactive.NotBody;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/auth/v1")
@RegisterRestClient(configKey = "supabase-api")
@RegisterClientHeaders(RequestUUIDHeaderFactory.class)
@ClientHeaderParam(name = "apiKey", value = "{apiKey}")
public interface SupabaseClient {
    
    @POST
    @Path("/token")
    @Consumes(MediaType.APPLICATION_JSON)
    CompletionStage<TokenResponseDTO> token(
        @QueryParam("grant_type") SupabaseGrantType grantType,
        @NotBody String apiKey,
        TokenRequestDTO tokenRequestDTO);
    
    
    @POST
    @Path("/logout")
    @ClientHeaderParam(name = "Authorization", value = "Bearer {accessToken}")
    CompletionStage<Void> logout(
        @NotBody String accessToken,
        @NotBody String apiKey
    );

    //TODO: FIX Serialization of enums problems
    // @ClientObjectMapper 
    // static ObjectMapper objectMapper(ObjectMapper defaultObjectMapper) {
    //     ObjectMapper modifiedObjectMapper = defaultObjectMapper.copy()
    //         .configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
    //         .configure(EnumFeature.WRITE_ENUMS_TO_LOWERCASE, true);

    //     SerializationConfig serializationConfig = modifiedObjectMapper.getSerializationConfig();

    //     System.out.println("Active Serialization Features:");
    //     System.out.println("-------------------------------");

    //     for (SerializationFeature feature : SerializationFeature.values()) {
    //         System.out.println(feature.name() + ": " + serializationConfig.isEnabled(feature));
    //     }

    //     // Log the configuration to verify it's applied correctly
    //     System.out.println(modifiedObjectMapper.getSerializationConfig().isEnabled(EnumFeature.WRITE_ENUMS_TO_LOWERCASE));


    //     return modifiedObjectMapper;
    // }

}
