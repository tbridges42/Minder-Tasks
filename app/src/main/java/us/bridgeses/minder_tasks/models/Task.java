package us.bridgeses.minder_tasks.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

/**
 * Basic model of a task. Immutable. Uses the builder pattern to handle multiple optional fields
 */
public class Task implements Parcelable {

    private String name;
    private long creationTime;
    private long dueTime;
    private long duration;
    private Category category;

    private Task(String name, long creationTime, long dueTime, long duration, Category category) {
        this.name = name;
        this.creationTime = creationTime;
        this.dueTime = dueTime;
        this.duration = duration;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public long getDueTime() {
        return dueTime;
    }

    public long getDuration() {
        return duration;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(creationTime);
        dest.writeLong(dueTime);
        dest.writeLong(duration);
        dest.writeParcelable(category,0);
    }

    public static final Parcelable.Creator<Task> CREATOR =
            new Parcelable.Creator<Task>() {

                @Override
                public Task createFromParcel(Parcel source) {
                    Task.Builder builder = new Task.Builder(source.readString());
                    builder.setCreationTime(source.readLong());
                    builder.setDueTime(source.readLong());
                    builder.setDuration(source.readLong());
                    builder.setCategory((Category)
                            source.readParcelable(Category.class.getClassLoader()));
                    return builder.build();
                }

                @Override
                public Task[] newArray(int size) {
                    return new Task[size];
                }
            };

    public static class Builder {
        private String name;
        private long creationTime = Calendar.getInstance().getTimeInMillis();
        private long dueTime = -1L;
        private long duration = -1L;

        public Category getCategory() {
            return category;
        }

        public String getName() {
            return name;
        }

        public long getCreationTime() {
            return creationTime;
        }

        public long getDueTime() {
            return dueTime;
        }

        public long getDuration() {
            return duration;
        }

        private Category category = null;

        public Builder(String name) {
            this.name = name;
        }

        public Builder(Task task) {
            this.name = task.getName();
            this.creationTime = task.getCreationTime();
            this.dueTime = task.dueTime;
            this.duration = task.duration;
            this.category = task.category;
        }

        public Builder setCreationTime(long time) {
            if (time < -1L) {
                throw new IllegalArgumentException("Invalid time");
            }
            this.creationTime = time;
            return this;
        }

        public Builder setDueTime(long time) {
            if (time < -1L) {
                throw new IllegalArgumentException("Invalid time");
            }
            this.dueTime = time;
            return this;
        }

        public Builder setDuration(long time) {
            if (time < -1L) {
                throw new IllegalArgumentException("Invalid time");
            }
            this.duration = time;
            return this;
        }

        public Builder setCategory(Category category) {
            this.category = category;
            return this;
        }

        public Task build() {
            return new Task(name, creationTime, dueTime, duration, category);
        }
    }
}
