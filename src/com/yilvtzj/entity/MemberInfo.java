package com.yilvtzj.entity;

/**
 * 会员信息的javabean
 * @author gaowenhui 2013-2-16 下午3:07:02
 */
public class MemberInfo {

    public int    _id;
    public String name;
    public int age;
    public String website;
    public String weibo;
    public MemberInfo(){}
    public MemberInfo(int _id,String name,int age,String website,String weibo){
        this._id = _id;
        this.name = name;
        this.age = age;
        this.website = website;
        this.weibo = weibo;
    }

}
