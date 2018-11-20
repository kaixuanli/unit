package cn.com.innodev.treicbcelife;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/11/19 18:01
 * @since 1.8
 */
public class EicbcApiUrlConst {

    public static final String ELIFE_BASE_URL = "https://elife.icbc.com.cn";

    public static final String GET_COMMUNITY_ALL_BY_CITYCODE = "/OFSTCUST/community/getCommunityAllByCityCode.action";
    public static final String GET_NEAR_STORE_DISCOUNT = "/OFSTCUST/iFavorableList/getNearStoreDiscount.action";

    public static String formatUrl(String requestUrl) {
        return String.format("%s%s", ELIFE_BASE_URL, requestUrl);
    }
}
