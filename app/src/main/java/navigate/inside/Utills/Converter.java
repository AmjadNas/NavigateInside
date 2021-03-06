package navigate.inside.Utills;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public final class Converter {

    private Converter(){}
    /**
     * helper class for performing image conversion operations
     */
    public static Bitmap decodeImage(byte[] arr){

        return BitmapFactory.decodeByteArray(arr, 0,arr.length);
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap){
        if (bitmap != null) {
            ByteArrayOutputStream bas;
            byte[] bArray;
            try {
                bas = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bas);
                bArray = bas.toByteArray();
                bas.flush();
                bas.close();
            } catch (IOException e) {
                Log.e("IOException ", e.getMessage());
                return null;
            }
            return bArray;
        }
        return null;

    }

    public static Bitmap compreesBitmap(Bitmap img){
        byte[] arr = getBitmapAsByteArray(img);
        return decodeImage(arr);
    }

    public static Bitmap getImageTHumbnail(Bitmap img){
        return ThumbnailUtils.extractThumbnail(img,500,300);
    }

}
