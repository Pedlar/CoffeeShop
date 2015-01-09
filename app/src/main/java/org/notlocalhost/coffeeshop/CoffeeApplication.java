package org.notlocalhost.coffeeshop;

import android.app.Application;

import org.notlocalhost.coffeeshop.module.LocationModule;
import org.notlocalhost.coffeeshop.module.NetworkModule;

import dagger.ObjectGraph;

public class CoffeeApplication extends Application {
    private ObjectGraph mObjectGraph;


    @Override
    public void onCreate() {
        mObjectGraph = ObjectGraph.create(getModules());
        mObjectGraph.injectStatics();
    }

    public void inject(Object o) {
        mObjectGraph.inject(o);
    }

    private Object[] getModules() {
        return new Object[] {
                new NetworkModule(),
                new LocationModule(this)
        };
    }
}
