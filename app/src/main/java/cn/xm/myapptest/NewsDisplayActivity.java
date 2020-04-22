package cn.xm.myapptest;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class NewsDisplayActivity extends AppCompatActivity {
    private WebView webView;
    private String newsUrl;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() !=null) {

            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_display_news);
        newsUrl = getIntent().getStringExtra("news_url");
        webView = (WebView) findViewById(R.id.web_view);
        webView.requestFocus();
        webView.loadUrl(newsUrl);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        webSettings.setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        webSettings.setSupportZoom(true);//是否可以缩放，默认true
        webSettings.setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        webSettings.setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        webSettings.setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        webSettings.setDomStorageEnabled(true);//DOM Storage
        webSettings.setUserAgentString("User-Agent:Android");//设置用户代理，一般不用

        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.setWebChromeClient(new WebChromeClient() {

            private ProgressDialog progressDialog;
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // newProgress 1 ~ 100 之间的证书
                if (newProgress == 100) {
                    // 加载完成，关闭ProgressDialog
                    closeDialog();
                } else {
                    // 还在加载，打开ProgressDialog
                    openDialog(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
            private void closeDialog() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
            private void openDialog(int newProgress) {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(NewsDisplayActivity.this);
                    progressDialog.setProgress(newProgress);
                    progressDialog.setTitle("正在加载");
                    progressDialog.show();
                } else {
                    progressDialog.setProgress(newProgress);
                }
            }

        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}