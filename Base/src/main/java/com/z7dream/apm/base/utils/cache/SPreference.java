package com.z7dream.apm.base.utils.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.z7dream.apm.base.Appli;
import com.z7dream.apm.base.api.entity.LoginUserInfoEntity;
import com.z7dream.apm.base.api.entity.UserInfo;
import com.z7dream.apm.base.api.entity.UserToken;
import com.z7dream.apm.base.api.entity.b_enum.AddFriendsSettingEnum;
import com.z7dream.apm.base.mvp.model.BaseEntity;
import com.z7dream.apm.base.utils.constant.Constant;
import com.z7dream.apm.base.utils.dao.BaseInfoUtils;
import com.z7dream.apm.base.utils.net.NetConfig;
import com.z7dream.apm.base.utils.tools.Base64;
import com.z7dream.apm.base.utils.tools.Utils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import static com.z7dream.apm.base.utils.cache.CPConstant.CHAT_LOGIN_NAME;
import static com.z7dream.apm.base.utils.cache.CPConstant.CHAT_LOGIN_TOKEN;
import static com.z7dream.apm.base.utils.cache.CPConstant.COMPANY_ID_KEY;
import static com.z7dream.apm.base.utils.cache.CPConstant.COMPANY_NAME_KEY;
import static com.z7dream.apm.base.utils.cache.CPConstant.IS_FIRST_CHECK_SESSIONLIST;
import static com.z7dream.apm.base.utils.cache.CPConstant.PRIVACY_LOCATION_KEY;
import static com.z7dream.apm.base.utils.cache.CPConstant.TO_CLEAR_14_ONCE;
import static com.z7dream.apm.base.utils.cache.CPConstant.USER_ID_KEY;
import static com.z7dream.apm.base.utils.cache.CPConstant.USER_INFO_KEY;
import static com.z7dream.apm.base.utils.cache.CPConstant.USER_INFO_SMTC_KEY;
import static com.z7dream.apm.base.utils.cache.CPConstant.USER_LOGINFLAG_KEY;
import static com.z7dream.apm.base.utils.cache.CPConstant.USER_LOGIN_NAME;

/**
 * Created by xiaoyu.zhang on 2016/11/11 09:05
 * 不要用于跨进程使用，虽然也可以用，如果需要数据跨进程请使用OtherDataProvider
 */
public class SPreference implements Constant {
    private static SharedPreferences getBase() {
        return Appli.Companion.getContext().getApplicationContext().getSharedPreferences(USER_SETTING, Context.MODE_PRIVATE);
    }

    private static SharedPreferences getBase(@NonNull String key) {
        return Appli.Companion.getContext().getSharedPreferences(key, Context.MODE_PRIVATE);
    }

    private static void putString(@NonNull String key, @NonNull String value) {
        SharedPreferences.Editor edit = getBase().edit();
        edit.putString(key, value);
        edit.apply();
    }

    private static String getString(@NonNull String key) {
        return getBase().getString(key, null);
    }

    private static void putInt(@NonNull String key, @NonNull int value) {
        SharedPreferences.Editor edit = getBase().edit();
        edit.putInt(key, value);
        edit.apply();
    }

    /**
     * 默认值为-1
     *
     * @param key
     * @return
     */
    private static int getInt(@NonNull String key) {
        return getBase().getInt(key, -1);
    }

    private static void putBoolean(@NonNull String key, @NonNull boolean value) {
        SharedPreferences.Editor edit = getBase().edit();
        edit.putBoolean(key, value);
        edit.apply();
    }

    private static boolean getBoolean(@NonNull String key) {
        return getBase().getBoolean(key, false);
    }

    private static void putLong(@NonNull String key, @NonNull long value) {
        SharedPreferences.Editor edit = getBase().edit();
        edit.putLong(key, value);
        edit.apply();
    }

    private static long getLong(@NonNull String key) {
        return getBase().getLong(key, 0L);
    }

    private static <T> void put(@NonNull String key, @NonNull T value) {
        if (value instanceof String) {
            putString(key, (String) value);
        } else if (value instanceof Integer) {
            putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            putBoolean(key, (Boolean) value);
        } else if (value instanceof BaseEntity) {
            try {
                putString(key, new Gson().toJson(((BaseEntity) value).getData()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 用于判断应用后台到前台
     *
     * @return boolean
     */
    public static boolean isCurrentRunningForeground() {
        return BaseInfoUtils.isJoinForground();
//        return OtherDataExtra.isJoinForground(Appli.Companion.getContext());
    }

    /**
     * 是否在前台
     *
     * @param isForeground
     */
    public static void saveCurrentRunningForeground(boolean isForeground) {
        BaseInfoUtils.saveJoinForground(isForeground);
//        OtherDataExtra.saveJoinForground(Appli.Companion.getContext(), isForeground);
    }


    /**
     * app是否升级了，通过appVersion判断
     *
     * @param isSave 是否自动保存当前版本号
     * @return boolean
     */
    public static boolean isAppUpdate(boolean isSave) {
        boolean b;
        int oldVersion = getInt(LAST_APP_VERSION);
        int nowVersion = Utils.getVersionCode(Appli.Companion.getContext());
        if (oldVersion < 0) {
            b = true;
        } else {
            b = oldVersion < nowVersion;
        }
        if (isSave)
            putInt(LAST_APP_VERSION, nowVersion);
        return b;
    }

    /**
     * 保存app是否升级信息，可以用于显示开机大图完成后
     */
    public static void saveIsAppUpdate() {
        putInt(LAST_APP_VERSION, Utils.getVersionCode(Appli.Companion.getContext()));
    }

    public static void saveCameraFlash(int which) {
        putInt(CAMERA_FLASH, which);
    }

    public static int getCameraFlash() {
        int which = getInt(CAMERA_FLASH);
        return which < 0 ? 1 : which;
    }

    /**
     * 是否第一次验证会话列表(只能为true 一次)
     *
     * @return
     */
    public static boolean isFirstCheckSessionList() {
        String key = IS_FIRST_CHECK_SESSIONLIST + SPreference.getUserId();
        String txt = getString(key);
        boolean b = TextUtils.isEmpty(txt);
        if (b) {
            putString(key, "true");
        }
        return b;
    }

    public static boolean isClear14Once() {
        String isLocal = NetConfig.INSTANCE.isLocal() ? "_local" : "_net";
        String txt = getString(TO_CLEAR_14_ONCE + isLocal);
        if (TextUtils.isEmpty(txt)) {
            putString(TO_CLEAR_14_ONCE + isLocal, "true");
            return false;
        }
        return true;
    }

    /**
     * 获取登陆用户信息。
     *
     * @return 用户信息类
     */
    public static LoginUserInfoEntity.ResultBean getLoginUserInfoData() {
        String userInfoDataJson = BaseInfoUtils.queryUserInfoData();
        if (TextUtils.isEmpty(userInfoDataJson)) {
            return null;
        }
        return new Gson().fromJson(userInfoDataJson, LoginUserInfoEntity.ResultBean.class);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public static UserInfo getUserInfo() {
        LoginUserInfoEntity.ResultBean bean = getLoginUserInfoData();
        if (bean == null) {
            return null;
        }
        return bean.user;
    }

    /**
     * 获取用户token信息
     *
     * @return
     */
    public static UserToken getUserToken() {
        LoginUserInfoEntity.ResultBean bean = getLoginUserInfoData();
        if (bean == null) {
            return null;
        }
        return bean.userToken;
    }

    /**
     * 保存默认公司id
     *
     * @param companyId
     */
    public static void saveDefaultCompanyId(Long companyId) {
        BaseInfoUtils.saveDefaultCompanyId(companyId);
    }

    /**
     * 获取默认公司id
     *
     * @return
     */
    public static Long getDefaultCompanyId() {
        Long id = BaseInfoUtils.getDefaultCompanyId();
        if (id < 0) {
            UserInfo userInfo = getUserInfo();
            if (userInfo != null && userInfo.companyId != null && userInfo.companyId > 0) {
                id = userInfo.companyId;
            }
        }
        return id;
    }

    /**
     * 获取默认公司名
     *
     * @return
     */
    public static String getDefaultCompanyName() {
        return BaseInfoUtils.getDefaultCompanyName();
    }


    /**
     * 获取用户统计MTC信息
     */
    public static String getUserStatsMTC() {
        return BaseInfoUtils.getUserStatsMTC();
    }

    /**
     * 存储用户统计MTC信息
     *
     * @param data
     */
    public static void saveUserStatsMTC(String data) {
        BaseInfoUtils.saveUserStatsMTC(data);
    }

    /**
     * 保存默认公司名
     *
     * @param name
     */
    public static void saveDefaultCompanyName(String name) {
        BaseInfoUtils.saveDefaultCompanyName(name);
    }


    /**
     * 保存用户信息
     *
     * @param json
     */
    public static void saveUserInfoData(String json) {
        BaseInfoUtils.saveUserInfo(json);
    }

    /**
     * 保存登录名
     *
     * @param loginName
     */
    public static void saveLoginName(String loginName) {
        BaseInfoUtils.saveLoginName(loginName);
    }

    public static String getLoginName() {
        return BaseInfoUtils.getLoginName();
    }

    public static String getRealName() {
        UserInfo userInfo = getUserInfo();
        if (userInfo != null) {
            if (!TextUtils.isEmpty(userInfo.realName)) {
                return userInfo.realName;
            }
        }
        return "";
    }

    /**
     * 清理用户信息
     */
    public static void quitLogin() {
        BaseInfoUtils.quitLogin();
    }


    /**
     * 获取用户id
     *
     * @return 用户id
     */
    public static Long getUserId() {
        Long userId = BaseInfoUtils.getUserId();
        if (userId != null) {
            return userId;
        }
        LoginUserInfoEntity.ResultBean userInfoData = getLoginUserInfoData();
        if (userInfoData != null && userInfoData.userToken != null && userInfoData.userToken.userId != null) {
            return userInfoData.userToken.userId;
        }
        return -1L;
    }

    public static void saveUserId(@NonNull Long uid) {
        BaseInfoUtils.saveUserId(uid);
    }

    public static void savePrivacy(String privacyLocation) {
        BaseInfoUtils.savePrivacy(privacyLocation);
    }

    /**
     * 用户是否打开位置显示
     *
     * @return 用户是否打开位置显示
     */
    public static String getPrivacy() {
        String privacy = BaseInfoUtils.getPrivacy();
        if (privacy != null) {
            return privacy;
        }
        LoginUserInfoEntity.ResultBean userInfoData = getLoginUserInfoData();
        if (userInfoData != null && userInfoData.userToken != null && userInfoData.userToken.privacyLocation != null) {
            return userInfoData.userToken.privacyLocation;
        }
        return null;
    }

    public static String getExtension() {
        LoginUserInfoEntity.ResultBean userInfoData = getLoginUserInfoData();
        if (userInfoData != null && userInfoData.user != null && userInfoData.user.extension != null) {
            return userInfoData.user.extension;
        }
        return null;
    }

    //    false:是需要审核
    public static void initAddFriendsSetting() {
        String extension = SPreference.getExtension();
        putAddFriendsSetting(ADD_FRIENDS_INIT, true);
        if (TextUtils.isEmpty(extension)) {
            putAddFriendsSetting(FRIEND_ADD_AUDIT, true);
            putAddFriendsSetting(WORKMATE_IM_SEND, true);
            putAddFriendsSetting(STRANGER_IM_SEND, true);
            return;
        }
        JSONObject jsonObject = null;
        HashMap<String, String> hashMap = new HashMap<>();
        try {
            jsonObject = new JSONObject(extension);
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                hashMap.put(next, jsonObject.get(next).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Set<String> strings = hashMap.keySet();
        Iterator<String> iterator = strings.iterator();

        while (iterator.hasNext()) {
            String next = iterator.next();
            if (next.equals("friend_add_audit")) {
                String s = hashMap.get(next);
                if (s.equals("1")) {
                    putAddFriendsSetting(FRIEND_ADD_AUDIT, false);
                } else {
                    putAddFriendsSetting(FRIEND_ADD_AUDIT, true);
                }
            } else if (next.equals("workmate_im_send")) {
                String s = hashMap.get(next);
                if (s.equals("1")) {
                    putAddFriendsSetting(WORKMATE_IM_SEND, true);
                } else {
                    putAddFriendsSetting(WORKMATE_IM_SEND, false);
                }
            } else if (next.equals("stranger_im_send")) {
                String s = hashMap.get(next);
                if (s.equals("1")) {
                    putAddFriendsSetting(STRANGER_IM_SEND, true);
                } else {
                    putAddFriendsSetting(STRANGER_IM_SEND, false);
                }
            }
        }
    }

    public static void putAddFriendsSetting(String key, boolean value) {
        put(key, value);
    }

    public static boolean getAddFriendsSetting(AddFriendsSettingEnum addFriendsSettingEnum) {
        if (!SPreference.getBoolean(ADD_FRIENDS_INIT)) {
            initAddFriendsSetting();
        }
        switch (addFriendsSettingEnum) {
            case friend_add_audit: {
                return SPreference.getBoolean(FRIEND_ADD_AUDIT);
            }
            case workmate_im_send: {
                return SPreference.getBoolean(WORKMATE_IM_SEND);
            }
            case stranger_im_send: {
                return SPreference.getBoolean(STRANGER_IM_SEND);
            }
        }
        return true;
    }

    public static void setPictureSaveInLocal(Boolean saveOrNot) {
        BaseInfoUtils.setPictureSaveInLocal(saveOrNot);
    }

    public static void setVideoSaveInLocal(Boolean saveOrNot) {
        BaseInfoUtils.setVideoSaveInLocal(saveOrNot);
    }

    public static boolean getPictureSaveInLocal() {
        return BaseInfoUtils.getPictureSaveInLocal();
    }

    public static boolean getVideoSaveInLocal() {
        return BaseInfoUtils.getVideoSaveInLocal();
    }


    /**
     * 保存登陆状态
     *
     * @param flag 是否成功
     */
    public static void saveLoginFlag(boolean flag) {
        BaseInfoUtils.saveLoginFlag(flag);
    }

    /**
     * 是否登陆
     *
     * @return 登陆状态
     */
    public static boolean isLogin() {
        return BaseInfoUtils.queryLoginFlag();
    }

    /**
     * 是否真正登陆了
     *
     * @return
     */
    public static boolean isRealLogin() {
        return isLogin() && (SPreference.getLoginUserInfoData() != null) && !TextUtils.isEmpty(SPreference.getLoginName());
    }

    /**
     * 获取本地H5包是否更新
     * @param bNew
     */
    public static void putH5LocalVersion(boolean bNew) {
        putBoolean("IsNewH5Version", bNew);
    }

    public static Boolean getH5LocalVersion() {
        return SPreference.getBoolean("IsNewH5Version");
    }

    public static void saveFloatViewX(int x) {
        putInt(FLOAT_POSITION_X, x);
    }

    public static void saveFloatViewY(int y) {
        putInt(FLOAT_POSITION_Y, y);
    }

    public static int getFloatViewX() {
        return getInt(FLOAT_POSITION_X);
    }

    public static int getFloatViewY() {
        return getInt(FLOAT_POSITION_Y);
    }

    public static void saveOpenJsonLog(boolean b) {
        putBoolean("couldOpenJsonLog", b);
    }

    public static boolean getOpenJsonLog() {
        return getBoolean("couldOpenJsonLog");
    }


    public static void dataReset(Appli appli) {
        BaseInfoUtils.userDelete(USER_LOGIN_NAME);
        BaseInfoUtils.userDelete(USER_LOGINFLAG_KEY);
        BaseInfoUtils.userDelete(USER_ID_KEY);
        BaseInfoUtils.userDelete(USER_INFO_KEY);
        BaseInfoUtils.userDelete(CHAT_LOGIN_NAME);
        BaseInfoUtils.userDelete(CHAT_LOGIN_TOKEN);
        BaseInfoUtils.userDelete(COMPANY_ID_KEY);
        BaseInfoUtils.userDelete(COMPANY_NAME_KEY);
        BaseInfoUtils.userDelete(USER_INFO_SMTC_KEY);
        BaseInfoUtils.userDelete(PRIVACY_LOCATION_KEY);

//        SessionDaoUtils.deleteAll(appli);
//        OSSDaoUtils.deleteAll(appli);
//        ESDaoUtils.deleteAll(appli);
//        AliyunDaoUtils.deleteAll(appli);

//        CompanyInfoForDBDao companyInfoForDBDao = Appli.Companion.getDaoSession().getCompanyInfoForDBDao();
//        DepartInfoForDBDao departInfoForDBDao = Appli.Companion.getDaoSession().getDepartInfoForDBDao();
//        UserInfoForDBDao userInfoForDBDao = Appli.Companion.getDaoSession().getUserInfoForDBDao();

//        if (companyInfoForDBDao != null)
//            companyInfoForDBDao.deleteAll();
//        if (departInfoForDBDao != null)
//            departInfoForDBDao.deleteAll();
//        if (userInfoForDBDao != null)
//            userInfoForDBDao.deleteAll();
    }

    /**
     * 将对象进行base64编码后保存到SharePref中
     *
     * @param key
     * @param object
     */
    public static void saveObj(String key, Object object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            String objBase64 = null;
            if (object != null) {
                oos = new ObjectOutputStream(baos);
                oos.writeObject(object);
                objBase64 = new String(Base64.encode(baos.toByteArray()));
            }
            SharedPreferences.Editor edit = getBase().edit();
            edit.putString(key, objBase64).commit();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将SharePref中经过base64编码的对象读取出来
     *
     * @param key
     * @return
     */
    public static Object getObj(String key) {
        String objBase64 = getBase().getString(key, null);
        if (TextUtils.isEmpty(objBase64)) {
            return null;
        }
        byte[] base64Bytes = null;
        try {
            base64Bytes = Base64.decode(new String(objBase64.getBytes()));
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
        ObjectInputStream ois;
        Object obj = null;
        try {
            ois = new ObjectInputStream(bais);
            obj = (Object) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}
