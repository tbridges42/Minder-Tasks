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

package us.bridgeses.minder_tasks.factories;

import android.content.Context;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.io.FileOutputStream;

import us.bridgeses.minder_tasks.interfaces.ThemePersister;
import us.bridgeses.minder_tasks.storage.FileWriter;
import us.bridgeses.minder_tasks.storage.GsonThemeParser;

/**
 * Created by tbrid on 6/30/2016.
 */
public class ThemePersisterFactory {

    private final Context context;

    public ThemePersisterFactory(Context context) {
        this.context = context;
    }

    private ThemePersister getFileWriter() {
        return new FileWriter(new GsonThemeParser(new Gson()), context.getFilesDir());
    }

    public ThemePersister getThemePersister(@Nullable String location, @Nullable String type) {
        return getFileWriter();
    }
}
