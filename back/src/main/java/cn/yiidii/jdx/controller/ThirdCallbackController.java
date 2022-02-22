package cn.yiidii.jdx.controller;

import cn.hutool.core.util.StrUtil;
import cn.yiidii.jdx.config.prop.JDUserConfigProperties;
import cn.yiidii.jdx.config.prop.JDUserConfigProperties.JDUserConfig;
import cn.yiidii.jdx.model.R;
import cn.yiidii.jdx.util.WXPushUtil;
import com.alibaba.fastjson.JSONObject;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ThirdCallbackController
 *
 * @author ed w
 * @since 1.0
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/third")
@RequiredArgsConstructor
public class ThirdCallbackController {

    private final JDUserConfigProperties jdUserConfigProperties;

    /**
     * wxPusher订阅回调，可用于给管理员推送
     *
     * @param paramJo 参数
     * @return R
     */
    @PostMapping("/wxPusher/follow/callback")
    public R<?> aa(@RequestBody JSONObject paramJo) {
        String appSubscribe = paramJo.getString("action");
        if (StrUtil.equals("app_subscribe", appSubscribe)) {

        }
        return R.ok();
    }

    /**
     * 青龙任务执行后的通知，来自sendNotify.js
     *
     * @param paramJo 参数
     * @return R
     */
    @PostMapping("/qlNotify")
    public R<?> qlNotify(@RequestBody JSONObject paramJo) {
        try {
            this.handle(paramJo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok();
    }

    private void handle(JSONObject paramJo) {
        String text = paramJo.getString("text");
        String desp = paramJo.getString("desp");
        switch (text) {
            case "京东资产变动通知": {
                List<String> userContentList = Arrays.stream(desp.split("\\n\\n\\n\\n")).collect(Collectors.toList()).stream().filter(s -> StrUtil.isNotBlank(s) && !s.contains("本通知")).collect(Collectors.toList());
                Map<String, String> userContentMap = userContentList.stream().collect(Collectors.toMap(e -> {
                    String s = e.split("\\n\\n")[1];
                    return s.replaceAll("账号名称：", "");
                }, e -> e, (e1, e2) -> e2));

                for (Entry<String, String> entry : userContentMap.entrySet()) {
                    String k = entry.getKey();
                    String v = entry.getValue();
                    JDUserConfig byPtPin = jdUserConfigProperties.getByPtPin(k);
                    if (Objects.isNull(byPtPin)) {
                        continue;
                    }
                    WXPushUtil.send(jdUserConfigProperties.getAppToken(), Arrays.asList(byPtPin.getWxPusherUid()), text, v, "1");
                }
                break;
            }
            default: {
                return;
            }
        }
    }

}
