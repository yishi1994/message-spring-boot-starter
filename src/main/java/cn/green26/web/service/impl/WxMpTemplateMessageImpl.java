package cn.green26.web.service.impl;

import cn.green26.web.config.WxProperties;
import cn.green26.web.model.WxMpTemplateMessage;
import cn.green26.web.model.WxMpTemplateMessageSend;
import cn.green26.web.service.IMessage;
import cn.green26.web.utils.WxMpErrorMsgEnum;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class WxMpTemplateMessageImpl implements IMessage<WxMpTemplateMessage, String, String> {
    @Autowired
    private WxProperties wxProperties;

    public static String token;

    public static final List<Integer> ACCESS_TOKEN_ERROR_CODES = Arrays.asList(WxMpErrorMsgEnum.CODE_40001.getCode(),
            WxMpErrorMsgEnum.CODE_40014.getCode(), WxMpErrorMsgEnum.CODE_42001.getCode());

    private String getToken() {
        String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", wxProperties.getAppId(), wxProperties.getSecret());
        String post = HttpUtil.get(url);
        JSONObject jsonObject = JSONObject.parseObject(post);
        token = jsonObject.getString("access_token");
        return token;
    }


    @Override
    public String send(WxMpTemplateMessage message, String toUser) {
        WxMpTemplateMessageSend messageSend = new WxMpTemplateMessageSend();
        BeanUtils.copyProperties(message, messageSend);
        messageSend.setToUser(toUser);
        if (token == null) {
            getToken();
        }
        String body = JSONObject.toJSONString(messageSend);
        String post = HttpUtil.post("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2?access_token=" + token, body);
        JSONObject result = JSONObject.parseObject(post);
        if (ACCESS_TOKEN_ERROR_CODES.contains(result.getInteger("errcode"))) {
            post = HttpUtil.post("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2?access_token=" + getToken(), body);
        }
        return post;
    }

}
