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

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@ShellComponent
public class DatasetCommands {
    private final DatasetReader reader;

    public DatasetCommands(DatasetReader reader) {
        this.reader = reader;
    }

    @ShellMethod(key = "import")
    public String importDataset(
            @ShellOption(defaultValue = "file") File file) throws IOException {
        final var buf = new StringBuffer();
        final var dataset = reader.read(new FileInputStream(file));
        for (final var movie : dataset.movies()) {
            buf.append("Movie: ").append(movie.title()).append("\n");
        }
        return buf.toString();
    }
}
