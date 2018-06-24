package app.ssl.libs.imageLoad

/**
 * Created by long
 * on 2017/12/9.
 *
 * 图片加载参数；自定统一的接口参数，可以底层图片加载图的简单替换
 */
class ImageConfig {

    var emptyResId: Int = -1;//错误或空的时候显示
    var loadingResId: Int = -1;//加载过程的时候显示

    var width: Int = -1;  //宽度，像素
    var height: Int = -1; //高度，像素

    var isCricle = false;//是否加载圆形
}