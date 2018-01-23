package com.z7dream.apm.base.utils.constant;

/**
 *  事事模块常用的常量
 * Created by duCong on 2017/7/3.
 */

public class ThingsConstant {

    /**
     * 动态附件类型
     */

    //图片
    public static final int IMAGE_TYPE = 1;
    //视频
    public static final int VIDEO_TYPE = 2;
    //录音
    public static final int RECORD_TYPE = 3;
    //文件
    public static final int FILE_TYPE = 4;
    //工作表
    public static final int WORKSHEET_TYPE = 5;
    //视频图片类型
    public static final int VIDEO_IMAGE_TYPE = 6;
    //被转发动态
    public static final int FORWARD_DYNAMIC_TYPE = 10;

    /**
     * 动态列表条目类型
     */
    //头部
    public static final int HEADER_TYPE = 1;
    // 底部
    public static final int FLOOR_TYPE = 2;
    // 图片
    public static final int PICTURE_TYPE = 3;
    // 视频及视频图片
    public static final int VIDEOS_TYPE = 4;
    // 文件 工作表 转发动态
    public static final int FILES_TYPE = 5;

    /**
     * 事事成员身份
     */
    //创建者
    public static final String ROLE_CREATOR_TYPE = "1";
    //管理员
    public static final String ROLE_MANAGER_TYPE = "2";
    //成员
    public static final String ROLE_MEMBER_TYPE = "3";
    //非本公司成员
    public static final String ROLE_NO_COMPANY_TYPE = "4";

    /**
     * 事事号类型
     */
    //个人事事号
    public static final String THINGS_TYPE_PERSONAL = "3";


    public static final String SHOW_RED_DOT = "1";


    /**
     * 公司类型
     */

    //合作方
    public static final String COMPANY_EXTENSION_FOR_COOPERATION = "1";

    /**
     * 归档
     */

    //未归档
    public static final String THINGS_NO_ARCHIVE = "0";
    //已归档
    public static final String THINGS_ARCHIVE = "1";


    //第一页
    public static final int FIRST_PAGE = 1;

    //事事首页页码
    public static final int START_THINGS_ACTIVITY_TYPE = 2;

    //非本公司成员
    public static String NON_COMPANY = "1";


    /**
     * 评论类型
     */
    // 谈一谈
    public static final int THINGS_DYNAMIC_COMMENT_CHAT = 1;
    // 说问题
    public static final int THINGS_DYNAMIC_COMMENT_PROBLEM = 2;
    // 提建议
    public static final int THINGS_DYNAMIC_COMMENT_PROPOSAL = 3;
    // 打分
    public static final int THINGS_DYNAMIC_COMMENT_MARK = 4;

    //主评论
    public static final int PRIMARY_TYPE = 1;
    //二级评论
    public static final int SECONDARY_TYPE = 2;

    public static String[] TypeName = {"公开", "本公司可见", "本部门可见(下级部门不可见)", "本部门及下级部门可见", "事事号成员可见", "本公司部门和成员可见", "部分人员可见","多个部门可见"};


    // 转发动态
    public static final int INTENT_REQUEST_SELECT_THINGS = 120;
    // 工作表返回值
    public static final int INTENT_REQUEST_WARNING = 121;

    /**
     * 考勤
     */
    public final static String MAIN_NAVIGATION_ITEM_ATTENDANCE = "attendance";
    /**
     * 统计
     */
    public final static String MAIN_NAVIGATION_ITEM_STATS = "statistics";
    /**
     * 任务
     */
    public final static String MAIN_NAVIGATION_ITEM_TASK = "task";
    /**
     * 工作表
     */
    public final static String MAIN_NAVIGATION_ITEM_WORKSHEET = "worksheet";
    /**
     * 预警
     */
    public final static String MAIN_NAVIGATION_ITEM_WARNING = "warning";
    /**
     * 更多
     */
    public final static String MAIN_NAVIGATION_ITEM_MORE = "more";


}
