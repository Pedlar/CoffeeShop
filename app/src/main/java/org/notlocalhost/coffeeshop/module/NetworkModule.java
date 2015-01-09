package org.notlocalhost.coffeeshop.module;

import com.squareup.okhttp.OkHttpClient;

import org.notlocalhost.coffeeshop.Constants;
import org.notlocalhost.coffeeshop.MapActivity;
import org.notlocalhost.coffeeshop.network.FoursquareService;
import org.notlocalhost.coffeeshop.ui.fragment.VenueDetailsFragment;
import org.notlocalhost.coffeeshop.ui.fragment.VenueListFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

@Module(
        complete = false,
        injects = {
                MapActivity.class, VenueListFragment.class, VenueDetailsFragment.class
        }
)
public class NetworkModule {
    RestAdapter mRestAdapter;

    public NetworkModule() {
        mRestAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.FOURSQUARE_ENDPOINT)
                .setClient(getOkHttpClient())
                .setRequestInterceptor(getRequestInterceptor())
                .build();
    }

    @Provides
    @Singleton
    public FoursquareService providesFoursquareService() {
        return mRestAdapter.create(FoursquareService.class);
    }

    private OkClient getOkHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient();
        return new OkClient(okHttpClient);
    }

    private RequestInterceptor getRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addQueryParam("client_id", Constants.CLIENT_ID);
                request.addQueryParam("client_secret", Constants.CLIENT_SECRET);
                request.addQueryParam("v", "20150107");
            }
        };
    }

}
