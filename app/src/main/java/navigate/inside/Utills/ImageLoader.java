package navigate.inside.Utills;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import navigate.inside.Logic.Listeners.ImageLoadedListener;
import navigate.inside.Logic.SysData;
import navigate.inside.Objects.BeaconID;

/**
 * class for loading images on a new thread
 */
public class ImageLoader extends AsyncTask<Bitmap,Bitmap,Bitmap> {
    private boolean downloaded;
    private ImageLoadedListener imageLoadedListener;
    private BeaconID currentID;
    private int imageNum;

    public ImageLoader(BeaconID currentID, int num ,ImageLoadedListener imageLoadedListener){
        this.imageLoadedListener = imageLoadedListener;
        this.currentID = currentID;
        imageNum = num;
    }
    @Override
    protected Bitmap doInBackground(Bitmap... bitmaps) {

        return Converter.getImageTHumbnail(bitmaps[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        imageLoadedListener.onImageLoaded(bitmap);
    }
}
