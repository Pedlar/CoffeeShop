package org.notlocalhost.coffeeshop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.notlocalhost.coffeeshop.model.Icon;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class GalleryActivity extends FragmentActivity {
    private static final String ICON_LIST = "icon_list";

    @InjectView(R.id.view_pager)
    ViewPager mViewPager;

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    List<Icon> mIconList;

    public static void start(Context context, List<Icon> iconList) {
        Intent intent = new Intent(context, GalleryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putParcelableArrayListExtra(ICON_LIST, new ArrayList<>(iconList));
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_activity);
        ButterKnife.inject(this);

        mIconList = (ArrayList<Icon>)getIntent().getSerializableExtra(ICON_LIST);

        mViewPager.setAdapter(new PhotoViewPager());
        mViewPager.setCurrentItem(0);

        mToolbar.setTitle(String.format(getString(R.string.gallery_toolbar_text), 1, mIconList.size()));
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            @Override public void onPageScrollStateChanged(int state) { }
            @Override
            public void onPageSelected(int position) {
                mToolbar.setTitle(String.format(getString(R.string.gallery_toolbar_text), position + 1, mIconList.size()));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public class PhotoViewPager extends PagerAdapter {

        @Override
        public int getCount() {
            return mIconList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            ImageView view = new ImageView(GalleryActivity.this);
            view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            container.addView(view, layoutParams);

            Picasso.with(GalleryActivity.this).load(mIconList.get(position).getIconUrl(Constants.ORIGINAL)).into(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }
}
