package org.notlocalhost.coffeeshop.model;

import java.util.List;

public class TimeFrame {
    String days;
    boolean includesToday;
    List<OpenData> open;

    public class OpenData {
        String renderedTime;

        public String getRenderedTime() {
            return renderedTime;
        }

        @Override
        public String toString() {
            return renderedTime;
        }
    }

    public String getDays() {
        return days;
    }

    public boolean isIncludesToday() {
        return includesToday;
    }

    public List<OpenData> getOpen() {
        return open;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeFrame timeFrame = (TimeFrame) o;

        if (includesToday != timeFrame.includesToday) return false;
        if (days != null ? !days.equals(timeFrame.days) : timeFrame.days != null) return false;
        if (open != null ? !open.equals(timeFrame.open) : timeFrame.open != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = days != null ? days.hashCode() : 0;
        result = 31 * result + (includesToday ? 1 : 0);
        result = 31 * result + (open != null ? open.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TimeFrame{" +
                "days='" + days + '\'' +
                ", includesToday=" + includesToday +
                ", open=" + open +
                '}';
    }
}
