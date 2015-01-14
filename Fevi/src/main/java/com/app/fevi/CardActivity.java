package com.app.fevi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.UUID;

/**
 * Created by bungubbang on 3/2/14.
 */
public class CardActivity extends Activity {

    private WebView mWebView;

    private Handler mHandler;
    private boolean mFlag = false;
    private String card_id = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setLayout();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            card_id = extras.getString("card_id");
        }

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("http://fe-vi.com/trend/card?id=" + card_id);

        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.addJavascriptInterface(new FeviJavaScriptInterface(), "feviJs");

        mWebView.setWebViewClient(new WebViewClientClass());
        mWebView.setWebChromeClient(new CustomChromeClient());
        mWebView.getSettings().setUserAgentString(mWebView.getSettings().getUserAgentString() + " Fevi(inapp)");

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 0) {
                    mFlag = false;
                }
            }
        };

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 백 키를 터치한 경우
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

    private class WebViewClientClass extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost().equals("fe-vi.com")) {
                // This is my web site, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }

    private class CustomChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result)
        {
            Toast.makeText(getApplicationContext(), message, 3000).show();
            return true;
        }
    }

    /*
     * Layout
     */
    private void setLayout(){
        mWebView = (WebView) findViewById(R.id.webview);
    }

    public final class FeviJavaScriptInterface {

        @JavascriptInterface
        public void play(final String url) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.parse(url);
            i.setDataAndType(uri, "video/*");
            startActivity(i);
        }

        @JavascriptInterface
        public String getUid() {
            final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

            final String tmDevice, tmSerial, androidId;
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

            UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
            return "a-" + deviceUuid.toString();
        }

        @JavascriptInterface
        public void share(final String text) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, text);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "[Fevi] Share to text ..."));
        }
    }

}

