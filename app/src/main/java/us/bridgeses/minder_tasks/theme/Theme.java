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

import android.text.Spanned;

/**
 * An interface for a theme that will provide all customizable resources.
 */
public abstract class Theme {
    public abstract int getLogo();
    public abstract int getPrimaryFontColor();
    public abstract int getHighlightFontColor();
    public abstract int getPrimaryColor();
    public abstract int getHighlightColor();
    public abstract int getTextSize();
    public abstract int getHeadlineSize();
    public abstract int getSmallTextSize();
    public abstract boolean isDark();
    public abstract Spanned[] getAllBadStuff();
    public abstract Spanned getBadStuff(int index);
}
