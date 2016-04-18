package us.bridgeses.minder_tasks.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper for creating and updating database.
 * Not threadsafe. Should only be used through ContentProvider.
 */
public class DBHelper extends SQLiteOpenHelper implements TasksContract {

    private static final String DATABASE_NAME = "tasks_database";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION << 16 | DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CATEGORIES_TABLE + " (" + CategoryEntry.COLUMN_DECLARATION + ");");
        db.execSQL("CREATE TABLE " + TASKS_TABLE + " (" + TasksEntry.COLUMN_DECLARATION + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + CATEGORIES_TABLE);
        db.execSQL("DROP TABLE " + TASKS_TABLE);
        onCreate(db);
    }
}
