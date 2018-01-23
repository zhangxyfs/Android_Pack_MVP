package com.z7dream.apm.base.utils.constant;

import android.Manifest;

/**
 * 用于存放静态变量 final static
 *  Created by xiaoyu.zhang on 2016/11/10 15:45
 *  
 */
public interface Constant {
    // 用户设置信息
    String USER_SETTING = "user_setting";
    //上一次更新的应用版本号，如果刚安装则为当前版本号
    String LAST_APP_VERSION = "last_app_version";
    //相机闪光灯状态
    String CAMERA_FLASH = "camera_flash_status";
//    是否进行过 添加好友隐私设置 初始化
String ADD_FRIENDS_INIT = "add_friends_init";
//    添加好友
    String FRIEND_ADD_AUDIT = "friend_add_audit";
//    同事发送IM
    String WORKMATE_IM_SEND = "workmate_im_send";
//    陌生人
    String STRANGER_IM_SEND = "stranger_im_send";

    //OSS
    String OSS_JSON = "oss_json";

    String FLOAT_POSITION_X = "float_position_x";
    String FLOAT_POSITION_Y = "float_position_y";

    String LOGIN_RECEIVER_ACTION = "com.eblog.receiver.login";
    String TO_CHAT_ROOM_RECEIVER_ACTION = "com.eblog.receiver.chat";

    // mColors用于SwipeRefreshLayout
    int[] mColors = {0xFFF8698F, 0xFFFF4081, 0xFFF5F5F5};

    String NIGHT_MODE = "appcompat_night_mode";

    String THINGS_BACKGROUND = "things_background";

    //----------------权限管理参数(危险权限)------------------

    int REQUEST_CODE_ASK_PERMISSIONS = 0x0101;
    //危险权限,需要在运行时请求.注意: 危险权限是按组来分的,所以,当你申请了多个同组的危险权限时,运行时只需要申请一个就行
    // 联系人 
    String PERMISSION_WRITE_CONTANCTS = Manifest.permission.WRITE_CONTACTS;
    String PERMISSION_GET_CONTANCTS = Manifest.permission.GET_ACCOUNTS;
    String PERMISSION_READ_CONTANCTS = Manifest.permission.READ_CONTACTS;
    //电话
    String PERMISSION_READ_CALL_LOG = Manifest.permission.READ_CALL_LOG;
    String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    String PERMISSION_WRITE_CALL_LOG = Manifest.permission.WRITE_CALL_LOG;
    String PERMISSION_USE_SIP = Manifest.permission.USE_SIP;
    String PERMISSION_PROCESS_CALLS = Manifest.permission.PROCESS_OUTGOING_CALLS;
    String PERMISSION_ADD_VICEMAIL = Manifest.permission.ADD_VOICEMAIL;
    //日历
    String PERMISSION_READ_CALENDAR = Manifest.permission.READ_CALENDAR;
    String PERMISSION_WRITE_CALENDAR = Manifest.permission.WRITE_CALENDAR;
    // 相机 
    String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    //传感器
    String PERMISSION_BODY_SENSORS = Manifest.permission.BODY_SENSORS;
    //定位
    String PERMISSION_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    String PERMISSION_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    // 存储
    String PERMISSION_READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    String PERMISSION_WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    // 录音 
    String PERMISSION_AUDIO = Manifest.permission.RECORD_AUDIO;
    // 短信 
    String PERMISSION_SEND_SMS = Manifest.permission.SEND_SMS;
    String PERMSSION_READ_SMS = Manifest.permission.READ_SMS;
    String PERMISSION_RECEIVE_MMS = Manifest.permission.RECEIVE_MMS;
    String PERMISSION_RECEIVE_SMS = Manifest.permission.RECEIVE_SMS;
    String PERMISISON_WAP_PUSH = Manifest.permission.RECEIVE_WAP_PUSH;

    String PERMISSION_WIFI = Manifest.permission.ACCESS_WIFI_STATE;

    String CHANGE_NETWORK_STATE = Manifest.permission.CHANGE_NETWORK_STATE;
    String WRITE_SETTINGS = Manifest.permission.WRITE_SETTINGS;

    String LAST_ACTIVITY_NAME = "last_activity_name";

    /**
     * 事事相关常量
     */
    interface Things {

        /**
         * 事事权限 游客可见
         * 对应字段 visitorCanViewLeader
         */
        String THINGS_AUTHORITY_VISITOR_VISIBLE = "0";

        /**
         * 事事权限 游客不可见
         * 对应字段 visitorCanViewLeader
         */
        String THINGS_AUTHORITY_VISITOR_HIDE = "1";

        /**
         * 事事权限 公开
         * 对应字段 privacySettings
         */
        String THINGS_AUTHORITY_OPEN = "1";

        /**
         * 事事权限 公司公开
         * 对应字段 privacySettings
         */
        String THINGS_AUTHORITY_OPEN_COMPNY = "2";

        /**
         * 事事权限 部门公开
         * 对应字段 privacySettings
         */
        String THINGS_AUTHORITY_OPEN_DEPARTMENT = "3";

        /**
         * 事事权限 部门和下级公开
         * 对应字段 privacySettings
         */
        String THINGS_AUTHORITY_OPEN_DEPARTMENT_SUBORDINATE = "4";

        /**
         * 事事权限 事事成员公开
         * 对应字段 privacySettings
         */
        String THINGS_AUTHORITY_OPEN_MEMBER = "5";

        /**
         * 事事权限 部分部门公开
         * 对应字段 privacySettings
         */
        String THINGS_AUTHORITY_OPEN_DEPARTMENT_CHOICE = "6";

        /**
         * 事事权限 成员可见
         * 对应字段 memberCanView
         */
        String THINGS_AUTHORITY_MEMBER_VISIBLE = "0";

        /**
         * 事事权限 成员不可见
         * 对应字段 memberCanView
         */
        String THINGS_AUTHORITY_MEMBER_HIDE = "1";

        /**
         * 事事类型 企业事事
         * 对应字段 thingsType
         */
        String THINGS_TYPE_COMPANY = "1";

        /**
         * 事事类型 个人事事
         * 对应字段 thingsType
         */
        String THINGS_TYPE_MEMBER = "2";

        /**
         * 事事类型 企业下的个人工作日志
         * 对应字段 thingsType
         */
        String THINGS_TYPE_MEMBER_PRIVATE = "3";

        /**
         * 事事状态 红点 存在
         * 对应字段 isNew
         */
        String THINGS_STATUS_RED_EXIT = "1";

        /**
         * 事事状态 红点 不存在
         * 对应字段 isNew
         */
        String THINGS_STATUS_RED_EXIT_NO = "0";

        /**
         * 事事状态 归档 已归档
         * 对应字段 isArchive
         */
        String THINGS_STATUS_ARCHIVE_YES = "0";

        /**
         * 事事状态 归档 未归档
         * 对应字段 isArchive
         */
        String THINGS_STATUS_ARCHIVE_NO = "1";
    }

    /**
     * 事事成员相关常量
     */
    interface ThingsMember {

        /**
         * 事事成员权限 创建者角色
         */
        String THINGS_AUTHORITY_CREATOR = "1";

        /**
         * 事事成员权限 管理员角色
         */
        String THINGS_AUTHORITY_MANAGER = "2";

        /**
         * 事事成员权限 成员角色
         */
        String THINGS_AUTHORITY_MEMBER = "3";
    }

}
