package eu.unipv.epsilon.enigma.ui.quiz.drawer;

import android.graphics.drawable.ColorDrawable;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import eu.unipv.epsilon.enigma.R;
import eu.unipv.epsilon.enigma.quest.Quest;
import eu.unipv.epsilon.enigma.ui.bitmap.AssetsURLImageLoader;
import eu.unipv.epsilon.enigma.ui.bitmap.ImageLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class QuizListItem extends RecyclerView.ViewHolder {

    private static final Random DEFAULT_COLOR_RNG = new Random();
    private static final int[] DEFAULT_COLOR_PALETTE = new int[] {
            // https://www.google.com/design/spec/style/color.html#color-color-palette
            0xFFF44336, // Red
            0xFFE91E63, // Pink
            0xFF9C27B0, // Purple
            0xFF673AB7, // Deep Purple
            0xFF3F51B5, // Indigo
            0xFF2196F3, // Blue
            0xFF03A9F4, // Light Blue
            0xFF00BCD4, // Cyan
            0xFF009688, // Teal
            0xFF4CAF50, // Green
            0xFF8BC34A, // Light Green
            0xFFCDDC39, // Lime
            0xFFFFEB3B, // Yellow
            0xFFFFC107, // Amber
            0xFFFF9800, // Orange
            0xFFFF5722, // Deep Orange
            0xFF795548, // Brown
            0xFF607D8B  // Blue Grey
    };


    private static final Logger LOG = LoggerFactory.getLogger(QuizListItem.class);

    private final TextView nameRef;
    private final TextView descriptionRef;
    private final ImageView imageRef;

    @LayoutRes
    public static final int LAYOUT_RESOURCE = R.layout.quiz_list_element;

    public QuizListItem(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(LAYOUT_RESOURCE, parent, false));

        nameRef = (TextView) itemView.findViewById(R.id.item_name);
        descriptionRef = (TextView) itemView.findViewById(R.id.item_description);
        imageRef = (ImageView) itemView.findViewById(R.id.item_image);
    }

    public void updateViewFromData(Quest data) {
        nameRef.setText(data.getName());
        descriptionRef.setText(data.getDescription());
        loadImage(data.getIconUrl());
    }

    // TODO: This is a copy-paste from CollectionCardHolder!
    protected void loadImage(final URL path) {
        imageRef.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                imageRef.getViewTreeObserver().removeOnPreDrawListener(this);
                try {
                    ImageLoader loader = new AssetsURLImageLoader(path);
                    imageRef.setImageBitmap(loader.decodeSampledBitmap(imageRef.getMeasuredWidth(), imageRef.getMeasuredHeight()));
                } catch (IOException e) {
                    int selectedColor = DEFAULT_COLOR_PALETTE[DEFAULT_COLOR_RNG.nextInt(DEFAULT_COLOR_PALETTE.length)];
                    imageRef.setImageDrawable(new ColorDrawable(selectedColor));
                    LOG.warn("The image was not found", e);
                }
                return true;
            }
        });
    }

}
