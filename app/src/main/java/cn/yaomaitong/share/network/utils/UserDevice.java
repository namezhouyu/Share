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
package cn.yaomaitong.share.network.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/*
* Copyright (C) 2016-2017 Yaomaitong Inc.All Rights Reserved.
* FileName：UserDevice
* @Description：简要描述本文件的内容
* History：
* v1.0 danggui 2017/1/21 Create
*/
public class UserDevice {
  public static String buildAgent(DeviceInfo deviceInfo) throws IOException {
    String agentInfo = "app4Android/";
    agentInfo += deviceInfo.getDeviceInfo(Device.YMT_APP_VERSION);
    agentInfo += "(";
    agentInfo += deviceInfo.getDeviceInfo(Device.DEVICE_HARDWARE_MODEL);
    agentInfo += "; ";
    agentInfo += "Android " + deviceInfo.getDeviceInfo(Device.DEVICE_VERSION_RELEASE);
    agentInfo += ")";
    return agentInfo;
  }

  /**
   * encode msg,页面上显示使用
   *
   * @throws UnsupportedEncodingException
   */
  public static String urlEncoder(String message) {
    message = message.replaceAll("%", "%25");
    message = message.replaceAll(" ", "%20");
    message = message.replaceAll("\\+", "%2B");
    message = message.replaceAll("/", "%2F");
    message = message.replaceAll("\\?", "%3F");
    message = message.replaceAll("#", "%23");
    message = message.replaceAll("&", "%26");
    message = message.replaceAll("=", "%3D");
    try {
      return URLEncoder.encode(message, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return "";
  }

  public static String urlEncode(String s, String charset) {
    try {
      return URLEncoder.encode(s, charset);
    } catch (UnsupportedEncodingException e) {
      throw new UnsupportedOperationException(e);
    }
  }
}