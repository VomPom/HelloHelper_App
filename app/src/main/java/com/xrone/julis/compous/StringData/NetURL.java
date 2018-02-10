package com.xrone.julis.compous.StringData;

/**
 * Created by Julis on 17/6/16.
 */

/**
 * 链接地址的常量
 */
public class NetURL {

//
//    public static final String GET_NEWS_URL="http://10.130.251.23/hello/getJSONinfomation.php";
//  public static final String SHARE_URL="http://10.130.251.23/hello/share.php";
//    public static final String REG_URL="http://10.130.251.23/hello/register.php";
//    public static final String LOGIN_URL="http://10.130.251.23/hello/login.php";
// public static final String APPINFORMATION_URL="http://10.130.251.23/hello/getJSONApplicationinfomation.php";


    public static final String WEBSITE="http://123.207.24.167";
    public static final String DIRECTIONARY="/hello/";
    public static final String USER_HEAD_DIR="image/user/";
    public static final String APPINFORMATION_URL=WEBSITE+DIRECTIONARY+"getCultureAtricle.php";

    /**
     * 新闻信息
     */
    public static final String GET_NEWS_URL=WEBSITE+DIRECTIONARY+"getJSONinfomation.php";
    /**
     * 分享交流
     */
    public static final String SHARE_URL=WEBSITE+DIRECTIONARY+"share.php";
    /**
     * 注册链接
     */
    public static final String REG_URL=WEBSITE+DIRECTIONARY+"register.php";
    /**
     * 登陆链接
     */
    public static final String LOGIN_URL=WEBSITE+DIRECTIONARY+"login.php";
    /**
     * 快递查询
     */
    public static final String EXPRESS_URL= WEBSITE+DIRECTIONARY+"/hellohelper/appsearch.html";
    /**
     * 默认头像
     */
    public static final String DEFAULT_HEAD_URL="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498042419651&di=d45ce92bfa08b0304b0faacd4098b9f3&imgtype=0&src=http%3A%2F%2Fwww.czgongzuo.com%2FFiles%2FPerPhoto%2Fphotoman.gif";
    /**
     * 检查更新
     */
    public static final String CHECKUPDATE_URL =WEBSITE+DIRECTIONARY+"versionControl.php";
    /**
     *获取快递点信息
     */
    public static final String GET_EXPRESS_INFO_URL =WEBSITE+DIRECTIONARY+"getExpressInfo.php";
    /**
     *获取快递点信息
     */
    public static final String GET_EXPRESS_PLACES_URL =WEBSITE+"getExpressPlaces.php";
    /**
     * 反馈页面网址
     */
    public static final String FEEDBACK_URL=WEBSITE+DIRECTIONARY+"feedback.php";
    /**
     * 获取评论主页
     */
    public static final String TOPIC_URL=WEBSITE+DIRECTIONARY+"commucation/getTopic.php";
    /**
     * 获取评论
     */
    public static final String COMMENT_URL=WEBSITE+DIRECTIONARY+"commucation/getComment.php";

    /**
     * 添加话题
     */
    public static final String ADDATOPIC_URL=WEBSITE+DIRECTIONARY+"commucation/addATopic.php";
    /**
     * 添加评论
     */
    public static final String ADDACOMMENT_URL=WEBSITE+DIRECTIONARY+"commucation/addAComment.php";
    /**
     * 更改个人信息
     */
    public static final String PERSONALINFOCHANGE_URL=WEBSITE+DIRECTIONARY+"personInfoChange.php";
    /**
     * 获取个人详细信息
     */
    public static final String PERSONAL_DEATL_URL=WEBSITE+DIRECTIONARY+"commucation/getUserDetail.php";

}
