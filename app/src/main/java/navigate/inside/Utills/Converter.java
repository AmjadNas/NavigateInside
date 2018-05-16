package navigate.inside.Utills;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public final class Converter {

    private Converter(){}

    public static Bitmap decodeImage(byte[] arr){
        Bitmap img;

        img = BitmapFactory.decodeByteArray(arr, 0,arr.length);

        return img;
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap){
        ByteArrayOutputStream bas;
        byte[] bArray;
        try {
            bas = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,75 ,bas);
            bArray = bas.toByteArray();
            bas.flush();
            bas.close();
        } catch (IOException e) {
            Log.e("IOException ",e.getMessage());
            return null;
        }
        return bArray;

    }

    public static Bitmap compreesBitmap(Bitmap img){
        byte[] arr = getBitmapAsByteArray(img);
        return BitmapFactory.decodeByteArray(arr, 0, arr.length);
    }

}
