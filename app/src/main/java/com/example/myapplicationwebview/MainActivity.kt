package com.example.myapplicationwebview

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var fullScreenContainer: FrameLayout
    private var customView: View? = null
    private var customViewCallback: WebChromeClient.CustomViewCallback? = null

    private val BASE_URL = "https://next.kick.com/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        fullScreenContainer = findViewById(R.id.fullScreenContainer)

        // Configurando WebChromeClient para manejo de video en pantalla completa
        webView.webChromeClient = object : WebChromeClient() {

            override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                if (customView != null) {
                    callback?.onCustomViewHidden()
                    return
                }
                customView = view
                customViewCallback = callback

                fullScreenContainer.addView(customView, ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                ))
                fullScreenContainer.visibility = View.VISIBLE
                webView.visibility = View.GONE
            }

            override fun onHideCustomView() {
                fullScreenContainer.visibility = View.GONE
                webView.visibility = View.VISIBLE

                fullScreenContainer.removeView(customView)
                customView = null
                customViewCallback?.onCustomViewHidden()
            }
        }

        webView.webViewClient = object : WebViewClient() {
            // Aquí puedes personalizar el comportamiento del WebViewClient si es necesario
        }

        val settings: WebSettings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true  // Habilitar almacenamiento DOM para manejar videos
        settings.mediaPlaybackRequiresUserGesture = false  // Permitir la reproducción automática de medios

        webView.loadUrl(BASE_URL)
    }

    override fun onBackPressed() {
        if (customView != null) {
            webView.webChromeClient.onHideCustomView()
        } else if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
