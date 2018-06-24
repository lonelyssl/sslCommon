package app.ssl.libs.widgets.recyclerView.base

import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import app.ssl.libs.widgets.recyclerView.IUnit
import app.ssl.libs.widgets.recyclerView.IViewHolder
import com.ssl.common.library.R
import com.ssl.common.library.widgets.recyclerView.base.IRefresh
import com.ssl.common.library.widgets.recyclerView.base.IRefresh.*

/**
 * Created by long
 * on 2017/12/13.
 */
class LoadingUnit : IUnit<String>("加载中..."), IRefresh {
    var mState: Int = STATE_NORMAL
        private set
    private var curView: View? = null

    //初始化高度
    var initHeight: Int = 0
        private set

    companion object {
        val UnitType = -100;
    }


    fun getViewHeight(): Int {
        return curView?.height ?: 0;
    }

    override fun setState(state: Int) {
        if (mState == state) {
            return;
        }
        when (state) {
            STATE_NORMAL -> {
                setViewHeight(0)
            }
            STATE_PULL_TO_REFRESH -> {

            }
            STATE_REFRESHING -> {
                if (initHeight > 0)
                    setViewHeight(initHeight)
            }
        }
        mState = state;
    }

    /**
     * 设置视图高度
     */
    fun setViewHeight(height: Int) {
        if (curView != null) {
            var params = curView!!.layoutParams;
            params.height = height;
            curView!!.layoutParams = params;
        }
    }

    override fun getType(): Int {
        return UnitType;
    }

    override fun getLayoutId(): Int {
        return R.layout.custom_recycler_header_loading_layout;
    }


    override fun onBindViewHolder(vh: IViewHolder?, position: Int) {
        curView = vh?.getRootView()
        if (curView != null) {
            //防止高度取的不对
            if (initHeight <= 0) {
                curView!!.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        curView!!.viewTreeObserver.removeOnGlobalLayoutListener(this);
                        initHeight = curView!!.height;
                        setViewHeight(0);
                    }
                })
            }

        }

        vh?.getView<TextView>(R.id.showText)?.setText(mData)
    }


}