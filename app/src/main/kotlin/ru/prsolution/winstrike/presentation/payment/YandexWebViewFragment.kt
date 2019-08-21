package ru.prsolution.winstrike.presentation.payment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fmt_web_view.*
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.presentation.utils.gone
import ru.prsolution.winstrike.presentation.utils.inflate

/**
 * Created by Oleg Sitnikov on 2019-01-27
 */

class YandexWebViewFragment : Fragment() {
    private var mWebView: WebView? = null
    private var url: String? = "https://passport.yandex.ru/"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return context?.inflate(R.layout.fmt_web_view)
    }

    private var progBar: ProgressBar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progBar = view.findViewById(R.id.pbar)

        arguments?.let {
            val safeArgs = YandexWebViewFragmentArgs.fromBundle(it)
            url = safeArgs.url
        }

        mWebView = view.findViewById<View>(R.id.webView) as WebView
        mWebView?.webViewClient = MyWebViewClient()
        mWebView?.isHorizontalScrollBarEnabled = false
        // (TODO: Remove if don't need it. Introduce XSS vulnerabilities.)
        mWebView?.settings?.javaScriptEnabled = true
        mWebView?.settings?.loadWithOverviewMode = true
        mWebView?.settings?.useWideViewPort = true
        if (!TextUtils.isEmpty(url)) {
            mWebView?.loadUrl(url)
        }
    }

    private inner class MyWebViewClient : WebViewClient() {

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            progBar?.gone()
        }
    }
}
