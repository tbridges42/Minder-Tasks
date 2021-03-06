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
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spanned;

import us.bridgeses.minder_tasks.R;

/**
 * A default theme to be used when no other theme is set.
 */
public class DefaultTheme extends Theme {

    // A default selection of badStuff
    private Spanned[] badStuff = new Spanned[] {
            Html.fromHtml("&#8230;or a kitten will be sad."),
            Html.fromHtml("&#8230;or <i>they</i> win."),
            Html.fromHtml("&#8230;and nobody gets hurt."),
            Html.fromHtml("&#8230;or I'll tell."),
            Html.fromHtml("&#8230;pretty please?")
    };

    @Override
    public String getName() {
        return "default";
    }

    @Override
    public String getTitle() {
        return "Minder Tasks";
    }

    @Override
    public String getCaption() {
        return "";
    }

    @Override
    public int getLogo() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int getPrimaryFontColor() {
        return Color.DKGRAY;
    }

    @Override
    public int getHighlightFontColor() {
        return Color.WHITE;
    }

    @Override
    public int getBackgroundColor() {
        return Color.parseColor("#F5F5F5");
    }

    @Override
    public int getPrimaryColor() {
        return Color.parseColor("#9C27B0");
    }

    @Override
    public int getSecondaryColor() {
        return Color.parseColor("#BA68C8");
    }

    @Override
    public int getHighlightColor() {
        return Color.parseColor("#00BFA5");
    }

    @Override
    public int getTextSize() {
        return 12;
    }

    @Override
    public int getHeadlineSize() {
        return 21;
    }

    @Override
    public int getSmallTextSize() {
        return 9;
    }

    @Override
    public boolean isDark() {
        return false;
    }

    @Override
    public Spanned[] getAllBadStuff() {
        return badStuff;
    }

    @Override
    public Spanned getBadStuff(int index) {
        if (index < badStuff.length) {
            return badStuff[index];
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // The values of DefaultTheme never change from original intialization. There is no need
        // to persist data
    }

    public static final Parcelable.Creator<DefaultTheme> CREATOR
            = new Parcelable.Creator<DefaultTheme>() {
        public DefaultTheme createFromParcel(Parcel in) {
            return new DefaultTheme();
        }

        public DefaultTheme[] newArray(int size) {
            return new DefaultTheme[size];
        }
    };
}
