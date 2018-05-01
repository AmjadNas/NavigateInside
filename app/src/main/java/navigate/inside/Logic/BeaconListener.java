package navigate.inside.Logic;

import com.estimote.coresdk.recognition.packets.Beacon;

public interface BeaconListener {

    void onBeaconEvent(Beacon beacon);
}
