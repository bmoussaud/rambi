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
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
class CSVDatasetReader implements DatasetReader {
    @Override
    public Dataset read(InputStream in) throws IOException {
        final List<Movie> movies = new ArrayList<>();
        final var records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(new InputStreamReader(in));
        for (final var record : records) {
            final var id = Integer.parseInt(record.get("id"));
            final var title = record.get("title");
            final var genres = Arrays.stream(record.get("genres").split("-")).toList();
            final var plot = record.get("overview");

            final var movie = new Movie(id, title, genres, plot);
            movies.add(movie);
        }
        return new Dataset(movies);
    }
}
