package io.events.configs;

import io.smallrye.config.WithName;

public interface SupabaseBackendConfig {

    String host();

    @WithName("api_key")
    String apiKey();

    AuthorityConfig authority();
}
