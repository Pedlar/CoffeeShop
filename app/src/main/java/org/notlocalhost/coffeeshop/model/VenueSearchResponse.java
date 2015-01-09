package org.notlocalhost.coffeeshop.model;

import java.util.List;

public class VenueSearchResponse {
    public Response response;

    public class Response {
        public List<Venue> venues;
        public Venue venue;
    }
}
