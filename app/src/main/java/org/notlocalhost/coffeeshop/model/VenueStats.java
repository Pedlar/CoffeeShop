package org.notlocalhost.coffeeshop.model;

public class VenueStats {
    int checkinsCount;
    int usersCount;
    int tipCount;

    public int getCheckinsCount() {
        return checkinsCount;
    }

    public int getUsersCount() {
        return usersCount;
    }

    public int getTipCount() {
        return tipCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VenueStats that = (VenueStats) o;

        if (checkinsCount != that.checkinsCount) return false;
        if (tipCount != that.tipCount) return false;
        if (usersCount != that.usersCount) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = checkinsCount;
        result = 31 * result + usersCount;
        result = 31 * result + tipCount;
        return result;
    }

    @Override
    public String toString() {
        return "VenueStats{" +
                "checkinsCount=" + checkinsCount +
                ", usersCount=" + usersCount +
                ", tipCount=" + tipCount +
                '}';
    }
}
