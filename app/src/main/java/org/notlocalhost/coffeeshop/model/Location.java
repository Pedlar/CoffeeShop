package org.notlocalhost.coffeeshop.model;

import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("lat")
    double latitude;

    @SerializedName("lng")
    double longitude;

    String address;
    String crossStreet;
    String city;
    String state;
    String postalCode;
    String country;

    // This is in Meters
    double distance;

    public double getDistance() {
        return distance;
    }

    public String getAddress() {
        return address;
    }

    public String getCrossStreet() {
        return crossStreet;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }

    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if(address != null) {
            sb.append(address);
            sb.append(", ");
        }
        if(city != null) {
            sb.append(city);
            sb.append(", ");
        }
        if(state != null) {
            sb.append(state);
            sb.append(" ");
        }
        if(postalCode != null) {
            sb.append(postalCode);
        }
        return sb.toString();
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (Double.compare(location.distance, distance) != 0) return false;
        if (Double.compare(location.latitude, latitude) != 0) return false;
        if (Double.compare(location.longitude, longitude) != 0) return false;
        if (address != null ? !address.equals(location.address) : location.address != null)
            return false;
        if (city != null ? !city.equals(location.city) : location.city != null) return false;
        if (country != null ? !country.equals(location.country) : location.country != null)
            return false;
        if (crossStreet != null ? !crossStreet.equals(location.crossStreet) : location.crossStreet != null)
            return false;
        if (postalCode != null ? !postalCode.equals(location.postalCode) : location.postalCode != null)
            return false;
        if (state != null ? !state.equals(location.state) : location.state != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (crossStreet != null ? crossStreet.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (postalCode != null ? postalCode.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        temp = Double.doubleToLongBits(distance);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Location{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                ", crossStreet='" + crossStreet + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", country='" + country + '\'' +
                ", distance=" + distance +
                '}';
    }
}
