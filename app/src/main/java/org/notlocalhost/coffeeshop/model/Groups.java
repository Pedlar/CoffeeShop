package org.notlocalhost.coffeeshop.model;

import com.google.gson.JsonArray;

public class Groups {
    String type;
    String name;
    int count;
    JsonArray items;

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public JsonArray getItems() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Groups groups = (Groups) o;

        if (count != groups.count) return false;
        if (items != null ? !items.equals(groups.items) : groups.items != null) return false;
        if (name != null ? !name.equals(groups.name) : groups.name != null) return false;
        if (type != null ? !type.equals(groups.type) : groups.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + count;
        result = 31 * result + (items != null ? items.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Groups{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", count=" + count +
                ", items=" + items +
                '}';
    }
}
