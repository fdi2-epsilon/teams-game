package eu.unipv.epsilon.enigma.ui.widget;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CircularImageView extends ImageView {

    private boolean bitmapChanged = true;
    private Bitmap croppedBitmap;

    public CircularImageView(Context context) {
        super(context);
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        bitmapChanged = true;
        super.setImageDrawable(drawable);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();

        ImageView v;
        if (drawable == null)
            return;

        int w = getWidth(), h = getHeight();

        if (w == 0 || h == 0)
            return;

        if (bitmapChanged) {
            if (drawable instanceof BitmapDrawable) {
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap().copy(Bitmap.Config.ARGB_8888, true);
                croppedBitmap = getCroppedBitmap(bitmap, w);
            } else if (drawable instanceof ColorDrawable) {
                croppedBitmap = getColoredCircle(((ColorDrawable) drawable).getColor(), w);
            } else
                throw new UnsupportedOperationException("Only BitmapDrawable and ColorDrawable instances are supported");
            bitmapChanged = false;
        }
        canvas.drawBitmap(croppedBitmap, 0, 0, null);
    }

    public static Bitmap getCroppedBitmap(Bitmap bitmap, int radius) {
        Bitmap scaledBitmap;

        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius) {
            float smallest = Math.min(bitmap.getWidth(), bitmap.getHeight());
            float factor = smallest / radius;
            scaledBitmap = Bitmap.createScaledBitmap(bitmap,
                    (int) (bitmap.getWidth() / factor), (int) (bitmap.getHeight() / factor), false);
        } else
            scaledBitmap = bitmap;

        Bitmap output = Bitmap.createBitmap(radius, radius, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, radius, radius);

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(radius / 2 + .7f, radius / 2 + .7f, radius / 2 + .1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(scaledBitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap getColoredCircle(int color, int radius) {
        Bitmap output = Bitmap.createBitmap(radius, radius, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(radius / 2 + .7f, radius / 2 + .7f, radius / 2 + .1f, paint);

        return output;
    }

}
