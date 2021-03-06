package navigate.inside.Network;

import android.graphics.Bitmap;

import org.json.JSONObject;

/**
 * NetworkResListener interface
 */
public interface NetworkResListener {
    /**
     * callback method which called when the resources update is started
     */
    public void onPreUpdate();

    public void onPostUpdate(JSONObject res, String req, ResStatus status);

    public void onPostUpdate(Bitmap res, String id, String id2, ResStatus status);
}
