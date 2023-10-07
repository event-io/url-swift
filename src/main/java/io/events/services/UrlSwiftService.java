package io.events.services;

import java.net.URI;
import java.time.Duration;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.events.configs.SupabaseBackendConfig;
import io.events.configs.SupabaseBase62KeyGeneratorConfig;
import io.events.configs.SupabaseUrlSwiftConfig;
import io.events.dto.TokenRequestDTO;
import io.events.dto.TokenResponseDTO;
import io.events.models.SupabaseGrantType;
import io.events.restclients.Base62KeyGeneratorClient;
import io.events.restclients.SupabaseClient;
import io.events.restclients.SupabaseUrlSwiftClient;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.subscription.Cancellable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

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

    private String accessToken;

    private Cancellable refreshTokenSubscription;

    void onStart(@Observes StartupEvent ev) {
        this.supabaseClient = SupabaseClient.createInstance(URI.create(supabaseUrlSwiftConfig.host()));

        //this.refreshTokenSubscription = //TODO: Check why this is not working if assigned to a variable
        this.refreshTokenEvery(supabaseUrlSwiftConfig, supabaseClient, 15)
            .subscribe().with(
                tokenResponse -> this.accessToken = tokenResponse.getAccessToken(),
                failure -> new RuntimeException(failure)
            );
    }

    void onStop(@Observes StartupEvent ev) {
        // this.refreshTokenSubscription.cancel();
    }

    private Multi<TokenResponseDTO> refreshTokenEvery(
        SupabaseBackendConfig config,
        SupabaseClient supabaseClient,
        int everyMinutes
    ) {
        return Multi.createFrom().ticks().every(Duration.ofMinutes(everyMinutes)).onItem()
            .transformToUniAndConcatenate(tick -> Uni.createFrom().completionStage(
                    supabaseClient.token(SupabaseGrantType.password, config.apiKey(), new TokenRequestDTO(
                        config.authority().email(), config.authority().password()
                    ))
            ));
    }
    
    public Uni<String> getRedirectURL(String shortenedLink) {
        return Uni.createFrom().completionStage(
            this.supabaseUrlSwiftClient.getById(
                this.accessToken,
                shortenedLink
            )
        ).map(linkShorteningResponseDTO -> {
            if (linkShorteningResponseDTO.isEmpty()) {
                throw new NotFoundException("Not found resource URL with shortened link: " + shortenedLink);
            }
            return linkShorteningResponseDTO.iterator().next().getOriginalLink();
        });
    }

}
