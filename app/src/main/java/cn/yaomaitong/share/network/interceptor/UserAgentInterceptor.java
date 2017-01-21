/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 YMT <yaomaitong.cn>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package cn.yaomaitong.share.network.interceptor;

import cn.yaomaitong.share.network.utils.Device;
import cn.yaomaitong.share.network.utils.DeviceInfo;
import cn.yaomaitong.share.network.utils.UserDevice;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/*
* Copyright (C) 2016-2017 Yaomaitong Inc.All Rights Reserved.
* FileName：UserAgentInterceptor
* @Description：简要描述本文件的内容
* History：
* v1.0 danggui 2017/1/21 Create
*/
public class UserAgentInterceptor implements Interceptor {

  private DeviceInfo deviceInfo;

  public UserAgentInterceptor(DeviceInfo deviceInfo) {
    this.deviceInfo = deviceInfo;
  }

  @Override public Response intercept(Chain chain) throws IOException {
    Request originalRequest = chain.request();
    Request.Builder builder = originalRequest.newBuilder();
    //if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
    //  builder.addHeader("Connection", "close");
    //}
    Request requestWithUserAgent = builder.addHeader("Accept", "application/json")
        //header必须url编码否则服务端无法解析
        .addHeader("appVer",
            UserDevice.urlEncoder(deviceInfo.getDeviceInfo(Device.YMT_APP_VERSION)))
        .addHeader("channel",
            UserDevice.urlEncoder(deviceInfo.getDeviceInfo(Device.YMT_APP_CHANNEL)))
        .addHeader("deviceId", deviceInfo.getDeviceInfo(Device.DEVICE_UUID))
        .addHeader("firstInstall", deviceInfo.getDeviceInfo(Device.YMT_APP_INSTALL_TIME))
        .addHeader("lastUpdate", deviceInfo.getDeviceInfo(Device.YMT_APP_LAST_UPDATE))
        .addHeader("model", deviceInfo.getDeviceInfo(Device.DEVICE_HARDWARE_MODEL))
        .addHeader("network", deviceInfo.getDeviceInfo(Device.DEVICE_NETWORK_TYPE))
        .addHeader("operator", UserDevice.urlEncoder(deviceInfo.getDeviceInfo(Device.OPERATOR)))
        .addHeader("os", "android")
        .addHeader("osV", deviceInfo.getDeviceInfo(Device.DEVICE_VERSION_RELEASE))
        .addHeader("resolution", deviceInfo.getDeviceInfo(Device.DEVICE_RESOLUTION))
        .addHeader("User-Agent", UserDevice.buildAgent(deviceInfo))
        .build();
    return chain.proceed(requestWithUserAgent);
  }
}
