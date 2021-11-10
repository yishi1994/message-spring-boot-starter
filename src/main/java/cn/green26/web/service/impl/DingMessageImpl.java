package cn.green26.web.service.impl;

import cn.green26.web.config.DingPushProperties;
import cn.green26.web.service.IMessage;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DingMessageImpl implements IMessage<String, String, String> {
    @Autowired
    private DingPushProperties dingPushProperties;

    private String getToken() {
        Map<String, Object> map = new HashMap<>();
        map.put("appKey", dingPushProperties.getAppKey());
        map.put("appSecret", dingPushProperties.getAppSecret());
        String post = HttpUtil.post("https://api.dingtalk.com/v1.0/oauth2/accessToken", JSONObject.toJSONString(map));
        JSONObject jsonObject = JSONObject.parseObject(post);
        return jsonObject.getString("accessToken");
    }

    private JSONObject getUserId(String mobile) {
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", mobile);
        String post = HttpUtil.post("https://oapi.dingtalk.com/topapi/v2/user/getbymobile?access_token=" + getToken(), JSONObject.toJSONString(map));
        return JSONObject.parseObject(post);
    }


    @Override
    public String send(String message, String mobile) {
        final JSONObject user = getUserId(mobile);
        if (!user.getString("errmsg").equals("ok")) {
            return user.toJSONString();
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("agent_id", dingPushProperties.getAgentId());
        jsonObject.put("userid_list", JSONObject.parseObject(user.getString("result")).getString("userid"));
        JSONObject msg = new JSONObject();
        msg.put("msgtype", "text");
        JSONObject text = new JSONObject();
        text.put("content", message);
        msg.put("text", text);
        jsonObject.put("msg", msg);
        return HttpUtil.post("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2?access_token=" + getToken(), jsonObject);
    }
}
