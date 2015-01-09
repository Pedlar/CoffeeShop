package org.notlocalhost.coffeeshop.ui.interfaces;

import com.google.android.gms.maps.model.LatLng;

import org.notlocalhost.coffeeshop.model.Venue;

public interface OnDrawerListener {
    public void setToolbarTitle(String title);
    public void goTo(Venue venue);
    public double getDistanceFromMe(LatLng latLng);
    void closeDrawer();
    void openDrawer();
}
