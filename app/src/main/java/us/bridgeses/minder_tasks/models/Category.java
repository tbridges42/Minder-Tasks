package us.bridgeses.minder_tasks.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Basic model of a category. Immutable
 */
public class Category implements Parcelable {

    private final long id;
    private final String name;
    private final int color;

    public Category(long id, String name, int color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeInt(color);
    }

    public static final Parcelable.Creator<Category> CREATOR
            = new Parcelable.Creator<Category>() {

        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source.readLong(), source.readString(), source.readInt());
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
