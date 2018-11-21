package cn.com.innodev.trbaiduunit;

import org.json.JSONObject;

import java.util.Scanner;
import java.util.UUID;

/**
 * 获取token：https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=3xpzaeCFjgBLgHNvI5He0SCH&client_secret=e5wHgs07wpGyRiV8ZQ3NRXbVLw00nW8O
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @date 2018/11/19 15:57
 * @since 1.8
 */
public class UnitUtil {
    //设置APPID/AK/SK
    private static final String APP_ID = "14859164";
    private static final String API_KEY = "3xpzaeCFjgBLgHNvI5He0SCH";
    private static final String SECRET_KEY = "e5wHgs07wpGyRiV8ZQ3NRXbVLw00nW8O";
    private static final Scanner input = new Scanner(System.in);
    private static final AipNlpExt client = new AipNlpExt(APP_ID, API_KEY, SECRET_KEY);

    public static void main(String[] args) {

//        lexer();
        foreign();
    }

    /**
     * 理解与交互技术unit  foreign
     */
    private static void foreign() {
        ForeignBot bot = new ForeignBot(UUID.randomUUID().toString())
                .initClient(client)
                .chat("预约外币");
        while (!bot.isRequestDone()) {
            System.out.println(bot.getBotSay());
            bot.chat(input.nextLine());
        }
        ForeignBot.Foreign foreign = bot.getForeign();
        System.out.println(bot.getBotSay());
        System.out.println("  " + foreign.getOriginalCurrency() + "（" + foreign.getCurrency() + "）");
        System.out.println("  " + foreign.getOriginalAmount() + "（" + foreign.getAmount() + "）");
        System.out.println("  " + foreign.getOriginalTime() + "（" + foreign.getTime() + "）");
    }

    /**
     * 词法分析
     */
    private static void lexer() {
        // 初始化一个AipNlp
        AipNlpExt client = new AipNlpExt(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//        client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
//        System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        // 调用接口
        String text = "查询理财产品";
        JSONObject res = client.lexer(text, null);
        System.out.println(res.toString(2));
    }
}
