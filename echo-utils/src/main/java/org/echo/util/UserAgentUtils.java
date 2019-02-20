package org.echo.util;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 客户端工具
 *
 * @author Liguiqing
 * @since V1.0
 */

public class UserAgentUtils {
    private UserAgentUtils(){
        throw new AssertionError("No org.echo.util.UserAgentUtils instances for you!");
    }

    /**
     * 获取用户代理对象
     * @param request
     * @return
     */
    public static UserAgent getUserAgent(HttpServletRequest request){
        return UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
    }

    /**
     * 获取设备类型
     * @param request
     * @return
     */
    public static DeviceType getDeviceType(HttpServletRequest request){
        return getUserAgent(request).getOperatingSystem().getDeviceType();
    }

    /**
     * 是否是PC
     * @param request
     * @return
     */
    public static boolean isComputer(HttpServletRequest request){
        return DeviceType.COMPUTER.equals(getDeviceType(request));
    }

    /**
     * 是否是手机
     * @param request
     * @return
     */
    public static boolean isMobile(HttpServletRequest request){
        return DeviceType.MOBILE.equals(getDeviceType(request));
    }

    /**
     * 是否是平板
     * @param request
     * @return
     */
    public static boolean isTablet(HttpServletRequest request){
        return DeviceType.TABLET.equals(getDeviceType(request));
    }

    /**
     * 是否是手机和平板
     * @param request
     * @return
     */
    public static boolean isMobileOrTablet(HttpServletRequest request){
        DeviceType deviceType = getDeviceType(request);
        return DeviceType.MOBILE.equals(deviceType) || DeviceType.TABLET.equals(deviceType);
    }

    /**
     * 是否是浏览器访问
     *
     * @param request
     * @return
     */
    public static boolean isBrowser(HttpServletRequest request){
        if(Objects.isNull(request))
            return false;

        return getUserAgent(request).getBrowser() != Browser.UNKNOWN;
    }

    /**
     * 判断是否是微信访问
     *
     * @param request
     * @return
     */
    public static boolean isWeChat(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent").toLowerCase();
        return userAgent != null && userAgent.indexOf("micromessenger") != -1;
    }
}