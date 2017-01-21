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

import java.util.regex.Pattern;

/*
* Copyright (C) 2016-2017 Yaomaitong Inc.All Rights Reserved.
* FileName：InetAddressUtils
* @Description：简要描述本文件的内容
* History：
* v1.0 danggui 2017/1/21 Create
*/
public class InetAddressUtils {

  private InetAddressUtils() {
  }

  private static final String IPV4_BASIC_PATTERN_STRING =
      "(([1-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){1}" +
          // initial first field, 1-255
          "(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){2}" +
          // following 2 fields, 0-255 followed by .
          "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])"; // final field, 0-255

  private static final Pattern IPV4_PATTERN =
      Pattern.compile("^" + IPV4_BASIC_PATTERN_STRING + "$");

  private static final Pattern IPV4_MAPPED_IPV6_PATTERN =
      // TODO does not allow for redundant leading zeros
      Pattern.compile("^::[fF]{4}:" + IPV4_BASIC_PATTERN_STRING + "$");

  private static final Pattern IPV6_STD_PATTERN =
      Pattern.compile("^[0-9a-fA-F]{1,4}(:[0-9a-fA-F]{1,4}){7}$");

  private static final Pattern IPV6_HEX_COMPRESSED_PATTERN =
      Pattern.compile("^(([0-9A-Fa-f]{1,4}(:[0-9A-Fa-f]{1,4}){0,5})?)" + // 0-6 hex fields
          "::" +
          "(([0-9A-Fa-f]{1,4}(:[0-9A-Fa-f]{1,4}){0,5})?)$"); // 0-6 hex fields

  /*
   *  The above pattern is not totally rigorous as it allows for more than 7 hex fields in total
   */
  private static final char COLON_CHAR = ':';

  // Must not have more than 7 colons (i.e. 8 fields)
  private static final int MAX_COLON_COUNT = 7;

  /**
   * Checks whether the parameter is a valid IPv4 address
   *
   * @param input the address string to check for validity
   * @return true if the input parameter is a valid IPv4 address
   */
  public static boolean isIPv4Address(final String input) {
    return IPV4_PATTERN.matcher(input).matches();
  }

  public static boolean isIPv4MappedIPv64Address(final String input) {
    return IPV4_MAPPED_IPV6_PATTERN.matcher(input).matches();
  }

  /**
   * Checks whether the parameter is a valid standard (non-compressed) IPv6 address
   *
   * @param input the address string to check for validity
   * @return true if the input parameter is a valid standard (non-compressed) IPv6 address
   */
  public static boolean isIPv6StdAddress(final String input) {
    return IPV6_STD_PATTERN.matcher(input).matches();
  }

  /**
   * Checks whether the parameter is a valid compressed IPv6 address
   *
   * @param input the address string to check for validity
   * @return true if the input parameter is a valid compressed IPv6 address
   */
  public static boolean isIPv6HexCompressedAddress(final String input) {
    int colonCount = 0;
    for (int i = 0; i < input.length(); i++) {
      if (input.charAt(i) == COLON_CHAR) {
        colonCount++;
      }
    }
    return colonCount <= MAX_COLON_COUNT && IPV6_HEX_COMPRESSED_PATTERN.matcher(input).matches();
  }

  /**
   * Checks whether the parameter is a valid IPv6 address (including compressed).
   *
   * @param input the address string to check for validity
   * @return true if the input parameter is a valid standard or compressed IPv6 address
   */
  public static boolean isIPv6Address(final String input) {
    return isIPv6StdAddress(input) || isIPv6HexCompressedAddress(input);
  }
}
