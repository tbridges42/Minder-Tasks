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
import android.text.Spannable;
import android.text.Spanned;

import java.util.ArrayList;
import java.util.List;

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
            Html.fromHtml("&#8230;please?")
    };

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
    public int getPrimaryColor() {
        return Color.WHITE;
    }

    @Override
    public int getHighlightColor() {
        return Color.RED;
    }

    @Override
    public int getTextSize() {
        return R.dimen.text_size;
    }

    @Override
    public int getHeadlineSize() {
        return R.dimen.header_size;
    }

    @Override
    public int getSmallTextSize() {
        return R.dimen.small_text_size;
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
}
