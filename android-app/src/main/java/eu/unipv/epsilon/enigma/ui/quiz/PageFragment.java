package eu.unipv.epsilon.enigma.ui.quiz;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.myid, container, false);

        WebView view = new WebView(container.getContext());
        view.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        WebViewClient wvc = new WebViewClient() {

            // http://stackoverflow.com/questions/8332474/android-webview-protocol-handler
            // http://stackoverflow.com/questions/8273991/webview-shouldinterceptrequest-example

            // View @ level 19:
            // http://developer.android.com/reference/android/webkit/WebViewClient.html

            @TargetApi(21)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                String urlString = request.getUrl().toString();

                if (urlString.startsWith("eqc:")) {
                    try {
                        String ext = urlString.substring(urlString.lastIndexOf('.') + 1);

                        URL url = new URL(urlString);
                        String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);

                        return new WebResourceResponse(mime, "UTF-8", url.openStream());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return super.shouldInterceptRequest(view, request);
                    }
                } else
                    return super.shouldInterceptRequest(view, request);
            }

        };
        view.setWebViewClient(wvc);

        view.getSettings().setJavaScriptEnabled(true);

        //view.clearCache(true);
        view.loadUrl(mDocumentUrl.toString());
        return view;
    }

}
