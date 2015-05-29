package eu.unipv.epsilon.enigma.ui.util;

import android.annotation.TargetApi;
import android.webkit.*;
import eu.unipv.epsilon.enigma.loader.levels.protocol.ClasspathURLStreamHandler;
import eu.unipv.epsilon.enigma.loader.levels.protocol.LevelAssetsURLStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * A WebViewClient that converts specific protocols from Android to Java format,
 * a class like this is not required if working with JavaFX WebViews.
 */
public class InterceptingWebViewClient extends WebViewClient {

    private static final Logger LOG = LoggerFactory.getLogger(InterceptingWebViewClient.class);

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

        if (url.startsWith(LevelAssetsURLStreamHandler.PROTOCOL_NAME) ||
                url.startsWith(ClasspathURLStreamHandler.PROTOCOL_NAME)) {
            try {
                LOG.info("Loading URL " + url);
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

}
