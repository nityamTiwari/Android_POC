package com.ferrytech.android_poc.bridge;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import org.json.JSONObject;

public class KioskJsBridge {

    private  final WebView webView;
    private final CommandHandler commandHandler;

    public KioskJsBridge(WebView webView, CommandHandler commandHandler){
        this.commandHandler = commandHandler;
        this.webView = webView;
    }

    // Webview (script) -> Android
    @JavascriptInterface
    public void execute(String command , String payload, String tag){
        System.out.println("KioskJsBridge.execute");
        commandHandler.handleCommand(command,payload,tag);
    }

    // Android -> Webview (script)
    public void sendResponse(String command, String data, String tag){
        String javaScript =
                "window.onKioskResponse && window.onKioskResponse("
                + JSONObject.quote(command) + ","
                + JSONObject.quote(data) + ","
                + JSONObject.quote(tag)
                + ");";

        webView.post(() ->
                webView.evaluateJavascript(javaScript, null)
                );
    }

}
