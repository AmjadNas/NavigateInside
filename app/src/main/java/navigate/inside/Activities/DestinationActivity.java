package navigate.inside.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import navigate.inside.Logic.SysData;
import navigate.inside.Objects.Node;
import navigate.inside.Objects.Room;
import navigate.inside.R;


public class DestinationActivity extends AppCompatActivity {

    ListView listview;
    ArrayList<String> values;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);
        listview =(ListView)findViewById(R.id.list);
        GetAllRooms();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,values);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = (String)listview.getItemAtPosition(position);
                String[] BothItems = itemValue.split("-");
                Intent intent = new Intent();
                intent.putExtra("RoomNumber",BothItems[1]);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    private void GetAllRooms(){
        values = new ArrayList<>();
        String S = "";
        for(Node n : SysData.getInstance().getAllNodes()){
            for(Room R : n.getRooms()){
                S = R.GetRoomName()+"-"+R.GetRoomNum();
                values.add(S);
            }
        }
    }






}
