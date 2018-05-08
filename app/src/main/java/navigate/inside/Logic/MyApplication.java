package navigate.inside.Logic;

import android.app.Application;
import android.util.Log;

import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.observation.utils.Proximity;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class MyApplication extends Application {

    private BeaconManager beaconManager;
    private List<BeaconListener> listeners;
    private BeaconRegion region;

    @Override
    public void onCreate() {
        super.onCreate();

        listeners = new ArrayList<>();
        beaconManager = new BeaconManager(getApplicationContext());
        beaconManager.setBackgroundScanPeriod(500,500);
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new BeaconRegion(
                        "monitored region",
                        null,
                        null, null));
            }
        });

        beaconManager.setMonitoringListener(new BeaconManager.BeaconMonitoringListener() {
            @Override
            public void onEnteredRegion(BeaconRegion region, List<Beacon> beacons) {

                for (Beacon beacon : beacons)
                    Log.i("enter becon", beacon.getMajor() + " ");
                /*for (BeaconListener ltnr : listeners)
                    ltnr.onBeaconEvent(beacons.get(0));*/
            }
            @Override
            public void onExitedRegion(BeaconRegion region) {
                // could add an "exit" notification too if you want (-:

                Log.i("exit becon", region.getProximityUUID().toString());
            }
        });

    }

    public void registerListener(BeaconListener listener){
        if (!listeners.contains(listener)){
            listeners.add(listener);
        }
    }

    public void unRegisterListener(BeaconListener listener){

        listeners.remove(listener);

    }
}
