package org.notlocalhost.coffeeshop.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Icon implements Parcelable{
    String prefix;
    String suffix;

    public String getIconUrl(String size) {
        return prefix + size + suffix;
    }

    @Override
    public String toString() {
        return prefix + suffix;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(prefix);
        dest.writeString(suffix);
    }

    public static final Parcelable.Creator<Icon> CREATOR
            = new Parcelable.Creator<Icon>() {
        public Icon createFromParcel(Parcel in) {
            return new Icon(in);
        }

        public Icon[] newArray(int size) {
            return new Icon[size];
        }
    };

    private Icon(Parcel in) {
        prefix = in.readString();
        suffix = in.readString();
    }
}
