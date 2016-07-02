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
import java.io.FileOutputStream;
import java.io.IOException;

import us.bridgeses.minder_tasks.interfaces.ThemeParser;
import us.bridgeses.minder_tasks.interfaces.ThemePersister;
import us.bridgeses.minder_tasks.theme.Theme;

/**
 * Created by tbrid on 6/28/2016.
 */
public class FileWriter implements ThemePersister {

    private final ThemeParser parser;
    private final File dir;

    public FileWriter(ThemeParser parser, File dir) {
        this.parser = parser;
        this.dir = dir;
    }

    public String getEncoding() {
        return "UTF16";
    }

    @Override
    public Theme read(String name) {
        File file = new File(dir, name);
        try {
            FileInputStream inputStream = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            inputStream.read(bytes);
            String result;
            result = new String(bytes, getEncoding());
            return parser.parseTheme(result);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void write(Theme theme) {
        String name = theme.getName();
        try {
            File file = new File(dir, name);
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(parser.encodeTheme(theme).getBytes(getEncoding()));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
