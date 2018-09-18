package com.cc.android.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.*;

import com.cc.android.R;
import com.cc.android.base.BaseActivity;
import com.cc.android.net.Api;

public class BrowserActivity extends BaseActivity {
	private WebView webView;
	private Boolean isActive = false;
	private ValueCallback<Uri> mUploadMessage;
	private ValueCallback<Uri[]> mUploadCallbackAboveL;
	private final static int FILECHOOSER_RESULTCODE = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WebView.setWebContentsDebuggingEnabled(true);
		setContentView(R.layout.browser);
		setProgressBarVisibility(true);
		webView = (WebView) findViewById(R.id.webview);
		webView.addJavascriptInterface(this, "BrowserHandle");
		WebSettings websettings = webView.getSettings();
		websettings.setJavaScriptEnabled(true);
		websettings.setBuiltInZoomControls(true);
		websettings.setAllowFileAccess(true);
		websettings.setDomStorageEnabled(true);
		websettings.setSupportMultipleWindows(true);
		websettings.setAllowFileAccessFromFileURLs(true);
		websettings.setDefaultTextEncodingName("utf-8");
		websettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
			@Override
			public void onPageStarted(WebView view, String url, Bitmap facIcon) {
				super.onPageStarted(view, url, facIcon);
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}
		});

		webView.setWebChromeClient(new WebChromeClient() {


			public void openFileChooser(ValueCallback<Uri> uploadMsg) {
				mUploadMessage = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("image/*");
				BrowserActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);

			}

			// For Android 3.0+
			public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
				mUploadMessage = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("*/*");
				BrowserActivity.this.startActivityForResult(
						Intent.createChooser(i, "File Browser"),
						FILECHOOSER_RESULTCODE);
			}

			//For Android 4.1
			public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
				mUploadMessage = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("image/*");
				BrowserActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"),FILECHOOSER_RESULTCODE);
			}

			// Set progress bar during loading
			public void onProgressChanged(WebView view, int progress) {
				BrowserActivity.this.setProgress(progress * 100);
			}

			@Override
			public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg)
			{
				WebView newWebView = new WebView(view.getContext());
				view.addView(newWebView);
				newWebView.setWebViewClient(new WebViewClient());
				newWebView.setWebChromeClient(this);
				WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
				transport.setWebView(newWebView);
				resultMsg.sendToTarget();
				return true;
			}
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
									 JsResult result)
			{
				return super.onJsAlert(view, url, message, result);
			}
		});

	}
	@JavascriptInterface
	public void goLogin(){
		if(Api.getToken().get("token") == null){
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			rightToleft();
		}else{
			Intent intent = new Intent(this, Location_Activity.class);
			startActivity(intent);
			rightToleft();
		}
	}
	@Override
	public void onResume() {
		super.onResume();
		// Reload URL
		if(!isActive){
			webView.loadUrl("http://120.27.111.37:8099");
			isActive = true;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		/**
		 * 这里监控的是物理返回或者设置了该接口的点击事件
		 * 当按钮事件为返回时，且WebView可以返回，即触发返回事件
		 */
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if( webView.canGoBack())
				webView.goBack(); // goBack()表示返回WebView的上一页面
			else
				BrowserActivity.this.finish();
			return true; // 返回true拦截事件的传递
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==FILECHOOSER_RESULTCODE)
		{
			if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
			Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
			if (mUploadCallbackAboveL != null) {
				onActivityResultAboveL(requestCode, resultCode, data);
			}
			else  if (mUploadMessage != null) {
				mUploadMessage.onReceiveValue(result);
				mUploadMessage = null;
			}
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
		if (requestCode != FILECHOOSER_RESULTCODE
				|| mUploadCallbackAboveL == null) {
			return;
		}


		Uri[] results = null;
		if (resultCode == Activity.RESULT_OK) {
			if (data == null) {


			} else {
				String dataString = data.getDataString();
				ClipData clipData = data.getClipData();


				if (clipData != null) {
					results = new Uri[clipData.getItemCount()];
					for (int i = 0; i < clipData.getItemCount(); i++) {
						ClipData.Item item = clipData.getItemAt(i);
						results[i] = item.getUri();
					}
				}


				if (dataString != null)
					results = new Uri[]{Uri.parse(dataString)};
			}
		}
		mUploadCallbackAboveL.onReceiveValue(results);
		mUploadCallbackAboveL = null;
		return;
	}
}