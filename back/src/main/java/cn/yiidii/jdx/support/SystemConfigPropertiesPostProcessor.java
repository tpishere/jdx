package cn.yiidii.jdx.support;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.yiidii.jdx.config.prop.SystemConfigProperties;
import cn.yiidii.jdx.config.prop.SystemConfigProperties.QLConfig;
import cn.yiidii.jdx.config.prop.SystemConfigProperties.SocialPlatform;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author ed w
 * @since 1.0
 */
@Slf4j
@Component
public class SystemConfigPropertiesPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof SystemConfigProperties) {
            // 检查
            this.checkQLs(((SystemConfigProperties) bean).getQls());
            this.checkSocialPlatforms(((SystemConfigProperties) bean).getSocialPlatforms());
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    private void checkQLs(List<QLConfig> qlConfigs) {
        // 检查QL名称重复
        List<String> duplicateDisplayName = qlConfigs.stream().collect(Collectors.groupingBy(QLConfig::getDisplayName))
                .entrySet().stream().filter(e -> e.getValue().size() > 1).map(e -> e.getKey()).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(duplicateDisplayName)) {
            String msg = StrUtil.format("青龙配置异常, 以下名称出现重复: {}", CollUtil.join(duplicateDisplayName, ","));
            log.debug(msg);
            throw new RuntimeException(msg);
        }
    }

    private void checkSocialPlatforms(List<SocialPlatform> socialPlatforms) {
        // 检查社交配置
        List<String> duplicateDisplayName = socialPlatforms.stream().collect(Collectors.groupingBy(SocialPlatform::getSource))
                .entrySet().stream().filter(e -> e.getValue().size() > 1).map(e -> e.getKey()).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(duplicateDisplayName)) {
            String msg = StrUtil.format("社交登录信息配置异常, 以下平台配置重复: {}", CollUtil.join(duplicateDisplayName, ","));
            log.debug(msg);
            throw new RuntimeException(msg);
        }
    }
}
