package com.ssl.common.library.imageload;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import app.sslong.libs.imageLoad.IImageLoader;
import app.sslong.libs.imageLoad.ImageConfig;
import app.sslong.libs.imageLoad.ImageLoaderListener;

/***
 * 个人图片工具类
 * @author shishaolong
 */
public class ImageLoaderFactory {


    /**
     * 默认的Bitmap的宽\高
     */
    private static final int defBitmapW = 1000;
    private static final int defBitmapH = 1200;

    private static IImageLoader imageLoader; //图片加载核心类

    private ImageLoaderFactory() {
    }

    public static void initLoader(IImageLoader loader) {
        imageLoader = loader;
    }

    /**
     * 加载正常图片
     *
     * @param url
     * @param view
     */
    public static void loadImage(String url, ImageView view) {
        try {
            loadImage(url, view, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 加载正常图片
     *
     * @param url
     * @param view
     */
    public static void loadImage(String url, ImageView view, ImageConfig config) {
        try {
            loadImage(url, view, config, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 加载正常图片
     *
     * @param url
     * @param view
     */
    public static void loadImage(String url, ImageView view, ImageLoaderListener listener) {
        try {
            loadImage(url, view, null, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void loadImage(@NotNull String path, @NotNull ImageView view, ImageConfig config, ImageLoaderListener listener) {
        if (imageLoader != null)
            imageLoader.loadImage(path, view, listener, config);
    }


    /***
     * 将BitMap压缩到对应的最大字节
     *
     * @param byteLength 最大字节长度
     * @param bitmap
     * @return
     */
    public static Bitmap getMaxSizeBitmap(long byteLength, Bitmap bitmap) {

        int bitmapSize = bitmap.getRowBytes() * bitmap.getHeight();

        if (bitmap == null || byteLength == 0 || bitmapSize <= byteLength) {
            return bitmap;
        }

        if (bitmap != null) {

            while (bitmap.getRowBytes() * bitmap.getHeight() > byteLength) {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                bitmap = ThumbnailUtils.extractThumbnail(bitmap, width / 2, height / 2);
            }
        }
        return getMaxSizeBitmap(byteLength, bitmap);
    }

    /***
     * 根据图片大小进行压缩
     *
     * @param bitmap
     * @param newPath
     * @param isRecycle
     * @return
     */
    public static boolean createImage(Bitmap bitmap, String newPath, boolean isRecycle) {
        try {
            if (bitmap != null) {
                long size = bitmap.getWidth() * bitmap.getHeight();
                int percentage = 100;
                if (size > 2 * 1024 * 1024) {
                    percentage = 30;
                } else if (size > 1024 * 1024) {
                    percentage = 50;
                } else if (size > 400 * 1024) {
                    percentage = 60;
                } else if (size > 80 * 1024) {
                    percentage = 80;
                }
                return createImage(bitmap, newPath, isRecycle, percentage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * 不压缩图片进行保存
     *
     * @param bitmap
     * @param newPath
     * @return
     */
    public static boolean createImage(Bitmap bitmap, String newPath) {
        try {
            if (bitmap != null) {
                return createImage(bitmap, newPath, true, 100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError error) {

        }
        return false;

    }


    /***
     * 将一个Bitmap存储一个本地文件
     *
     * @param bitmap
     * @param newPath
     * @param isRecycle
     * @return
     */
    public static boolean createImage(Bitmap bitmap, String newPath, boolean isRecycle, int percentage) {
        boolean result = false;
        if (bitmap != null) {
            try {
                File newF = new File(newPath);
                FileOutputStream fos = new FileOutputStream(newF);
                result = bitmap.compress(CompressFormat.JPEG, percentage, fos);
                fos.close();
                if (isRecycle) {
                    if (bitmap != null) {
                        bitmap.recycle();
                        bitmap = null;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 压缩文件,默认压缩至800*800左右
     *
     * @return
     */
    public static boolean compressImageFile(String imagePath) {
        return compressImageFile(imagePath, defBitmapW, defBitmapH);

    }

    /**
     * @param imagePath
     * @param mdW
     * @param mdH
     * @return
     */
    public static boolean compressImageFile(String imagePath, int mdW, int mdH) {
        Bitmap bitmap;
        try {
            if (imagePath == null || "".equals(imagePath.trim())) {
                return false;
            }
            File file = new File(imagePath);
            if (!file.exists()) {
                return false;
            }
            bitmap = null;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imagePath, options);
            // 同时大于宽、高时，进行压缩
            if (options.outHeight > mdH && options.outWidth > mdW) {
                bitmap = getResizedBitmap(imagePath, mdW, mdH);
            } else {
                try {
                    bitmap = BitmapFactory.decodeFile(imagePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 对上面可能出现的异常进行补救
            if (bitmap == null) {
                bitmap = decodedBitmapFromPath(imagePath, mdW, mdH);
            }
            return createImage(bitmap, imagePath, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * @param path
     * @return
     */
    public static Bitmap decodedBitmapFromPath(String path) {
        return decodedBitmapFromPath(path, defBitmapW, defBitmapH);
    }

    /**
     * 从地址里面解析，根据期望的宽、高设置缩放比例。
     *
     * @param path 图片的路径
     * @return
     */
    public static Bitmap decodedBitmapFromPath(String path, int reqWidth, int reqHeight) {
        try {

            // 首先设置 inJustDecodeBounds=true 来获取图片尺寸
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);

            // 计算 inSampleSize 的值
            // 设置inJustDecodeBounds为true后，decodeFile并不分配空间，但可计算出原始图片的长度和宽度，即opts.width和opts.height。有了这两个参数，再通过一定的算法，即可得到一个恰当的inSampleSize。
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // 根据计算出的 inSampleSize 来解码图片生成Bitmap
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            return BitmapFactory.decodeFile(path, options);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap decodedBitmapFromResId(Resources res, int resID, int reqWidth, int reqHeight) {
        try {

            // 首先设置 inJustDecodeBounds=true 来获取图片尺寸
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, resID, options);

            // 计算 inSampleSize 的值
            // 设置inJustDecodeBounds为true后，decodeFile并不分配空间，但可计算出原始图片的长度和宽度，即opts.width和opts.height。有了这两个参数，再通过一定的算法，即可得到一个恰当的inSampleSize。
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // 根据计算出的 inSampleSize 来解码图片生成Bitmap
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            return BitmapFactory.decodeResource(res, resID, options);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 计算缩放系数
     *
     * @param options
     * @param reqWidth  目标宽度
     * @param reqHeight 目标高度
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 原始图片的宽高
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // 在保证解析出的bitmap宽高分别大于目标尺寸宽高的前提下，取可能的inSampleSize的最大值
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        if (inSampleSize > 1) {
            inSampleSize++;
        }
        return inSampleSize;
    }

    public static Bitmap getResizedBitmap(String path) {
        File file = new File(path);
        return getResizedBitmap(null, Uri.fromFile(file));
    }

    /***
     * 更具Uri对图片进行放缩，放缩接近至目标的宽、高。最终的大小基本相等
     *
     * @param path
     * @return
     */
    public static Bitmap getResizedBitmap(String path, int targetWidth, int targetHeight) {
        File file = new File(path);
        return getResizedBitmap(null, Uri.fromFile(file), targetWidth, targetHeight);
    }

    public static Bitmap getResizedBitmap(Context context, Uri uri) {
        return getResizedBitmap(context, uri, defBitmapW, defBitmapH);
    }

    /***
     * 更具Uri对图片进行放缩，放缩至目标的宽、高
     *
     * @param context
     * @param uri
     * @param widthLimit  目标宽度
     * @param heightLimit 目标高度
     * @return
     * @throws IOException
     */
    public static Bitmap getResizedBitmap(Context context, Uri uri, int widthLimit, int heightLimit) {

        Bitmap result = null;
        try {
            String path = null;
            result = null;

            if (uri.getScheme().equals("file")) {
                path = uri.getPath();
            } else if (uri.getScheme().equals("content")) {
                Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                cursor.moveToFirst();
                path = cursor.getString(0);
                cursor.close();
            } else {
                return null;
            }

            ExifInterface exifInterface = new ExifInterface(path);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            BitmapFactory.decodeFile(path, options);

            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90 || orientation == ExifInterface.ORIENTATION_ROTATE_270 || orientation == ExifInterface.ORIENTATION_TRANSPOSE || orientation == ExifInterface.ORIENTATION_TRANSVERSE) {
                int tmp = widthLimit;
                widthLimit = heightLimit;
                heightLimit = tmp;
            }

            int width = options.outWidth;
            int height = options.outHeight;
            int sampleW = 1, sampleH = 1;
            while (width / 2 > widthLimit) {
                width /= 2;
                sampleW <<= 1;

            }

            while (height / 2 > heightLimit) {
                height /= 2;
                sampleH <<= 1;
            }
            int sampleSize = 1;

            options = new BitmapFactory.Options();
            if (widthLimit == Integer.MAX_VALUE || heightLimit == Integer.MAX_VALUE) {
                sampleSize = Math.max(sampleW, sampleH);
            } else {
                sampleSize = Math.max(sampleW, sampleH);
            }
            options.inSampleSize = sampleSize;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeFile(path, options);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                options.inSampleSize = options.inSampleSize << 1;
                bitmap = BitmapFactory.decodeFile(path, options);
            }

            Matrix matrix = new Matrix();
            if (bitmap == null) {
                return result;
            }
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90 || orientation == ExifInterface.ORIENTATION_ROTATE_270 || orientation == ExifInterface.ORIENTATION_TRANSPOSE || orientation == ExifInterface.ORIENTATION_TRANSVERSE) {
                int tmp = w;
                w = h;
                h = tmp;
            }
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.setRotate(90, w / 2f, h / 2f);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.setRotate(180, w / 2f, h / 2f);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.setRotate(270, w / 2f, h / 2f);
                    break;
                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    matrix.preScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    matrix.preScale(1, -1);
                    break;
                case ExifInterface.ORIENTATION_TRANSPOSE:
                    matrix.setRotate(90, w / 2f, h / 2f);
                    matrix.preScale(1, -1);
                    break;
                case ExifInterface.ORIENTATION_TRANSVERSE:
                    matrix.setRotate(270, w / 2f, h / 2f);
                    matrix.preScale(1, -1);
                    break;
            }
            float xS = (float) widthLimit / bitmap.getWidth();
            float yS = (float) heightLimit / bitmap.getHeight();

            matrix.postScale(Math.min(xS, yS), Math.min(xS, yS));
            try {
                result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                Log.d("ResourceCompressHandler", "OOM" + "Height:" + bitmap.getHeight() + "Width:" + bitmap.getHeight() + "matrix:" + xS + " " + yS);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将一个Bitmap放缩至指定的宽、高
     *
     * @param bitmap
     * @param dmW
     * @param dmH
     * @return
     */
    public static Bitmap changeBitmapToMdBitmap(Bitmap bitmap, int dmW, int dmH) {
        Bitmap result = null;
        Matrix matrix = new Matrix();
        if (bitmap == null) {
            return result;
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        float xS = (float) dmW / bitmap.getWidth();
        float yS = (float) dmH / bitmap.getHeight();
        matrix.postScale(Math.min(xS, yS), Math.min(xS, yS));
        try {
            result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            Log.d("ResourceCompressHandler", "OOM" + "Height:" + bitmap.getHeight() + "Width:" + bitmap.getHeight() + "matrix:" + xS + " " + yS);
            return result;
        }

        return result;

    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /***
     * 生成缩略图并保存到一个文件中
     *
     * @param bitmap
     * @param newImage
     * @param w
     * @param h
     * @return
     */
    public static boolean thumbImageFile(Bitmap bitmap, String newImage, int w, int h) {
        try {
            if (TextUtils.isEmpty(newImage)) {
                return false;
            }
            Bitmap newBitmap = ThumbnailUtils.extractThumbnail(bitmap, w, h);
            return createImage(newBitmap, newImage, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    /***
     * 图片回收方法
     *
     * @param view
     * @return
     */
    public static boolean recycledBitmap(ImageView view) {
        try {
            if (view != null) {
                Drawable drawable = view.getDrawable();
                if (drawable instanceof BitmapDrawable) {
                    BitmapDrawable bitmapD = (BitmapDrawable) drawable;
                    if (bitmapD != null) {
                        Bitmap bitmap = bitmapD.getBitmap();

                        if (bitmap != null) {
                            if (bitmap != null && !bitmap.isRecycled()) {
                                bitmap.recycle();
                            }
                        }
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * @param bitmap     原图
     * @param edgeLength 希望得到的正方形部分的边长
     * @return 缩放截取正中部分后的位图。
     */
    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
        if (null == bitmap || edgeLength <= 0) {
            return null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if (widthOrg >= edgeLength && heightOrg >= edgeLength) {
            // 压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;

            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e) {
                return null;
            }

            // 从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;

            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        }

        return result;
    }

    public static Uri getResDrawUri(Resources r, int resId) {
        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + r.getResourcePackageName(resId) + "/" + r.getResourceTypeName(resId) + "/" + r.getResourceEntryName(resId));
        return uri;
    }

    /************************************************************************************************************/

    /**
     * 根据url获取图片
     *
     * @param url
     * @return
     */
    public static void getBitMapByUrl(String url) {

        //return ImageLoader.getInstance().loadImage((url), original_options);
    }

}
