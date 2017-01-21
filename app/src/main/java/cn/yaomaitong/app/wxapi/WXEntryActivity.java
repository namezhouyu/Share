package cn.yaomaitong.app.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import cn.yaomaitong.share.YmtApplication;
import cn.yaomaitong.share.model.ShareEvent;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    YmtApplication.getIwxapi().handleIntent(getIntent(), this);
  }

  @Override protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    setIntent(intent);
    YmtApplication.getIwxapi().handleIntent(getIntent(), this);
  }

  /**
   * 微信分享成功后如果留在微信，退出微信后不会触发回调
   */
  @Override public void onReq(BaseReq req) {
  }

  @Override public void onResp(BaseResp resp) {
    //String msg;
    //switch (resp.errCode) {
    //  case BaseResp.ErrCode.ERR_OK:
    //    msg = "分享成功";
    //    break;
    //  case BaseResp.ErrCode.ERR_USER_CANCEL:
    //    msg = "分享取消";
    //    break;
    //  case BaseResp.ErrCode.ERR_AUTH_DENIED:
    //    msg = "分享被拒绝";
    //    break;
    //  default:
    //    msg = "分享返回";
    //    break;
    //}
    YmtApplication.getBus().post(ShareEvent.produceShareEvent());
    finish();
  }

  @Override protected void onResume() {
    YmtApplication.getBus().register(this);
    super.onResume();
  }

  @Override protected void onPause() {
    YmtApplication.getBus().unregister(this);
    super.onPause();
  }
}