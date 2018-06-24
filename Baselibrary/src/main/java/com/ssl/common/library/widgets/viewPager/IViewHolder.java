package com.ssl.common.library.widgets.viewPager;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by long
 * on 2017/12/9.
 */

public interface IViewHolder<T> {

    View createView(Context context);

    void onBindView(ViewGroup parent, int position, T data);


}
