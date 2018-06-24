package app.ssl.libs.widgets.recyclerView.base

import android.view.View
import android.widget.TextView
import app.ssl.libs.widgets.recyclerView.IUnit
import app.ssl.libs.widgets.recyclerView.IViewHolder
import com.ssl.common.library.R
import com.ssl.common.library.widgets.recyclerView.base.IRefresh

/**
 * Created by long
 * on 2017/12/13.
 */
class LoadMoreUnit() : IUnit<String>("加载更多"), IRefresh {
    private var curView: View? = null;
    var mState = IRefresh.STATE_NORMAL
        private set


    //初始化高度
    private var initHeight: Int = 0
        private set


    companion object {
        val UnitType = -101;
    }

    override fun getLayoutId(): Int {
        return R.layout.custom_recycler_footer_loading_layout
    }


    override fun getType(): Int {
        return UnitType;
    }

    /**
     * 设置视图高度
     */
    private fun setViewHeight(height: Int) {
        if (false && curView != null) {
            var params = curView!!.layoutParams;
            params.height = height;
            curView!!.layoutParams = params;
        }
    }

    override fun onBindViewHolder(vh: IViewHolder?, position: Int) {
        curView = vh?.getRootView();
//        if (curView != null) {
//            //防止高度取的不对
//            if (initHeight <= 0) {
//                curView!!.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
//                    override fun onGlobalLayout() {
//                        curView!!.viewTreeObserver.removeOnGlobalLayoutListener(this);
//                        var height = curView!!.measuredHeight;
//                        if (height > 0) {
//                            initHeight = height;
//                        }
//
//                    }
//                })
//            }
//
//        }
        vh?.getView<TextView>(R.id.showText)?.setText(mData)
    }

    override fun setState(state: Int) {
        if (state == mState)
            return;
        when (state) {
            IRefresh.STATE_NORMAL -> {
//                setViewHeight(0);
            }
            IRefresh.STATE_PULL_TO_REFRESH -> {
//                setViewHeight(initHeight);
            }
            IRefresh.STATE_REFRESHING -> {
//                setViewHeight(initHeight);
            }
        }
        mState = state;
    }


}