package io.events.configs;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "supabase.base62_key_generator")
public interface SupabaseBase62KeyGeneratorConfig extends SupabaseBackendConfig { }