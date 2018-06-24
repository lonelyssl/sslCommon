package com.ssl.common.library.widgets.viewPager;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.ssl.common.library.R;

import java.util.List;


/**
 * Created by long
 * on 2017/12/9.
 */

public class SSLViewPager extends LinearLayout {

    public static final String tag = "SSLViewPager";
    private long IntervalTime = 3 * 1000;//轮播时间 3秒

    private int sResId = R.mipmap.ic_launcher;
    private int noResId = R.mipmap.ic_launcher_round;
    private int indicatorWH = 100;//px
    private int indicatorLR = 10;//px
    private boolean isTiming = true;//是否开启定时

    private ViewPager viewPager;
    private LinearLayout indicators;
    private List<?> mData;
    private Handler mHandler = null;

    public SSLViewPager(Context context) {
        super(context);
        initView(context);
    }

    public SSLViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.custom_viewpager_layout, null);
        viewPager = view.findViewById(R.id.sslViewPager);
        indicators = view.findViewById(R.id.sslIndicator);
        addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        initViewPager();
    }

    public <T, H extends IViewHolder> void setData(List<T> data, ViewAdapterProxy<H> proxy) {
        setData(data, proxy, -1, -1);
    }

    public <T, H extends IViewHolder> void setData(List<T> data, ViewAdapterProxy<H> proxy, int selectedResId, int noSelectedResId) {
        this.mData = data;
        mHandler = new Handler();
        if (selectedResId > 0) {
            this.sResId = selectedResId;
        }
        if (noSelectedResId > 0) {
            this.noResId = noSelectedResId;
        }
        indicatorWH = getResources().getDimensionPixelSize(R.dimen.ssl_viewpager_indicator_wh);
        indicatorLR = getResources().getDimensionPixelSize(R.dimen.ssl_viewpager_indicator_margin_rl);
        viewPager.setAdapter(new ViewPagerAdapter(data, proxy));
        indicators.removeAllViews();
        if (mData != null && mData.size() > 1) {
            int len = mData.size();
            for (int i = 0; i < len; i++) {
                View tem = new View(getContext());
                tem.setBackgroundResource(noResId);
                LayoutParams params = new LayoutParams(indicatorWH, indicatorWH);
                params.leftMargin = indicatorLR;
                params.rightMargin = indicatorLR;
                indicators.addView(tem, params);
            }
            startTiming();
        }

    }

    /**
     * 开始定时
     */
    private void startTiming() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isTiming)
                    return;
                int item = viewPager.getCurrentItem();
                item = (item + 1) % viewPager.getAdapter().getCount();
                viewPager.setCurrentItem(item, true);
                mHandler.postDelayed(this, IntervalTime);
            }
        }, IntervalTime);


    }

    private void refreshIndicateView() {
        if (mData != null && mData.size() > 1) {
            int select = viewPager.getCurrentItem();
            for (int i = 0; i < mData.size(); i++) {
                if (i == select) {
                    indicators.getChildAt(i).setBackgroundResource(sResId);
                } else {
                    indicators.getChildAt(i).setBackgroundResource(noResId);
                }
            }

        }
    }


    private void initViewPager() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                refreshIndicateView();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void destroy() {
        isTiming = false;
        viewPager = null;
        indicators = null;
    }

    void log(String msg) {
        Log.e(tag, msg);
    }

}
