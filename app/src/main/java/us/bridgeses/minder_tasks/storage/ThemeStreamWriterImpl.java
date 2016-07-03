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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import us.bridgeses.minder_tasks.interfaces.ThemeParser;
import us.bridgeses.minder_tasks.interfaces.ThemePersister;
import us.bridgeses.minder_tasks.interfaces.ThemeStreamWriter;
import us.bridgeses.minder_tasks.theme.Theme;

/**
 * Created by tbrid on 6/28/2016.
 */
public class ThemeStreamWriterImpl implements ThemeStreamWriter {

    private final ThemeParser parser;

    public ThemeStreamWriterImpl(ThemeParser parser) {
        this.parser = parser;
    }

    public String getEncoding() {
        return "UTF16";
    }

    @Override
    public Theme read(InputStream inputStream, int length) throws IOException {
        final byte[] bytes = new byte[length];
        final int actualLength = inputStream.read(bytes);
        final String result = new String(bytes, getEncoding());
        if (actualLength != length) {
            throw new IOException("Failed to read from stream");
        }
        return parser.parseTheme(result);
    }

    @Override
    public void write(Theme theme, OutputStream outputStream) throws IOException {
        outputStream.write(parser.encodeTheme(theme).getBytes(getEncoding()));
    }
}
