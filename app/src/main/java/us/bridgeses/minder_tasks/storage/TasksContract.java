package us.bridgeses.minder_tasks.storage;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Contract for persistence of Tasks and related classes.
 * Should be implemented by any classes involved in the persistence of both tasks and related classes
 */
public interface TasksContract {

    int SCHEMA_VERSION = 1;

    String CONTENT_AUTHORITY = "us.bridgeses.tasks_provider";
    Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    String CATEGORIES_TABLE = "contracts_table";

    String TASKS_TABLE = "tasks_table";

    Uri CATEGORIES_URI = BASE_CONTENT_URI.buildUpon().appendPath(CATEGORIES_TABLE).build();
    Uri TASKS_URI = BASE_CONTENT_URI.buildUpon().appendPath(TASKS_TABLE).build();

    /**
     * Contract for the persistence of categories
     * Should be implemented by any classes that deal specifically with the persistence of categories
     */
    interface CategoryEntry extends BaseColumns {

        Uri CATEGORY_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(CATEGORIES_TABLE).build();

        String CONTENT_CATEGORY =
                "vnd.android.cursor.item/"
                + CONTENT_AUTHORITY
                + "/"
                + CATEGORIES_TABLE;

        String CONTENT_CATEGORIES =
                "vnd.android.cursor.dir/"
                + CONTENT_AUTHORITY
                + "/"
                + CATEGORIES_TABLE;

        String COLUMN_NAME = "name";
        String COLUMN_NAME_TYPE = "TEXT NOT NULL";
        String COLUMN_COLOR = "color";
        String COLUMN_COLOR_TYPE = "INTEGER";

        String COLUMN_DECLARATION = COLUMN_NAME + COLUMN_NAME_TYPE + ", "
                + COLUMN_COLOR + COLUMN_COLOR_TYPE;
    }

    /**
     * Contract for the persistence of tasks
     * Should be implemented by any classes that deal specifically with the persistence of tasks
     */
    interface TasksEntry extends BaseColumns {

        Uri TASK_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TASKS_TABLE).build();

        String CONTENT_TASK =
                "vnd.android.cursor.item/"
                + CONTENT_AUTHORITY
                + "/"
                + TASKS_TABLE;

        String CONTENT_TASKS =
                "vnd.android.cursor.dir/"
                + CONTENT_AUTHORITY
                + "/"
                + TASKS_TABLE;

        String COLUMN_NAME = "name";
        String COLUMN_NAME_TYPE = "TEXT NOT NULL";
        String COLUMN_CREATION_TIME = "created";
        String COLUMN_CREATION_TIME_TYPE = " INTEGER NOT NULL";
        String COLUMN_DUE_TIME = "due";
        String COLUMN_DUE_TIME_TYPE = " INTEGER";
        String COLUMN_DURATION = "duration";
        String COLUMN_DURATION_TYPE = " INTEGER";
        String COLUMN_CATEGORY = "category";
        String COLUMN_CATEGORY_TYPE = " INTEGER, FOREIGN KEY("
                + COLUMN_CATEGORY
                + ") REFERENCES "
                + CATEGORIES_TABLE
                + "(" + _ID + ")"
                + " ON DELETE SET NULL";

        String COLUMN_DECLARATION = COLUMN_NAME + COLUMN_NAME_TYPE + ", "
                + COLUMN_CREATION_TIME + COLUMN_CREATION_TIME_TYPE + ", "
                + COLUMN_DUE_TIME + COLUMN_DUE_TIME_TYPE + ", "
                + COLUMN_DURATION + COLUMN_DURATION_TYPE + ", "
                + COLUMN_CATEGORY + COLUMN_CATEGORY_TYPE;
    }
}
