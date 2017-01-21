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
package cn.yaomaitong.share.service;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import cn.yaomaitong.share.YmtApplication;
import java.util.List;

/*
* Copyright (C) 2016-2017 yaomaitong Inc.All Rights Reserved.
* FileName：ClickService
* @Description：简要描述本文件的内容
* History：
* v1.0 tianlong 17/1/6 Create
*/
public class ClickService extends AccessibilityService {

  private void performClick() {
    AccessibilityNodeInfo nodeInfo = null;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      nodeInfo = this.getRootInActiveWindow();
    }
    if (null != nodeInfo) {
      AccessibilityNodeInfo targetNode;
      targetNode = findNodeInfosById(nodeInfo, "com.tencent.mm:id/fb");
      if (targetNode == null) {
        targetNode = findNodeInfosById(nodeInfo, "com.tencent.mm:id/g_");
      }
      if (null != targetNode && targetNode.isClickable()) {
        targetNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
      }
    }
  }

  //调用兵力（通过id查找）
  public static AccessibilityNodeInfo findNodeInfosById(AccessibilityNodeInfo nodeInfo,
      String resId) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
      List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(resId);
      if (list != null && !list.isEmpty()) {
        return list.get(0);
      }
    }
    return null;
  }

  //调用船只（通过文本查找）
  public static AccessibilityNodeInfo findNodeInfosByText(AccessibilityNodeInfo nodeInfo,
      String text) {
    List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(text);
    if (list == null || list.isEmpty()) {
      return null;
    }
    return list.get(0);
  }

  @Override public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
    if (YmtApplication.get().getAutoFlag() && accessibilityEvent.getSource() != null) {
      performClick();
    }
  }

  @Override public void onInterrupt() {

  }
}
