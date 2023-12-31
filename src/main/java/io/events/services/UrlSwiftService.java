/**
 * This class provides services related to URL shortening using Supabase and Base62 key generation.
 * 
 * @author Luca Corallo
*/
package io.events.services;

import java.net.URI;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.events.configs.SupabaseBase62KeyGeneratorConfig;
import io.events.configs.SupabaseUrlSwiftConfig;
import io.events.dto.LinkShorteningCreationDTO;
import io.events.dto.LinkShorteningResponseDTO;
import io.events.exceptions.RedirectUrlMatchException;
import io.events.restclients.Base62KeyGeneratorClient;
import io.events.restclients.SupabaseClient;
import io.events.restclients.SupabaseUrlSwiftClient;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.subscription.Cancellable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.validation.Valid;

@ApplicationScoped
public class UrlSwiftService {

    @Inject
    SupabaseUrlSwiftConfig supabaseUrlSwiftConfig;

    @Inject
    SupabaseBase62KeyGeneratorConfig supabaseBase62KeyGeneratorConfig;

    @RestClient
    private SupabaseUrlSwiftClient supabaseUrlSwiftClient;

    @RestClient
    private Base62KeyGeneratorClient base62KeyGeneratorClient;

    private SupabaseClient supabaseClient;

    private String accessToken = ""; //Assigned only for testing purposes

    private Cancellable refreshTokenSubscription;

    /**
     * Method called on application startup.
     * Initializes Supabase client and sets up token refreshing.
     * @param ev The startup event.
     */
    void onStart(@Observes StartupEvent ev) {
        this.supabaseClient = SupabaseClient.createInstance(URI.create(supabaseUrlSwiftConfig.host()));

        //this.refreshTokenSubscription = //TODO: Check why this is not working if assigned to a variable
        this.supabaseClient.refreshTokenEvery(supabaseUrlSwiftConfig, 15)
            .subscribe().with(
                tokenResponse -> this.accessToken = tokenResponse.getAccessToken(),
                failure -> new RuntimeException(failure)
            );
    }

    /**
     * Method called on application shutdown.
     * Cancels token refreshing subscription.
     * @param ev The startup event.
     */
    void onStop(@Observes StartupEvent ev) {
        // this.refreshTokenSubscription.cancel();
    }

    /**
     * Retrieves the redirect URL for a given shortened link.
     * @param shortenedLink The shortened link.
     * @return A Uni that resolves to the redirect URL.
     */
    public Uni<LinkShorteningResponseDTO> getInfo(String shortenedLink) {
        return Uni.createFrom().completionStage(
            this.supabaseUrlSwiftClient.getByShortened(
                this.accessToken,
                shortenedLink
            )
        ).map(linkShorteningResponseDTO -> {
            if (linkShorteningResponseDTO.isEmpty()) throw new RedirectUrlMatchException();
            return linkShorteningResponseDTO.iterator().next();
        });
    }

    /**
     * Creates a new shortened link.
     * @param linkShorteningCreationDTO The DTO containing link information.
     * @return A Uni that resolves to the created link.
     */
    public Uni<LinkShorteningCreationDTO> create(@Valid LinkShorteningCreationDTO linkShorteningCreationDTO) {
        return Uni.createFrom().completionStage(
            this.base62KeyGeneratorClient.generateKey(accessToken)
        ).map(baseKeyDTO -> {
            linkShorteningCreationDTO.setShortnedLink(baseKeyDTO.getKey());
            return linkShorteningCreationDTO;
        }).chain(key -> Uni.createFrom().completionStage(
            this.supabaseUrlSwiftClient.create(this.accessToken, linkShorteningCreationDTO)
        )).map(v -> linkShorteningCreationDTO);
    }

}
