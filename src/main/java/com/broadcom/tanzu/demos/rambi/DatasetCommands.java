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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DatasetCommands {
    private final Logger logger = LoggerFactory.getLogger(DatasetCommands.class);
    private final DatasetReader reader;
    private final DatasetStore store;

    public DatasetCommands(DatasetReader reader, DatasetStore store) {
        this.reader = reader;
        this.store = store;
    }

    public String importDataset( File file) throws IOException {
        final var buf = new StringBuffer();
        final var dataset = reader.read(new FileInputStream(file));
        for (final var movie : dataset.movies()) {
            logger.info("Read movie: {}", movie.title());
        }
        store.save(dataset);
        return String.format("Inserted %d movies", dataset.movies().size());
    }

    String resetDataset() {
        store.clear();
        return "Cleared datastore";
    }
}
