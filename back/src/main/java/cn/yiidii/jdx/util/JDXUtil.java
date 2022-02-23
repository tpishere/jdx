package cn.yiidii.jdx.util;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import java.util.Arrays;
import lombok.experimental.UtilityClass;

/**
 * JDXUtil
 *
 * @author ed w
 * @since 1.0
 */
@UtilityClass
public class JDXUtil {

    public String getPtPinFromCK(String cookie) {
        cookie = StrUtil.isBlank(cookie) ? "" : ReUtil.replaceAll(cookie, "\\s+", "");
        return Arrays.stream(cookie.split(";"))
                .filter(e -> e.contains("pt_pin"))
                .findFirst().orElse("")
                .split("=")[1];
    }
}
