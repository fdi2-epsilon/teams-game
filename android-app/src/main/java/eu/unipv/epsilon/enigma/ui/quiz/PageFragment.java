package eu.unipv.epsilon.enigma.ui.quiz;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.*;
import android.widget.FrameLayout;

import java.net.URL;

/**
 * This fragment displays the page number passed as an argument to
 * {@link PageFragment#newInstance(int, URL)}.
 */
public class PageFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ARG_DOCURL = "ARG_DOCURL";

    private int mPage;
    private URL mDocumentUrl;

    public static PageFragment newInstance(int page, URL documentUrl) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putSerializable(ARG_DOCURL, documentUrl);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        mDocumentUrl = (URL) getArguments().getSerializable(ARG_DOCURL);
    }

    @Nullable
    @Override
    @SuppressLint("SetJavaScriptEnabled")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.myid, container, false);

        WebView view = new WebView(container.getContext());
        view.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        WebViewClient wvc = new WebViewClient() {

            // http://stackoverflow.com/questions/8332474/android-webview-protocol-handler
            // http://stackoverflow.com/questions/8273991/webview-shouldinterceptrequest-example

            @Override
            @TargetApi(21)
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                // This causes a call to the pre-21 version 'shouldInterceptRequest(WebView, String)'
                return super.shouldInterceptRequest(view, request);
            }

            // Android pre-21 calls this instead
            @Override
            @SuppressWarnings("deprecation")
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

                if (url.startsWith("eqc:")) {
                    try {
                        String ext = url.substring(url.lastIndexOf('.') + 1);
                        String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
                        return new WebResourceResponse(mime, "UTF-8", new URL(url).openStream());
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Return super
                    }
                }

                return super.shouldInterceptRequest(view, url);
            }

        };
        view.setWebViewClient(wvc);

        view.getSettings().setJavaScriptEnabled(true);

        //view.clearCache(true);
        view.loadUrl(mDocumentUrl.toString());
        return view;
    }

}
