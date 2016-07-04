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

package us.bridgeses.minder_tasks.theme;

import android.graphics.Color;

/**
 * Created by tbrid on 7/3/2016.
 */
public class DarkDefaultTheme extends DefaultTheme {

    @Override
    public boolean isDark() {
        return true;
    }

    @Override
    public int getBackgroundColor() {
        return Color.parseColor("#424242");
    }

    @Override
    public int getPrimaryFontColor() {
        return Color.parseColor("#F5F5F5");
    }
}
