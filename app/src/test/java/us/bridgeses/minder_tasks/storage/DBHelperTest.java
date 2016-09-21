package us.bridgeses.minder_tasks.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import us.bridgeses.minder_tasks.BuildConfig;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for DBHelper
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, packageName = "test_us.bridgeses.minder_tasks")
public class DBHelperTest implements TasksContract {

    private Context context;
    SQLiteDatabase db;
    DBHelper dbHelper;

    @Before
    public void setUp() throws Exception {
        context = InstrumentationRegistry.getContext();
        if (dbHelper == null) {
            dbHelper = new DBHelper(context);
        }
        if (db == null) {
            db = dbHelper.getWritableDatabase();
            assertTrue(db.isOpen());
        }
    }

    @After
    public void tearDown() throws Exception {
        if (db != null && db.isOpen()) {
            db.close();
            assertFalse(db.isOpen());
            db = null;
        }
        if (dbHelper != null) {
            dbHelper.close();
            dbHelper = null;
        }
    }

    @Test
    public void testCreateDB() throws Exception {
        dbHelper = new DBHelper(context);
        assertNotNull(dbHelper);
        db = dbHelper.getWritableDatabase();
        Assert.assertTrue(db.isOpen());
        db.close();
        assertFalse(db.isOpen());
        db = null;
        dbHelper.close();
        dbHelper = null;
    }


}