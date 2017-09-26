package ch.bozaci.footballtrainertoolapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by Alp.Bozaci on 26.09.2017.
 */

public class PictureUtil
{
    public static byte[] convertBitmapToByteArray(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] picture = baos.toByteArray();
        return picture;
    }

    public static Bitmap convertByteArrayToBitmap(byte[] picture)
    {
        ByteArrayInputStream bais = new ByteArrayInputStream(picture);
        Bitmap bitmap = BitmapFactory.decodeStream(bais);
        return bitmap;
    }
}
