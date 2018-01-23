package com.z7dream.apm.base.utils.dao;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.z7dream.apm.base.Appli;
import com.z7dream.apm.base.utils.cache.CPConstant;
import com.z7dream.apm.base.utils.cache.SPreference;
import com.z7dream.apm.base.utils.dao.bean.BaseOtherInfo;
import com.z7dream.apm.base.utils.dao.bean.BaseOtherInfo_;
import com.z7dream.apm.base.utils.dao.bean.BaseUserInfo;
import com.z7dream.apm.base.utils.dao.bean.BaseUserInfo_;
import com.z7dream.apm.base.utils.tools.Base64Util;
import com.z7dream.apm.base.utils.tools.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.objectbox.Box;

/**
 * Created by Z7Dream on 2017/10/31 16:03.
 * Email:zhangxyfs@126.com
 */

public class BaseInfoUtils implements CPConstant {
    private static Box<BaseUserInfo> baseUserInfoBox;
    private static Box<BaseOtherInfo> baseOtherInfoBox;

    static {
        baseUserInfoBox = Appli.Companion.getBoxStore().boxFor(BaseUserInfo.class);
        baseOtherInfoBox = Appli.Companion.getBoxStore().boxFor(BaseOtherInfo.class);
    }

    private static Box<BaseUserInfo> getBaseUserInfoBox() {
        return baseUserInfoBox;
    }

    private static Box<BaseOtherInfo> getBaseOtherInfoBox() {
        return baseOtherInfoBox;
    }

    /**
     * 获取是否登陆
     *
     * @return
     */
    public static Boolean queryLoginFlag() {
        String flag = userQueryByTitle(USER_LOGINFLAG_KEY);
        if (flag.equals("1")) {
            return true;
        }
        return false;
    }

    /**
     * 退出登陆
     */
    public static void quitLogin() {
        userDelete(USER_LOGINFLAG_KEY);
        userDelete(USER_ID_KEY);
        userDelete(USER_INFO_KEY);
        userDelete(CHAT_LOGIN_NAME);
        userDelete(CHAT_LOGIN_TOKEN);
    }

    /**
     * 保存用户id
     *
     * @param uid
     */
    public static void saveUserId(Long uid) {
        userInsertUpDate(USER_ID_KEY, String.valueOf(uid));
    }

    /**
     * 获取用户id
     *
     * @return
     */
    public static Long getUserId() {
        Long uid = null;
        String id = userQueryByTitle(USER_ID_KEY);
        if (!TextUtils.isEmpty(id) && Utils.isNumber(id)) {
            uid = Long.parseLong(id);
        }
        return uid;
    }

    /**
     * 保存环信信息
     *
     * @param privacyLocation
     */
    public static void savePrivacy(String privacyLocation) {
        userInsertUpDate(PRIVACY_LOCATION_KEY, privacyLocation);
    }

    /**
     * 获取环信信息
     *
     * @return
     */
    public static String getPrivacy() {
        String privacy = null;
        String privacy_ = userQueryByTitle(PRIVACY_LOCATION_KEY);
        if (!TextUtils.isEmpty(privacy_) && Utils.isNumber(privacy_)) {
            privacy = privacy_;
        }
        return privacy;
    }

    public static void setPictureSaveInLocal(Boolean saveOrNot) {
        userInsertUpDate(SAVE_PICTURE, saveOrNot ? "1" : "0");
    }

    public static boolean getPictureSaveInLocal() {
        String flag = userQueryByTitle(SAVE_PICTURE);
        if (flag.equals("0")) {
            return false;
        }
        return true;
    }

    public static void setVideoSaveInLocal(Boolean saveOrNot) {
        userInsertUpDate(SAVE_VIDEO, saveOrNot ? "1" : "0");
    }

    public static boolean getVideoSaveInLocal() {
        String flag = userQueryByTitle(SAVE_VIDEO);
        if (flag.equals("0")) {
            return false;
        }
        return true;
    }

    public static void saveLoginName(String loginName) {
        userInsertUpDate(USER_LOGIN_NAME, loginName);
    }

    public static void saveUserStatsMTC(String loginName) {
        userInsertUpDate(USER_INFO_SMTC_KEY, loginName);
    }

    public static String getUserStatsMTC() {
        return userQueryByTitle(USER_INFO_SMTC_KEY);
    }

    public static String getLoginName() {
        return userQueryByTitle(USER_LOGIN_NAME);
    }

    public static void saveChatLoginName(String loginName) {
        userInsertUpDate(CHAT_LOGIN_NAME, loginName);
    }

    public static String getChatLoginName() {
        return userQueryByTitle(CHAT_LOGIN_NAME);
    }

    public static void saveChatToken(String token) {
        userInsertUpDate(CHAT_LOGIN_TOKEN, TextUtils.isEmpty(token) ? "" : token);
    }

    public static String getChatToken() {
        return userQueryByTitle(CHAT_LOGIN_TOKEN);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public static String queryUserInfoData() {
        String json = userQueryByTitle(USER_INFO_KEY);
        if (!TextUtils.isEmpty(json)) {
            return json;
        }
        return null;
    }

    public static void saveUserInfo(String json) {
        userInsertUpDate(USER_INFO_KEY, json);
    }

    public static void saveLoginFlag(boolean flag) {
        userInsertUpDate(USER_LOGINFLAG_KEY, flag ? "1" : "0");
    }

    public static void saveDefaultCompanyId(Long companyId) {
        userInsertUpDate(getUserId() + COMPANY_ID_KEY, String.valueOf(companyId));
    }

    public static void saveDefaultCompanyName(String name) {
        userInsertUpDate(getUserId() + COMPANY_NAME_KEY, name);
    }

    public static Long getDefaultCompanyId() {
        String id = userQueryByTitle(getUserId() + COMPANY_ID_KEY);
        return TextUtils.isEmpty(id) ? -1L : Long.parseLong(Utils.isNumber(id) ? id : "-1");
    }

    public static String getDefaultCompanyName() {
        return userQueryByTitle(getUserId() + COMPANY_NAME_KEY);
    }


    public static void userClear() {
        getBaseUserInfoBox().removeAll();
    }

    ///////////////////////////////////////////////////////

    public static String findTopActivity() {
        return otherQueryByTitle(ACTIVITY_KEY);
    }

    public static void addTopActivity(String which) {
        otherInsertUpDate(ACTIVITY_KEY, which);
    }

    public static String findLastTopActivity() {
        return otherQueryByTitle(LAST_ACTIVITY_KEY);
    }

    public static void addLastTopActivity(String which) {
        otherInsertUpDate(LAST_ACTIVITY_KEY, which);
    }

    public static void delTopActivity() {
        otherDelete(ACTIVITY_KEY);
    }

    public static void clear() {
        getBaseOtherInfoBox().removeAll();
    }

    /**
     * 是否第一次打开应用
     *
     * @return
     */
    public static boolean isFirstOpenApp() {
        String txt = otherQueryByTitle(ISFIRSTOEPNAPP_KEY);
        if (TextUtils.isEmpty(txt)) {
            return true;
        }
        return false;
    }

    public static void saveFirstOpenApp() {
        otherInsertUpDate(ISFIRSTOEPNAPP_KEY, "true");
    }

    public static void saveLastAppVersion() {
        otherInsertUpDate(LAST_APP_VERSION_KEY, String.valueOf(Utils.getVersionCode(Appli.Companion.getContext())));
    }

    public static int getLastAppVersion() {
        String txt = otherQueryByTitle(LAST_APP_VERSION_KEY);
        if (TextUtils.isEmpty(txt)) {
            return 0;
        }

        if (!isNumeric(txt)) {
            return 0;
        }

        return Integer.parseInt(txt);
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static boolean isJustStartHightLoadService() {
        String str = otherQueryByTitle(IS_JUST_START_HIGHTLOADSERVICE);
        return TextUtils.isEmpty(str);
    }

    public static void saveJustStartHightLoadService(String str) {
        otherInsertUpDate(IS_JUST_START_HIGHTLOADSERVICE, str == null ? "" : str);
    }


    /**
     * 是否第一次启动
     *
     * @return
     */
    public static boolean isFirstLaunched() {
        String txt = otherQueryByTitle(IS_ALREADY_LAUNCH);
        if (TextUtils.isEmpty(txt)) {
            return false;
        }
        return true;
    }

    /**
     * 保存apk路径
     */
    public static void saveDownloadApk(String path) {
        otherInsertUpDate(DOWNLOADAPK_KEY, path);
    }

    public static String getDownloadApk() {
        return otherQueryByTitle(DOWNLOADAPK_KEY);
    }

    public static void saveDownloadApkId(long id) {
        otherInsertUpDate(DOWNLOADAPKID_KEY, String.valueOf(id));
    }

    public static long getDownloadApkId() {
        String id = otherQueryByTitle(DOWNLOADAPKID_KEY);
        return Utils.isNumber(id) ? Long.parseLong(id) : -1;
    }

    public static void saveJoinForground(boolean isForground) {
        String value = isForground ? "1" : "0";
        otherInsertUpDate(IS_JOIN_FORGROUND_KEY, value);
    }

    public static boolean isJoinForground() {
        String value = otherQueryByTitle(IS_JOIN_FORGROUND_KEY);
        return TextUtils.equals(value, "1");
    }

    public static void saveKeyBoradHeight(int height) {
        otherInsertUpDate(KEYBOARD_HEIGHT_KEY, String.valueOf(height));
    }

    public static int getKeyBroadHeight() {
        String value = otherQueryByTitle(KEYBOARD_HEIGHT_KEY);
        return Utils.isNumber(value) ? Integer.parseInt(value) : 0;
    }

    public static String getQRCodeFilePath(String qrcode) {
        return otherQueryByTitle(qrcode + QRCODE_PATH_KEY);
    }

    public static void saveQRCodeFilePath(String qrcode, String path) {
        otherInsertUpDate(qrcode + QRCODE_PATH_KEY, path);
    }

    public static void saveIMPushHasSound(boolean b) {
        otherInsertUpDate(SPreference.getUserId() + IM_PUSH_HAS_SOUND_KEY, b ? "true" : "false");
    }

    public static boolean getIMPushHasSound() {
        String value = otherQueryByTitle(SPreference.getUserId() + IM_PUSH_HAS_SOUND_KEY);
        if (TextUtils.isEmpty(value)) {
            saveIMPushHasSound(true);
            return true;
        }
        return TextUtils.equals(value, "true");
    }

    public static void saveIMPushHasVibration(boolean b) {
        otherInsertUpDate(SPreference.getUserId() + IM_PUSH_HAS_VIBRATION_KEY, b ? "true" : "false");
    }

    public static boolean getIMPushHasVibration() {
        String value = otherQueryByTitle(SPreference.getUserId() + IM_PUSH_HAS_VIBRATION_KEY);
        if (TextUtils.isEmpty(value)) {
            saveIMPushHasVibration(true);
            return true;
        }
        return TextUtils.equals(value, "true");
    }

    public static String findChatSession() {
        return otherQueryByTitle(OPEN_WHICH_CHAT_KEY);
    }

    /**
     * 添加是哪个聊天界面
     * todo 去掉toLowerCase()
     *
     * @param sendID 如果是一对一，那么保存的是发送者id，如果是多对多那么保存的是群组id，当应用启动时候将清除这个值。
     */
    public static void setChatSession(String sendID) {
        otherInsertUpDate(OPEN_WHICH_CHAT_KEY, TextUtils.isEmpty(sendID) ? "" : sendID);//.toLowerCase());
    }

    public static boolean isChatActivityFinish() {
        return !TextUtils.equals(otherQueryByTitle(IS_CHAT_ACTIVITY_FINISH_KEY), "1");
    }

    public static void setChatActivityFinish(boolean b) {
        otherInsertUpDate(IS_CHAT_ACTIVITY_FINISH_KEY, b ? "0" : "1");
    }

    public static boolean isChatActivityPause() {
        return !TextUtils.equals(otherQueryByTitle(IS_CHAT_ACTIVITY_PAUSE_KEY), "1");
    }

    public static void setChatActivityPause(boolean b) {
        otherInsertUpDate(IS_CHAT_ACTIVITY_PAUSE_KEY, b ? "0" : "1");
    }


    //------------------------------------------------------------------

    private static String userQueryByTitle(String key) {
        String base64 = "";
        BaseUserInfo userInfo = getBaseUserInfoBox().query().equal(BaseUserInfo_.title, key).build().findUnique();
        if (userInfo != null) {
            base64 = userInfo.getValue();
        }
        if (!TextUtils.isEmpty(base64)) {
            return Base64Util.fromBase64(base64, Base64.DEFAULT);
        }
        return base64;
    }

    public static void userDelete(String key) {
        getBaseUserInfoBox().query().equal(BaseUserInfo_.title, key).build().remove();
    }

    private static void userInsertUpDate(@NonNull String title, @NonNull String content) {
        String base64 = Base64Util.toBase64(content, Base64.DEFAULT);
        BaseUserInfo baseUserInfo = getBaseUserInfoBox().query().equal(BaseUserInfo_.title, title).build().findUnique();

        if (baseUserInfo == null) {
            baseUserInfo = new BaseUserInfo();
            baseUserInfo.setTitle(title);
        }
        baseUserInfo.setValue(base64);
        getBaseUserInfoBox().put(baseUserInfo);
    }

    private static String otherQueryByTitle(String title) {
        String info = "";
        BaseOtherInfo otherInfo = getBaseOtherInfoBox().query().equal(BaseUserInfo_.title, title).build().findUnique();
        if (otherInfo != null) {
            info = otherInfo.getValue();
        }
        return info;
    }

    private static void otherDelete(String key) {
        getBaseOtherInfoBox().query().equal(BaseUserInfo_.title, key).build().remove();
    }

    private static void otherInsertUpDate(@NonNull String title, @NonNull String content) {
        BaseOtherInfo baseOtherInfo = getBaseOtherInfoBox().query().equal(BaseUserInfo_.title, title).build().findUnique();
        if (baseOtherInfo == null) {
            baseOtherInfo = new BaseOtherInfo();
            baseOtherInfo.setTitle(title);
        }
        baseOtherInfo.setValue(content);
        getBaseOtherInfoBox().put(baseOtherInfo);
    }

    public static void testInsert() {//49ms
        List<BaseOtherInfo> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            BaseOtherInfo baseOtherInfo = new BaseOtherInfo();
            baseOtherInfo.setTitle("title" + i);
            baseOtherInfo.setValue("value" + i);
            list.add(baseOtherInfo);
        }
        Log.e("testInsert", System.currentTimeMillis() + "-------start");
        getBaseOtherInfoBox().put(list);
        Log.e("testInsert", System.currentTimeMillis() + "-------end");
    }

    public static void testRead() {//922ms
        Log.e("testRead", System.currentTimeMillis() + "-------start");
        List<BaseOtherInfo> list = getBaseOtherInfoBox().query().contains(BaseOtherInfo_.title, "title").build().find();
        Log.e("testRead", System.currentTimeMillis() + "-------end");
    }
}
