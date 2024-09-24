package org.moussaud.demos.rambi.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rambi")
public record RambiConfiguration(String tmdb_api_key, boolean failfast) {

}
