package us.bridgeses.minder_tasks.startup;

import android.content.Context;

import java.util.Map;

/**
 * Factory class to return a class whose run method should be called at app startup.
 * HashMap preferences should be a subset of SharedPreferences or similar.
 */
public class StartupFactory {

    private final Context context;

    public StartupFactory(Context context) {
        this.context = context;
    }

    /**
     * @param preferences should be a map of Strings and Objects representing User preferences,
     *                    most likely created through SharedPreferences.getAll() or similar.
     */
    public Startup getStartup(Map<String, Object> preferences) {
        if (!preferences.containsKey("hasRun")) {
            return new FirstRun(preferences, context);
        }
        return new None(preferences, context);
    }
}
