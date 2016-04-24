package us.bridgeses.minder_tasks.startup;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.Map;

/**
 * Startup is a basic interface for an object with an init method that takes a map of preferences
 * and a run method. It extends Runnable to simplify threading.
 */
public abstract class Startup implements Runnable{

    Map<String, Object> preferences;
    Context context;

    public Startup(Map<String, Object> preferences, Context context) {
        this.preferences = preferences;
        this.context = context;
    }
}

/**
 * None is the default implementation of Startup. It simply stubs out the methods for when no
 * start up procedure is required.
 */
final class None extends Startup {

    public None(Map<String, Object> preferences, Context context) {
        super(preferences, context);
    }

    @Override
    public void run() {}
}

/**
 * FirstRun is an implementation of Startup intended to be used the first time the app is run.
 */
final class FirstRun extends Startup {

    public FirstRun(Map<String, Object> preferences, Context context) {
        super(preferences, context);
    }

    @Override
    public void run() {
        Boolean success = createStyleDirectory();
        if (!success) {
            Log.w(this.getClass().getSimpleName(), "Could not create styles directory");
        }
        else {
            // TODO: Persist hasRun
        }
    }

    private Boolean createStyleDirectory() {
        File mydir = context.getDir("styles", Context.MODE_PRIVATE); //Creating an internal dir;
        return mydir.mkdirs();
    }
}