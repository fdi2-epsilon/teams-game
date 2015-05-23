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
import eu.unipv.epsilon.enigma.status.AndroidQuestViewInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * This fragment displays the page number passed as an argument to
 * {@link PageFragment#newInstance(int, URL, AndroidQuestViewInterface)}.
 */
public class PageFragment extends Fragment {

    private static final Logger LOG = LoggerFactory.getLogger(PageFragment.class);
    private static final String JAVASCRIPT_INTERFACE_MAPPED_NAME = "enigma";

    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ARG_DOCURL = "ARG_DOCURL";
    public static final String ARG_VIEW_INTERFACE = "ARG_VIEW_INTERFACE";

    private int mPage;
    private URL mDocumentUrl;
    private AndroidQuestViewInterface mViewInterface;

    public static PageFragment newInstance(int page, URL documentUrl, AndroidQuestViewInterface viewInterface) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putSerializable(ARG_DOCURL, documentUrl);
        args.putSerializable(ARG_VIEW_INTERFACE, viewInterface);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        mDocumentUrl = (URL) getArguments().getSerializable(ARG_DOCURL);
        mViewInterface = (AndroidQuestViewInterface) getArguments().getSerializable(ARG_VIEW_INTERFACE);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // We flush status when the fragment is no longer visible
        if (mViewInterface != null && !isVisibleToUser)
            mViewInterface.flushData();
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

                if (url.startsWith("eqc:") || url.startsWith("assets:")) {
                    try {
                        String mime;
                        if (url.lastIndexOf('.') > url.lastIndexOf('/')) {
                            String ext = url.substring(url.lastIndexOf('.') + 1);
                            mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
                        } else {
                            // With a directory link, we assume that we have 'text/html' default documents
                            mime = "text/html";
                        }
                        return new WebResourceResponse(mime, "UTF-8", new URL(url).openStream());
                    } catch (Exception e) {
                        LOG.error("Cannot load custom protocol resource", e);
                        // Return super
                    }
                }

                return super.shouldInterceptRequest(view, url);
            }

        };

        view.setWebViewClient(wvc);
        view.getSettings().setJavaScriptEnabled(true);
        view.addJavascriptInterface(mViewInterface, JAVASCRIPT_INTERFACE_MAPPED_NAME);

        //view.clearCache(true);
        view.loadUrl(mDocumentUrl.toString());
        return view;
    }

}
