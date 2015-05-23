package eu.unipv.epsilon.enigma.ui.quiz;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import eu.unipv.epsilon.enigma.EnigmaApplication;
import eu.unipv.epsilon.enigma.status.AndroidQuestViewInterface;
import eu.unipv.epsilon.enigma.ui.util.InterceptingWebViewClient;
import eu.unipv.epsilon.enigma.ui.util.SimpleCollectionDataRetriever;

import java.net.URL;

/**
 * This fragment displays the page number passed as an argument to {@link PageFragment#newInstance(String, int)}.
 */
public class PageFragment extends Fragment {

    public static final String ARG_COLLECTION_ID = "ARG_COLLECTION_ID";
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String JAVASCRIPT_INTERFACE_MAPPED_NAME = "enigma";

    private URL mDocumentUrl;
    private AndroidQuestViewInterface mViewInterface;

    public static PageFragment newInstance(String collectionId, int page) {
        Bundle args = new Bundle();
        args.putString(ARG_COLLECTION_ID, collectionId);
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int pageIndex = getArguments().getInt(ARG_PAGE);
        String collectionId = getArguments().getString(ARG_COLLECTION_ID);

        EnigmaApplication application = (EnigmaApplication) getActivity().getApplication();
        SimpleCollectionDataRetriever cData = new SimpleCollectionDataRetriever(application, collectionId);

        mDocumentUrl = cData.getCollection().get(pageIndex - 1).getMainDocumentUrl();
        mViewInterface = new AndroidQuestViewInterface(cData.getCollectionStatus(), pageIndex);
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
        // To display a view from layout, use:
        //   View view = inflater.inflate(R.layout.my_id, container, false);

        WebView view = new WebView(container.getContext());
        view.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        view.setWebViewClient(new InterceptingWebViewClient());
        view.getSettings().setJavaScriptEnabled(true);
        view.addJavascriptInterface(mViewInterface, JAVASCRIPT_INTERFACE_MAPPED_NAME);

        //view.clearCache(true);
        view.loadUrl(mDocumentUrl.toString());
        return view;
    }

}
