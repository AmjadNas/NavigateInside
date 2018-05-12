package navigate.inside.Logic;

import android.app.Application;
import android.util.Log;

import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.util.ArrayList;
import java.util.List;


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
                Log.i("event enter", "entered");
                for (BeaconListener ltnr : listeners)
                    ltnr.onBeaconEvent(beacons.get(0));
            }
            @Override
            public void onExitedRegion(BeaconRegion region) {
                // could add an "exit" notification too if you want (-:
                beaconManager.stopMonitoring(region.getIdentifier());
                Log.i("exit becon", region.getProximityUUID().toString());
            }
        });

    }

    public void registerListener(BeaconListener listener){
        if (!listeners.contains(listener)){
        Log.i("event ", "I received it");
            listeners.add(listener);
        }
    }

    public void unRegisterListener(BeaconListener listener){

        listeners.remove(listener);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        beaconManager.disconnect();
    }
}
