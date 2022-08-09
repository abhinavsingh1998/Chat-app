package com.emproto.hoabl.feature.portfolio.views

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.emproto.core.BaseFragment
import com.emproto.hoabl.databinding.FragmentFmBinding
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.networklayer.response.webview.ShareObjectModel
import com.google.gson.Gson
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FmFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FmFragment : BaseFragment() {

    lateinit var binding: FragmentFmBinding

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFmBinding.inflate(layoutInflater)
        (requireActivity() as HomeActivity).hideHeader()
        binding.webView.webViewClient = MyWebViewclient(binding.progressBaar, requireContext())
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setBuiltInZoomControls(true);
        binding.webView.getSettings().setDisplayZoomControls(false);
        binding.webView.getSettings().setDomStorageEnabled(true);
        binding.webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        binding.webView.addJavascriptInterface(JSBridge(),"JSBridge")
        binding.webView.loadUrl(param1!!)


        return binding.root
    }

    inner class JSBridge() {
        @JavascriptInterface
        fun shareActionInNative(message:String){
            //Received message from webview in native, process data
            Log.d("Share","message from webview= ${message.toString()}")
            val shareObjectModel = ShareObjectModel("","","","")
            val gson = Gson()
            val model = gson.fromJson<ShareObjectModel>(message,ShareObjectModel::class.java)
            Log.d("Share","objectUrl = ${model.download_url}")
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            shareIntent.type = "text/plain"
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                "${model.message}\nDownload the gatepass via this url: ${model.download_url}"
            )
            startActivity(shareIntent)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FmFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FmFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    public open class MyWebViewclient(val progressBaar: ProgressBar, val requireContext: Context) :
        WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            Log.d("Share",url.toString())
            if (url!!.startsWith("tel:")) {
                val intent = Intent(
                    Intent.ACTION_DIAL,
                    Uri.parse(url)
                )
                requireContext.startActivity(intent)
            } else if (url!!.startsWith("http:") || url!!.startsWith("https:")) {
                view!!.loadUrl(url!!)
            }
            return true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            progressBaar.visibility = View.GONE
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            progressBaar.visibility = View.VISIBLE
        }
    }
}