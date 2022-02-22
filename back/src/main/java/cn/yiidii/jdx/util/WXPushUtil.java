package cn.yiidii.jdx.util;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import java.util.List;
import lombok.experimental.UtilityClass;

/**
 * WXPushUtil
 *
 * @author ed w
 * @since 1.0
 */
@UtilityClass
public class WXPushUtil {

    private static final String PUSH_URL = "http://wxpusher.zjiecode.com/api/send/message";

    public void send(String appToken, List<String> uids, String title, String content, String contentType) {
        JSONObject reqParamJo = new JSONObject();
        reqParamJo.put("appToken", appToken);
        reqParamJo.put("content", content);
        reqParamJo.put("summary", title);
        reqParamJo.put("contentType", contentType);
        reqParamJo.put("uids", uids);
        HttpRequest.post(PUSH_URL)
                .body(reqParamJo.toJSONString())
                .execute();
    }

}
