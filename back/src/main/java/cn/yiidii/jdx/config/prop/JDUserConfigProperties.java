package cn.yiidii.jdx.config.prop;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import java.io.File;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * JDInfoConfigProperties
 *
 * @author ed w
 * @since 1.0
 */
@Data
@Slf4j
@Component
public class JDUserConfigProperties implements InitializingBean {

    public static final String JD_USER_CONFIG_FILE_PAH = System.getProperty("user.dir") + File.separator + "config" + File.separator + "JDInfoConfig.json";
    private static boolean INIT = false;


    private String appToken;
    private List<JDUserConfig> JDUsers;

    @PostConstruct
    public void init() {
        INIT = true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        update(true);
    }

    public String update(boolean throwException) {
        try {
            String configStr = FileUtil.readUtf8String(JD_USER_CONFIG_FILE_PAH);
            JSONObject configJo = JSONObject.parseObject(configStr);
            BeanUtil.copyProperties(configJo, this);
            return configJo.toJSONString();
        } catch (Exception e) {
            if (throwException) {
                throw new IllegalArgumentException(StrUtil.format("{}不存在", JD_USER_CONFIG_FILE_PAH));
            }
            log.error("JDUser更新配置文件[{}]发生异常, e: {}", JD_USER_CONFIG_FILE_PAH, e.getMessage());
            return null;
        }
    }

    @Data
    @Slf4j
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JDUserConfig {

        private String ptPin;
        private String wxPusherUid;
    }


    public JDUserConfig getByPtPin(String ptPin) {
        return this.getJDUsers().stream().filter(e -> e.getPtPin().equals(ptPin)).findFirst().orElse(null);
    }

    public void bindWXPusherUid(String ptPin, String wxPusherUid) {
        JDUserConfig jdUserConfig = this.getByPtPin(ptPin);
        if (Objects.nonNull(jdUserConfig)) {
            jdUserConfig.setWxPusherUid(wxPusherUid);
        } else {
            this.getJDUsers().add(new JDUserConfig(ptPin, wxPusherUid));
        }

    }
}
