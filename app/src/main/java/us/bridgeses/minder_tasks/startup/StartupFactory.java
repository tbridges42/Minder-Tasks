package us.bridgeses.minder_tasks.startup;

import android.content.Context;

import java.util.Map;

/**
 * Factory class to return a class whose run method should be called at app startup.
 * HashMap preferences should be a subset of SharedPreferences or similar.
 */
public class StartupFactory {

    private final Map<String, Object> preferences;

    /**
     * @param preferences should be a map of Strings and Objects representing User preferences,
     *                    most likely created through SharedPreferences.getAll() or similar.
     */
    public StartupFactory(Map<String, Object> preferences) {
        this.preferences = preferences;
    }

    public Startup getStartup(Context context) {
        if (!preferences.containsKey("hasRun")) {
            return new FirstRun(preferences, context);
        }
        return new None(preferences, context);
    }
}
