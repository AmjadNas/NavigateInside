package navigate.inside.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONObject;

import navigate.inside.Logic.SysData;
import navigate.inside.Network.NetworkConnector;
import navigate.inside.Network.NetworkResListener;
import navigate.inside.Network.ResStatus;
import navigate.inside.R;

public class SplashActivity extends AppCompatActivity implements NetworkResListener {

    private boolean firstInit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SysData.getInstance();
       //SysData.getInstance().initDatBase(getApplicationContext());
        NetworkConnector.getInstance().initialize(getApplicationContext());

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        firstInit = sharedPref.getBoolean(getResources().getString(R.string.firstLaunch), true);

        if (firstInit) {
            NetworkConnector.getInstance().update(this);
            sharedPref.edit()
                    .putBoolean(getResources().getString(R.string.firstLaunch), false)
                    .apply();
        }else{
            SysData.getInstance().InitializeData();
            finish();
        }
    }

    @Override
    public void onPreUpdate(String str) {

    }

    @Override
    public void onPostUpdate(JSONObject res, ResStatus status) {

      /*  if (firstInit && status == ResStatus.FAIL)
            Toast.makeText(this, "couldn't initialize app.", Toast.LENGTH_SHORT).show();*/
       // else if (!firstInit && status == ResStatus.FAIL){
            SysData.getInstance().InitializeData();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        //}

        finish();
    }

    @Override
    public void onPostUpdate(Bitmap res, ResStatus status) {

    }
}
