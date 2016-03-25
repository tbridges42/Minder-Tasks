package us.bridgeses.minder_tasks.startup;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.Map;

/**
 * Startup is a basic interface for an object with an init method that takes a map of preferences
 * and a run method. It extends Runnable to simplify threading.
 */
public interface Startup extends Runnable{
    void init(Map<String, Object> preferences, Context context);
}

/**
 * None is the default implementation of Startup. It simply stubs out the methods for when no
 * start up procedure is required.
 */
final class None implements Startup {
    @Override
    public void run() {}

    @Override
    public void init(Map<String, Object> preferences, Context context) {}
}

/**
 * FirstRun is an implementation of Startup intended to be used the first time the app is run.
 */
final class FirstRun implements Startup {

    Context context;

    @Override
    public void init(Map<String, Object> preferences, Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        Boolean success = createStyleDirectory();
        if (!success) {
            Log.w(this.getClass().getSimpleName(), "Could not create styles directory");
        }
    }

    private Boolean createStyleDirectory() {
        File mydir = context.getDir("styles", Context.MODE_PRIVATE); //Creating an internal dir;
        return mydir.mkdirs();
    }
}