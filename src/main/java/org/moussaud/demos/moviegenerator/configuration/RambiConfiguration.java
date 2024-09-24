package org.moussaud.demos.moviegenerator.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rambi")
public record RambiConfiguration(String tmdb_api_key, boolean failfast) {

}
