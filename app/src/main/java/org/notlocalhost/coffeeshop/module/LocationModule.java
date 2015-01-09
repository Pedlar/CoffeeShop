package org.notlocalhost.coffeeshop.module;

import android.content.Context;
import android.location.LocationManager;

import org.notlocalhost.coffeeshop.MapActivity;

import dagger.Module;
import dagger.Provides;

@Module(
        complete = false,
        injects = {
                MapActivity.class
        },
        library = true
)
public class LocationModule {
    private Context mApplicationContext;
    public LocationModule(Context context) {
        mApplicationContext = context;
    }

    @Provides
    public LocationManager providesLocationManager() {
        return (LocationManager)mApplicationContext.getSystemService(Context.LOCATION_SERVICE);
    }

}
