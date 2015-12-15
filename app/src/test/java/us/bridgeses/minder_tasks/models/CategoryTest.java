package us.bridgeses.minder_tasks.models;

import android.graphics.Color;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import us.bridgeses.minder_tasks.BuildConfig;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Tony on 12/14/2015.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, packageName = "test_us.bridgeses.minder_tasks")
public class CategoryTest {

    @Test
    public void testCreateCategory() {
        Category testCategory = new Category("Name", Color.RED);
    }

    @Test
    public void testGetName() {
        String testName = "Test Name";
        Category testCategory = new Category(testName, Color.RED);
        assertEquals(testName, testCategory.getName());
    }

    @Test
    public void testGetColor() {
        int color = Color.GREEN;
        Category testCategory = new Category("Test Name", color);
        assertEquals(color, testCategory.getColor());
    }
}
