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
package cn.yaomaitong.share;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import cn.jpush.android.api.JPushInterface;
import com.squareup.otto.Bus;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/*
* Copyright (C) 2016-2017 Yaomaitong Inc.All Rights Reserved.
* FileName：YmtApplication
* @Description：简要描述本文件的内容
* History：
* v1.0 danggui 2017/1/20 Create
*/
public class YmtApplication extends Application {
  private static IWXAPI _iwxapi;
  private static Bus bus;
  private static YmtApplication instance;
  private static SharedPreferences sharedPreferences;

  @Override public void onCreate() {
    super.onCreate();
    instance = (YmtApplication) getApplicationContext();
    _iwxapi = WXAPIFactory.createWXAPI(this, "wxb15d14061170c983", false);
    _iwxapi.registerApp("wxb15d14061170c983");
    bus = new Bus();
    JPushInterface.setDebugMode(true);
    JPushInterface.init(getApplicationContext());
    sharedPreferences = getSharedPreferences("ymt-auto-share", Activity.MODE_PRIVATE);
  }

  public static IWXAPI getIwxapi() {
    return _iwxapi;
  }

  public static Bus getBus() {
    return bus;
  }

  public static YmtApplication get() {
    return instance;
  }

  public void setAutoFlag(boolean flag) {
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putBoolean("auto", flag);
    editor.commit();
  }

  public boolean getAutoFlag() {
    return sharedPreferences.getBoolean("auto", false);
  }
}