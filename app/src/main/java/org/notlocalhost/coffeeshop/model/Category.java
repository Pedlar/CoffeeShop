package org.notlocalhost.coffeeshop.model;

import org.notlocalhost.coffeeshop.Constants;

public class Category {
    String id;
    String name;
    String pluralName;
    String shortName;
    boolean primary;
    Icon icon;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPluralName() {
        return pluralName;
    }

    public String getShortName() {
        return shortName;
    }

    public boolean isPrimary() {
        return primary;
    }

    public String getIconUrl() {
        return icon.getIconUrl(Constants.ORIGINAL);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", pluralName='" + pluralName + '\'' +
                ", shortName='" + shortName + '\'' +
                ", primary=" + primary +
                ", icon=" + icon +
                '}';
    }
}
