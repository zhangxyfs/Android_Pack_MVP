package com.z7dream.apm.base.api.entity;

/**
 * Created by Z7Dream on 2017/3/1 16:22.
 * Email:zhangxyfs@126.com
 */

public class UserToken {
    public Long id;
    public Long createTime;
    public Long updateTime;
    public Long userId;
    public String deviceId;
    public String accessToken;
    public String refreshToken;
    public long tokenValidTime;
    public long tokenExpireTime;
    public long refreshTokenValidTime;
    public long refreshTokenExpireTime;
    public String extension;
    public String privacyLocation;
}
