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

package us.bridgeses.minder_tasks.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Date;

/**
 * Basic model of a task. Immutable. Uses the builder pattern to handle multiple optional fields
 */
public class Task implements Parcelable {

    // To be used to indicate no time. Time is handled in increments of minutes, so a user will
    // never select a time of one millisecond before epoch.
    public static final long INVALID_TIME = -1L;

    private final long id;
    private final String name;
    private final long creationTime;
    private final long dueTime;
    private final int duration;
    private final Category category;
    private final boolean completed;

    private Task(long id, String name, long creationTime, long dueTime,
                 int duration, Category category, boolean completed) {
        this.id = id;
        this.name = name;
        this.creationTime = creationTime;
        this.dueTime = dueTime;
        this.duration = duration;
        this.category = category;
        this.completed = completed;
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

    public int getDuration() {
        return duration;
    }

    // Category is immutable. No need for defensive copying
    public Category getCategory() {
        return category;
    }

    public boolean isCompleted() {
        return completed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(id);
        dest.writeLong(creationTime);
        dest.writeLong(dueTime);
        dest.writeInt(duration);
        dest.writeParcelable(category,0);
        dest.writeInt(completed ? 1 : 0);
    }

    public static final Parcelable.Creator<Task> CREATOR =
            new Parcelable.Creator<Task>() {

                @Override
                public Task createFromParcel(Parcel source) {
                    Task.Builder builder = new Task.Builder(source.readString());
                    builder.setId(source.readLong());
                    builder.setCreationTime(source.readLong());
                    builder.setDueTime(source.readLong());
                    builder.setDuration(source.readInt());
                    builder.setCategory((Category)
                            source.readParcelable(Category.class.getClassLoader()));
                    builder.setCompleted(source.readInt() == 1);
                    return builder.build();
                }

                @Override
                public Task[] newArray(int size) {
                    return new Task[size];
                }
            };

    public long getId() {
        return id;
    }

    public static class Builder {
        private String name;
        private long creationTime = Calendar.getInstance().getTimeInMillis();
        private long dueTime = -1L;
        private int duration = -1;
        private long id = -1L;
        private boolean completed;

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

        public int getDuration() {
            return duration;
        }

        private Category category = null;

        public Builder(String name) {
            this.name = name;
        }

        public Builder(Task task) {
            this.id = task.getId();
            this.name = task.getName();
            this.creationTime = task.getCreationTime();
            this.dueTime = task.getDueTime();
            this.duration = task.getDuration();
            this.category = task.getCategory();
            this.completed = task.isCompleted();
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

        public Builder setDueTime(Date time) {
            this.dueTime = time.getTime();
            return this;
        }

        public Builder setDuration(int time) {
            if (time < -1) {
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
            return new Task(id, name, creationTime, dueTime, duration, category, completed);
        }

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setCompleted(boolean completed) {
            this.completed = completed;
            return this;
        }
    }
}
