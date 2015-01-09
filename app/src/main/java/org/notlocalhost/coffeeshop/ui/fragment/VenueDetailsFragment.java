package org.notlocalhost.coffeeshop.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.notlocalhost.coffeeshop.CoffeeApplication;
import org.notlocalhost.coffeeshop.Constants;
import org.notlocalhost.coffeeshop.GalleryActivity;
import org.notlocalhost.coffeeshop.R;
import org.notlocalhost.coffeeshop.model.Icon;
import org.notlocalhost.coffeeshop.model.TimeFrame;
import org.notlocalhost.coffeeshop.model.Venue;
import org.notlocalhost.coffeeshop.model.VenueSearchResponse;
import org.notlocalhost.coffeeshop.network.FoursquareService;
import org.notlocalhost.coffeeshop.widget.LockScrollView;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class VenueDetailsFragment extends BaseDrawerFragment {
    private static final String VENUE_NAME_KEY = "venue_name_key";
    private static final String VENUE_ID_KEY = "venue_id_key";
    private static final String TODAY = "Today";

    private String mVenueId;
    private String mVenueName;
    private Venue mVenue;

    @Inject
    FoursquareService mFoursquareService;

    @InjectView(R.id.details_title_container) RelativeLayout mDetailsTitleContainer;
    @InjectView(R.id.call_website_container) LinearLayout mCallWebsiteContainer;
    @InjectView(R.id.address_container) LinearLayout mAddressContainer;
    @InjectView(R.id.details_container) LinearLayout mDetailsContainer;
    @InjectView(R.id.phone_container) LinearLayout mPhoneContainer;
    @InjectView(R.id.website_container) LinearLayout mWebsiteContainer;
    @InjectView(R.id.photos_container) LinearLayout mPhotosContainer;
    @InjectView(R.id.hours_container) LinearLayout mHoursContainer;
    @InjectView(R.id.hour_item_container) LinearLayout mHoursItemContainer;
    @InjectView(R.id.hours_collapsed_container) LinearLayout mHoursCollapsedContainer;
    @InjectView(R.id.venue_foursquare) LinearLayout mFoursquareBtn;

    @InjectView(R.id.venue_title) TextView mVenueTitle;
    @InjectView(R.id.progress_spinner) ProgressBar mProgressSpinner;
    @InjectView(R.id.rating_bar) RatingBar mRatingBar;
    @InjectView(R.id.rating_text) TextView mRatingText;
    @InjectView(R.id.description_container) LinearLayout mDescriptionContainer;
    @InjectView(R.id.venue_description) TextView mVenueDescription;
    @InjectView(R.id.venue_call) LinearLayout mVenueCall;
    @InjectView(R.id.venue_website) LinearLayout mVenueWebsite;
    @InjectView(R.id.address) TextView mAddress;
    @InjectView(R.id.phone_number) TextView mPhoneNumber;
    @InjectView(R.id.website) TextView mWebsite;
    @InjectView(R.id.distance) TextView mDistance;
    @InjectView(R.id.call_btn_icon) ImageView mCallBtnIcon;
    @InjectView(R.id.website_btn_icon) ImageView mWebsiteBtnIcon;
    @InjectView(R.id.image_one) ImageView mImageOne;
    @InjectView(R.id.image_two) ImageView mImageTwo;
    @InjectView(R.id.image_more) TextView mMoreImages;
    @InjectView(R.id.hour_closed) TextView mHoursClosed;
    @InjectView(R.id.hour_today) TextView mHoursToday;

    private View.OnClickListener mImageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            GalleryActivity.start(getActivity(), mVenue.getPhotos());
        }
    };

    public VenueDetailsFragment() {

    }

    public static VenueDetailsFragment newInstance(String venueName, String venueId) {
        VenueDetailsFragment fragment = new VenueDetailsFragment();
        Bundle args = new Bundle();
        args.putString(VENUE_NAME_KEY, venueName);
        args.putString(VENUE_ID_KEY, venueId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        if(getArguments() != null) {
            mVenueId = getArguments().getString(VENUE_ID_KEY);
            mVenueName = getArguments().getString(VENUE_NAME_KEY);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((CoffeeApplication)activity.getApplication()).inject(this);
    }

    @Override
    public void onPanelSlide(View view, float v) {
        if(mDetailsTitleContainer != null) {
            float percent = v * mDetailsTitleContainer.getHeight();
            moveContainers(-1 * percent);
        }
        if(getView() != null) {
            ((LockScrollView)getView()).setScrollingEnabled(v == 1);
        }

        if(v > .5) {
            mDrawerListener.setToolbarTitle(mVenueName);
        } else {
            mDrawerListener.setToolbarTitle(getResources().getString(R.string.app_name));
        }

        if(v == 0 && getView() != null) {
            (getView()).scrollTo(0, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle icicle) {
        View view = inflater.inflate(R.layout.venue_details_fragment, container, false);
        ((LockScrollView)view).setScrollingEnabled(false);

        ButterKnife.inject(this, view);

        mVenueTitle.setText(mVenueName);

        if(mVenue == null) {
            mFoursquareService.getVenue(mVenueId, new Callback<VenueSearchResponse>() {
                @Override
                public void success(VenueSearchResponse venueSearchResponse, Response response) {
                    mProgressSpinner.setVisibility(View.GONE);
                    mVenue = venueSearchResponse.response.venue;
                    updateVenueInformation(venueSearchResponse.response.venue);
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        } else {
            updateVenueInformation(mVenue);
        }

        return view;
    }

    private void moveContainers(float y) {
        if(mDetailsTitleContainer != null) {
            mDetailsTitleContainer.setTranslationY(y);
        }
        if(mDescriptionContainer != null) {
            mDescriptionContainer.setTranslationY(y);
        }
        if(mCallWebsiteContainer != null) {
            mCallWebsiteContainer.setTranslationY(y);
        }
        if(mDetailsContainer != null) {
            mDetailsContainer.setTranslationY(y);
        }
        if(mPhotosContainer != null) {
            mPhotosContainer.setTranslationY(y);
        }
    }

    private void updateVenueInformation(Venue venue) {
        mRatingText.setText(String.valueOf(venue.getRating()));
        mRatingBar.setRating(venue.getRating());

        if(venue.getContact() != null
                && venue.getContact().getPhone() != null) {
            mVenueCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + mVenue.getContact().getPhone()));
                    startActivity(intent);
                }
            });
        } else {
            mVenueCall.setEnabled(false);
            mCallBtnIcon.setImageResource(R.drawable.ic_phone_disabled);
        }

        if(venue.getUrl() != null) {
            mVenueWebsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(mVenue.getUrl()));
                    startActivity(intent);
                }
            });
        } else {
            mVenueWebsite.setEnabled(false);
            mWebsiteBtnIcon.setImageResource(R.drawable.ic_globe_disabled);
        }

        if(venue.getCanonicalUrl() != null && venue.getCanonicalUrl().length() > 0) {
            mFoursquareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(mVenue.getCanonicalUrl()));
                    startActivity(intent);
                }
            });
        }

        if(venue.getDescription() != null) {
            mDescriptionContainer.setVisibility(View.VISIBLE);
            mVenueDescription.setText(venue.getDescription());
        }

        if(venue.getLocation() != null
                && venue.getLocation().getFullAddress().length() > 0) {
            mAddress.setText(venue.getLocation().getFullAddress());
        } else {
            mAddressContainer.setVisibility(View.GONE);
        }

        if(venue.getContact() != null
                && venue.getContact().getFormattedPhone() != null) {
            mPhoneNumber.setText(venue.getContact().getFormattedPhone());
        } else {
            mPhoneContainer.setVisibility(View.GONE);
        }

        if(venue.getUrl() != null && venue.getUrl().length() > 0) {
            mWebsite.setText(venue.getUrl());
        } else {
            mWebsiteContainer.setVisibility(View.GONE);
        }

        if(mDrawerListener != null) {
            double distance = mDrawerListener.getDistanceFromMe(new LatLng(venue.getLocation().getLatitude(), venue.getLocation().getLongitude()));
            mDistance.setText(String.format(getActivity().getString(R.string.distance_mi), distance));
        }

        if(venue.getPhotos() != null && venue.getPhotos().size() > 0) {
            List<Icon> iconList = venue.getPhotos();
            Picasso.with(getActivity()).load(iconList.get(0).getIconUrl(Constants.ORIGINAL)).into(mImageOne);
            mImageOne.setOnClickListener(mImageClickListener);
            if(venue.getPhotos().size() >= 2) {
                Picasso.with(getActivity()).load(iconList.get(1).getIconUrl(Constants.ORIGINAL)).into(mImageTwo);
                if(venue.getPhotos().size() > 2) {
                    mMoreImages.setText(String.format(getActivity().getString(R.string.gallery_more_images), (iconList.size() - 2)));
                    mImageTwo.setOnClickListener(mImageClickListener);
                }
            } else {
                mImageTwo.setVisibility(View.GONE);
                mMoreImages.setVisibility(View.GONE);
            }
        } else {
            mPhotosContainer.setVisibility(View.GONE);
        }

        setupHours();

        mRatingBar.setVisibility(View.VISIBLE);
        mRatingText.setVisibility(View.VISIBLE);
    }

    private void setupHours() {
        if(mVenue.getPopular() != null && mVenue.getPopular().getTimeframes().size() > 0) {
            mHoursContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHoursCollapsedContainer.setVisibility(
                            mHoursCollapsedContainer.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE
                    );
                    mHoursItemContainer.setVisibility(
                            mHoursItemContainer.getVisibility() == View.GONE ? View.VISIBLE : View.GONE
                    );
                }
            });
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            for(TimeFrame timeFrame : mVenue.getPopular().getTimeframes()) {
                View view = inflater.inflate(R.layout.hours_list_item, mHoursItemContainer, false);
                ((TextView)view.findViewById(R.id.day)).setText(timeFrame.getDays());
                ((TextView)view.findViewById(R.id.hours)).setText(TextUtils.join("\n", timeFrame.getOpen().toArray()));
                mHoursItemContainer.addView(view);

                if(TODAY.equals(timeFrame.getDays())) {
                    if(!mVenue.getPopular().isOpen()) {
                        mHoursClosed.setVisibility(View.VISIBLE);
                    } else {
                        mHoursToday.setVisibility(View.VISIBLE);
                        mHoursToday.setText(String.format(getActivity().getString(R.string.open_now), timeFrame.getOpen().get(0)));
                    }
                }
            }
        } else {
            mHoursContainer.setVisibility(View.GONE);
        }
    }
}
