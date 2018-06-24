package com.ssl.common.library.widgets.viewPager;

import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


/**
 * Created by long
 * on 2017/12/9.
 */
public class ViewPagerAdapter<T, H extends IViewHolder> extends PagerAdapter {
    private List<T> mData;
    private ViewAdapterProxy<H> proxy;

    /*View缓存*/
    private SparseArray<H> holders = new SparseArray<>();
    private SparseArray<View> views = new SparseArray<>();


    public ViewPagerAdapter(List<T> data, ViewAdapterProxy<H> proxy) {
        this.mData = data;
        this.proxy = proxy;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = views.get(position);
        H holder;
        if (view == null) {
            holder = holders.get(position);
            if (holder == null) {
                holder = proxy.createViewHolder();
                holders.put(position, holder);
            }
            view = holder.createView(container.getContext());
            view.setTag(holder);
            views.put(position, view);
        } else {
            holder = (H) view.getTag();
        }
        container.addView(view);
        holder.onBindView(container, position, mData.get(position));
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}