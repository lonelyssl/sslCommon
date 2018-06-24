package com.ssl.common.library.widgets.viewPager;


/**
 * Created by long
 * on 2017/12/9.
 */

public interface ViewAdapterProxy<H extends IViewHolder> {
    H createViewHolder();
}
