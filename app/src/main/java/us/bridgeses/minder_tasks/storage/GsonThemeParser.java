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

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import us.bridgeses.minder_tasks.interfaces.ThemeParser;
import us.bridgeses.minder_tasks.theme.GsonTheme;
import us.bridgeses.minder_tasks.theme.Theme;

/**
 * Created by tbrid on 6/28/2016.
 */
public class GsonThemeParser implements ThemeParser {

    private final Gson parser;

    public GsonThemeParser(Gson parser) {
        this.parser = parser;
    }

    @Override
    public Theme parseTheme(String input) throws JsonParseException {
        return parser.fromJson(input, GsonTheme.class);
    }

    @Override
    public String encodeTheme(Theme theme) {
        if (theme instanceof GsonTheme) {
            return parser.toJson(theme);
        }
        else {
            throw new IllegalArgumentException("GsonTheme required for GsonThemeParser");
        }
    }
}
