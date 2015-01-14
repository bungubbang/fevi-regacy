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

public class MainActivity extends Activity {

    private WebView mWebView;

    private Handler mHandler;
    private boolean mFlag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        if(Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            if(uri.getHost().equals("fe-vi.com") && uri.getPath().contains("card")) {
                startCardActivity(uri);
            }
        }

        setContentView(R.layout.activity_main);
        setLayout();



        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.loadUrl("http://192.168.0.24:8080/");
        mWebView.loadUrl("http://fe-vi.com/");

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

        startActivity(new Intent(getApplicationContext(), SplashActivity.class));

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 백 키를 터치한 경우
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(!mFlag) {
                Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
                mFlag = true;
                mHandler.sendEmptyMessageDelayed(0, 2000); // 2초 내로 터치시
                return false;
            } else {
                finish();
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    private class WebViewClientClass extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri uri = Uri.parse(url);
            if(uri.getHost().equals("fe-vi.com") && uri.getPath().contains("card")) {
                startCardActivity(uri);
                return true;
            } else if (uri.getHost().equals("fe-vi.com")) {
                // This is my web site, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        }
    }

    private void startCardActivity(Uri uri) {
        Intent intent = new Intent(getApplicationContext(), CardActivity.class);
        intent.putExtra("card_id", uri.getQueryParameter("id"));
        startActivity(intent);
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
