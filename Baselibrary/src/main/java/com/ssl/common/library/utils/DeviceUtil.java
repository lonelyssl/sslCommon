package com.ssl.common.library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

/***
 * 与设备相关的工具类
 *
 * @author shishaolong
 *         <p>
 *         主要功能:
 *         </p>
 *         <p>
 *         创建日期:2015-2-6
 *         </p>
 *         <p>
 *         修改人,修改日期,修改内容
 *         </p>
 * @com.utils.DensityUtil
 */
public final class DeviceUtil {


    /**
     * 默认的设备Id
     */
    public static final String DEF_DEVICEID = "1234567890abcdef";

    /**
     * dp单位转化为px单位
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px单位转化为dp单位
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    private static int dWidth = -1;
    private static int dheight = -1;

    /**
     * 获取手机宽度;
     *
     * @param context
     * @return
     */
    public static int getWindowWidth(Context context) {
        if (dWidth > 0) {
            return dWidth;
        }
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;

    }

    /**
     * 获取手机高度;包括任务栏，但是不包括虚拟按钮栏的高度
     *
     * @param context
     * @return
     */
    public static int getWindowHeight(Context context) {
        if (dheight > 0) {
            return dheight;
        }
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }


    /**
     * 虚拟按钮栏的高度
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        int navigationBarHeight = -1;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            navigationBarHeight = resources.getDimensionPixelSize(resourceId);
        }
        return navigationBarHeight;
    }


    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = 0;
        try {
            Resources res = context.getResources();
            int rId = res.getIdentifier("status_bar_height", "dimen", "android");
            if (rId > 0) {
                statusHeight = res.getDimensionPixelSize(rId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }


    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getWindowWidth(activity);
        int height = getWindowHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getWindowWidth(activity);
        int height = getWindowHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * 获得设备的唯一标示
     * <p/>
     * model , brand , name , deviceID , androidID , serial
     *
     * @param context
     * @return MD5加密后的唯一标示字符串
     */
    public static String getDeviceId(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String deviceID = "";
            String serial = "";
            String simSN = "";
            try {
                PackageManager pm = context.getPackageManager();
                if (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.READ_PHONE_STATE", context.getPackageName())) {//权限检查

            /*    Android系统为开发者提供的用于标识手机设备的串号;非手机设备：
                 但是如果只带有Wifi的设备或者音乐播放器没有通话的硬件功能的话就没有这个DEVICE_ID
                 权限： 获取DEVICE_ID需要READ_PHONE_STATE权限，但如果我们只为了获取它，没有用到其他的通话功能，那这个权限有点大才小用
                 bug：在少数的一些手机设备上，该实现有漏洞，会返回垃圾，如:zeros或者asterisks的产品*/
                    deviceID = tm.getDeviceId();



                /*
                * 装有SIM卡的Android 2.3设备，可以通过下面的方法获取到Sim Serial Number：
                * */
                }
                simSN = tm.getSimSerialNumber();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Build.SERIAL 在api level 9 加入, 9一下 无法使用.
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
                 /*
                * Android系统2.3版本以上可以通过下面的方法得到Serial Number，且非手机设备也可以通过该接口获取。
                * */
                serial = Build.SERIAL;

            }

            String model = Build.MODEL;//机型
            String brand = Build.BRAND; //系统定制商
            String name = Build.PRODUCT;//手机制造商
            String androidId = "";
            try {
                /*
                * 在设备首次启动时，系统会随机生成一个64位的数字，并把这个数字以16进制字符串的形式保存下来，
                * 这个16进制的字符串就是ANDROID_ID，但是如果返厂的手机，或者被root的手机，可能会变
                * */
                androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String temp = new String(deviceID + androidId + serial + model + brand + name + simSN);
            return MD5.getMD5Str(temp.toLowerCase());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return MD5.getMD5Str(DEF_DEVICEID);
    }


    /****************************** 版本信息 *******************************/
    /***
     * 获取版本信息，VersionName
     *
     * @param context
     * @return
     */
    public static String getVersion(Context context) {
        String result = "";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            result = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /***
     * 获取版本信息code
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int result = 0;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            result = info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /***
     * 获取所有的应用信息
     */

    /***
     * 检查是否安装对应的应用
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAvilible(Context context, String packageName, String appName) {
        // 获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        // 用于存储所有已安装程序的包名
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo info = packageInfos.get(i);
                String packName = info.packageName;
                String appN = "" + info.applicationInfo.loadLabel(packageManager);
                if (TextUtils.isEmpty(appName)) {
                    if (packName.equals(packageName)) {
                        return true;
                    }
                } else {
                    if (packName.equals(packageName) || appN.equals(appName)) {
                        return true;
                    }
                }

            }
        }
        // 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return false;
    }

    /***
     * 计算并设置ListView的高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView, int pancValue) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + listView.getPaddingBottom() + listView.getPaddingTop() + DeviceUtil.dip2px(listView.getContext(), pancValue);
        listView.setLayoutParams(params);
    }

    /**
     * 计算设置视图的高度(宽度已知)
     *
     * @param view
     * @param w
     * @param w_h    宽高比
     * @param params
     */
    public static void setViewParams(View view, int w, double w_h, LayoutParams params) {
        try {
            // 计算高度
            int h = (int) (w * (1 / w_h));
            params.height = h;
            params.width = w;
            view.setLayoutParams(params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 设置view的margin值(像素)
     *
     * @param v
     * @param l
     * @param t
     * @param r
     * @param b
     */
    public static void setMargins(View v, int l, int t, int r, int b) {
        ViewGroup.MarginLayoutParams p = null;
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        } else {
            p = new ViewGroup.MarginLayoutParams(v.getLayoutParams());
        }
        p.setMargins(l, t, r, b);
        v.requestLayout();
    }

    /**
     * 计算设置视图的宽度(高度度已知)
     *
     * @param view
     * @param h
     * @param h_w    宽高比
     * @param params
     */
    public static void setViewParamsByHeight(View view, int h, double h_w, LayoutParams params) {
        try {
            // 计算宽度
            int w = (int) (h * (1 / h_w));
            params.height = h;
            params.width = w;
            view.setLayoutParams(params);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null)
            return false;
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {

        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            if (null != connectivity) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (null != info && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
