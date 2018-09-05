package navigate.inside.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import navigate.inside.Logic.SysData;
import navigate.inside.Network.NetworkConnector;
import navigate.inside.Network.NetworkResListener;
import navigate.inside.Network.ResStatus;
import navigate.inside.Objects.BeaconID;
import navigate.inside.Objects.Node;
import navigate.inside.R;
import navigate.inside.Utills.Constants;

public class SplashActivity extends AppCompatActivity implements NetworkResListener {

    private boolean firstInit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SysData.getInstance();
        SysData.getInstance().initDatBase(getApplicationContext());
        NetworkConnector.getInstance().initialize(getApplicationContext());

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        firstInit = sharedPref.getBoolean(getResources().getString(R.string.firstLaunch), true);

       if (firstInit) {
            NetworkConnector.getInstance().update(this);
            sharedPref.edit()
                    .putBoolean(getResources().getString(R.string.firstLaunch), false)
                    .apply();
        }else{

       // SysData.getInstance().initDatBase(getApplicationContext());
            SysData.getInstance().InitializeData();
            launchActivity();
            finish();
        }
    }

    @Override
    public void onPreUpdate(String str) {

    }

    @Override
    public void onPostUpdate(JSONObject res, ResStatus status) {
        if(status == ResStatus.SUCCESS){
            try {
                JSONArray arr = res.getJSONArray(Constants.Node), nbers;
                JSONObject o, nbr;
                Node n;

                for(int i = 0; i < arr.length(); i++){
                    n = Node.parseJson(arr.getJSONObject(i));
                    SysData.getInstance().insertNode(n);
                }
                for(int i = 0; i < arr.length(); i++){
                    o = arr.getJSONObject(i);
                    nbers = o.getJSONArray(Constants.Node);

                    for(int j = 0; j < nbers.length(); j++){
                        nbr = nbers.getJSONObject(j);
                        SysData.getInstance().insertNeighbourToNode(o.getString(Constants.BEACONID), nbr.getString(Constants.BEACONID), nbr.getInt(Constants.Direction));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            //temporary
            SysData.getInstance().InitializeData();
            launchActivity();

        }
      /*  if (firstInit && status == ResStatus.FAIL)
            Toast.makeText(this, "couldn't initialize app.", Toast.LENGTH_SHORT).show();*/
       // else if (!firstInit && status == ResStatus.FAIL){}
         //


        finish();
    }

    private void launchActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPostUpdate(Bitmap res, ResStatus status) {

    }
}
