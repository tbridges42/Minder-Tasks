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

import android.os.Environment;

import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowEnvironment;

import java.io.File;

import us.bridgeses.minder_tasks.BuildConfig;
import us.bridgeses.minder_tasks.interfaces.ThemePersister;
import us.bridgeses.minder_tasks.interfaces.ThemeStreamWriter;
import us.bridgeses.minder_tasks.theme.GsonTheme;
import us.bridgeses.minder_tasks.theme.Theme;

import static org.junit.Assert.assertEquals;


/**
 * Created by tbrid on 6/30/2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, packageName = "test_us.bridgeses.minder_tasks")
public class ThemePersistIntegrateTest {

    File dir;
    ThemePersister persister;

    @Before
    public void setUp() throws Exception {
        ShadowEnvironment.setExternalStorageState(Environment.MEDIA_MOUNTED);
        dir = RuntimeEnvironment.application.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        ThemeStreamWriter writer = new ThemeStreamWriterImpl(new GsonThemeParser(new Gson()));
        persister = new ThemePersisterImpl(writer, dir);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void saveChanges() throws Exception {
        GsonTheme test = new GsonTheme();
        test.setName("New name");
        test.setDark(true);
        test.setHeadlineSize(5);
        persister.write(test);
        Theme result = persister.read(test.getName());
        assertEquals(test.getName(), result.getName());
        assertEquals(test.isDark(), result.isDark());
        assertEquals(test.getHeadlineSize(), result.getHeadlineSize());
        new File(dir, "New name").delete();
    }

    @Test
    public void testNameList() throws Exception {
        GsonTheme test = new GsonTheme();
        test.setName("New name");
        persister.write(test);
        persister.getThemeNames().contains("New name");
        new File(dir, "New name").delete();
    }
}
