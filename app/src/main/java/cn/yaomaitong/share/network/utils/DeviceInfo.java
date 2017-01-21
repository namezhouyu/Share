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

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.Key;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/*
* Copyright (C) 2016-2017 Yaomaitong Inc.All Rights Reserved.
* FileName：DeviceInfo
* @Description：简要描述本文件的内容
* History：
* v1.0 danggui 2017/1/21 Create
*/
public class DeviceInfo {

  private Context context;
  private static final String CHARSET_NAME = "UTF-8";
  private static final String SIG = "MCPT";
  private static final String VERSION_1_1 = "1.1";
  private static final byte[] IV = new byte[] { 1, 3, 1, 4, 5, 2, 0, 1 };

  public DeviceInfo(Context context) {
    this.context = context;
  }

  public String getDeviceInfo(Device device) {
    try {
      switch (device) {
        case DEVICE_LANGUAGE:
          return Locale.getDefault().getDisplayLanguage();
        case DEVICE_TIME_ZONE:
          return TimeZone.getDefault().getID();//(false, TimeZone.SHORT);
        case DEVICE_LOCAL_COUNTRY_CODE:
          return context.getResources().getConfiguration().locale.getCountry();
        case DEVICE_CURRENT_YEAR:
          return "" + (Calendar.getInstance().get(Calendar.YEAR));
        case DEVICE_CURRENT_DATE_TIME:
          Calendar calendarTime = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
          long time = (calendarTime.getTimeInMillis() / 1000);
          return String.valueOf(time);
        //                    return DateFormat.getDateTimeInstance().format(new Date());
        case DEVICE_CURRENT_DATE_TIME_ZERO_GMT:
          Calendar calendarTime_zero =
              Calendar.getInstance(TimeZone.getTimeZone("GMT+0"), Locale.getDefault());
          return String.valueOf((calendarTime_zero.getTimeInMillis() / 1000));
        //                    DateFormat df = DateFormat.getDateTimeInstance();
        //                    df.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        //                    return df.format(new Date());
        case DEVICE_HARDWARE_MODEL:
          return getDeviceName();
        case DEVICE_NUMBER_OF_PROCESSORS:
          return Runtime.getRuntime().availableProcessors() + "";
        case DEVICE_LOCALE:
          return Locale.getDefault().getISO3Country();
        case DEVICE_IP_ADDRESS_IPV4:
          return getIPAddress(true);
        case DEVICE_IP_ADDRESS_IPV6:
          return getIPAddress(false);
        case DEVICE_MAC_ADDRESS:
          String mac = getMACAddress("wlan0");
          if (TextUtils.isEmpty(mac)) {
            mac = getMACAddress("eth0");
          }
          if (TextUtils.isEmpty(mac)) {
            mac = "DU:MM:YA:DD:RE:SS";
          }
          return mac;
        case DEVICE_TOTAL_MEMORY:
          if (Build.VERSION.SDK_INT >= 16) {
            return String.valueOf(getTotalMemory(context));
          } else {
            return "";
          }
        case DEVICE_FREE_MEMORY:
          return String.valueOf(getFreeMemory(context));
        case DEVICE_USED_MEMORY:
          if (Build.VERSION.SDK_INT >= 16) {
            long freeMem = getTotalMemory(context) - getFreeMemory(context);
            return String.valueOf(freeMem);
          }
          return "";
        case DEVICE_TOTAL_CPU_USAGE:
          int[] cpu = getCpuUsageStatistic();
          if (cpu != null) {
            int total = cpu[0] + cpu[1] + cpu[2] + cpu[3];
            return String.valueOf(total);
          }
          return "";
        case DEVICE_TOTAL_CPU_USAGE_SYSTEM:
          int[] cpu_sys = getCpuUsageStatistic();
          if (cpu_sys != null) {
            int total = cpu_sys[1];
            return String.valueOf(total);
          }
          return "";
        case DEVICE_TOTAL_CPU_USAGE_USER:
          int[] cpu_usage = getCpuUsageStatistic();
          if (cpu_usage != null) {
            int total = cpu_usage[0];
            return String.valueOf(total);
          }
          return "";
        case DEVICE_MANUFACTURE:
          return android.os.Build.MANUFACTURER;
        case DEVICE_SYSTEM_VERSION:
          return String.valueOf(getDeviceName());
        case DEVICE_VERSION:
          return String.valueOf(android.os.Build.VERSION.SDK_INT);
        case DEVICE_VERSION_RELEASE:
          return String.valueOf(Build.VERSION.RELEASE);
        case DEVICE_IN_INCH:
          return getDeviceInch(context);
        case DEVICE_TOTAL_CPU_IDLE:
          int[] cpu_idle = getCpuUsageStatistic();
          if (cpu_idle != null) {
            int total = cpu_idle[2];
            return String.valueOf(total);
          }
          return "";
        case DEVICE_NETWORK_TYPE:
          return getNetworkType(context);
        case DEVICE_NETWORK:
          return checkNetworkStatus(context);
        case DEVICE_TYPE:
          if (isTablet(context)) {
            if (getDeviceMoreThan5Inch(context)) {
              return "Tablet";
            } else {
              return "Mobile";
            }
          } else {
            return "Mobile";
          }
        case DEVICE_UUID:
          return getDeviceId(context);
        case DEVICE_NETWORK_SSID:
          return getCurrentSsid(context);
        case YMT_APP_VERSION:
          return getYmtVersion(context);
        case OPERATOR:
          return getOperator(context);
        case DEVICE_RESOLUTION:
          return getResolution(context);
        case YMT_APP_INSTALL_TIME:
          return long2DateString(getYmtInstallTime(context));
        case YMT_APP_LAST_UPDATE:
          return long2DateString(getYmtUpdateTime(context));
        case YMT_APP_CHANNEL:
          return getYmtChannelId(context, "Ymt123654", "yingyongbao");
        default:
          break;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return "";
  }

  public static String getDeviceId(Context context) {
    String device_uuid =
        Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    if (device_uuid == null) {
      device_uuid = "12356789"; // for emulator testing
    } else {
      try {
        byte[] _data = device_uuid.getBytes();
        MessageDigest _digest = java.security.MessageDigest.getInstance("MD5");
        _digest.update(_data);
        _data = _digest.digest();
        BigInteger _bi = new BigInteger(_data).abs();
        device_uuid = _bi.toString(36);
      } catch (Exception e) {
        if (e != null) {
          e.printStackTrace();
        }
      }
    }
    return device_uuid;
  }

  @SuppressLint("NewApi") private static long getTotalMemory(Context activity) {
    try {
      ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
      ActivityManager activityManager =
          (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
      activityManager.getMemoryInfo(mi);
      long availableMegs = mi.totalMem / 1048576L; // in megabyte (mb)

      return availableMegs;
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  private static long getFreeMemory(Context activity) {
    try {
      ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
      ActivityManager activityManager =
          (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
      activityManager.getMemoryInfo(mi);
      long availableMegs = mi.availMem / 1048576L; // in megabyte (mb)

      return availableMegs;
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  private static String getDeviceName() {
    String manufacturer = Build.MANUFACTURER;
    String model = Build.MODEL;
    if (model.startsWith(manufacturer)) {
      return capitalize(model);
    } else {
      return capitalize(manufacturer) + " " + model;
    }
  }

  private static String capitalize(String s) {
    if (s == null || s.length() == 0) {
      return "";
    }
    char first = s.charAt(0);
    if (Character.isUpperCase(first)) {
      return s;
    } else {
      return Character.toUpperCase(first) + s.substring(1);
    }
  }

  /**
   * Convert byte array to hex string
   */
  private static String bytesToHex(byte[] bytes) {
    StringBuilder sbuf = new StringBuilder();
    for (int idx = 0; idx < bytes.length; idx++) {
      int intVal = bytes[idx] & 0xff;
      if (intVal < 0x10) sbuf.append("0");
      sbuf.append(Integer.toHexString(intVal).toUpperCase());
    }
    return sbuf.toString();
  }

  /**
   * Returns MAC address of the given interface name.
   *
   * @param interfaceName eth0, wlan0 or NULL=use first interface
   * @return mac address or empty string
   */
  @SuppressLint("NewApi") private static String getMACAddress(String interfaceName) {
    try {

      List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
      for (NetworkInterface intf : interfaces) {
        if (interfaceName != null) {
          if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
        }
        byte[] mac = intf.getHardwareAddress();
        if (mac == null) return "";
        StringBuilder buf = new StringBuilder();
        for (int idx = 0; idx < mac.length; idx++)
          buf.append(String.format("%02X:", mac[idx]));
        if (buf.length() > 0) buf.deleteCharAt(buf.length() - 1);
        return buf.toString();
      }
    } catch (Exception ex) {
      return "";
    } // for now eat exceptions
    return "";
            /*
             * try { // this is so Linux hack return
             * loadFileAsString("/sys/class/net/" +interfaceName +
             * "/address").toUpperCase().trim(); } catch (IOException ex) { return
             * null; }
             */
  }

  /**
   * Get IP address from first non-localhost interface
   *
   * @return address or empty string
   */
  private static String getIPAddress(boolean useIPv4) {
    try {
      List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
      for (NetworkInterface intf : interfaces) {
        List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
        for (InetAddress addr : addrs) {
          if (!addr.isLoopbackAddress()) {
            String sAddr = addr.getHostAddress().toUpperCase();
            boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
            if (useIPv4) {
              if (isIPv4) return sAddr;
            } else {
              if (!isIPv4) {
                int delim = sAddr.indexOf('%'); // drop ip6 port
                // suffix
                return delim < 0 ? sAddr : sAddr.substring(0, delim);
              }
            }
          }
        }
      }
    } catch (Exception ex) {
    } // for now eat exceptions
    return "";
  }

  /*
   *
   * @return integer Array with 4 elements: user, system, idle and other cpu
   * usage in percentage.
   */
  private static int[] getCpuUsageStatistic() {
    try {
      String tempString = executeTop();

      tempString = tempString.replaceAll(",", "");
      tempString = tempString.replaceAll("User", "");
      tempString = tempString.replaceAll("System", "");
      tempString = tempString.replaceAll("IOW", "");
      tempString = tempString.replaceAll("IRQ", "");
      tempString = tempString.replaceAll("%", "");
      for (int i = 0; i < 10; i++) {
        tempString = tempString.replaceAll("  ", " ");
      }
      tempString = tempString.trim();
      String[] myString = tempString.split(" ");
      int[] cpuUsageAsInt = new int[myString.length];
      for (int i = 0; i < myString.length; i++) {
        myString[i] = myString[i].trim();
        cpuUsageAsInt[i] = Integer.parseInt(myString[i]);
      }
      return cpuUsageAsInt;
    } catch (Exception e) {
      e.printStackTrace();
      Log.e("executeTop", "error in getting cpu statics");
      return null;
    }
  }

  private static String executeTop() {
    java.lang.Process p = null;
    BufferedReader in = null;
    String returnString = null;
    try {
      p = Runtime.getRuntime().exec("top -n 1");
      in = new BufferedReader(new InputStreamReader(p.getInputStream()));
      while (returnString == null || returnString.contentEquals("")) {
        returnString = in.readLine();
      }
    } catch (IOException e) {
      Log.e("executeTop", "error in getting first line of top");
      e.printStackTrace();
    } finally {
      try {
        in.close();
        p.destroy();
      } catch (IOException e) {
        Log.e("executeTop", "error in closing and destroying top process");
        e.printStackTrace();
      }
    }
    return returnString;
  }

  public static String getNetworkType(final Context activity) {
    String networkStatus = "";
    final ConnectivityManager connMgr =
        (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
    // check for wifi
    final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    // check for mobile data
    final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

    if (wifi.isAvailable()) {
      networkStatus = "Wifi";
    } else if (mobile.isAvailable()) {
      networkStatus = getDataType(activity);
    } else {
      networkStatus = "noNetwork";
    }
    return networkStatus;
  }

  public static String checkNetworkStatus(final Context activity) {
    String networkStatus = "";
    try {
      // Get connect mangaer
      final ConnectivityManager connMgr =
          (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
      // // check for wifi
      final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
      // // check for mobile data
      final android.net.NetworkInfo mobile =
          connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

      if (wifi.isAvailable()) {
        networkStatus = "Wifi";
      } else if (mobile.isAvailable()) {
        networkStatus = getDataType(activity);
      } else {
        networkStatus = "noNetwork";
        networkStatus = "0";
      }
    } catch (Exception e) {
      e.printStackTrace();
      networkStatus = "0";
    }
    return networkStatus;
  }

  public static boolean isTablet(Context context) {
    return (context.getResources().getConfiguration().screenLayout
        & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
  }

  public static boolean getDeviceMoreThan5Inch(Context activity) {
    try {
      DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
      // int width = displayMetrics.widthPixels;
      // int height = displayMetrics.heightPixels;

      float yInches = displayMetrics.heightPixels / displayMetrics.ydpi;
      float xInches = displayMetrics.widthPixels / displayMetrics.xdpi;
      double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);
      if (diagonalInches >= 7) {
        // 5inch device or bigger
        return true;
      } else {
        // smaller device
        return false;
      }
    } catch (Exception e) {
      return false;
    }
  }

  public static String getDeviceInch(Context activity) {
    try {
      DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();

      float yInches = displayMetrics.heightPixels / displayMetrics.ydpi;
      float xInches = displayMetrics.widthPixels / displayMetrics.xdpi;
      double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);
      return String.valueOf(diagonalInches);
    } catch (Exception e) {
      return "-1";
    }
  }

  public static String getDataType(Context activity) {
    String type = "Mobile Data";
    TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
    switch (tm.getNetworkType()) {
      case TelephonyManager.NETWORK_TYPE_HSDPA:
        type = "Mobile Data 3G";
        Log.d("Type", "3g");
        // for 3g HSDPA networktype will be return as
        // per testing(real) in device with 3g enable
        // data
        // and speed will also matters to decide 3g network type
        break;
      case TelephonyManager.NETWORK_TYPE_HSPAP:
        type = "Mobile Data 4G";
        Log.d("Type", "4g");
        // No specification for the 4g but from wiki
        // i found(HSPAP used in 4g)
        break;
      case TelephonyManager.NETWORK_TYPE_GPRS:
        type = "Mobile Data GPRS";
        Log.d("Type", "GPRS");
        break;
      case TelephonyManager.NETWORK_TYPE_EDGE:
        type = "Mobile Data EDGE 2G";
        Log.d("Type", "EDGE 2g");
        break;
    }

    return type;
  }

  public static String getCurrentSsid(Context context) {
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_WIFI_STATE)
        == PackageManager.PERMISSION_GRANTED) {
      String ssid = null;
      ConnectivityManager connManager =
          (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
      if (networkInfo.isConnected()) {
        final WifiManager wifiManager =
            (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
          ssid = connectionInfo.getSSID();
        }
      }
      return ssid;
    } else {
      return "";
    }
  }

  public static String getYmtVersion(Context context) {
    try {
      PackageManager manager = context.getPackageManager();
      PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
      return info.versionName;
    } catch (Exception e) {
      e.printStackTrace();
      return "未知版本";
    }
  }

  public static String getOperator(Context context) {
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
        == PackageManager.PERMISSION_GRANTED) {
      TelephonyManager telManager =
          (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
      String imsi = telManager.getSubscriberId();
      if (imsi != null) {
        if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
          return "中国移动";
        } else if (imsi.startsWith("46001")) {
          return "中国联通";
        } else if (imsi.startsWith("46003")) {
          return "中国电信";
        }
      }
    }
    return "";
  }

  public static String getResolution(Context context) {
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();
    return display.getWidth() + "x" + display.getHeight();
  }

  public static long getYmtUpdateTime(Context context) {
    long time = 0;
    try {
      PackageManager pm = context.getPackageManager();
      ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), 0);
      String appFile = appInfo.sourceDir;
      time = new File(appFile).lastModified();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return time;
  }

  public static long getYmtInstallTime(Context context) {
    long time = 0;
    PackageManager packageManager = context.getPackageManager();
    try {
      PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
      time = packageInfo.firstInstallTime;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return time;
  }

  public static String getYmtChannelId(Object context, String mcptoolPassword, String defValue) {
    String content = readContent(new File(getPackageCodePath(context)), mcptoolPassword);
    return content == null || content.length() == 0 ? defValue : content;
  }

  public static String readContent(File path, String password) {
    try {
      return new String(read(path, password), CHARSET_NAME);
    } catch (Exception ignore) {
    }
    return null;
  }

  private static String getPackageCodePath(Object context) {
    try {
      return (String) context.getClass().getMethod("getPackageCodePath").invoke(context);
    } catch (Exception ignore) {
    }
    return null;
  }

  private static byte[] read(File path, String password) throws Exception {
    byte[] bytesContent = null;
    byte[] bytesMagic = SIG.getBytes(CHARSET_NAME);
    byte[] bytes = new byte[bytesMagic.length];
    RandomAccessFile raf = new RandomAccessFile(path, "r");
    Object[] versions = getVersion(raf);
    long index = (long) versions[0];
    String version = (String) versions[1];
    if (VERSION_1_1.equals(version)) {
      bytes = new byte[1];
      index -= bytes.length;
      readFully(raf, index, bytes); // 读取内容长度；
      boolean isEncrypt = bytes[0] == 1;

      bytes = new byte[2];
      index -= bytes.length;
      readFully(raf, index, bytes); // 读取内容长度；
      int lengthContent = stream2Short(bytes, 0);

      bytesContent = new byte[lengthContent];
      index -= lengthContent;
      readFully(raf, index, bytesContent); // 读取内容；

      if (isEncrypt && password != null && password.length() > 0) {
        bytesContent = decrypt(password, bytesContent);
      }
    }
    raf.close();
    return bytesContent;
  }

  private static Object[] getVersion(RandomAccessFile raf) throws IOException {
    String version = null;
    byte[] bytesMagic = SIG.getBytes(CHARSET_NAME);
    byte[] bytes = new byte[bytesMagic.length];
    long index = raf.length();
    index -= bytesMagic.length;
    readFully(raf, index, bytes); // 读取SIG标记；
    if (Arrays.equals(bytes, bytesMagic)) {
      bytes = new byte[2];
      index -= bytes.length;
      readFully(raf, index, bytes); // 读取版本号长度；
      int lengthVersion = stream2Short(bytes, 0);
      index -= lengthVersion;
      byte[] bytesVersion = new byte[lengthVersion];
      readFully(raf, index, bytesVersion); // 读取内容；
      version = new String(bytesVersion, CHARSET_NAME);
    }
    return new Object[] { index, version };
  }

  private static void readFully(RandomAccessFile raf, long index, byte[] buffer)
      throws IOException {
    raf.seek(index);
    raf.readFully(buffer);
  }

  private static short stream2Short(byte[] stream, int offset) {
    ByteBuffer buffer = ByteBuffer.allocate(2);
    buffer.order(ByteOrder.LITTLE_ENDIAN);
    buffer.put(stream[offset]);
    buffer.put(stream[offset + 1]);
    return buffer.getShort(0);
  }

  private static byte[] decrypt(String password, byte[] content) throws Exception {
    return cipher(Cipher.DECRYPT_MODE, password, content);
  }

  private static byte[] cipher(int cipherMode, String password, byte[] content) throws Exception {
    DESKeySpec dks = new DESKeySpec(password.getBytes(CHARSET_NAME));
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
    Key secretKey = keyFactory.generateSecret(dks);
    Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
    IvParameterSpec spec = new IvParameterSpec(IV);
    cipher.init(cipherMode, secretKey, spec);
    return cipher.doFinal(content);
  }

  private static String long2DateString(long date) {
    if (date == 0) {
      return "";
    }
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return sdf.format(new Long(date));
  }
}