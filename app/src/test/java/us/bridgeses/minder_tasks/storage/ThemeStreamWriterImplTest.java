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

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.CapturesArguments;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;

import us.bridgeses.minder_tasks.interfaces.ThemeParser;
import us.bridgeses.minder_tasks.interfaces.ThemeStreamWriter;
import us.bridgeses.minder_tasks.theme.Theme;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by tbrid on 7/2/2016.
 */
public class ThemeStreamWriterImplTest {

    @Test
    public void getEncoding() throws Exception {
        Charset.forName(new ThemeStreamWriterImpl(Mockito.mock(ThemeParser.class)).getEncoding());
    }

    @Test
    public void testRead() throws Exception {
        ThemeParser parser = Mockito.mock(ThemeParser.class);
        ThemeStreamWriterImpl writer = new ThemeStreamWriterImpl(parser);
        InputStream anyInputStream = new ByteArrayInputStream("test data".getBytes(writer.getEncoding()));
        writer.read(anyInputStream, 20);
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(parser).parseTheme(stringArgumentCaptor.capture());
        assertEquals("test data", stringArgumentCaptor.getValue());
    }

    @Test(expected = IOException.class)
    public void invalidLength() throws Exception {
        ThemeParser parser = Mockito.mock(ThemeParser.class);
        ThemeStreamWriterImpl writer = new ThemeStreamWriterImpl(parser);
        InputStream anyInputStream = new ByteArrayInputStream("test data".getBytes(writer.getEncoding()));
        writer.read(anyInputStream, 21);
    }

    @Test
    public void testWrite() throws Exception {
        ThemeParser parser = Mockito.mock(ThemeParser.class);
        when(parser.encodeTheme(any(Theme.class))).thenReturn("test data");
        ThemeStreamWriterImpl writer = new ThemeStreamWriterImpl(parser);
        OutputStream outputStream = Mockito.mock(OutputStream.class);
        writer.write(null, outputStream);
        ArgumentCaptor<byte[]> byteArgumentCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(outputStream).write(byteArgumentCaptor.capture());
        assertEquals(Arrays.toString("test data".getBytes(writer.getEncoding())),
                Arrays.toString(byteArgumentCaptor.getValue()));
    }
}