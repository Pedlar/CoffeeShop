package org.notlocalhost.coffeeshop.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

public class Venue {
    String id;
    String name;
    String description;
    ContactInfo contact;
    Location location;
    List<Category> categories;
    boolean verified;
    VenueStats stats;
    String url;
    float rating;
    VenuePopular popular;
    Photos photos;
    String canonicalUrl;


    public float getRating() {
        return rating;
    }

    public VenuePopular getPopular() {
        return popular;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ContactInfo getContact() {
        return contact;
    }

    public Location getLocation() {
        return location;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public boolean isVerified() {
        return verified;
    }

    public VenueStats getStats() {
        return stats;
    }

    public String getUrl() {
        return url;
    }

    public String getCanonicalUrl() {
        return canonicalUrl;
    }

    public List<Icon> getPhotos() {
        List<Icon> iconList = new ArrayList<>();
        if(photos != null && (photos.groups != null && photos.groups.size() > 0)) {
            JsonArray items = photos.groups.get(0).getItems();
            for (int i = 0; i < items.size(); i++) {
                Icon icon = new Gson().fromJson(items.get(i).getAsJsonObject(), Icon.class);
                iconList.add(icon);
            }
        }
        return iconList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Venue venue = (Venue) o;

        if (Float.compare(venue.rating, rating) != 0) return false;
        if (verified != venue.verified) return false;
        if (canonicalUrl != null ? !canonicalUrl.equals(venue.canonicalUrl) : venue.canonicalUrl != null)
            return false;
        if (categories != null ? !categories.equals(venue.categories) : venue.categories != null)
            return false;
        if (contact != null ? !contact.equals(venue.contact) : venue.contact != null) return false;
        if (description != null ? !description.equals(venue.description) : venue.description != null)
            return false;
        if (id != null ? !id.equals(venue.id) : venue.id != null) return false;
        if (location != null ? !location.equals(venue.location) : venue.location != null)
            return false;
        if (name != null ? !name.equals(venue.name) : venue.name != null) return false;
        if (photos != null ? !photos.equals(venue.photos) : venue.photos != null) return false;
        if (popular != null ? !popular.equals(venue.popular) : venue.popular != null) return false;
        if (stats != null ? !stats.equals(venue.stats) : venue.stats != null) return false;
        if (url != null ? !url.equals(venue.url) : venue.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (contact != null ? contact.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (categories != null ? categories.hashCode() : 0);
        result = 31 * result + (verified ? 1 : 0);
        result = 31 * result + (stats != null ? stats.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (rating != +0.0f ? Float.floatToIntBits(rating) : 0);
        result = 31 * result + (popular != null ? popular.hashCode() : 0);
        result = 31 * result + (photos != null ? photos.hashCode() : 0);
        result = 31 * result + (canonicalUrl != null ? canonicalUrl.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Venue{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", contact=" + contact +
                ", location=" + location +
                ", categories=" + categories +
                ", verified=" + verified +
                ", stats=" + stats +
                ", url='" + url + '\'' +
                ", rating=" + rating +
                ", popular=" + popular +
                ", photos=" + photos +
                ", canonicalUrl='" + canonicalUrl + '\'' +
                '}';
    }

    public class Photos {
        List<Groups> groups;

        @Override
        public String toString() {
            return "Photos{" +
                    "groups=" + groups +
                    '}';
        }
    }
}
