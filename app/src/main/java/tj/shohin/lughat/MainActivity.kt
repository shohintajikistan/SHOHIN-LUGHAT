package tj.shohin.lughat

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import java.io.FileNotFoundException

class MainActivity : ComponentActivity() {
    private var appWebView: WebView? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(7, 28, 22)
        window.navigationBarColor = Color.rgb(7, 28, 22)

        val webView = WebView(this)
        appWebView = webView
        webView.setBackgroundColor(Color.rgb(7, 28, 22))
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            allowFileAccess = false
            allowContentAccess = false
            javaScriptCanOpenWindowsAutomatically = false
            setSupportMultipleWindows(false)
            loadsImagesAutomatically = true
            mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_NEVER_ALLOW
        }
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest
            ): WebResourceResponse? {
                val uri = request.url
                if (uri.scheme != "https" || uri.host != "shohin.local") return WebResourceResponse(
                    "text/plain", "UTF-8", 403, "Blocked", emptyMap(), ByteArray(0).inputStream()
                )

                val relativePath = (uri.path ?: "/index.html").removePrefix("/")
                if (relativePath.contains("..") || relativePath.contains('\\')) {
                    return WebResourceResponse(
                        "text/plain", "UTF-8", 403, "Blocked", emptyMap(), ByteArray(0).inputStream()
                    )
                }
                val assetPath = if (relativePath.isBlank()) "www/index.html" else "www/$relativePath"
                val extension = assetPath.substringAfterLast('.', "").lowercase()
                val mime = when (extension) {
                    "html" -> "text/html"
                    "json" -> "application/json"
                    "js" -> "application/javascript"
                    "css" -> "text/css"
                    "png" -> "image/png"
                    "jpg", "jpeg" -> "image/jpeg"
                    "webp" -> "image/webp"
                    "svg" -> "image/svg+xml"
                    else -> "application/octet-stream"
                }
                return try {
                    WebResourceResponse(
                        mime,
                        if (mime.startsWith("text/") || mime.contains("json") || mime.contains("javascript") || mime.contains("svg")) "UTF-8" else null,
                        assets.open(assetPath)
                    )
                } catch (_: FileNotFoundException) {
                    WebResourceResponse(
                        "text/plain", "UTF-8", 404, "Not Found", emptyMap(),
                        "App asset not found".byteInputStream()
                    )
                } catch (_: Exception) {
                    WebResourceResponse(
                        "text/plain", "UTF-8", 500, "Asset Error", emptyMap(),
                        "Unable to read app asset".byteInputStream()
                    )
                }
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest): Boolean {
                return request.url.host != "shohin.local"
            }
        }

        setContentView(webView)
        webView.loadUrl("https://shohin.local/index.html")

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()) webView.goBack() else finish()
            }
        })
    }

    override fun onDestroy() {
        appWebView?.apply {
            stopLoading()
            loadUrl("about:blank")
            clearHistory()
            removeAllViews()
            destroy()
        }
        appWebView = null
        super.onDestroy()
    }
}
