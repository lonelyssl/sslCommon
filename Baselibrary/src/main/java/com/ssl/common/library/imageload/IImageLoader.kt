package app.sslong.libs.imageLoad

import android.widget.ImageView


/**
 * Created by long
 * on 2017/12/9.
 * 图片加载接口；自定统一的接口参数，可以底层图片加载图的简单替换
 */
public interface IImageLoader {
    fun loadImage(url: String, imageView: ImageView, listener: ImageLoaderListener?, config: ImageConfig?): Any?
}