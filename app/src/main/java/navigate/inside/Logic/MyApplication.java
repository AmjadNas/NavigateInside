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

    @Override
    public void onCreate() {
        super.onCreate();

        listeners = new ArrayList<>();
        beaconManager = new BeaconManager(getApplicationContext());
       // beaconManager.setBackgroundScanPeriod(1000,500);
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(new BeaconRegion(
                        "ranged region",
                        null,
                        null, null));
            }
        });
        beaconManager.setForegroundScanPeriod(200, 2000);
        beaconManager.setRangingListener(new BeaconManager.BeaconRangingListener() {
            @Override
            public void onBeaconsDiscovered(BeaconRegion beaconRegion, List<Beacon> beacons) {
                for (BeaconListener ltnr : listeners)
                    if(!beacons.isEmpty())
                        ltnr.onBeaconEvent(beacons.get(0));
            }
        });
     /*   beaconManager.setMonitoringListener(new BeaconManager.BeaconMonitoringListener() {
            @Override
            public void onEnteredRegion(BeaconRegion region, List<Beacon> beacons) {


            }
            @Override
            public void onExitedRegion(BeaconRegion region) {
                // could add an "exit" notification too if you want (-:
                //beaconManager.stopMonitoring(region.getIdentifier());
               // beaconManager.stop();
              //  Log.i("exit becon", region.getProximityUUID().toString());
            }
        });*/

    }

    public void registerListener(BeaconListener listener){
        if (!listeners.contains(listener)){
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
