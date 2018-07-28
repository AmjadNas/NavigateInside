package navigate.inside.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import navigate.inside.Activities.Navigation.Activities.GetDirectionsActivity;
import navigate.inside.Activities.Navigation.Activities.MyLocationActivity;
import navigate.inside.Logic.SysData;
import navigate.inside.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SysData.getInstance().closeDatabase();
    }

    private void launchActivity(Class<?> cls){
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    //IMPORTANT: if you want to change the buttons to Image (ImageView)
    // use these methods in the onClick attribute in the XML Layout edit
    public void find_location(View view) {
        launchActivity(MyLocationActivity.class);
    }

    public void get_directions(View view) {
        launchActivity(GetDirectionsActivity.class);
    }


}
