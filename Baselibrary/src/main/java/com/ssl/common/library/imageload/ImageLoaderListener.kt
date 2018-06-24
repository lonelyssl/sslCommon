package app.ssl.libs.imageLoad

import android.graphics.Bitmap
import android.widget.ImageView

/**
 * 监听
 * Created by long
 * on 2017/12/9.
 */
abstract class ImageLoaderListener {
    abstract fun successLoaded(view: ImageView, draw: Bitmap?);
    fun errorLoaded(view: ImageView) {

    };
}