package org.notlocalhost.coffeeshop;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.notlocalhost.coffeeshop.model.Venue;
import org.notlocalhost.coffeeshop.model.VenueSearchResponse;
import org.notlocalhost.coffeeshop.network.FoursquareService;
import org.notlocalhost.coffeeshop.ui.fragment.VenueDetailsFragment;
import org.notlocalhost.coffeeshop.ui.fragment.VenueListFragment;
import org.notlocalhost.coffeeshop.ui.interfaces.OnDrawerListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MapActivity extends FragmentActivity implements
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, OnDrawerListener {

    private static final String VENUE_LIST_TAG = "VENUE_LIST";
    private static final int TOOLBAR_ANIM_DURATION = 200;

    private GoogleMap mMap;

    @Inject
    FoursquareService mFourSquareService;

    Marker mCurrentFocusedMarker;
    VenueListFragment mVenueListFragment;

    @InjectView(R.id.sliding_drawer)
    SlidingUpPanelLayout mSlidingDrawer;

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    Map<Marker, Venue> mMarkers = new HashMap<>();
    boolean isToolbarShowing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CoffeeApplication)getApplication()).inject(this);
        setContentView(R.layout.map_activity);
        ButterKnife.inject(this);

        hideToolbar(false);
        mToolbar.setTitle(R.string.app_name);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlidingDrawer.setSlidingEnabled(true);
                if(mSlidingDrawer.isPanelExpanded()) {
                    mSlidingDrawer.collapsePanel();
                }
            }
        });
        mToolbar.setTitleTextColor(Color.WHITE);

        setUpMapIfNeeded();
        mSlidingDrawer.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {
                if (mVenueListFragment != null && !mVenueListFragment.isHidden()) {
                    mVenueListFragment.onPanelSlide(view, v);
                }
                if(mCurrentFocusedMarker != null) {
                    VenueDetailsFragment venueDetailsFragment = (VenueDetailsFragment)getFragmentManager().findFragmentByTag(mMarkers.get(mCurrentFocusedMarker).getId());
                    if(venueDetailsFragment != null) {
                        venueDetailsFragment.onPanelSlide(view, v);
                    }
                }
                if(v > .6f) {
                    showToolbar(true);
                } else {
                    hideToolbar(true);
                }

                mSlidingDrawer.setSlidingEnabled(v != 1.0f);
            }
            @Override public void onPanelCollapsed(View view) { }
            @Override public void onPanelExpanded(View view) { }
            @Override public void onPanelAnchored(View view) { }
            @Override public void onPanelHidden(View view) { }
        });

        mSlidingDrawer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                View panel = mSlidingDrawer.getChildAt(1);
                ViewGroup.LayoutParams layoutParams = panel.getLayoutParams();
                int toolbarHeight = (int)getResources().getDimension(R.dimen.abc_action_bar_default_height_material);
                layoutParams.height = panel.getMeasuredHeight() - toolbarHeight;
                panel.setLayoutParams(layoutParams);

                mSlidingDrawer.requestLayout();
                mSlidingDrawer.invalidate();

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mSlidingDrawer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mSlidingDrawer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case android.R.id.home:
                if(mSlidingDrawer.isPanelExpanded()) {
                    mSlidingDrawer.collapsePanel();
                }
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public void onBackPressed() {
        mSlidingDrawer.setSlidingEnabled(true);
        if(mSlidingDrawer.isPanelExpanded()) {
            mSlidingDrawer.collapsePanel();
        } else {
            super.onBackPressed();
        }
    }

    private void hideToolbar(boolean animate) {
        if(isToolbarShowing) {
            int toolbarHeight = (int) getResources().getDimension(R.dimen.abc_action_bar_default_height_material);
            isToolbarShowing = false;

            if (animate) {
                ObjectAnimator animator = new ObjectAnimator();
                animator.setTarget(mToolbar);
                animator.setDuration(TOOLBAR_ANIM_DURATION);
                animator.setPropertyName("translationY");
                animator.setFloatValues(0, -1 * toolbarHeight);
                animator.start();
            } else {
                mToolbar.setTranslationY(-1 * toolbarHeight);
            }
        }
    }

    private void showToolbar(boolean animate) {
        if(!isToolbarShowing) {
            isToolbarShowing = true;
            if (animate) {
                ObjectAnimator animator = new ObjectAnimator();
                animator.setTarget(mToolbar);
                animator.setDuration(TOOLBAR_ANIM_DURATION);
                animator.setPropertyName("translationY");
                animator.setFloatValues(mToolbar.getTranslationY(), 0);
                animator.start();
            } else {
                mToolbar.setTranslationY(0);
            }
        }
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12f));
                updateMapPlots(location.getLatitude(), location.getLongitude());
                mMap.setOnMyLocationChangeListener(null);
            }
        });
    }

    private void setupVenueList(List<Venue> venues) {
        if(mVenueListFragment == null) {
            mVenueListFragment = VenueListFragment.newInstance();
            mVenueListFragment.setVenueList(venues);
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.drawer_container, mVenueListFragment, VENUE_LIST_TAG)
                    .commit();
        }
    }

    private void updateMapPlots(final double lat, final double lng) {
        mFourSquareService.searchVenues(lat + "," + lng, Constants.COFFEE, new Callback<VenueSearchResponse>() {
            @Override
            public void success(VenueSearchResponse venueSearchResponse, Response response) {
                mMarkers.clear();
                for(Venue venue : venueSearchResponse.response.venues) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(venue.getLocation().getLatitude(), venue.getLocation().getLongitude()));
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.coffee_badge));
                    mMarkers.put(mMap.addMarker(markerOptions), venue);
                }
                setupVenueList(venueSearchResponse.response.venues);
            }

            @Override
            public void failure(RetrofitError error) {
                new AlertDialog.Builder(MapActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                        .setMessage(R.string.network_error)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                mSlidingDrawer.hidePanel();
                            }
                        })
                        .setCancelable(false)
                        .create().show();
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        updateFocusedMarker(marker);
        return false;
    }

    private void updateFocusedMarker(Marker marker) {
        if(marker != mCurrentFocusedMarker) {
            if(mCurrentFocusedMarker != null) {
                mCurrentFocusedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.coffee_badge));
            }
            if (!marker.isInfoWindowShown()) {
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.coffee));
            }

            FragmentTransaction ft = getFragmentManager().beginTransaction();

            Venue oldVenue = mMarkers.get(mCurrentFocusedMarker);
            if(oldVenue != null) {
                Fragment fragment = getFragmentManager().findFragmentByTag(oldVenue.getId());
                ft.remove(fragment);
            }

            Venue venue = mMarkers.get(marker);

            ft.hide(mVenueListFragment)
              .add(R.id.drawer_container, VenueDetailsFragment.newInstance(venue.getName(), venue.getId()), venue.getId())
              .commit();
        }
        mCurrentFocusedMarker = marker;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(mCurrentFocusedMarker != null) {
            mCurrentFocusedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.coffee_badge));

            Venue venue = mMarkers.get(mCurrentFocusedMarker);

            Fragment fragment = getFragmentManager().findFragmentByTag(venue.getId());
            getFragmentManager()
                    .beginTransaction()
                    .remove(fragment)
                    .show(mVenueListFragment)
                    .commit();

            mCurrentFocusedMarker = null;
        }
    }

    @Override
    public void closeDrawer() {
        mSlidingDrawer.collapsePanel();
    }

    @Override
    public void openDrawer() {
        mSlidingDrawer.expandPanel();
    }

    @Override
    public void setToolbarTitle(String title) {
        if(mToolbar != null) {
            mToolbar.setTitle(title);
        }
    }

    @Override
    public void goTo(Venue venue) {
        mSlidingDrawer.collapsePanel();
        mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                        new LatLng(venue.getLocation().getLatitude(),
                        venue.getLocation().getLongitude()),
                        16f
                )
        );

        for(Marker marker : mMarkers.keySet()) {
            if(marker.getPosition().latitude == venue.getLocation().getLatitude()
                    && marker.getPosition().longitude == venue.getLocation().getLongitude()) {
                updateFocusedMarker(marker);
            }
        }
    }

    @Override
    public double getDistanceFromMe(LatLng latLng) {
        Location location = mMap.getMyLocation();
        float[] results = new float[1];
        Location.distanceBetween(latLng.latitude, latLng.longitude, location.getLatitude(), location.getLongitude(), results);
        double distanceInMiles = (results[0] / 1609.344);

        return 0.01 * Math.floor(distanceInMiles * 100);
    }
}
