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

package utils;

import android.util.Log;

/**
 * A convenience class for logging. Rather than calling the logger with a tag, call it with the
 * calling object: TaggedLog.d(this, "Something useful")
 */
public class TaggedLog {
    public static void d(Object tagClass, String message) {
        Log.d(tagClass.getClass().getName(), message);
    }
    public static void w(Object tagClass, String message) {
        Log.w(tagClass.getClass().getName(), message);
    }
    public static void i(Object tagClass, String message) {
        Log.i(tagClass.getClass().getName(), message);
    }
    public static void e(Object tagClass, String message) {
        Log.e(tagClass.getClass().getName(), message);
    }
    public static void v(Object tagClass, String message) {
        Log.v(tagClass.getClass().getName(), message);
    }
    public static void w(Object tagClass, Throwable tr) {
        Log.w(tagClass.getClass().getName(), tr);
    }
    public static void d(Object tagClass, String message, Throwable tr) {
        Log.d(tagClass.getClass().getName(), message, tr);
    }
    public static void w(Object tagClass, String message, Throwable tr) {
        Log.w(tagClass.getClass().getName(), message, tr);
    }
    public static void i(Object tagClass, String message, Throwable tr) {
        Log.i(tagClass.getClass().getName(), message, tr);
    }
    public static void e(Object tagClass, String message, Throwable tr) {
        Log.e(tagClass.getClass().getName(), message, tr);
    }
    public static void v(Object tagClass, String message, Throwable tr) {
        Log.v(tagClass.getClass().getName(), message, tr);
    }
}
