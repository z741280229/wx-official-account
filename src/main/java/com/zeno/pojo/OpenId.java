package com.zeno.pojo;

import java.util.List;

/**
 * @program: wx-official-account
 * @description:
 * @author: Zeno
 * @create: 2019-12-26 11:41
 **/
public class OpenId {

    private String openid;

    private String lang;

    public OpenId(String openid, String lang) {
        this.openid = openid;
        this.lang = lang;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
