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
package cn.yaomaitong.share.network.model;

/*
* Copyright (C) 2016-2017 yaomaitong Inc.All Rights Reserved.
* FileName：News
* @Description：简要描述本文件的内容
* History：
* v1.0 tianlong 16/8/18 Create
*/
public class News {
  private String id;
  private String postAuthor;
  private String postDate;
  private String postTitle;
  private String postType;
  private String postScanCount;
  private String postExcerpt;
  private String publishTime;
  private String url;
  private PreviewImg previewImg;

  public static class PreviewImg {
    private String imageName;
    private String large;
    private String middle;
    private String small;

    public String getImageName() {
      return imageName;
    }

    public void setImageName(String imageName) {
      this.imageName = imageName;
    }

    public String getLarge() {
      return large;
    }

    public void setLarge(String large) {
      this.large = large;
    }

    public String getMiddle() {
      return middle;
    }

    public void setMiddle(String middle) {
      this.middle = middle;
    }

    public String getSmall() {
      return small;
    }

    public void setSmall(String small) {
      this.small = small;
    }

    @Override public String toString() {
      return "PreviewImg{" +
          "imageName='" + imageName + '\'' +
          ", large='" + large + '\'' +
          ", middle='" + middle + '\'' +
          ", small='" + small + '\'' +
          '}';
    }
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPostAuthor() {
    return postAuthor;
  }

  public void setPostAuthor(String postAuthor) {
    this.postAuthor = postAuthor;
  }

  public String getPostDate() {
    return postDate;
  }

  public void setPostDate(String postDate) {
    this.postDate = postDate;
  }

  public String getPostTitle() {
    return postTitle;
  }

  public void setPostTitle(String postTitle) {
    this.postTitle = postTitle;
  }

  public String getPostType() {
    return postType;
  }

  public void setPostType(String postType) {
    this.postType = postType;
  }

  public String getPostScanCount() {
    return postScanCount;
  }

  public void setPostScanCount(String postScanCount) {
    this.postScanCount = postScanCount;
  }

  public String getPostExcerpt() {
    return postExcerpt;
  }

  public void setPostExcerpt(String postExcerpt) {
    this.postExcerpt = postExcerpt;
  }

  public PreviewImg getPreviewImg() {
    return previewImg;
  }

  public void setPreviewImg(PreviewImg previewImg) {
    this.previewImg = previewImg;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getPublishTime() {
    return publishTime;
  }

  public void setPublishTime(String publishTime) {
    this.publishTime = publishTime;
  }

  @Override public String toString() {
    return "News{" +
        "id='" + id + '\'' +
        ", postAuthor='" + postAuthor + '\'' +
        ", postDate='" + postDate + '\'' +
        ", postTitle='" + postTitle + '\'' +
        ", postType='" + postType + '\'' +
        ", postScanCount='" + postScanCount + '\'' +
        ", postExcerpt='" + postExcerpt + '\'' +
        ", url='" + url + '\'' +
        ", previewImg=" + previewImg +
        '}';
  }
}
