package eu.unipv.epsilon.enigma;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * An activity whose recycler view content falls behind system areas.
 *
 * This can be used by any activity that uses a {@link RecyclerView} to display a list of elements,
 * like MainActivity and ListActivity.
 */
public class TranslucentControlsActivity extends AppCompatActivity {

    protected Toolbar toolbarView;
    protected RecyclerView recyclerView;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Semitransparent UI configuration available only on compatible devices
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return;

        if (toolbarView == null || recyclerView == null)
            throw new IllegalStateException("At least one view is null, call locateViews to assign them");

        alterLayouts();
        setRecyclerPaddings();
    }

    public void locateViews(@IdRes int toolbarId, @IdRes int recyclerId) {
        toolbarView = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.main_collections_view);
    }

    private void alterLayouts() {
        RelativeLayout.LayoutParams toolbarLParams, recyclerLParams;

        try {
            toolbarLParams = (RelativeLayout.LayoutParams) toolbarView.getLayoutParams();
            recyclerLParams = (RelativeLayout.LayoutParams) recyclerView.getLayoutParams();
        } catch (ClassCastException e) {
            throw new IllegalStateException(
                    "Activity view can only be extended while using Relative Layout as root", e);
        }

        // Remove 'android:layout_below' constraint from Recycler to let it flow behind the toolbar
        recyclerLParams.removeRule(RelativeLayout.BELOW);

        // Enable fullscreen flags: http://stackoverflow.com/a/28041425 and others
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );

        // Add 'layout_marginTop=[status_bar_height]' to Toolbar, because these flags above set its
        // position relative to screen and it should not be behind the status bar but under it.
        toolbarLParams.setMargins(0, getDefaultStatusBarHeight(), 0, 0);
    }

    private void setRecyclerPaddings() {
        int cardPadding = (int) getResources().getDimension(R.dimen.card_screen_edge_padding_half);

        // Set RecyclerView padding values to keep bounds (scroll limits) in main screen
        recyclerView.setPadding(
                recyclerView.getPaddingLeft(),
                getDefaultStatusBarHeight() + toolbarView.getMinimumHeight() + cardPadding,
                recyclerView.getPaddingRight(),
                getDefaultNavigationBarHeight() + cardPadding);

        // Set 'android:clipToPadding="false"' on Recycler to extend content in system (padding) areas
        recyclerView.setClipToPadding(false);
    }

    private int getDefaultStatusBarHeight() {
        // Get in XML from the private attribute: @*android:dimen/status_bar_height
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            return getResources().getDimensionPixelSize(resourceId);
        return 0;
    }

    private int getDefaultNavigationBarHeight() {
        // Get in XML from the private attribute: @*android:dimen/navigation_bar_height
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0)
            return getResources().getDimensionPixelSize(resourceId);
        return 0;
    }

}
