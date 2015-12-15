package us.bridgeses.minder_tasks.models;

import android.graphics.Color;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import us.bridgeses.minder_tasks.BuildConfig;

import static junit.framework.Assert.assertEquals;

/**
 * Tests for Task class
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, packageName = "test_us.bridgeses.minder_tasks")
public class TaskTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testCreateCompleteTaskFromBuilder() {
        Task testTask = new Task.Builder("TestName")
                .setCreationTime(0)
                .setDueTime(0)
                .setDuration(0)
                .setCategory(new Category("Test Name", Color.RED))
                .build();
    }

    @Test
    public void testCopyBuilder() {
        String testName = "Test Name";
        long createTime = 5435436L;
        long dueTime = 43254656L;
        long duration = 43524746587L;
        Category category = new Category("Test",Color.RED);
        Task testTask = new Task.Builder(testName)
                .setCreationTime(createTime)
                .setDueTime(dueTime)
                .setDuration(duration)
                .setCategory(category)
                .build();
        Task newTask = new Task.Builder(testTask).build();
        assertEquals(testName, newTask.getName());
        assertEquals(createTime, newTask.getCreationTime());
        assertEquals(dueTime, newTask.getDueTime());
        assertEquals(duration, newTask.getDuration());
        assertEquals(category, newTask.getCategory());
    }

    @Test
    public void testIllegalCreationTimeWithBuilder() {
        thrown.expect(IllegalArgumentException.class);
        Task testTask = new Task.Builder("TestName")
                .setCreationTime(-5L)
                .build();
    }

    @Test
    public void testIllegalDueTimeWithBuilder() {
        thrown.expect(IllegalArgumentException.class);
        Task testTask = new Task.Builder("TestName")
                .setDueTime(-5L)
                .build();
    }

    @Test
    public void testIllegalDurationTimeWithBuilder() {
        thrown.expect(IllegalArgumentException.class);
        Task testTask = new Task.Builder("TestName")
                .setDuration(-5L)
                .build();
    }

    @Test
    public void testGetName() {
        String testName = "Test Name";
        Task testTask = new Task.Builder(testName).build();
        assertEquals(testName, testTask.getName());
    }
    
    @Test
    public void testGetCreationTime() {
        long creationTime = 54364365446546L;
        Task testTask = new Task.Builder("Test Name")
                .setCreationTime(creationTime)
                .build();
        assertEquals(creationTime, testTask.getCreationTime());
    }

    @Test
    public void testGetDueTime() {
        long dueTime = 54364365446546L;
        Task testTask = new Task.Builder("Test Name")
                .setDueTime(dueTime)
                .build();
        assertEquals(dueTime, testTask.getDueTime());
    }

    @Test
    public void testGetDuration() {
        long duration = 54364365446546L;
        Task testTask = new Task.Builder("Test Name")
                .setDuration(duration)
                .build();
        assertEquals(duration, testTask.getDuration());
    }

    @Test
    public void testGetCategory() {
        Category testCategory = new Category("Test name", Color.RED);
        Task testTask = new Task.Builder("Test name").setCategory(testCategory).build();
        assertEquals(testCategory, testTask.getCategory());
    }
}
