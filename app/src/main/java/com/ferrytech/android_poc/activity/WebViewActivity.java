package com.ferrytech.android_poc.activity;

import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ferrytech.android_poc.R;
import com.ferrytech.android_poc.bridge.CommandHandler;
import com.ferrytech.android_poc.bridge.CommandType;
import com.ferrytech.android_poc.bridge.KioskJsBridge;

import org.json.JSONObject;

public class WebViewActivity extends AppCompatActivity implements CommandHandler {

    private WebView webView;
    private KioskJsBridge bridge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        bridge = new KioskJsBridge(webView, this);

        webView.addJavascriptInterface(bridge, "AndroidKiosk");

        setContentView(webView);
        webView.loadUrl("file:///android_asset/index.html");
    }

    @Override
    public void handleCommand(String command, String payload, String executeTag) {
           CommandType commandType = CommandType.fromString(command);

           String responseData;

           switch(commandType){

               case PING:
                   responseData = "pong";
                   break;

               case ECHO:
                   responseData = payload;
                   break;

               case DEVICE_INFO:
                   JSONObject deviceInfo = new JSONObject();

                   try{
                       deviceInfo.put("manufacturer", Build.MANUFACTURER);
                       deviceInfo.put("model", Build.MODEL);
                       deviceInfo.put("androidVersion", Build.VERSION.RELEASE);
                       deviceInfo.put("sdkInt", Build.VERSION.SDK_INT);
                   }
                   catch (Exception e){
                       responseData = "{\"error\":\"Could not read device info\"}";
                       bridge.sendResponse(command, responseData, executeTag);
                       return;
                   }
                   responseData = deviceInfo.toString();
                   break;

               case UNKNOWN:
               default:
                   responseData = "{\"error\":\"Unsupported command\"}";
                   break;
           }

           bridge.sendResponse(command, responseData, executeTag);
    }

    @Override
    protected void onDestroy() {
        if(webView != null){
            webView.removeJavascriptInterface("AndroidKiosk");
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}