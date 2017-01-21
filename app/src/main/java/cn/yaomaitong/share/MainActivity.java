package cn.yaomaitong.share;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;
import cn.yaomaitong.app.R;
import cn.yaomaitong.share.adapter.NewsAdapter;
import cn.yaomaitong.share.model.News;
import cn.yaomaitong.share.model.ShareContent;
import cn.yaomaitong.share.model.ShareEvent;
import cn.yaomaitong.share.network.RetrofitClient;
import cn.yaomaitong.share.network.api.NewsApi;
import com.bumptech.glide.Glide;
import com.squareup.otto.Subscribe;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
  NewsApi newsApi;
  List<News> newsData = new ArrayList<>();
  NewsAdapter adapter;
  List<ShareContent> toShareData = new ArrayList<>();
  PowerManager.WakeLock mWakelock;//唤醒屏幕相关
  KeyguardManager.KeyguardLock kl;//解锁相关

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    YmtApplication.getBus().register(this);
    newsApi = RetrofitClient.get(this).create(NewsApi.class);
    initScreenSetting();//锁屏幕唤起
    registerMessageReceiver();//jpush
    initView();
    netRequest();
  }

  private void netRequest() {
    Call<ResponseBody> call = newsApi.getNewsList(1, 20);
    call.enqueue(new Callback<ResponseBody>() {
      @Override public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
          String resJson = response.body().string();
          JSONObject jsonObject = new JSONObject(resJson);
          JSONArray data = jsonObject.getJSONArray("data");
          for (int i = 0; i < data.length(); i++) {
            JSONObject object = data.getJSONObject(i);
            News news = new News();
            news.setId(object.getString("id"));
            news.setPostAuthor(object.getString("postAuthor"));
            news.setPostDate(object.getString("postDate"));
            news.setPostExcerpt(object.getString("postExcerpt"));
            news.setPostScanCount(object.getString("postScanCount"));
            news.setPostTitle(object.getString("postTitle"));
            news.setPostType(object.getString("postType"));
            news.setPublishTime(object.getString("publishTime"));
            news.setUrl(object.getString("url"));
            News.PreviewImg img = new News.PreviewImg();
            img.setImageName(new JSONObject(object.getString("previewImg")).getString("imageName"));
            img.setMiddle(new JSONObject(object.getString("previewImg")).getString("middle"));
            news.setPreviewImg(img);
            newsData.add(news);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
      }

      @Override public void onFailure(Call<ResponseBody> call, Throwable t) {
      }
    });
  }

  private void initView() {
    final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        toShareData.clear();
        for (News news : newsData) {
          if (news.isChecked()) {
            //// TODO: 2017/1/20 share
            ShareContent shareContent = new ShareContent();
            shareContent.setDes(news.getPostDate());
            shareContent.setImageUrl(news.getPreviewImg().getMiddle());
            shareContent.setLink(news.getUrl());
            shareContent.setTitle(news.getPostTitle());
            toShareData.add(shareContent);
          }
        }
        if (null != toShareData && toShareData.size() > 0) {
          doShare(toShareData.get(0));
        }
        return false;
      }
    });
    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_news);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.addItemDecoration(new DividerItemDecoration(this, 1));
    adapter = new NewsAdapter(this, newsData);
    recyclerView.setAdapter(adapter);
  }

  private void initScreenSetting() {
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
    mWakelock =
        pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK,
            "SimpleTimer");
    KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
    //这里参数”unLock”作为调试时LogCat中的Tag
    kl = km.newKeyguardLock("unLock");
  }

  @Override protected void onResume() {
    super.onResume();
    mWakelock.acquire();//点亮
    kl.disableKeyguard();  //解锁
    YmtApplication.get().setAutoFlag(true);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu, menu);
    return true;
  }

  @Subscribe public void onShareFinish(ShareEvent event) {
    Random random = new Random();
    int sleep = random.nextInt(1000) + 1000;//休眠时间1--2s
    if (null != toShareData && toShareData.size() > 1) {
      toShareData.remove(0);
      new Handler().postDelayed(new Runnable() {
        @Override public void run() {
          doShare(toShareData.get(0));
        }
      }, sleep);
    }
  }

  private void doShare(ShareContent shareContent) {
    Observable.just(shareContent)
        .subscribeOn(Schedulers.io())
        .flatMap(new Func1<ShareContent, Observable<ShareContent>>() {
          @Override public Observable<ShareContent> call(ShareContent shareContent) {
            try {
              if (!TextUtils.isEmpty(shareContent.getImageUrl())) {
                byte[] bytes = Glide.with(MainActivity.this)
                    .load(shareContent.getImageUrl())
                    .asBitmap()
                    .toBytes()
                    .centerCrop()
                    .into(100, 100)
                    .get();
                shareContent.setThumbnail(bytes);
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
            return Observable.just(shareContent);
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<ShareContent>() {
          @Override public void onCompleted() {

          }

          @Override public void onError(Throwable e) {

          }

          @Override public void onNext(ShareContent pShareContent) {
            //验证数据是否正确
            if (TextUtils.isEmpty(pShareContent.getLink())
                && null == pShareContent.getThumbnail()) {
              Toast.makeText(MainActivity.this, "分享数据异常", Toast.LENGTH_SHORT);
              return;
            }
            IWXAPI iwxapi = YmtApplication.getIwxapi();
            WXWebpageObject webPageObject = new WXWebpageObject();
            webPageObject.webpageUrl = pShareContent.getLink();
            WXMediaMessage wxMediaMessage = new WXMediaMessage(webPageObject);
            wxMediaMessage.title = pShareContent.getTitle();
            wxMediaMessage.description = pShareContent.getDes();
            wxMediaMessage.thumbData = pShareContent.getThumbnail();
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = "ymt" + System.currentTimeMillis();
            req.message = wxMediaMessage;
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
            iwxapi.sendReq(req);
          }
        });
  }

  @Override protected void onDestroy() {
    YmtApplication.getBus().unregister(this);
    super.onDestroy();
    mWakelock.release();
    kl.reenableKeyguard();
    YmtApplication.get().setAutoFlag(false);
  }

  //---------JPUSH-----------

  //for receive customer msg from jpush server
  private MessageReceiver mMessageReceiver;
  public static final String MESSAGE_RECEIVED_ACTION =
      "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
  public static final String KEY_TITLE = "title";
  public static final String KEY_MESSAGE = "message";
  public static final String KEY_EXTRAS = "extras";

  public void registerMessageReceiver() {
    mMessageReceiver = new MessageReceiver();
    IntentFilter filter = new IntentFilter();
    filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
    filter.addAction(MESSAGE_RECEIVED_ACTION);
    registerReceiver(mMessageReceiver, filter);
  }

  public class MessageReceiver extends BroadcastReceiver {

    @Override public void onReceive(Context context, Intent intent) {
      if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {

        //String messge = intent.getStringExtra(KEY_MESSAGE);
        //String extras = intent.getStringExtra(KEY_EXTRAS);
        //StringBuilder showMsg = new StringBuilder();
        //showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
      }
    }
  }
}
