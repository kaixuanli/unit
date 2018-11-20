package cn.com.innodev.trbaiduface;

import com.baidu.aip.face.AipFace;
import org.json.JSONObject;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/11/20 17:07
 * @since 1.8
 */
public class FaceUtil {
    //设置APPID/AK/SK
    private static final String APP_ID = "14874394";
    private static final String API_KEY = "l9eB71oro2nfCiD7qFmHOhZm";
    private static final String SECRET_KEY = "0w9LIEVYW0NuHNgPHIFanfiXxX9c2lEP";

    public static void main(String[] args) {
        simple();
    }

    /**
     * {
     * "result": {
     * "face_num": 1,           //检测到的图片中的人脸数量
     * "face_list": [{          //人脸信息列表
     * "angle": {             //人脸旋转角度参数
     * "roll": -3.13,       //平面内旋转角[-180(逆时针), 180(顺时针)]
     * "pitch": 11.76,      //三维旋转之俯仰角度[-90(上), 90(下)]
     * "yaw": 6.5           //三维旋转之左右旋转角[-90(左), 90(右)]
     * },
     * "face_token": "469faab9be05d159e1526eef6dd61445",//人脸图片的唯一标识
     * "location": {      //人脸在图片中的位置
     * "top": 88.65,    //人脸区域离上边界的距离
     * "left": 165.95,  //人脸区域离左边界的距离
     * "rotation": -1,  //人脸框相对于竖直方向的顺时针旋转角，[-180,180]
     * "width": 103,    //人脸区域的宽度
     * "height": 98     //人脸区域的高度
     * },
     * "face_probability": 1//人脸置信度，范围【0~1】，代表这是一张人脸的概率，0最小、1最大。
     * }]
     * },
     * "log_id": 304592827068287571,
     * "error_msg": "SUCCESS",
     * "cached": 0,
     * "error_code": 0,
     * "timestamp": 1542706828
     * }
     */
    private static void simple() {
        // 初始化一个AipFace
        AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
//        client.setConnectionTimeoutInMillis(2000);
//        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//        client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 调用接口
        String path = "D:\\project\\java\\domestic\\technical-reserve\\tr-baidu-face\\imgs\\liudehua.png";
        BASE64Encoder encoder = new BASE64Encoder();
        encoder.encode(file2byte(path));
        // BASE64字符串或URL字符串或FACE_TOKEN字符串
        JSONObject res = client.detect(encoder.encode(file2byte(path)), "BASE64", new HashMap<String, String>());
        System.out.println(res.toString(2));
    }

    private static byte[] file2byte(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }
}
