package us.bridgeses.minder_tasks.theme;

import java.util.List;

import us.bridgeses.minder_tasks.R;

/**
 * Created by Tony on 12/19/2015.
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
    public abstract String[] getAllBadStuff();
    public abstract String getBadStuff();
}
