# 创建应用
https://console.bce.baidu.com/ai/?fromai=1#/ai/nlp/overview/index
# 理解与交互技术unit 技能列表
https://ai.baidu.com/unit/v2#/sceneliblist
# 理解与交互技术unit 帮助文档
https://ai.baidu.com/docs#/UNIT-v2-guide/top
https://ai.baidu.com/docs#/UNIT-v2-API/top

#使用其他技能
1.UnitUtil.java里更改自己的APP_ID, API_KEY,SECRET_KEY（百度云--项目管理里有）  
2.UnitUtil.java里foreign()更改第一句传给bot的话，比如我的chat("你好")，不然会导致bot识别不了,返回给用户的第一句话就是识别不了
3.ForeignBot.java里更改技能唯一标识，skill_id和bot_id等同，比如我的DEF_BOT_ID = "17447";  
4.时间词槽校验规则ForeignBot.java里checkData（）  


