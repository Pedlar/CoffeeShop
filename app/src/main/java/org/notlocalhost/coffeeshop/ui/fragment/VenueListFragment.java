package org.notlocalhost.coffeeshop.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.notlocalhost.coffeeshop.CoffeeApplication;
import org.notlocalhost.coffeeshop.Constants;
import org.notlocalhost.coffeeshop.R;
import org.notlocalhost.coffeeshop.model.Icon;
import org.notlocalhost.coffeeshop.model.Venue;
import org.notlocalhost.coffeeshop.model.VenueSearchResponse;
import org.notlocalhost.coffeeshop.network.FoursquareService;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class VenueListFragment extends BaseDrawerFragment {

    private List<Venue> mVenueList;

    @InjectView(R.id.show_venue_list)
    LinearLayout mShowVenueList;

    @InjectView(R.id.venue_list)
    RecyclerView mVenueRecyclerView;

    @Inject
    FoursquareService mFoursquareService;

    public VenueListFragment() {

    }

    public static VenueListFragment newInstance() {
        VenueListFragment fragment = new VenueListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.venue_list_fragment, container, false);
        ButterKnife.inject(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        mVenueRecyclerView.setLayoutManager(layoutManager);

        mVenueRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        updateVenueRecycler();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((CoffeeApplication)activity.getApplication()).inject(this);
    }

    @Override
    public void onPanelSlide(View view, float v) {
        if(mShowVenueList != null) {
            mShowVenueList.setVisibility(v > 0f ? View.GONE : View.VISIBLE);
        }
        if(mVenueRecyclerView != null) {
            mVenueRecyclerView.setVisibility(v > 0f ? View.VISIBLE : View.GONE);
            mVenueRecyclerView.setAlpha(v);
        }
    }

    public void setVenueList(List<Venue> venueList) {
        mVenueList = venueList;
        updateVenueRecycler();
    }

    private void updateVenueRecycler() {
        if(mVenueRecyclerView != null && mVenueList != null) {
            if (mVenueRecyclerView.getAdapter() != null) {
                mVenueRecyclerView.getAdapter().notifyDataSetChanged();
            } else {
                mVenueRecyclerView.setAdapter(new VenueListAdapter());
            }
        }
    }

    private void updateVenueInformation(VenueHolder venueHolder, Venue venue) {
        venueHolder.setRating(venue.getRating());
        if(venue.getDescription() != null) {
            venueHolder.setDescription(venue.getDescription());
        }

        if(venue.getPopular() != null && !venue.getPopular().isOpen()) {
            venueHolder.setClosed(true);
        }

        List<Icon> iconList = venue.getPhotos();
        if(iconList.size() > 0) {
            venueHolder.venueImage.setVisibility(View.VISIBLE);
            Picasso.with(getActivity()).load(iconList.get(0).getIconUrl(Constants.ORIGINAL)).into(venueHolder.venueImage);

        }
    }

    public class VenueHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public VenueHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
        @InjectView(R.id.venue_title)
        TextView venueTitle;

        @InjectView(R.id.progress_spinner)
        ProgressBar progressSpinner;

        @InjectView(R.id.rating_bar)
        RatingBar ratingBar;

        @InjectView(R.id.rating_text)
        TextView ratingText;

        @InjectView(R.id.venue_description)
        TextView description;

        @InjectView(R.id.venue_closed)
        TextView venueClosed;

        @InjectView(R.id.venue_screenshot)
        ImageView venueImage;

        public void setTitle(String title) {
            venueTitle.setText(title);
        }

        public void setRating(float rating) {
            ratingText.setText(String.valueOf(rating));
            ratingBar.setRating(rating);
            ratingBar.setVisibility(View.VISIBLE);
            ratingText.setVisibility(View.VISIBLE);
        }

        public void setDescription(String desc) {
            description.setText(desc);
            description.setVisibility(View.VISIBLE);
        }

        public void setClosed(boolean closed) {
            venueClosed.setVisibility(closed ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public class VenueListAdapter extends RecyclerView.Adapter<VenueHolder> {
        @Override
        public VenueHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.venue_list_item, parent, false);
            return new VenueHolder(view);
        }

        @Override
        public void onBindViewHolder(final VenueHolder holder, int position) {
            Venue venue = mVenueList.get(position);
            holder.setTitle(venue.getName());

            holder.ratingText.setVisibility(View.GONE);
            holder.ratingBar.setVisibility(View.GONE);
            holder.description.setVisibility(View.GONE);
            holder.venueClosed.setVisibility(View.GONE);
            holder.progressSpinner.setVisibility(View.VISIBLE);
            holder.venueImage.setVisibility(View.GONE);

            holder.itemView.setTag(venue);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Venue venue = (Venue)v.getTag();
                    if(mDrawerListener != null) {
                        mDrawerListener.goTo(venue);
                        mDrawerListener.closeDrawer();
                    }
                }
            });

            if(mFoursquareService != null) {
                mFoursquareService.getVenue(venue.getId(), new Callback<VenueSearchResponse>() {
                    @Override
                    public void success(VenueSearchResponse venueSearchResponse, Response response) {
                        Venue venue = venueSearchResponse.response.venue;
                        if(venue != null) {
                            updateVenueInformation(holder, venue);
                            holder.progressSpinner.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mVenueList.size();
        }
    }
}
