package com.z7dream.apm.base.api.entity;


import com.z7dream.apm.base.mvp.model.BaseEntity;

/**
 * Created by Z7Dream on 2017/1/12 16:38.
 * Email:zhangxyfs@126.com
 */

public class LoginUserInfoEntity extends BaseEntity<LoginUserInfoEntity.ResultBean> {

    public static class ResultBean {
        public UserToken userToken;
        public UserInfo user;
        public Thirdparty thirdparty;
        public String toBindMobile;
    }

    public static class Thirdparty {
        public Long id;
        public long createTime;
        public long updateTime;
        public int userId;
        public String type;
        public String thirdpartyId;
        public String token;
        public String expired;
        public String extension;
    }

}
