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

package us.bridgeses.minder_tasks.storage;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import us.bridgeses.minder_tasks.BuildConfig;
import us.bridgeses.minder_tasks.models.Category;
import us.bridgeses.minder_tasks.models.Task;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by tbrid on 7/2/2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, packageName = "test_us.bridgeses.minder_tasks")
public class PersistenceHelperImplTest {

    private Category newCategory;
    private Task newTask;
    private Category usedCategory;
    private Task usedTask;

    {
        newCategory = new Category(-1, "test", 0);

        Task.Builder builder = new Task.Builder("Test");
        builder.setDuration(5);
        builder.setCompleted(false);
        builder.setId(-1);
        builder.setDueTime(380);
        builder.setCategory(newCategory);
        newTask = builder.build();

        usedCategory = new Category(5, "test", 0);

        builder = new Task.Builder("Test");
        builder.setDuration(5);
        builder.setCompleted(false);
        builder.setId(5);
        builder.setDueTime(380);
        builder.setCategory(usedCategory);
        usedTask = builder.build();
    }

    @Test
    public void testSaveNewTask() throws Exception {
        ContentResolver resolver = Mockito.mock(ContentResolver.class);
        when(resolver.insert(any(), any())).thenReturn(Uri.parse("test/1"));
        PersistenceHelper persistenceHelper = new PersistenceHelperImpl(resolver);
        persistenceHelper.saveTask(newTask);
        ArgumentCaptor<Uri> uriArgumentCaptor = ArgumentCaptor.forClass(Uri.class);
        ArgumentCaptor<ContentValues> contentValuesArgumentCaptor =
                ArgumentCaptor.forClass(ContentValues.class);
        verify(resolver).insert(uriArgumentCaptor.capture(),
                contentValuesArgumentCaptor.capture());
        assertEquals(TasksContract.TasksEntry.TASK_URI, uriArgumentCaptor.getValue());
        assertEquals(newTask.getName(),
                contentValuesArgumentCaptor.getValue().getAsString(
                        TasksContract.TasksEntry.COLUMN_NAME
                ));
        assertEquals((Long) newTask.getDueTime(),
                contentValuesArgumentCaptor.getValue().getAsLong(
                        TasksContract.TasksEntry.COLUMN_DUE_TIME
                ));
        assertEquals((Integer)newTask.getDuration(),
                contentValuesArgumentCaptor.getValue().getAsInteger(
                        TasksContract.TasksEntry.COLUMN_DURATION
                ));
        assertEquals((Long)(-1L),
                contentValuesArgumentCaptor.getValue().getAsLong(
                        TasksContract.TasksEntry.COLUMN_CATEGORY
                ));
        assertEquals(newTask.isCompleted(),
                contentValuesArgumentCaptor.getValue().getAsBoolean(
                        TasksContract.TasksEntry.COLUMN_COMPLETED
                ));
    }
    
    @Test
    public void testSaveUsedTask() throws Exception {
        ContentResolver resolver = Mockito.mock(ContentResolver.class);
        when(resolver.update(any(), any(), any(), any())).thenReturn(1);
        PersistenceHelper persistenceHelper = new PersistenceHelperImpl(resolver);
        persistenceHelper.saveTask(usedTask);
        ArgumentCaptor<Uri> uriArgumentCaptor = ArgumentCaptor.forClass(Uri.class);
        ArgumentCaptor<ContentValues> contentValuesArgumentCaptor =
                ArgumentCaptor.forClass(ContentValues.class);
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String[]> arrayArgumentCaptor = ArgumentCaptor.forClass(String[].class);
        verify(resolver).update(uriArgumentCaptor.capture(),
                contentValuesArgumentCaptor.capture(), stringArgumentCaptor.capture(),
                arrayArgumentCaptor.capture());
        Uri targetUri = TasksContract.TasksEntry.TASK_URI.buildUpon().appendPath("5").build();
        assertEquals(targetUri, uriArgumentCaptor.getValue());
        assertEquals(usedTask.getName(),
                contentValuesArgumentCaptor.getValue().getAsString(
                        TasksContract.TasksEntry.COLUMN_NAME
                ));
        assertEquals((Long) usedTask.getDueTime(),
                contentValuesArgumentCaptor.getValue().getAsLong(
                        TasksContract.TasksEntry.COLUMN_DUE_TIME
                ));
        assertEquals((Integer)usedTask.getDuration(),
                contentValuesArgumentCaptor.getValue().getAsInteger(
                        TasksContract.TasksEntry.COLUMN_DURATION
                ));
        assertEquals((Long)(5L),
                contentValuesArgumentCaptor.getValue().getAsLong(
                        TasksContract.TasksEntry.COLUMN_CATEGORY
                ));
        assertEquals(usedTask.isCompleted(),
                contentValuesArgumentCaptor.getValue().getAsBoolean(
                        TasksContract.TasksEntry.COLUMN_COMPLETED
                ));
    }

    @Test
    public void testSaveNewCategory() throws Exception {
        ContentResolver resolver = Mockito.mock(ContentResolver.class);
        when(resolver.insert(any(), any())).thenReturn(Uri.parse("test/1"));
        PersistenceHelper persistenceHelper = new PersistenceHelperImpl(resolver);
        persistenceHelper.saveCategory(newCategory);
        ArgumentCaptor<Uri> uriArgumentCaptor = ArgumentCaptor.forClass(Uri.class);
        ArgumentCaptor<ContentValues> contentValuesArgumentCaptor =
                ArgumentCaptor.forClass(ContentValues.class);
        verify(resolver).insert(uriArgumentCaptor.capture(),
                contentValuesArgumentCaptor.capture());
        assertEquals(TasksContract.CategoryEntry.CATEGORY_URI, uriArgumentCaptor.getValue());
        assertEquals("test",
                contentValuesArgumentCaptor.getValue().getAsString(
                        TasksContract.CategoryEntry.COLUMN_NAME
                ));
        assertEquals((Integer) 0,
                contentValuesArgumentCaptor.getValue().getAsInteger(
                        TasksContract.CategoryEntry.COLUMN_COLOR
                ));
    }

    @Test
    public void testLoadTask() throws Exception {
        ContentResolver resolver = Mockito.mock(ContentResolver.class);
        when(resolver.query(any(), any(), any(), any(), any())).thenReturn(cursorFromTask(usedTask));
        PersistenceHelper persistenceHelper = new PersistenceHelperImpl(resolver);
        Task result = persistenceHelper.loadTask(5);
        assertEquals(usedTask.getId(), result.getId());
        assertEquals(usedTask.getCreationTime(), result.getCreationTime());
        assertEquals(usedTask.getName(), result.getName());
        assertEquals(usedTask.isCompleted(), result.isCompleted());
        assertEquals(usedTask.getDuration(), result.getDuration());
        assertEquals(usedTask.getDueTime(), result.getDueTime());

        Category resultCategory = result.getCategory();
        assertEquals(usedCategory.getId(), resultCategory.getId());
        assertEquals(usedCategory.getName(), resultCategory.getName());
        assertEquals(usedCategory.getColor(), resultCategory.getColor());
    }

    @Test
    public void testLoadCategory() throws Exception {
        ContentResolver resolver = Mockito.mock(ContentResolver.class);
        when(resolver.query(any(), any(), any(), any(), any())).thenReturn(cursorFromCategory(usedCategory));
        PersistenceHelper persistenceHelper = new PersistenceHelperImpl(resolver);
        Category result = persistenceHelper.loadCategory(5);
        assertEquals(usedCategory.getId(), result.getId());
        assertEquals(usedCategory.getName(), result.getName());
        assertEquals(usedCategory.getColor(), result.getColor());
    }

    @Test
    public void testLoadAllCategories() throws Exception {

    }

    @Test
    public void testLoadTaskSummaries() throws Exception {

    }

    @Test
    public void testRecordCompletedTask() throws Exception {

    }

    @Test
    public void testDeleteTask() throws Exception {

    }

    @Test
    public void testDeleteCategory() throws Exception {

    }

    private Cursor cursorFromCategory(Category category) {
        String[] columns = new String[] {
                TasksContract.CategoryEntry._ID,
                TasksContract.CategoryEntry.COLUMN_NAME,
                TasksContract.CategoryEntry.COLUMN_COLOR
        };

        MatrixCursor matrixCursor = new MatrixCursor(columns);

        matrixCursor.addRow(new Object[] {
                category.getId(),
                category.getName(),
                category.getColor()
        });
        return matrixCursor;
    }

    private Cursor cursorFromTask(Task task) {
        String[] columns = new String[] {
                TasksContract.TaskViewEntry._ID,
                TasksContract.TaskViewEntry.COLUMN_CREATION_TIME,
                TasksContract.TaskViewEntry.COLUMN_NAME,
                TasksContract.TaskViewEntry.COLUMN_DURATION,
                TasksContract.TaskViewEntry.COLUMN_DUE_TIME,
                TasksContract.TaskViewEntry.COLUMN_COMPLETED,
                TasksContract.TaskViewEntry.COLUMN_CATEGORY_ID,
                TasksContract.TaskViewEntry.COLUMN_CATEGORY,
                TasksContract.TaskViewEntry.COLUMN_CATEGORY_COLOR};

        MatrixCursor matrixCursor= new MatrixCursor(columns);

        Category category = task.getCategory();

        matrixCursor.addRow(new Object[] {
                task.getId(),
                task.getCreationTime(),
                task.getName(),
                task.getDuration(),
                task.getDueTime(),
                task.isCompleted() ? 1 : 0,
                category.getId(),
                category.getName(),
                category.getColor()
        });
        return matrixCursor;
    }
}