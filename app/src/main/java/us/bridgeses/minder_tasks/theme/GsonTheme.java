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

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spanned;
import android.text.SpannedString;

import com.google.gson.Gson;

/**
 * Created by tbrid on 6/28/2016.
 */
public class GsonTheme extends Theme {

    private String name;
    private String title;
    private String caption;
    private int logoRes;
    private int primaryFontColor;
    private int highlightFontColor;
    private int primaryColor;
    private int backgroundColor;
    private int secondaryColor;
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
    public String getTitle() {
        return title;
    }

    @Override
    public String getCaption() {
        return caption;
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

    @Override
    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    @Override
    public int getSecondaryColor() {
        return secondaryColor;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(title);
        dest.writeString(caption);
        dest.writeInt(logoRes);
        dest.writeInt(primaryFontColor);
        dest.writeInt(highlightFontColor);
        dest.writeInt(primaryColor);
        dest.writeInt(backgroundColor);
        dest.writeInt(secondaryColor);
        dest.writeInt(highlightColor);
        dest.writeInt(textSize);
        dest.writeInt(headlineSize);
        dest.writeInt(smallTextSize);
        dest.writeInt(isDark ? 1 : 0);
        dest.writeInt(badStuff.length);
        dest.writeStringArray(badStuff);
    }

    public static final Parcelable.Creator<GsonTheme> CREATOR
            = new Parcelable.Creator<GsonTheme>() {
        public GsonTheme createFromParcel(Parcel in) {
            return new GsonTheme(in);
        }

        public GsonTheme[] newArray(int size) {
            return new GsonTheme[size];
        }
    };
    
    public GsonTheme() {}
    
    private GsonTheme(Parcel in) {
        name = in.readString();
        title = in.readString();
        caption = in.readString();
        logoRes = in.readInt();
        primaryFontColor = in.readInt();
        highlightFontColor = in.readInt();
        primaryColor = in.readInt();
        backgroundColor = in.readInt();
        secondaryColor = in.readInt();
        highlightColor = in.readInt();
        textSize = in.readInt();
        headlineSize = in.readInt();
        smallTextSize = in.readInt();
        isDark = in.readInt() == 1;
        in.readStringArray(badStuff);
    }
}
