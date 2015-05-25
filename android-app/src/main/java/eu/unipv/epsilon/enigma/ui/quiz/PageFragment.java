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
import eu.unipv.epsilon.enigma.ui.util.CollectionDataBundle;
import eu.unipv.epsilon.enigma.ui.util.InterceptingWebViewClient;

import java.net.URL;

/**
 * Fragment containing an HTML quiz view.
 * ID and index inside the collection are passed using {@link #newInstance(String, int)}.
 */
public class PageFragment extends Fragment {

    public static final String ARG_COLLECTION_ID = "ARG_COLLECTION_ID";
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String JAVASCRIPT_INTERFACE_MAPPED_NAME = "enigma";

    private URL mDocumentUrl;
    private AndroidQuestViewInterface mViewInterface;

    /**
     * Creates a new {@link PageFragment} instance
     * @param collectionId the quest collection to load data from
     * @param page the page (quest index) to display
     * @return the new fragment instance
     */
    public static PageFragment newInstance(String collectionId, int page) {
        Bundle arguments = new Bundle();
        arguments.putString(ARG_COLLECTION_ID, collectionId);
        arguments.putInt(ARG_PAGE, page);

        PageFragment fragment = new PageFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get passed in arguments
        int pageIndex = getArguments().getInt(ARG_PAGE);
        String collectionId = getArguments().getString(ARG_COLLECTION_ID);

        // Get collection metadata and status from arguments
        CollectionDataBundle collectionBundle = CollectionDataBundle.fromId(
                (EnigmaApplication) getActivity().getApplication(), collectionId);

        mDocumentUrl = collectionBundle.getCollection().get(pageIndex - 1).getMainDocumentUrl();
        mViewInterface = new AndroidQuestViewInterface(collectionBundle.getCollectionStatus(), pageIndex);
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

        // Create an all-matching-parent WebView
        WebView view = new WebView(container.getContext());
        view.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // Configure WebView to accept custom URL schemes and allow JavaScript
        view.setWebViewClient(new InterceptingWebViewClient());
        view.getSettings().setJavaScriptEnabled(true);

        // Inject our (minimal) JavaScript API
        view.addJavascriptInterface(mViewInterface, JAVASCRIPT_INTERFACE_MAPPED_NAME);

        // Load!
        /* view.clearCache(true); */
        view.loadUrl(mDocumentUrl.toString());
        return view;
    }

}
