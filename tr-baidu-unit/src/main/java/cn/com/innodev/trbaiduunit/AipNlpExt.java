package cn.com.innodev.trbaiduunit;

import com.baidu.aip.exception.AipException;
import com.baidu.aip.http.AipHttpClient;
import com.baidu.aip.http.AipRequest;
import com.baidu.aip.http.AipResponse;
import com.baidu.aip.http.EBodyFormat;
import com.baidu.aip.nlp.AipNlp;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;

/**
 * AipNlp扩展
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @date 2018/11/20 11:18
 * @since 1.8
 */
public class AipNlpExt extends AipNlp {
    public AipNlpExt(String appId, String apiKey, String secretKey) {
        super(appId, apiKey, secretKey);
    }


    /**
     * unit chat
     * https://ai.baidu.com/docs#/UNIT-v2-API/top
     *
     * @param options
     * @return
     */
    public JSONObject chat(HashMap<String, Object> options) {
        if (options == null) {
            return null;
        }
        options.put("version", "2.0");
        options.put("log_id", UUID.randomUUID().toString().replaceAll("-", ""));
        AipRequest request = new AipRequest();
        this.preOperation(request);
        request.addBody(options);

        request.setUri("https://aip.baidubce.com/rpc/2.0/unit/bot/chat?access_token=" + this.getToken());
//        request.addHeader("Content-Encoding", "GBK");
        request.addHeader("Content-Type", "application/json");
        request.setBodyFormat(EBodyFormat.RAW_JSON);
        this.postOperation(request);
        return this.requestServer(request);
    }

    public String getToken() {
        AipRequest request = new AipRequest();
        this.preOperation(request);
        request.setUri(String.format("https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=%s&client_secret=%s", this.aipKey, this.aipToken));
        JSONObject object = this.requestServer(request);
        return object.getString("access_token");
    }
}
