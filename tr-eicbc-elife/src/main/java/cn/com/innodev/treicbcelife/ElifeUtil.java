package cn.com.innodev.treicbcelife;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/11/19 18:00
 * @since 1.8
 */
public class ElifeUtil {

    public static void main(String[] args) {
//        getCommunityAllByCityCode();
        getNearStoreDiscount();
    }

    /**
     * 离我最近的区域
     */
    public static void getNearStoreDiscount(){
        Map<String, Object> param = new HashMap<>();
        param.put("s_row", 1);// start_row
        param.put("e_row", 25);// end_row
        param.put("order_key", "query_key desc");//
//        param.put("query_distance", "2000");// 搜索附近时的距离
//        param.put("district_code", "11010129");// 按区域搜索时的地区编号
        param.put("city_code", 110100);
        param.put("c_no", 314);
        param.put("longitude", "116.487418");
        param.put("latitude", "40.002706");
        HttpResponse response = HttpRequest.post(EicbcApiUrlConst.formatUrl(EicbcApiUrlConst.GET_NEAR_STORE_DISCOUNT))
                .header("User-Agent", "F-OFST elife_moblie_androidMozilla/5.0 (Linux; Android 7.1.1; PRO 6 Build/NMF26O; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/51.0.2074.204 Mobile Safari/537.36 ICBCAndroidBS PromptFlag fullversion:2.2.2 ICBCAndroidBS OtherUA")
                .header("Origin", "https://elife.icbc.com.cn")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .form(param)
                .execute(true);
        System.out.println(response.body());
        JSONObject object = JSONObject.parseObject(response.body());
        JSONArray data = object.getJSONArray("data");// 列表
        int dataSize = data.size();
        for (int i = 0; i < dataSize; i++) {
            JSONObject temp = data.getJSONObject(i);
            System.out.println(String.format("%s（%s）, 所在区域：%s, 距此%s%s", temp.getString("store_name"), temp.getString("small_name1"), temp.getString("district_name1"), temp.getString("distance"), temp.getString("unit")));
            JSONArray tempArr = temp.getJSONArray("gpp_list");

            for (int j = 0; j < tempArr.size(); j++) {
                JSONObject tempNode = tempArr.getJSONObject(j);
                System.out.println(String.format("  %s, 优惠价格：%s, 原价：%s, 购买人数：%s, 浏览次数：%s",
                        tempNode.getString("gpp_pft_title"),
                        tempNode.getString("n_price"),
                        tempNode.getString("o_price"),
                        tempNode.getString("buy_count"),
                        tempNode.getString("view_count")
                        ));
            }
        }
    }

    /**
     * 获取区域信息
     */
    private static void getCommunityAllByCityCode() {
        Map<String, Object> param = new HashMap<>();
        param.put("city_code", 110100);
        param.put("c_no", 314);
        HttpResponse response = HttpRequest.post(EicbcApiUrlConst.formatUrl(EicbcApiUrlConst.GET_COMMUNITY_ALL_BY_CITYCODE))
                .header("User-Agent", "F-OFST elife_moblie_androidMozilla/5.0 (Linux; Android 7.1.1; PRO 6 Build/NMF26O; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/51.0.2074.204 Mobile Safari/537.36 ICBCAndroidBS PromptFlag fullversion:2.2.2 ICBCAndroidBS OtherUA")
                .header("Origin", "https://elife.icbc.com.cn")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .form(param)
                .execute(true);
        System.out.println(response.body());
        JSONObject object = JSONObject.parseObject(response.body());
        JSONArray topList = object.getJSONArray("top_list");// 热门商区
        JSONArray data = object.getJSONArray("data");// 列表
        int topListSize = topList.size(), dataSize = data.size();
        for (int i = 0; i < topListSize; i++) {
            JSONObject temp = topList.getJSONObject(i);
            System.out.println(temp.getString("district_code") + ": " + temp);
        }
        System.out.println(String.format("共%s条热门商区", topListSize));

        for (int i = 0; i < dataSize; i++) {
            JSONObject temp = data.getJSONObject(i);
            System.out.println(String.format("区域编号(district_code)：%s, 区域名称(district_name)：%s", temp.getString("district_code"), temp.getString("district_name")));
            JSONArray tempArr = temp.getJSONArray("community_list");

            for (int j = 0; j < tempArr.size(); j++) {
                JSONObject tempNode = tempArr.getJSONObject(j);
                System.out.println(String.format("  地区编号(community_code)：%s, 地区名称(community_name)：%s", tempNode.getString("community_code"), tempNode.getString("community_name")));
            }
            System.out.println(String.format("%s区域共有%s个地区", temp.getString("district_name"), tempArr.size()));
        }
    }
}
