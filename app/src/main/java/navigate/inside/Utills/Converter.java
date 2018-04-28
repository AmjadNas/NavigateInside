package navigate.inside.Utills;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by StiPro on 4/22/2018.
 */

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
            bitmap.compress(Bitmap.CompressFormat.PNG,0 ,bas);
            bArray = bas.toByteArray();
            bas.flush();
            bas.close();
        } catch (IOException e) {
            Log.e("IOException ",e.getMessage());
            return null;
        }
        return bArray;

    }

}
