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
package cn.yaomaitong.share.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.yaomaitong.app.R;
import cn.yaomaitong.share.model.News;
import com.bumptech.glide.Glide;
import java.util.List;

/*
* Copyright (C) 2016-2017 Yaomaitong Inc.All Rights Reserved.
* FileName：NewsAdapter
* @Description：简要描述本文件的内容
* History：
* v1.0 danggui 2017/1/19 Create
*/
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
  List<News> data;
  Context context;
  LayoutInflater layoutInflater;

  public NewsAdapter(Context context, List<News> data) {
    this.context = context;
    this.data = data;
    layoutInflater = LayoutInflater.from(context);
  }

  @Override public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new NewsViewHolder(layoutInflater.inflate(R.layout.news_list_item, parent, false));
  }

  @Override public void onBindViewHolder(final NewsViewHolder holder, final int position) {
    News news = data.get(position);
    holder.txtTitle.setText(news.getPostTitle());
    holder.txtDesc.setText(news.getPostExcerpt());
    if (null != news.getPreviewImg()) {
      Glide.with(holder.itemView.getContext())
          .load(news.getPreviewImg().getMiddle())
          .into(holder.imgPost);
    }
    holder.txtCount.setText(news.getPostScanCount());
    holder.txtTime.setText(news.getPublishTime());
    holder.txtHot.setVisibility("2".equals(news.getPostType()) ? View.VISIBLE : View.GONE);
    if (news.isChecked()) {
      holder.itemView.setBackgroundColor(Color.parseColor("#706775D1"));
    } else {
      holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
    }

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        data.get(position).setChecked(!data.get(position).isChecked());
        notifyDataSetChanged();
      }
    });
  }

  @Override public int getItemCount() {
    return (null != data && data.size() > 0 ? data.size() : 0);
  }

  public static class NewsViewHolder extends RecyclerView.ViewHolder {
    TextView txtTitle;
    TextView txtDesc;
    TextView txtCount;
    TextView txtTime;
    TextView txtHot;
    ImageView imgPost;
    View itemView;

    public NewsViewHolder(View itemView) {
      super(itemView);
      this.itemView = itemView;
      txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
      txtDesc = (TextView) itemView.findViewById(R.id.txt_desc);
      txtCount = (TextView) itemView.findViewById(R.id.txt_browse_count);
      txtTime = (TextView) itemView.findViewById(R.id.txt_time);
      txtHot = (TextView) itemView.findViewById(R.id.txt_type);
      imgPost = (ImageView) itemView.findViewById(R.id.img_post);
    }
  }
}