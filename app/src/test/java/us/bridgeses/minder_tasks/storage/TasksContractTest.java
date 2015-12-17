package us.bridgeses.minder_tasks.storage;

import android.net.Uri;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import us.bridgeses.minder_tasks.BuildConfig;

import static junit.framework.Assert.assertEquals;

/**
 * Tests for Contract
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, packageName = "test_us.bridgeses.minder_tasks")
public class TasksContractTest {

    @Test
    public void testContentAuthority() {
        assertEquals("us.bridgeses.tasks_provider", TasksContract.CONTENT_AUTHORITY);
    }

    @Test
    public void testBaseContentURI() {
        assertEquals(Uri.parse("content://us.bridgeses.tasks_provider"),
                TasksContract.BASE_CONTENT_URI);
    }
}
