package cn.green26.web.service.impl;

import cn.green26.web.config.AliPushProperties;
import cn.green26.web.model.AlibabaPushMessage;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.push.model.v20160801.PushRequest;
import com.aliyuncs.push.model.v20160801.PushResponse;
import com.aliyuncs.utils.ParameterHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import java.util.Date;
import java.util.concurrent.Future;

@Slf4j
public class AlibabaCloudPush {
    private static final PushRequest pushRequest = new PushRequest();
    private final Long appKeyAndroid;
    private final Long appKeyIOS;
    private final DefaultAcsClient client;

    public AlibabaCloudPush(AliPushProperties pushProperties) {
        IClientProfile profile = DefaultProfile.getProfile(pushProperties.getRegionId(), pushProperties.getAccessKeyId(), pushProperties.getAccessKeySecret());
        client = new DefaultAcsClient(profile);
        appKeyAndroid = pushProperties.getAppKeyAndroid();
        appKeyIOS = pushProperties.getAppKeyIOS();
    }

    @Async("threadPoolTaskExecutor")
    public Future<Boolean> push(AlibabaPushMessage alibabaPush, String receiver) {
        // 推送目标
        pushRequest.setTarget("ACCOUNT"); //推送目标: DEVICE:按设备推送 ALIAS : 按别名推送 ACCOUNT:按帐号推送  TAG:按标签推送; ALL: 广播推送
        pushRequest.setTargetValue(receiver); //根据Target来设定，如Target=DEVICE, 则对应的值为 设备id1,设备id2. 多个值使用逗号分隔.(帐号与设备有一次最多100个的限制)

        pushRequest.setPushType("NOTICE"); // 消息类型 MESSAGE NOTICE
        pushRequest.setDeviceType("ALL");// 设备类型 ANDROID iOS ALL.
        // 推送配置
        pushRequest.setTitle(alibabaPush.getTitle()); // 消息的标题
        pushRequest.setBody(alibabaPush.getBody()); // 消息的内容
        // 推送配置: iOS
        pushRequest.setIOSBadge(1); // iOS应用图标右上角角标
        pushRequest.setIOSMusic("default"); // iOS通知声音
        pushRequest.setIOSNotificationCategory("iOS10 Notification Category");//指定iOS10通知Category
        pushRequest.setIOSMutableContent(true);//是否允许扩展iOS通知内容
        pushRequest.setIOSApnsEnv("DEV");//iOS的通知是通过APNs中心来发送的，需要填写对应的环境信息。"DEV" : 表示开发环境 "PRODUCT" : 表示生产环境
        pushRequest.setIOSRemind(true); // 消息推送时设备不在线（既与移动推送的服务端的长连接通道不通），则这条推送会做为通知，通过苹果的APNs通道送达一次。注意：离线消息转通知仅适用于生产环境
        pushRequest.setIOSRemindBody(alibabaPush.getBody());//iOS消息转通知时使用的iOS通知内容，仅当iOSApnsEnv=PRODUCT && iOSRemind为true时有效
        pushRequest.setIOSExtParameters(alibabaPush.getMap()); //通知的扩展属性(注意 : 该参数要以json map的格式传入,否则会解析出错)

        // 推送配置: Android
        pushRequest.setAndroidNotifyType("BOTH");//通知的提醒方式 "VIBRATE" : 震动 "SOUND" : 声音 "BOTH" : 声音和震动 NONE : 静音
        pushRequest.setAndroidNotificationBarType(1);//通知栏自定义样式0-100
        pushRequest.setAndroidNotificationBarPriority(1);//通知栏自定义样式0-100
        pushRequest.setAndroidActivity("com.green.controller.WelcomeActivity"); // 设定通知打开的activity，仅当AndroidOpenType="Activity"有效
        pushRequest.setAndroidMusic("default"); // Android通知音乐
        pushRequest.setAndroidPopupActivity("com.green.controller.WelcomeActivity");
        pushRequest.setAndroidPopupTitle(alibabaPush.getTitle());
        pushRequest.setAndroidPopupBody(alibabaPush.getBody());
        pushRequest.setAndroidExtParameters(alibabaPush.getMap()); //设定通知的扩展属性。(注意 : 该参数要以 json map 的格式传入,否则会解析出错)
        pushRequest.setAndroidNotificationChannel("1");//安卓系统8.0及以上版本，这里加入设置

        String expireTime = ParameterHelper.getISO8601Time(new Date(System.currentTimeMillis() + 12 * 3600 * 1000)); // 12小时后消息失效, 不会再发送
        pushRequest.setExpireTime(expireTime);
        pushRequest.setStoreOffline(true); // 离线消息是否保存,若保存, 在推送时候，用户即使不在线，下一次上线则会收到
        pushRequest.setAndroidRemind(true);
        PushResponse pushResponse;
        try {
            // 推送目标 不同appkey要分别推送
            pushRequest.setAppKey(this.appKeyAndroid);
            client.getAcsResponse(pushRequest);
            pushRequest.setAppKey(this.appKeyIOS);
            pushResponse = client.getAcsResponse(pushRequest);
        } catch (ClientException e) {
            return new AsyncResult<>(Boolean.FALSE);
        }
        log.info("RequestId:{}, MessageID: {}",
                pushResponse.getRequestId(), pushResponse.getMessageId());
        return new AsyncResult<>(Boolean.TRUE);
    }
}