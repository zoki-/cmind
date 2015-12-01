package com.zoranbogdanovski.searchmoviesapp.core;

import android.app.Application;

import roboguice.RoboGuice;

/**
 * Application.
 */
public class App extends Application {

    static {
        RoboGuice.setUseAnnotationDatabases(false);
    }

    private static App instance;

    /**
     * Default constructor.
     */
    public App() {
        if (instance == null) {
            instance = this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        injectMembers();
    }

    protected void injectMembers() {
        // force injecting of the fields
        RoboGuice.getOrCreateBaseApplicationInjector(this).injectMembers(this);
    }

    /**
     * Gets the application instance.
     *
     * @return the application instance
     */
    public static App getInstance() {
        return instance;
    }

}
