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

package com.broadcom.tanzu.demos.rambi.dataset;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
class JdbcDatasetStore implements DatasetStore {
    private final Logger logger = LoggerFactory.getLogger(JdbcDatasetStore.class);
    private final JdbcTemplate jdbc;

    JdbcDatasetStore(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void save(Dataset dataset) {
        for (final Movie movie : dataset.movies()) {
            logger.debug("Inserting movie into database: {}", movie.title());
            jdbc.update("INSERT INTO movies(title, genres, plot) VALUES (?,?,?)",
                    movie.title(), String.join("-", movie.genres()), movie.plot());
        }
    }

    @Override
    public void clear() {
        logger.debug("Deleting movies");
        jdbc.update("DELETE FROM movies");
    }
}
