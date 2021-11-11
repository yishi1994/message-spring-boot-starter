package cn.green26.web.service;

import cn.green26.web.service.impl.DingMessageImpl;

public class Test {
    public static void main(String[] args) throws Exception {
        DingMessageImpl dingMessage = new DingMessageImpl();
        final String post = dingMessage.send("nihao", "13309683922");

        System.out.println(post);
    }
}
