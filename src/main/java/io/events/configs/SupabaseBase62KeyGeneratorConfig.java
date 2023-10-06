package io.events.configs;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "supabase.url_swift")
public interface SupabaseBase62KeyGeneratorConfig extends SupabaseBackendConfig { }