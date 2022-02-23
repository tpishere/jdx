package cn.yiidii.jdx.support;

import cn.yiidii.jdx.config.prop.JDUserConfigProperties;
import cn.yiidii.jdx.config.prop.SystemConfigProperties;
import cn.yiidii.jdx.service.QLService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


/**
 * 定时任务
 *
 * @author ed w
 * @since 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleApplicationRunner implements ApplicationRunner {

    private final QLService qlService;
    private final SystemConfigProperties systemConfigProperties;
    private final JDUserConfigProperties jdUserConfigProperties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        qlService.startTimerTask();
        systemConfigProperties.startTimerTask();
        jdUserConfigProperties.startTimerTask();
    }
}
