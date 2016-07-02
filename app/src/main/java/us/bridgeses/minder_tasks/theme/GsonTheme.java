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

import android.text.Html;
import android.text.Spanned;
import android.text.SpannedString;

/**
 * Created by tbrid on 6/28/2016.
 */
public class GsonTheme extends Theme {

    private String name;
    private int logoRes;
    private int primaryFontColor;
    private int highlightFontColor;
    private int primaryColor;
    private int highlightColor;
    private int textSize;
    private int headlineSize;
    private int smallTextSize;
    private boolean isDark;
    private String[] badStuff = new String[] {
        "&#8230;or a kitten will be sad.",
                "&#8230;or <i>they</i> win.",
                "&#8230;and nobody gets hurt.",
                "&#8230;or I'll tell.",
                "&#8230;pretty please?"
    };

    Spanned[] toSpanned(String[] strings) {
        Spanned[] result = new Spanned[strings.length];
        for (int i = 0; i < strings.length; i++) {
            result[i] = Html.fromHtml(strings[i]);
        }
        return result;
    }

    public String getName() {
        return name;
    }

    @Override
    public int getLogo() {
        return logoRes;
    }

    public int getPrimaryFontColor() {
        return primaryFontColor;
    }

    public int getHighlightFontColor() {
        return highlightFontColor;
    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    public int getHighlightColor() {
        return highlightColor;
    }

    public int getTextSize() {
        return textSize;
    }

    public int getHeadlineSize() {
        return headlineSize;
    }

    public int getSmallTextSize() {
        return smallTextSize;
    }

    public boolean isDark() {
        return isDark;
    }

    @Override
    public Spanned[] getAllBadStuff() {
        return toSpanned(badStuff);
    }

    @Override
    public Spanned getBadStuff(int index) {
        return Html.fromHtml(badStuff[index]);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLogoRes(int logoRes) {
        this.logoRes = logoRes;
    }

    public void setPrimaryFontColor(int primaryFontColor) {
        this.primaryFontColor = primaryFontColor;
    }

    public void setHighlightFontColor(int highlightFontColor) {
        this.highlightFontColor = highlightFontColor;
    }

    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
    }

    public void setHighlightColor(int highlightColor) {
        this.highlightColor = highlightColor;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setHeadlineSize(int headlineSize) {
        this.headlineSize = headlineSize;
    }

    public void setSmallTextSize(int smallTextSize) {
        this.smallTextSize = smallTextSize;
    }

    public void setDark(boolean dark) {
        isDark = dark;
    }

    public void setBadStuff(String[] badStuff) {
        this.badStuff = badStuff;
    }
}
