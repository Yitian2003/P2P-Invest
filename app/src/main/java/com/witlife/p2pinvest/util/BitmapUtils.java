package com.witlife.p2pinvest.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import java.io.ByteArrayOutputStream;

/**
 * Created by bruce on 29/08/2017.
 */

public class BitmapUtils {
    public static Bitmap circleBitmap(Bitmap source){

        int width = source.getWidth();

        Bitmap bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        canvas.drawCircle(width/2, width/2, width/2, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(source, 0, 0, paint);

        return bitmap;
    }

    public static Bitmap zoom(Bitmap source,float width ,float height){

        Matrix matrix = new Matrix();
        matrix.postScale(width / source.getWidth(),height / source.getHeight());

        Bitmap bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, false);
        return bitmap;
    }

    public static String bitmapToString(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        String encodedImage = Base64.encodeBytes(imageBytes);

        return encodedImage;
    }
}
