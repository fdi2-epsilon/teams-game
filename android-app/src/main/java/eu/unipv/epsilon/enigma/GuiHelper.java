package eu.unipv.epsilon.enigma;

import android.app.Activity;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

public class GuiHelper {

    static int getDefaultStatusBarHeight(Resources resources) {
        // Get in XML from the private attribute: @*android:dimen/status_bar_height
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0){
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    static int getDefaultNavigationBarHeight(Resources resources) {
        // Get in XML from the private attribute: @*android:dimen/navigation_bar_height
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0){
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /** Adds <i>transparencies</i> to system and navigation bar in the main activity. */
    static void extendMainActivityToSystemArea(Activity activity, Toolbar toolbar, RecyclerView list) {
        int statusBarHeight = getDefaultStatusBarHeight(activity.getResources());
        int navigationBarHeight = getDefaultNavigationBarHeight(activity.getResources());

        int cardPadding = (int) activity.getResources().getDimension(R.dimen.card_screen_edge_padding_half);

        RelativeLayout.LayoutParams toolbarLayoutParams = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
        RelativeLayout.LayoutParams listLayoutParams = (RelativeLayout.LayoutParams) list.getLayoutParams();

        // Remove 'android:layout_below' constraint from Recycler
        listLayoutParams.removeRule(RelativeLayout.BELOW);

        // Enable fullscreen flags: http://stackoverflow.com/a/28041425 and others
        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );

        // Add 'layout_marginTop=[status_bar_height]' to Toolbar, because now pos is relative to screen
        toolbarLayoutParams.setMargins(0, statusBarHeight, 0, 0);

        // Set RecyclerView padding values to keep bounds in main screen
        list.setPadding(
                list.getPaddingLeft(),
                statusBarHeight + toolbar.getMinimumHeight() + cardPadding,
                list.getPaddingRight(),
                navigationBarHeight + cardPadding);

        // Set 'android:clipToPadding="false"' on Recycler to extend content in system areas
        list.setClipToPadding(false);
    }

}
