package app.ssl.libs.widgets.recyclerView

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View

/**
 * Created by long
 * on 2017/12/13.
 */
open class IViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private var mView: View = view;
    private var views = SparseArray<View>();

    fun getRootView(): View {
        return mView;
    }

    fun <T : View> getView(resId: Int): T {
        var tem = views.get(resId);
        if (tem == null) {
            tem = mView.findViewById(resId);
            views.put(resId, tem);
        }
        return tem as T;
    }

}