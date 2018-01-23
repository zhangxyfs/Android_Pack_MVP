package com.z7dream.apm.base.utils.tools;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.z7dream.apm.base.Appli;
import com.z7dream.apm.base.R;
import com.z7dream.apm.base.utils.cache.CacheManager;
import com.z7dream.apm.base.utils.constant.Constant;
import com.z7dream.apm.base.utils.rx.RxBus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;
import static com.z7dream.apm.base.utils.constant.Constant.LAST_ACTIVITY_NAME;
import static com.z7dream.apm.base.utils.constant.RxConstant.IM_FINISH_NO_RESUME_OBSERVABLE;

/**
 * Created by user on 2016/11/4.
 */

public class Utils {

    public static int getCharacterWidth(String text, float size) {
        if (TextUtils.isEmpty(text)) return 0;
        Paint paint = new Paint();
        paint.setTextSize(size);
        return (int) paint.measureText(text);// 得到总体长度
    }

    /**
     * 转换dip为px
     *
     * @param context context
     * @param dip     dip
     * @return int
     */
    public static int convertDipOrPx(@NonNull Context context, float dip) {
        float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }

    public static int convertSp2Px(@NonNull Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale);
    }


    /**
     * 转换px为dip
     *
     * @param context context
     * @param px      px
     * @return float
     */
    public static float convertPxOrDip(@NonNull Context context, int px) {
        float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return px / scale + 0.5f * (px >= 0 ? 1 : -1);
    }

    /**
     * 获取屏幕的宽度
     *
     * @param context context
     * @return int
     */
    public static int getScreenWidth(@NonNull final Context context) {
        return context.getApplicationContext().getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param ctx context
     * @return int
     */
    public static int getScreenHeight(@NonNull Context ctx) {
        return ctx.getApplicationContext().getResources().getDisplayMetrics().heightPixels;
    }


    public static int getRealScreenWidth(Activity activity) {
        int widthPixels;
        WindowManager w = activity.getWindowManager();
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        widthPixels = metrics.widthPixels;
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
            try {
                widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
            } catch (Exception ignored) {
            }
        else if (Build.VERSION.SDK_INT >= 17)
            try {
                android.graphics.Point realSize = new android.graphics.Point();
                Display.class.getMethod("getRealSize", android.graphics.Point.class).invoke(d, realSize);
                widthPixels = realSize.x;
            } catch (Exception ignored) {
            }
        return widthPixels;
    }

    /**
     * 获取真正的屏幕高度
     *
     * @param activity
     * @return
     */
    public static int getRealScreenHeight(Activity activity) {
        int heightPixels;
        WindowManager w = activity.getWindowManager();
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        // since SDK_INT = 1;
        heightPixels = metrics.heightPixels;
        // includes window decorations (statusbar bar/navigation bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
            try {
                heightPixels = (Integer) Display.class
                        .getMethod("getRawHeight").invoke(d);
            } catch (Exception ignored) {
            }
            // includes window decorations (statusbar bar/navigation bar)
        else if (Build.VERSION.SDK_INT >= 17)
            try {
                android.graphics.Point realSize = new android.graphics.Point();
                Display.class.getMethod("getRealSize",
                        android.graphics.Point.class).invoke(d, realSize);
                heightPixels = realSize.y;
            } catch (Exception ignored) {
            }
        return heightPixels;
    }

    /**
     * 获取状态栏高度
     *
     * @param ctx activity
     * @return int
     */
    public static int getTop(Activity ctx) {
        Rect rect = new Rect();
        ctx.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        if (rect.top == 0) {
            try {
                Class c = Class.forName("com.android.internal.R$dimen");
                Object obj = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = Integer.parseInt(field.get(obj).toString());
                return ctx.getResources().getDimensionPixelSize(x);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return rect.top;
    }

    /**
     * 获取NavigationBar的高度
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        if (!checkDeviceHasNavigationBar(context.getApplicationContext())) {
            return 0;
        }
        Resources resources = context.getApplicationContext().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
     *
     * @param context
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        return !hasMenuKey && !hasBackKey;
    }

    /**
     * 检查是否连接网络
     *
     * @param context
     * @return
     */
    public static boolean checkNetWork(Context context) {
        ConnectivityManager cwjManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        cwjManager.getActiveNetworkInfo();
        NetworkInfo networkInfo = cwjManager.getActiveNetworkInfo();
        return networkInfo != null;
    }

    /**
     * @param context if null, use the default format
     *                (Mozilla/5.0 (Linux; U; Android %s) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 %sSafari/534.30).
     * @return
     */
    public static String getUserAgent(Context context) {
        String webUserAgent = null;
        if (context != null) {
            try {
                Class sysResCls = Class.forName("com.android.internal.R$string");
                Field webUserAgentField = sysResCls.getDeclaredField("web_user_agent");
                Integer resId = (Integer) webUserAgentField.get(null);
                webUserAgent = context.getString(resId);
            } catch (Throwable ignored) {
            }
        }
        if (TextUtils.isEmpty(webUserAgent)) {
            webUserAgent = "Mozilla/5.0 (Linux; U; Android %s) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 %sSafari/533.1";
        }

        Locale locale = Locale.getDefault();
        StringBuffer buffer = new StringBuffer();
        // Add version
        final String version = Build.VERSION.RELEASE;
        if (version.length() > 0) {
            buffer.append(version);
        } else {
            // default to "1.0"
            buffer.append("1.0");
        }
        buffer.append("; ");
        final String language = locale.getLanguage();
        if (language != null) {
            buffer.append(language.toLowerCase());
            final String country = locale.getCountry();
            if (country != null) {
                buffer.append("-");
                buffer.append(country.toLowerCase());
            }
        } else {
            // default to "en"
            buffer.append("en");
        }
        // add the model for the release build
        if ("REL".equals(Build.VERSION.CODENAME)) {
            final String model = Build.MODEL;
            if (model.length() > 0) {
                buffer.append("; ");
                buffer.append(model);
            }
        }
        final String id = Build.ID;
        if (id.length() > 0) {
            buffer.append(" build/");
            buffer.append(id);
        }
        return String.format(webUserAgent, buffer, "Mobile ");
    }

    /**
     * 需要權限 READ_PHONE_STATE READ_PHONE_STATE
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        @SuppressLint("HardwareIds") String imei = mTelephonyManager.getDeviceId();
        if (TextUtils.isEmpty(imei)) {
            imei = String.valueOf(System.currentTimeMillis());
        }
        return imei;
    }

    public static String URLDecoder(String converData) {
        String json = null;
        try {
            json = URLDecoder.decode(converData, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static String URLEncoder(String converData) {
        String json = null;
        try {
            json = URLEncoder.encode(converData, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersionName(Context context) {
        String versonName;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            versonName = info.metaData.getString("versionName");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versonName = "1";
        }
        return versonName;
    }


    /**
     * 获取app版本号
     *
     * @return 当前应用的版本号
     */
    public static int getVersionCode(Context context) {
        int versonName;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            versonName = info.metaData.getInt("versionCode");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versonName = 1;
        }
        return versonName;
    }

    /**
     * 获取数据库名字
     *
     * @param context
     * @return
     */
    public static String getDatabaseName(Context context) {
        String dbName;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            dbName = info.metaData.getString("dbName");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            dbName = "privateFundDB";
        }
        if (TextUtils.isEmpty(dbName)) {
            dbName = "privateFundDB";
        }

        return dbName;
    }

    /**
     * 数据库版本号
     *
     * @param context
     * @return
     */
    public static int getDBVersionCode(Context context) {
        int versonCode;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            versonCode = info.metaData.getInt("dbVersion");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versonCode = 1;
        }
        return versonCode;
    }

    /**
     * 是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public static boolean isDebug() {
//        if (Appli.Companion.getContext() != null) {
//            boolean isCouldOpen = SPreference.getOpenJsonLog();
//            if (isCouldOpen) {
//                return true;
//            } else if (NetConfig.INSTANCE.isLocal()) {
//                return true;
//            }
//        }
        return false;
    }

    public static void log(String tag, String msg) {
//        if (TextUtils.isEmpty(msg)) {
//            msg = "";
//        }
//        if (Appli.Companion.getContext() != null) {
//            boolean isCouldOpen = SPreference.getOpenJsonLog();
//            if (isCouldOpen) {
//                Log.e(tag, msg);
//            } else if (NetConfig.INSTANCE.isLocal()) {
//                Log.e(tag, msg);
//            }
//        }
    }

    public static void log(String tag, String msg, String which) {
//        if (TextUtils.isEmpty(msg)) {
//            msg = "";
//        }
//        if (Appli.Companion.getContext() != null) {
//            boolean isCouldOpen = SPreference.getOpenJsonLog();
//            if (isCouldOpen || NetConfig.INSTANCE.isLocal()) {
//                if (TextUtils.equals(which, "e")) {
//                    Log.e(tag, msg);
//                } else if (TextUtils.equals(which, "d")) {
//                    Log.d(tag, msg);
//                }
//            }
//        }
    }


    /**
     * 是否微信安装了
     *
     * @param context
     * @return
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isServiceRunning(Context context, String serviceName) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (serviceList == null || serviceList.size() == 0) return false;
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(serviceName)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * 是否为中文系统
     *
     * @param context
     * @return
     */
    public static boolean isSystemZh(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return TextUtils.equals(context.getResources().getConfiguration().getLocales().get(0).getLanguage(), "zh");
        } else
            return TextUtils.equals(context.getResources().getConfiguration().locale.getLanguage(), "zh");
    }

    /**
     * 当前应用是否在后台 5.0以上不好用
     *
     * @param context
     * @return
     */
    public static boolean isAppBackground(final Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                //前台程序
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                            break;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }


//    public static String getUserNickName(UserInfo userInfo) {
//        String userName = "";
//        if (userInfo != null) {
//            userName = userInfo.realName;
//            if (TextUtils.isEmpty(userName)) {
//                userName = userInfo.nickName;
//            }
//            if (TextUtils.isEmpty(userName)) {
//                userName = userInfo.mobile;
//            }
//        }
//
//        return userName;
//    }

    public static void toLogin() {
//        if (!BaseInfoUtils.findTopActivity().contains("LoginActivity")) {
//            Intent intent = new Intent();
//            intent.setAction(Constant.LOGIN_RECEIVER_ACTION);
//            Appli.Companion.getContext().sendBroadcast(intent);
//        }
    }

    public static void toChat(String sessionId, boolean isBack) {
        Intent intent = new Intent();
        intent.putExtra("sessionId", sessionId);
        intent.putExtra("isBack", isBack);
        intent.setAction(Constant.TO_CHAT_ROOM_RECEIVER_ACTION);
        Appli.Companion.getContext().sendBroadcast(intent);

        RxBus.Companion.get().post(IM_FINISH_NO_RESUME_OBSERVABLE, true);
    }

    /**
     * 生成 固定长度的字符串
     *
     * @param str
     * @param maxNum
     * @param split
     * @return
     */
    public static String createStr(String str, int maxNum, String split) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        String[] strs = str.split(split);
        String needStartStr, needEndStr, tmpStr;
        if (strs.length > 1) {
            needEndStr = "." + strs[strs.length - 1];
            tmpStr = str.substring(0, str.length() - needEndStr.length());
        } else {
            tmpStr = str;
            needEndStr = "";
        }

        int length = tmpStr.length();
        if (length >= maxNum) {
            int startEndNum = maxNum - maxNum / 2;
            int endStartNum = length - startEndNum;
            needStartStr = tmpStr.substring(0, startEndNum) + "…" + tmpStr.substring(endStartNum, length);
        } else {
            return str;
        }

        return needStartStr + needEndStr;
    }

//    public static boolean isMe(Long id) {
//        return id != null && SPreference.getUserId() != null && id.longValue() == SPreference.getUserId().longValue();
//    }
//
//    public static String getMinePhotoUrl(String defaultPhotoUrl) {
//        UserInfo userInfo = SPreference.getUserInfo();
//        if (userInfo != null) {
//            return userInfo.portrait;
//        }
//        return defaultPhotoUrl;
//    }

    public static void hideDatePickerHeader(Context context, DatePicker datePicker) {
        ViewGroup rootView = (ViewGroup) datePicker.getChildAt(0);
        if (rootView == null) {
            return;
        }
        View headerView = rootView.getChildAt(0);
        if (headerView == null) {
            return;
        }
        //5.0+
        int headerId = context.getResources().getIdentifier("day_picker_selector_layout", "id", "android");
        if (headerId == headerView.getId()) {
            headerView.setVisibility(View.GONE);

            ViewGroup.LayoutParams layoutParamsRoot = rootView.getLayoutParams();
            layoutParamsRoot.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            rootView.setLayoutParams(layoutParamsRoot);

            ViewGroup animator = (ViewGroup) rootView.getChildAt(1);
            ViewGroup.LayoutParams layoutParamsAnimator = animator.getLayoutParams();
            layoutParamsAnimator.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            animator.setLayoutParams(layoutParamsAnimator);

            View child = animator.getChildAt(0);
            ViewGroup.LayoutParams layoutParamsChild = child.getLayoutParams();
            layoutParamsChild.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            child.setLayoutParams(layoutParamsChild);
            return;
        }
        //6.0+
        headerId = context.getResources().getIdentifier("date_picker_header", "id", "android");
        if (headerId == headerView.getId()) {
            headerView.setVisibility(View.GONE);
        }
    }

    public static boolean isCameraCanUse() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            // TODO camera驱动挂掉,处理??
            mCamera = Camera.open();
        } catch (Exception e) {
            canUse = false;
        }
        if (canUse) {
            mCamera.release();
            mCamera = null;
        }
        return canUse;
    }

    /**
     * 当地时间 ---> UTC时间
     *
     * @return
     */
    public static String Local2UTC(String localTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("gmt"));
        String gmtTime = sdf.format(new Date(localTime));
        return gmtTime;
    }

    public static String Local2UTC(long localTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("gmt"));
        String gmtTime = sdf.format(new Date(localTime));
        return gmtTime;
    }


    /**
     * UTC时间 ---> 当地时间
     *
     * @param utcTime UTC时间
     * @return
     */
    public static String utc2Local(String utcTime) {
        SimpleDateFormat utcFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//UTC时间格式
        utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gpsUTCDate = null;
        try {
            gpsUTCDate = utcFormater.parse(utcTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat localFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//当地时间格式
        localFormater.setTimeZone(TimeZone.getDefault());
        String localTime = localFormater.format(gpsUTCDate.getTime());
        return localTime;
    }

    public static void killHightProcess() {
        ActivityManager appAM = (ActivityManager) Appli.Companion.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : appAM.getRunningAppProcesses()) {
            if (appProcess.processName.contains(":highLoad")) {
                android.os.Process.killProcess(appProcess.pid);
                break;
            }
        }
    }

    public static boolean hasHightProcess() {
        ActivityManager appAM = (ActivityManager) Appli.Companion.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : appAM.getRunningAppProcesses()) {
            if (appProcess.processName.contains(":highLoad")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 字符统计工具类<br></>
     * 中文为2，其它为1，如需要限制汉字数据未10个，那么在比较长度时小于或等于20<br></>
     *
     * @param text
     * @return
     */
    public static int charStats(String text) {
        int length = 0;
        if (!TextUtils.isEmpty(text)) {
            String Reg = "^[\u4e00-\u9fa5]{1}$";//正则
            int result = 0;
            for (int i = 0; i < text.length(); i++) {
                String b = Character.toString(text.charAt(i));
                if (b.matches(Reg)) result++;
            }
            length = text.length() + result;
        }
        return length;
    }


    //展开或缩回动画
    public static void expandOCollapseAnim(boolean isExpand, ImageView iv) {
        expandOCollapseAnim(isExpand, iv, 300);
    }

    public static void expandOCollapseAnim(boolean isExpand, ImageView iv, int duration) {
        float start, target;
        if (isExpand) {
            start = 0f;
            target = 90f;
        } else {
            start = 90f;
            target = 0f;
        }
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(iv, View.ROTATION, start, target);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }

    /**
     * 处理提示文本
     *
     * @param s
     * @return
     */
    public static String handlerPromptContent(String s) {
//        if (!TextUtils.isEmpty(s)) {
//            if (s.contains("failed to connect to") || s.contains("502") || s.contains("404") || s.contains("failed to connect to")
//                    || s.contains("No address associated with hostname"))
//                s = Appli.Companion.getContext().getString(R.string.timeout_str);
//        }
        return s;
    }

    /**
     * this screeshots form
     *
     * @param activity
     * @param isFullScreen
     */
    public static void screenshots(Activity activity, boolean isFullScreen, File mFileTemp) {
        if (mFileTemp.exists()) {
            mFileTemp.delete();
        }
        try {
            //View是你需要截图的View
            View decorView = activity.getWindow().getDecorView();
            decorView.setDrawingCacheEnabled(true);
            decorView.buildDrawingCache();
            Bitmap b1 = decorView.getDrawingCache();
            // 获取状态栏高度 /
            Rect frame = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;
            Utils.log(TAG, "statusBarHeight:" + statusBarHeight);
            // 获取屏幕长和高 Get screen width and height
            int width = activity.getWindowManager().getDefaultDisplay().getWidth();
            int height = activity.getWindowManager().getDefaultDisplay().getHeight();
            // 去掉标题栏 Remove the statusBar Height
            Bitmap bitmap;
            if (isFullScreen) {
                bitmap = Bitmap.createBitmap(b1, 0, 0, width, height);
            } else {
                bitmap = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
            }
            decorView.destroyDrawingCache();
            FileOutputStream out = new FileOutputStream(mFileTemp);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开侧滑返回页面
     *
     * @param activity
     * @param intent
     */
    public static void openSlidingBackActivity(Activity activity, Intent intent) {
        Utils.screenshots(activity, true, new File(CacheManager.getSystemPicCachePath(), activity.getClass().getName() + ".jpg"));
        intent.putExtra(LAST_ACTIVITY_NAME, activity.getClass().getName());
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    public static String getMacAddress() {
        String result = "";
        String Mac = "";
        result = callCmd("busybox ifconfig", "HWaddr");

        if (result == null) {
            return "";
        }
        if (result.length() > 0 && result.contains("HWaddr")) {
            Mac = result.substring(result.indexOf("HWaddr") + 6, result.length() - 1);
            if (Mac.length() > 1) {
                result = Mac.toLowerCase();
            }
        }
        return result.trim();
    }

    private static String callCmd(String cmd, String filter) {
        String result = "";
        String line = "";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);

            //执行命令cmd，只取结果中含有filter的这一行
            while ((line = br.readLine()) != null && line.contains(filter) == false) {
                //result += line;
                Log.i("test", "line: " + line);
            }

            result = line;
            Log.i("test", "result: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
