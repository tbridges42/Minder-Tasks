package us.bridgeses.minder_tasks;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Tony on 4/24/2016.
 *
 * Custom application to implement LeakCanary memory leak tracking.
 */
public class CanaryApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
