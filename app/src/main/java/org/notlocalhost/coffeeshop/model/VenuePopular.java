package org.notlocalhost.coffeeshop.model;

import java.util.List;

public class VenuePopular {
    boolean isOpen;
    List<TimeFrame> timeframes;

    public boolean isOpen() {
        return isOpen;
    }

    public List<TimeFrame> getTimeframes() {
        return timeframes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VenuePopular that = (VenuePopular) o;

        if (isOpen != that.isOpen) return false;
        if (timeframes != null ? !timeframes.equals(that.timeframes) : that.timeframes != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (isOpen ? 1 : 0);
        result = 31 * result + (timeframes != null ? timeframes.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "VenuePopular{" +
                "isOpen=" + isOpen +
                ", timeframes=" + timeframes +
                '}';
    }
}
