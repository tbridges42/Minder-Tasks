package us.bridgeses.minder_tasks.storage;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Contract for persistence of Tasks and related classes.
 * Should be implemented by any classes involved in the persistence of both tasks and related classes
 */
public interface TasksContract {

    int SCHEMA_VERSION = 7;

    String CONTENT_AUTHORITY = "us.bridgeses.tasks_provider";
    Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    String CATEGORIES_TABLE = "contracts_table";

    String TASKS_TABLE = "tasks_table";

    String TASKS_VIEW = "tasks_view";

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
        String COLUMN_NAME_TYPE = " TEXT NOT NULL";
        String COLUMN_COLOR = "color";
        String COLUMN_COLOR_TYPE = " INTEGER";

        String COLUMN_DECLARATION = _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + COLUMN_NAME_TYPE + ", "
                + COLUMN_COLOR + COLUMN_COLOR_TYPE;

        String[] SUMMARY_PROJECTION = { _ID, COLUMN_NAME, COLUMN_COLOR };
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
        String COLUMN_NAME_TYPE = " TEXT NOT NULL";
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
        String COLUMN_COMPLETED = "completed";
        String COLUMN_COMPLETED_TYPE = " INTEGER";

        String COLUMN_DECLARATION = _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME +  COLUMN_NAME_TYPE + ", "
                + COLUMN_CREATION_TIME + COLUMN_CREATION_TIME_TYPE + ", "
                + COLUMN_DUE_TIME + COLUMN_DUE_TIME_TYPE + ", "
                + COLUMN_DURATION + COLUMN_DURATION_TYPE + ", "
                + COLUMN_COMPLETED + COLUMN_COMPLETED_TYPE + ", "
                + COLUMN_CATEGORY + COLUMN_CATEGORY_TYPE
                ;

        String[] SUMMARY_PROJECTION = { _ID, COLUMN_NAME, COLUMN_COMPLETED };
    }

    interface TaskViewEntry extends BaseColumns {
        Uri TASK_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TASKS_VIEW).build();

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

        String COLUMN_NAME = "task_name";
        String COLUMN_CREATION_TIME = "task_created";
        String COLUMN_DUE_TIME = "task_due";
        String COLUMN_DURATION = "task_duration";
        String COLUMN_CATEGORY_ID = "category_id";
        String COLUMN_CATEGORY = "category_name";
        String COLUMN_CATEGORY_COLOR = "category_color";
        String COLUMN_COMPLETED = "task_completed";

        String COLUMN_DECLARATION = "AS SELECT "
                + TASKS_TABLE + "." + TasksEntry._ID + " AS " + _ID + ", "
                + TASKS_TABLE + "." + TasksEntry.COLUMN_NAME + " AS " + COLUMN_NAME + ", "
                + TASKS_TABLE + "." + TasksEntry.COLUMN_CREATION_TIME + " AS " + COLUMN_CREATION_TIME + ", "
                + TASKS_TABLE + "." + TasksEntry.COLUMN_DUE_TIME + " AS " + COLUMN_DUE_TIME + ", "
                + TASKS_TABLE + "." + TasksEntry.COLUMN_DURATION + " AS " + COLUMN_DURATION + ", "
                + TASKS_TABLE + "." + TasksEntry.COLUMN_COMPLETED + " AS " + COLUMN_COMPLETED + ", "
                + CATEGORIES_TABLE + "." + CategoryEntry._ID + " AS " + COLUMN_CATEGORY_ID + ", "
                + CATEGORIES_TABLE + "." + CategoryEntry.COLUMN_NAME + " AS " + COLUMN_CATEGORY + ", "
                + CATEGORIES_TABLE + "." + CategoryEntry.COLUMN_COLOR + " AS " + COLUMN_CATEGORY_COLOR
                + " FROM " + TASKS_TABLE
                + " LEFT JOIN " + CATEGORIES_TABLE + " ON "
                + TASKS_TABLE + "." + TasksEntry.COLUMN_CATEGORY + " = "
                + CATEGORIES_TABLE + "." + CategoryEntry._ID;

        String[] SUMMARY_PROJECTION = {
                _ID,
                COLUMN_NAME,
                COLUMN_CREATION_TIME,
                COLUMN_COMPLETED,
                COLUMN_DUE_TIME,
                COLUMN_DURATION,
                COLUMN_CATEGORY,
                COLUMN_CATEGORY_COLOR
        };
    }
}
