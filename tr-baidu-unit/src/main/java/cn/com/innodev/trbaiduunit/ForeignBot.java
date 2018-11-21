package cn.com.innodev.trbaiduunit;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @date 2018/11/20 15:25
 * @since 1.8
 */
public class ForeignBot {
    private static final String DEF_BOT_ID = "17115";

    /**
     * 请求aip的客户端工具
     */
    private AipNlpExt client;

    /**
     * 与BOT对话的用户id（如果BOT客户端是用户未登录状态情况下对话的，也需要尽量通过其他标识（比如设备id）来唯一区分用户），
     * 方便今后在平台的日志分析模块定位分析问题、从用户维度统计分析相关对话情况
     */
    private Object userId;

    /**
     * 技能唯一标识，在『我的技能』的技能列表中的技能ID
     */
    private String botId;
    /**
     * BOT的session信息，由BOT创建，client从上轮resonpse中取出并直接传递，不需要了解其内容。
     * 如果为空，则表示清空session（开发者判断用户意图已经切换且下一轮会话不需要继承上一轮会话中的词槽信息时可以把session置空，
     * 从而进行新一轮的会话）。传参时可只传session_id。
     */
    private String botSession = "";

    /**
     * 上一次会话时的session
     */
    private String prevBotSession;
    /**
     * 系统自动发现不置信意图/词槽，并据此主动发起澄清确认的频率。
     * 取值范围：0(关闭)、1(低频)、2(高频)。取值越高代表BOT对不置信意图/词槽的敏感度就越高，建议值为1
     */
    private int bernardLevel = 1;

    /**
     * 参数集合
     */
    private HashMap<String, Object> options = new HashMap<>();
    /**
     * 本轮请求体
     */
    private HashMap<String, Object> request = new HashMap<>();
    /**
     * 本轮请求query的附加信息
     */
    private HashMap<String, Object> queryInfo = new HashMap<>();

    /**
     * 请求完成
     */
    private boolean requestDone = false;

    /**
     * 预约成功后的外汇信息
     */
    private Foreign foreign;

    /**
     * bot要说的话
     */
    private String botSay;

    public ForeignBot(String botId, Object userId) {
        this.botId = botId;
        this.userId = userId;
    }

    public ForeignBot(Object userId) {
        this(DEF_BOT_ID, userId);
    }

    public String getBotSay() {
        return botSay;
    }

    public Foreign getForeign() {
        return foreign;
    }

    public ForeignBot initClient(AipNlpExt client) {
        this.client = client;
        return this;
    }

    public ForeignBot chat(String text) {
        this.checkClient();

        // 请求参数
        options.put("bot_id", this.botId);
        options.put("bot_session", this.botSession);
        options.put("request", request);

        // 本次请求体
        request.put("bernard_level", this.bernardLevel);
        request.put("client_session", "{\"client_results\":\"\", \"candidate_options\":[]}");
        request.put("query", text);
        request.put("user_id", this.userId);
        request.put("query_info", queryInfo);

        // 本轮请求query的附加信息
        queryInfo.put("type", "TEXT");
        queryInfo.put("source", "ASR");

        JSONObject res = this.client.chat(options);
        this.checkResponse(res);

        JSONObject result = res.getJSONObject("result");
        JSONObject response = result.getJSONObject("response");
        String curBotSession = result.getString("bot_session");
        JSONObject action = response.getJSONArray("action_list").getJSONObject(0);
        this.botSay = action.getString("say");
        if (action.getString("type").equals("failure")){
            // 调用失败了， bot没有正确理解用户的意图
            this.requestDone = false;
            this.resetSession(this.botSession);
        } else if (!action.getString("type").equals("satisfy")) {
            this.resetSession(curBotSession);
        } else {
            JSONObject schema = response.getJSONObject("schema");
            JSONArray slots = schema.getJSONArray("slots");
            this.requestDone = true;
            try {
                this.foreign = new Foreign(slots);
            } catch (DateTimeParseException e) {
                // 还原回上一次的会话
                this.requestDone = false;
                this.botSay = "我的天呐~~这个日期好像不对吧？请重新指定一个日期";
                this.resetSession(this.botSession);
            }
        }
        return this;
    }

    private void resetSession(String curSession) {
        this.prevBotSession = this.botSession;
        this.botSession = curSession;
    }

    private void checkClient() {
        if (client == null) {
            throw new RuntimeException("未初始化client");
        }
    }

    private void checkResponse(JSONObject res) {
        if (res == null || res.getInt("error_code") != 0 || !res.getString("error_msg").equals("ok")) {
            throw new RuntimeException("请求接口发生异常");
        }
    }

    private String checkData(String date) {
        if (date.contains("|")) {
            date = date.replace("|", " ");
            LocalDateTime localDateTime = LocalDateTime.parse(date);
            if (localDateTime.isBefore(LocalDateTime.now())) {
                throw new DateTimeParseException("Invalid date", "Invalid date", 0);
            }
        } else {
            LocalDate localDate = LocalDate.parse(date);
            if (localDate.isBefore(LocalDate.now())) {
                throw new DateTimeParseException("Invalid date", "Invalid date", 0);
            }
        }
        return date;
    }

    public boolean isRequestDone() {
        return requestDone;
    }

    public class Foreign {
        private String currency;
        private String amount;
        private String time;
        private String originalCurrency;
        private String originalAmount;
        private String originalTime;

        public Foreign(JSONArray slots) {
            if (null == slots || slots.length() == 0) {
                return;
            }
            for (int i = 0, len = slots.length(); i < len; i++) {
                JSONObject slot = slots.getJSONObject(i);
                String name = slot.getString("name"),
                        originalWord = slot.getString("original_word"),
                        normalizedWord = slot.getString("normalized_word");
                switch (name) {
                    case "user_amount":
                        this.amount = normalizedWord;
                        this.originalAmount = originalWord;
                        break;
                    case "user_currency":
                        this.currency = normalizedWord;
                        this.originalCurrency = originalWord;
                        break;
                    case "user_time":
                        this.time = checkData(normalizedWord);
                        this.originalTime = originalWord;
                        break;
                }
            }
        }

        public String getCurrency() {
            return currency;
        }

        public String getAmount() {
            return amount;
        }

        public String getTime() {
            return time;
        }

        public String getOriginalCurrency() {
            return originalCurrency;
        }

        public String getOriginalAmount() {
            return originalAmount;
        }

        public String getOriginalTime() {
            return originalTime;
        }
    }
}
