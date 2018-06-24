package com.ssl.common.library.widgets.recyclerView

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.ssl.common.library.widgets.recyclerView.base.ILoadListener
import com.ssl.common.library.widgets.recyclerView.base.LoadMoreUnit
import com.ssl.common.library.widgets.recyclerView.base.LoadingUnit
import com.ssl.common.library.widgets.recyclerView.base.IRefresh.*

/**
 * Created by long
 * on 2017/12/13.
 */
class SSLRecyclerView : RecyclerView {
    val Tag = "SSLRecyclerView";


    private var isOpenRefresh = true
    private var isOpenLoadMore = true

    private var loadingUnit: LoadingUnit? = null;
    private var loadMoreUnit: LoadMoreUnit? = null;

    private var loadListener: ILoadListener? = null;

    constructor(context: Context) : super(context) {
        initView(context);
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context);
    }


    private fun initView(context: Context) {
        var manger = LinearLayoutManager(context);
        manger.orientation = LinearLayoutManager.VERTICAL
    }

    fun setData(data: ArrayList<IUnit<out Any>>) {
        setData(data, true, false, null)
    }

    fun setData(data: ArrayList<IUnit<out Any>>, isRefresh: Boolean, isLoadMore: Boolean, loadListener: ILoadListener?) {
        isOpenRefresh = isRefresh;
        isOpenLoadMore = isLoadMore;
        this.loadListener = loadListener;
        if (data == null || data.isEmpty())
            return;
        if (isOpenRefresh) {
            loadingUnit = LoadingUnit();
            data.add(0, loadingUnit as IUnit<Any>);
        }
        if (isOpenLoadMore) {
            loadMoreUnit = LoadMoreUnit();
            data.add(loadMoreUnit as IUnit<Any>);
        }
        adapter = IBaseAdapter(data);

        if (isOpenLoadMore || isOpenRefresh) {
            initOnTouchListener();
        }
    }


    private fun initOnTouchListener() {
        setOnTouchListener(object : OnTouchListener {
            var downRawY = 0.0f;
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event == null) {
                    return false
                }
                Log.e(Tag, "onTouch:${event!!.action} y=${event!!.rawY}")
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        downRawY = event.rawY;
                    }
                    MotionEvent.ACTION_MOVE -> {
                        if (downRawY == 0.0f) {
                            downRawY = event.rawY
                        }
                        var distance = event.rawY - downRawY;
                        if (distance > 0 && loadingUnit != null && loadingUnit!!.mState != STATE_REFRESHING) {
                            loadingUnit!!.setState(STATE_PULL_TO_REFRESH)
                            loadingUnit?.setViewHeight(distance.toInt())
                        }
                        if (distance < 0) {//上滑
                            if (!canScrollVertically(1) && loadMoreUnit != null) {
                                if (loadMoreUnit!!.mState == STATE_NORMAL) {
                                    loadMoreUnit!!.setState(STATE_REFRESHING);
                                    loadListener?.onLoadMored();
                                }

                            }
                        }
                    }
                    else -> {
                        downRawY = 0.0f
                        if (loadingUnit != null) {
                            if (loadingUnit!!.getViewHeight() >= loadingUnit!!.initHeight) {
                                loadingUnit!!.setState(STATE_REFRESHING)
                                loadListener?.onRefreshed();
                            } else {
                                loadingUnit!!.setState(STATE_NORMAL)
                            }
                        }

                    }
                }
                return false;
            }

        })
    }

    fun loadCompleted() {
        if (loadingUnit != null) {
            loadingUnit!!.setState(STATE_NORMAL)
        }
        if (loadMoreUnit != null) {
            loadMoreUnit!!.setState(STATE_NORMAL)
        }
    }

}