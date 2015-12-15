package us.bridgeses.minder_tasks.models;

import java.util.Calendar;

/**
 * Basic model of a task. Immutable. Uses the builder pattern to handle multiple optional fields
 */
public class Task {

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

    public static class Builder {
        private String name;
        private long creationTime = Calendar.getInstance().getTimeInMillis();
        private long dueTime = -1L;
        private long duration = -1L;
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
