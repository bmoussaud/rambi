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

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.autoconfigure.vectorstore.pgvector.PgVectorStoreAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.io.ClassPathResource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "spring.shell.interactive.enabled=false",
        "spring.shell.noninteractive.enabled=false",
        "spring.shell.script.enabled=false"
})
@Testcontainers
@EnableAutoConfiguration(exclude = {PgVectorStoreAutoConfiguration.class})
class MovieDatasetReaderTests {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> pgsql = new PostgreSQLContainer<>("pgvector/pgvector:pg16");

    private final Logger logger = LoggerFactory.getLogger(MovieDatasetReaderTests.class);

    @Test
    void testRead() throws IOException {
        final var res = new ClassPathResource("/movies-dataset.csv");

        final var expectedTitles = List.of(
                "Meg 2: The Trench",
                "The Pope's Exorcist",
                "Transformers: Rise of the Beasts",
                "Ant-Man and the Wasp: Quantumania",
                "Creed III"
        );
        final var expectedGenres = List.of(
                List.of("Action", "Science Fiction", "Horror"),
                List.of("Horror", "Mystery", "Thriller"),
                List.of("Action", "Adventure", "Science Fiction"),
                List.of("Action", "Adventure", "Science Fiction"),
                List.of("Drama", "Action")
        );
        final var expectedPlots = List.of(
                "An exploratory dive into the deepest depths of the ocean of a daring research team spirals into chaos when a malevolent mining operation threatens their mission and forces them into a high-stakes battle for survival.",
                "Father Gabriele Amorth Chief Exorcist of the Vatican investigates a young boy's terrifying possession and ends up uncovering a centuries-old conspiracy the Vatican has desperately tried to keep hidden.",
                "When a new threat capable of destroying the entire planet emerges Optimus Prime and the Autobots must team up with a powerful faction known as the Maximals. With the fate of humanity hanging in the balance humans Noah and Elena will do whatever it takes to help the Transformers as they engage in the ultimate battle to save Earth.",
                "Super-Hero partners Scott Lang and Hope van Dyne along with with Hope's parents Janet van Dyne and Hank Pym and Scott's daughter Cassie Lang find themselves exploring the Quantum Realm interacting with strange new creatures and embarking on an adventure that will push them beyond the limits of what they thought possible.",
                "After dominating the boxing world Adonis Creed has been thriving in both his career and family life. When a childhood friend and former boxing prodigy Damien Anderson resurfaces after serving a long sentence in prison he is eager to prove that he deserves his shot in the ring. The face-off between former friends is more than just a fight. To settle the score Adonis must put his future on the line to battle Damien — a fighter who has nothing to lose."
        );

        try (final var in = res.getInputStream()) {
            final var records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(new InputStreamReader(in));
            int i = 0;
            for (final var record : records) {
                final var title = record.get("title");
                final var genres = Arrays.stream(record.get("genres").split("-")).toList();
                final var plot = record.get("overview");

                assertThat(title).isEqualTo(expectedTitles.get(i));
                assertThat(genres).isEqualTo(expectedGenres.get(i));
                assertThat(plot).isEqualTo(expectedPlots.get(i));
                i++;
            }
        }
    }
}
