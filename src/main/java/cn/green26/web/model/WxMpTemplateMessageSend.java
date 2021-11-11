package cn.green26.web.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class WxMpTemplateMessageSend extends WxMpTemplateMessage implements Serializable {
    private static final long serialVersionUID = 5063374783759519448L;
    /**
     * 接收者openid.
     */
    private String toUser;

}
