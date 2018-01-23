package com.z7dream.apm.base.utils.constant;

/**
 * 用于rxbus 的tag
 * Created by xiaoyu.zhang on 2016/11/16 14:46
 * Email:zhangxyfs@126.com
 *  
 */
public interface RxConstant {
    // 当改变身份的时候，向BottomNavigationBar发送个消息
    String BOTTOM_CHANGE_IDTENTIFY = "bottom_change_idtentify";

    String MAIN_BOTTOM_NAVIGATION_DOUBLE_CLICK_LEFT_FIRST = "main_bottom_navigation_double_click_left_first";

    String MAIN_BOTTOM_NAVIGATION_DOUBLE_CLICK_LEFT_SEC = "main_bottom_navigation_double_click_left_sec";

    String MAIN_BOTTOM_NAVIGATION_DOUBLE_CLICK_RIGHT_FIRST = "main_bottom_navigation_double_click_right_first";

    String MAIN_BOTTOM_NAVIGATION_DOUBLE_CLICK_RIGHT_SEC = "main_bottom_navigation_double_click_right_sec";

    String WELCOME_FINISH_OBSERVABLE = "welcome_finish_observable";

    String VIDEO_PLAY5MINUTES_OBSERVABLE = "video_play5minutes_observable";

    String VIDEO_LOCAL_REF_ONE_OBSERVABLE = "video_local_ref_one_observable";

    String VIDEO_DOWNLOAD_REF_ONE_OBSERVABE = "video_download_ref_one_observabe";

    String NOW_PLAY_VIDEOID_OBSERVABLE = "now_play_videoid_observable";

    String IS_PLAY_VIDEO_LOCAL_DELETE_OBSERVABLE = "is_play_video_local_delete_observable";

    String DOWNLOAD_TO_LIST_OBSERVABLE = "download_to_list_observable";

    String CLOSE_MAIN_OBSERVABLE = "close_main_observable";

    //接收到的新消息数量
    String IM_BOTTOM_BAR_MSG_NUM_OBSERVABLE = "im_new_message_receive_num_observable";
    //需要显示的新消息
    String IM_MSGLIST_ITEM_UPDATE_OBSERVABLE = "im_msglist_item_update_observable";
    //从IMService发送消息到ChatRoomActivity
    String IM_SEND_TO_CHAT_ROOM_OBSERVABLE = "im_send_new_message_to_chat_room_observable";
    //从IMService发送消息到MessageNotifyActivity
    String IM_SEND_TO_NOTIFY_ROOM_OBSERVABLE = "im_send_to_notify_room_observable";
    //从IMService发送消息到MessageCompanyNotifyActivity
    String IM_SEND_TO_COMPANY_NOTIFY_ROOM_OBSERVABLE = "im_send_to_company_notify_room_observable";
    //刷新MessageFragment消息列表
    String IM_REF_MESSAGE_DATA_LIST_OBSERVABLE = "imref_message_data_list_observable";
    //刷新chatRoom列表
    String IM_REF_CHAT_ROOM_LIST_OBSERVABLE = "im_ref_chat_room_list_observable";
    //刷新某个位置的消息
    String IM_REF_MESSAGE_DATA_BY_POSITION_OBSERVABLE = "im_ref_message_data_by_position_observable";
    //清除下方导航条的消息数量的显示
    String IM_CLEAR_BOTTOM_MSG_ALLNUM_DISPLAY_OBERVABLE = "im_clear_bottom_msg_allnum_display_obervable";
    //清除消息列表的消息数量显示
    String IM_CLEAR_MSGLIST_ALLNUM_DISPLAY_OBSERVABLE = "im_clear_msglist_allnum_display_observable";
    //用于chatRoomActivity 自己发消息回调
    String IM_CHAT_ROOM_SEND_MSG_OBSERVABLE = "im_chat_room_send_msg_observable";
    //聊天室单条数据更新文件路径
    String IM_CHAT_ROOM_REF_ITEM_BY_FILEPATH_OBSERVABLE = "im_chat_room_ref_item_by_filepath_observable";
    //跳转到首页的某个fragment上
    String JUMP_TO_MAIN_PAGE_OBSERVABLE = "jump_to_main_page_observable";
    //选中一张图片
    String FILE_CHOICE_PIC_FROM_MOBILE_OBSERVABLE = "file_choice_pic_from_mobile_observable";
    //选中一张图片（IM专用）
    String FILE_IM_CHOICE_PIC_FROM_MOBILE_OBSERVABLE = "file_im_choice_pic_from_mobile_observable";
    //选择文件
    String FILE_CHOICE_FILE_FROM_MOBILE_OBSERVABLE = "file_choice_file_from_mobile_observable";
    //选择文件（IM专用）
    String FILE_IM_CHOICE_FILE_FROM_MOBILE_OBSERVABLE = "file_im_choice_file_from_mobile_observable";
    //选择文件，发送文件信息
    String FILE_CHOICE_FILEINFO_FROM_MOBILE_OBSERVABLE = "file_choice_fileinfo_from_mobile_observable";
    //关闭fileExplorerActivity
    String FILE_CLOSE_FILEEXPLORER_PAGE_OBSERVABLE = "file_close_fileexplorer_page_observable";
    //消息页面红点
    String IM_RED_POINT_OBSERVABLE = "im_red_point_observable";
    //修改群名称
    String IM_CHANGE_GROUP_NAME_OBSERVABLE = "im_change_group_name_observable";
    //修改聊天室标题
    String IM_CHANGE_CHAT_ROOM_TITLE_OBSERVABLE = "im_change_chat_room_title_observable";
    //转让群主
    String IM_CHANGE_TEAM_OWNER_OBSERVABLE = "im_change_team_owner_observable";
    //转让群组成功
    String IM_CHANGE_TEAM_OWNER_SUCC_OBSERVABLE = "im_change_team_owner_succ_observable";
    //关闭聊天页面
    String IM_FINISH_CHAT_ROOM_OBSERVABLE = "im_finish_chat_room_observable";
    //关闭聊天信息页面
    String IM_FINISH_CHAT_ROOM_DETAIL_OBSERVABLE = "im_finish_chat_room_detail_observable";
    //刷新聊天用户列表
    String IM_REF_CHAT_ROOM_USER_LIST_OBSERVABLE = "im_ref_chat_room_user_list_observable";
    //关闭页面时候不调用上一个页面的onResume
    String IM_FINISH_NO_RESUME_OBSERVABLE = "im_finish_no_resume_observable";

    //关闭单聊 聊天详情页面
    String IM_CLOSE_SINGLE_DETAIL_OBSERVABLE = "im_close_single_detail_observable";
    //定位消息在列表上的位置
    String IM_TO_LOCATION_MESSAGE_POS_OBSERVABLE = "im_to_location_message_pos_observable";
    //转发，围观消息 类型<MessageListModel>
    String IM_MSG_SEND_DATA_COMPANY_OBSERVABLE = "im_msg_send_data_company_observable";

    String IM_MSG_SEND_DATA_PERSONAL_OBSERVABLE = "im_msg_send_data_personal_observable";

    String IM_FORWARD_MSG_PAG_FINISH_OBSERVABLE = "im_forward_msg_pag_finish_observable";
    //发送生成照相图片路径
    String CAMERA_SEND_PIC_PATH_OBSERVABLE = "camera_send_pic_path_observable";
    //选择并操作
    String FILE_CHOISE_AND_OERATION_OBSERVABLE = "file_choise_and_oeration_observable";

    //点赞
    String THINGS_DYNAMIC_DETAILS_PRAISE = "things_dynamic_details_praise";
    //取消点赞
    String THINGS_DYNAMIC_DETAILS_CANCEL_PRAISE = "things_dynamic_details_cancel_praise";
    //刷新动态列表评论数
    String THINGS_DYNAMIC_COMMENT_REFRESH = "things_dynamic_comment_refresh";
    //刷新点赞列表
    String THINGS_DYNAMIC_FAVORS_REFRESH = "things_dynamic_favors_refresh";
    //打开动态评论
    String THINGS_DYNAMIC_OPEN_COMMENT = "things_dynamic_open_comment";
    //刷新阅读人
    String THINGS_REFRESH_READ_COUNT = "things_refresh_read_count";
    //关闭阅读人
    String THINGS_BROWSE_POSTS = "things_browse_posts";
    //发送阅读人数量
    String THINGS_READ_COUNT = "things_read_count";
    //动态列表页面刷新是否收藏
    String THINGS_DYNAMIC_LIST_COLLECTION = "things_dynamic_list_collection";

    //事事号是否归档
    String THINGS_IS_ARCHIVE = "things_is_archive";
    // 刷新动态列表
    String THINGS_DYANMIC_REFRESH = "things_dyanmic_refresh";

    //关闭FilePicSelectActivity页面
    String FILE_PIC_SELECT_CLOSE_OBSERVABLE = "file_pic_select_close_observable";
    //选择文件路径 FilePicSelectActivity
    String FILE_PIC_SELECT_CHOICE_FOLDER_OBSERVABLE = "file_pic_select_choice_folder_observable";
    //选择并且关闭 FilePicSelectActivity
    String FILE_CHOICE_AND_CLOSE_PIC_SELECT_OBSERVABLE = "file_choice_and_close_pic_select_observable";
    //FilePicDisplayActivity 当点击选择check时候会向FilePicSelectActivity发送同步消息
    String FILE_PIC_DISPLAY_CHOICE_OBSERVABLE = "file_pic_display_choice_observable";
    //FilePicDisplayActivity 当点击原图check时候会向FilePicSelectActivity发送同步消息
    String FILE_PIC_DISPLAY_ORIGINAL_CHOICE_OBSERVABLE = "file_pic_display_original_choice_observable";
    //FileManagerActivity 页面finish
    String FILE_MANAGER_FINISH_OBSERVABLE = "File_manager_finish_observable";

    //关闭设置页面
    String CLOSE_SETTING_OBSERVABLE = "close_setting_observable";

    //刷新个人页面头像
    String REF_MINE_PHOTO_OBSERVABLE = "ref_mine_photo_observable";

    //打开新的聊天，关闭以前的聊天信息页面
    String IM_OPEN_NEW_PAGE_AND_CLOSE_PRE_PAGE = "im_open_new_page_and_close_pre_page";
    //创建聊天成功，关闭以前的页面
    String IM_CREATE_CHAT_AND_CLOSE_PRE_PAGE = "im_create_chat_and_close_pre_page";

    //事事资料界面 游客是否可见上级领导的权限
    String THINGS_VISITOR_CAN_VIEW_LEADER = "things_visitor_Can_view_Leader";
    //移交创建者后更改详情页thingsRole
    String THINGS_DETAILS_ROLE = "things_details_role";
    //删除事事成员后通知
    String THINGS_DETAILS_DEL_MEMBER = "things_details_del_member";
    //刷新事事访客权限值
    String THINGS_DETAILS_VISITOR_AUTHORITY = "things_details_visitor_authority";
    //发布动态后更新通知详情
    String THINGS_RELEASE_DYNAMIC = "things_release_dynamic";
    //更改Things Tab顺序或删除Tab标签时调用
    String THINGS_TAB_REFRESH = "things_tab_refresh";
    // 刷新事事资料页
    String THINGS_DETAILS_REFRESH = "Things_details_refresh";
    //考勤base页面，刷新toolbar title
    String CHECK_IN_BASE_REF_TITLE_DATE_OBSERVABLE = "check_in_base_ref_title_date_observable";
    //关闭考勤base页面
    String FINISH_CHECK_IN_BASE_OBSERVABLE = "finish_check_in_base_observable";
    //切换考勤fragment页面
    String SWITCH_CHECK_IN_BASE_FRAGMENT_OBSERVABLE = "switch_check_in_base_fragment_observable";
    //刷新事事首页标签
    String THINGS_TAB_FIRST_REFRESH = "THINGS_TAB_FIRST_REFRESH ";
    //主页事事图标点击
    String CLICK_THINGS_FRAGMENT = "click_things_fragment";
    //用户公司、部门进行了更新，通知外部刷新，boolean类型
    String USER_COMPANY_DEPART_UPDATE_OBSERVABLE = "user_company_depart_update_observable";
    //wifi是否连接了-用于考勤
    String CHECKINGIN_WIFI_CONNECTION_OBSERVABLE = "checkingin_wifi_connection_observable";
    //检测网络状态
    String CHECKING_IM_NET_CONNECTION_OBSERVABLE = "checking_im_net_connection_observable";
    //刷新考勤设置
    String REF_CHECKING_SET_OBSERVABLE = "ref_checking_set_observable";

    //工作表提交完成，关闭相应的页面，及返回url
    String COMMIT_WORKTABLE_COMPLET_OBSERVABLE = "commit_worktable_complet_observable";

    //网络监听--打卡
    String NET_WORK_LISTENER_BY_CHECKINGIN_OBSERVABLE = "net_work_listener_by_checkingin_observable";
    //网络监听--签到
    String NET_WORK_LISTENER_BY_SIGNIN_OBSERVABLE = "net_work_listener_by_signin_observable";
    //地图定位监听--打卡
    String MAP_LOCALTION_LISTNER_BY_CHECKINGIN_OBSERVABLE = "map_localtion_listner_by_checkingin_observable";
    //地图定位监听--签到
    String MAP_LOCALTION_LISTNER_BY_SIGNIN_OBSERVABLE = "map_localtion_listner_by_signin_observable";
    //地图定位监听--设置考勤
    String MAP_LOCATION_LISTENER_BY_PLANMODIFY_OBSERVABLE = "map_location_listener_by_planmodify_observable";
    //打卡--地图定位开启或者关闭
    String MAP_LOCALTION_STOP_OR_START_BYCHECKINGIN_OBSERVABLE = "map_localtion_stop_or_start_bycheckingin_observable";
    //刷新签到数据
    String REF_SIGN_LIST_OBSERVABLE = "ref_sign_list_observable";
    //刷新考勤设置列表
    String REF_CHECKING_IN_SET_LIST_OBSERVABLE = "ref_checking_in_set_list_observable";

    //发送名片选择完成
    String SEND_CARD_CHOOSE_COMPLETE = "send_card_choose_complete";
    //发送某一个用户的名片到其他用户完成，关闭IMPerson、AddressChoose、Friends
    String SEND_CARD_WITHUSER_COMPLETE = "send_card_withuser_complete";

    //用户公司、部门进行了更新，通知外部刷新，boolean类型
    String USER_THINGS_COMPANY_DEPART_UPDATE_OBSERVABLE = "user_things_company_depart_update_observable";
    //切换公司id
    String SWITCH_DEFAULT_COMPANY_OBSERVABLE = "switch_default_company_observable";

    //任务发布后，关闭任务模板页面
    String PUB_TASK_CLOSE_CREATE_PAGE_OBSERVABLE = "pub_task_close_create_page_observable";
    //动态关联到任务后，关闭任务提交页面
    String ADDBLOG_TASK_CLOSE_COMMIT_PAGE_OBSERVABLE = "addblog_task_close_commit_page_observable";
    //任务转发成功后，关闭任务详情页面
    String TRANSFER_TASK_CLOSE_TASKDETAIL_PAGE_OBSERVABLE = "transfer_task_close_taskdetail_page_observable";
    //任务分配单转发后，关闭Webview页面
    String TRANSFER_TASK_WT_CLOSE_WEBVIEW_PAGE_OBSERVABLE = "transfer_task_wt_close_webview_page_observable";

    //常用事事 处理 选中 取消数据的操作
    String THINGS_RECENTLY_SELECT_DATA = "things_recently_select_data";
    //常用事事 通知 Fragment 需要显示选中的数据
    String THINGS_RECENTLY_FRAGMENT_SELECT = "things_recently_fragment_select";
    String SELECT_WORKSHEET_ACTIVITY_SELECT = "SELECT_WORKSHEET_ACTIVITY_SELECT";
    //通知频道界面刷新本地数据
    String CHANNEL_LOAD_LOCAL_DATA = "CHANNEL_LOAD_LOCAL_DATA";

    //转发预警到其他用户完成，关闭AddressChoose、imperson、search
    String TRANSFER_EARLY_WITH_COMDEP_COMPLETE = "transfer_early_with_comdep_complete";
    //网络监听--Webview
    String NET_WORK_LISTENER_BY_WEBVIEW_OBSERVABLE = "net_work_listener_by_webview_observable";
    //    通知webview从SaveWorksheetActivity关闭
    String CLOSE_WEBVIEW_FORM_SAVEWORKSHEETACTIVITY = "close_webview_form_saveworksheetactivity";
    //    通知从webview关闭sellect_work_sheet
    String CLOSE_WORK_TABLE_FORM_WEBVIEW_ACTIVITY = "close_work_table_form_webview_activity";

}
