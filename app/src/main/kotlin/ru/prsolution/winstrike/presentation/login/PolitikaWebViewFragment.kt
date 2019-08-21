package ru.prsolution.winstrike.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.presentation.utils.gone
import ru.prsolution.winstrike.presentation.utils.inflate
import ru.prsolution.winstrike.presentation.utils.visible
import timber.log.Timber

/**
 * Created by Oleg Sitnikov on 2019-01-27
 */

class PolitikaWebViewFragment : Fragment() {
    private var mWebView: WebView? = null
    private var progressBar: ProgressBar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return context?.inflate(R.layout.fmt_web_view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var url = ""

        
     //   progressBar = view.findViewById(R.id.pbar)

        progressBar?.visible()

        arguments?.let {
            val safeArgs = PolitikaWebViewFragmentArgs.fromBundle(it)
            url = safeArgs.url
        }

        mWebView = view.findViewById<View>(R.id.webView) as WebView
        mWebView?.webViewClient = MyWebViewClient()
        mWebView?.isHorizontalScrollBarEnabled = false
        mWebView?.settings?.loadWithOverviewMode = true
        mWebView?.settings?.useWideViewPort = true
        if (!url.isEmpty()) {
            mWebView?.loadUrl(url)
        }
    }

    private inner class MyWebViewClient : WebViewClient() {

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            progressBar?.gone()
        }
    }
}
