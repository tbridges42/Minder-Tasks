/*
 * Copyright 2016 Tony Bridges
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package us.bridgeses.minder_tasks.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import us.bridgeses.minder_tasks.interfaces.ThemePersister;
import us.bridgeses.minder_tasks.interfaces.ThemeStreamWriter;
import us.bridgeses.minder_tasks.theme.Theme;

/**
 * Created by tbrid on 7/2/2016.
 */
public class ThemePersisterImpl implements ThemePersister {

    private File dir;
    private ThemeStreamWriter writer;

    public ThemePersisterImpl(ThemeStreamWriter writer, File dir) {
        this.writer = writer;
        this.dir = dir;
    }

    @Override
    public Theme read(String name) throws IOException {
        File file = new File(dir, name + EXTENSION);
        FileInputStream inputStream = new FileInputStream(file);
        return writer.read(inputStream, (int)file.length());
    }

    @Override
    public void write(Theme theme) throws IOException {
        File file = new File(dir, theme.getName() + EXTENSION);
        FileOutputStream outputStream = new FileOutputStream(file);
        writer.write(theme, outputStream);
    }

    @Override
    public List<String> getThemeNames() {
        File[] files = dir.listFiles();
        List<String> themes = new ArrayList<>(files.length);

        for (File file : files) {
            if (file.getName().endsWith(EXTENSION)) {
                themes.add(file.getName());
            }
        }
        return themes;
    }
}
