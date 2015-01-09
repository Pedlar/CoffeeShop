package org.notlocalhost.coffeeshop.network;

import org.notlocalhost.coffeeshop.Constants;
import org.notlocalhost.coffeeshop.model.VenueSearchResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface FoursquareService {
    @GET(Constants.VENUE_SEARCH)
    public void searchVenues(@Query("ll") String ll, @Query("query") String query, Callback<VenueSearchResponse> callback);

    @GET(Constants.GET_VENUE)
    public void getVenue(@Path("venue_id") String venueId, Callback<VenueSearchResponse> callback);
}
