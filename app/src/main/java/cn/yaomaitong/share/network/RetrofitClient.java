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
package cn.yaomaitong.share.network;

import android.content.Context;
import cn.yaomaitong.share.network.interceptor.UserAgentInterceptor;
import cn.yaomaitong.share.network.utils.DeviceInfo;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/*
* Copyright (C) 2016-2017 Yaomaitong Inc.All Rights Reserved.
* FileName：RetrofitClient
* @Description：简要描述本文件的内容
* History：
* v1.0 danggui 2017/1/19 Create
*/
public class RetrofitClient {
  private static Retrofit retrofit;

  public static Retrofit get(Context context) {
    if (null == retrofit) {
      OkHttpClient.Builder builder = new OkHttpClient.Builder();
      builder.connectTimeout(30, TimeUnit.SECONDS);
      DeviceInfo deviceInfo = new DeviceInfo(context);
      builder.addInterceptor(new UserAgentInterceptor(deviceInfo));
      retrofit = new Retrofit.Builder().baseUrl("https://api.yaomaitong.cn/").build();
    }
    return retrofit;
  }
}