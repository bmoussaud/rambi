/*
 * Copyright (c) 2024 Broadcom, Inc. or its affiliates
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.broadcom.tanzu.demos.rambi;

import com.broadcom.tanzu.demos.rambi.configuration.RambiConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableConfigurationProperties({ RambiConfiguration.class })
public class Application {
        
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    @Bean
    CommandLineRunner onStart(@Value("${rambi.chatProvider}") String param) {
        return (args) -> {
            logger.error("CHAT PROVIDER {}", param);
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
